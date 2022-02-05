package com.zerotb.zerotb.ui.home.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.zerotb.zerotb.R
import com.zerotb.zerotb.data.helper.*
import com.zerotb.zerotb.data.network.Resource
import com.zerotb.zerotb.data.responses.data.User
import com.zerotb.zerotb.databinding.FragmentSettingsBinding
import com.zerotb.zerotb.ui.auth.AuthActivity
import com.zerotb.zerotb.ui.home.settings.adapter.SettingsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@AndroidEntryPoint
class SettingsFragment : Fragment(), UploadRequestBody.UploadCallback {

    private val viewModel by viewModels<SettingsViewModel>()
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var settingsAdapter: SettingsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        setupObserver()
        return binding.root
    }

    private fun setupObserver() = with(binding) {
        user()
        viewModel.profileResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        requireContext().toast(it.value.message)
                    }
                }
                is Resource.Loading -> {
                    progressBar.visible()
                }
                is Resource.Dismiss -> {
                    progressBar.invisible()
                }
                is Resource.Failure -> handleApiError(it) {
                    profile(profileUri)
                }
            }
        })
        viewModel.userResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        if (it.value.user.profile == null){
                            civProfile.setImageResource(R.drawable.user)
                        }else{
                            civProfile.loadImage("${urlAssets()}${it.value.user.profile}")
                        }
                        user = it.value.user
                        updateUI()
                    }
                }
                is Resource.Loading -> {
                    progressBarUser.visible()
                    civProfile.invisible()
                }
                is Resource.Dismiss -> {
                    progressBarUser.invisible()
                    civProfile.visible()
                }
                is Resource.Failure -> handleApiError(it) {
                    user()
                }
            }
        })
        viewModel.logoutResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        requireContext().toast(it.value.message)
                        logout()
                        requireActivity().launchActivity(AuthActivity::class.java)
                    }
                }
                is Resource.Loading -> {
                    progressBar.visible()
                }
                is Resource.Dismiss -> {
                    progressBar.invisible()
                }
                is Resource.Failure -> handleApiError(it) {
                    clear()
                }
            }
        })
    }

    private fun updateUI() = with(binding) {
        txtChangeProfile.visible()
        txtChangeProfile.setOnClickListener {
            pickProfile.launch("image/*")
        }
        settingsAdapter = SettingsAdapter {
            when (it) {
                "1" -> {
                    findNavController().navigate(
                        SettingsFragmentDirections.actionSettingsFragmentToAccountFragment(
                            user
                        )
                    )
                }
                "2" -> {
                    val url = Intent(Intent.ACTION_VIEW)
                    url.data =
                        Uri.parse("https://www.privacypolicyonline.com/live.php?token=R8jI52OPMVo2poA56efZX04cf43WiSKs")
                    startActivity(url)
                }
                "3" -> {
                    requireContext().alertDialog(
                        "Tekan Ya jika anda ingin logout",
                        "Ya",
                        "Tidak"
                    ) { _, _ ->
                        clear()
                    }
                }
            }
        }
        rvSettings.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext())
            adapter = settingsAdapter
        }
    }

    private val pickProfile = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        it?.let {
            profileUri = it
            binding.civProfile.loadImage(profileUri.toString())
            profile(profileUri)
        }
    }

    private fun profile(profileUri: Uri?) {
        val profileDescriptor =
            requireContext().contentResolver.openFileDescriptor(profileUri!!, "r", null)
        val inputStreamProfile = FileInputStream(profileDescriptor?.fileDescriptor)
        val fileProfile = File(
            requireContext().cacheDir,
            requireContext().contentResolver.getFileName(profileUri)
        )
        val outputStreamProfile = FileOutputStream(fileProfile)
        inputStreamProfile.copyTo(outputStreamProfile)

        val bodyProfile = UploadRequestBody(fileProfile, "image", this@SettingsFragment)
        val profile = MultipartBody.Part.createFormData("profile", fileProfile.name, bodyProfile)

        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            viewModel.profile(
                "Bearer $it",
                profile
            )
        })
    }

    private fun user() {
        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            viewModel.user(
                "Bearer $it"
            )
        })
    }

    private fun clear() {
        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            viewModel.logout(
                "Bearer $it"
            )
        })
    }

    companion object {
        private var profileUri: Uri? = null
        private lateinit var user: User
    }

    override fun onProgressUpdate(percentage: Int) = with(binding) {
        progressBar.progress = percentage
    }

}