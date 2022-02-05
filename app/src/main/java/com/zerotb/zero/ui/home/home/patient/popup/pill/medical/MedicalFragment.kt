package com.zerotb.zero.ui.home.home.patient.popup.pill.medical

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.zerotb.zero.data.helper.*
import com.zerotb.zero.data.network.Resource
import com.zerotb.zero.databinding.FragmentMedicalBinding
import com.zerotb.zero.ui.home.settings.account.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MedicalFragment : Fragment() {

    private val viewModel by viewModels<AccountViewModel>()
    private val args by navArgs<MedicalFragmentArgs>()
    private lateinit var binding: FragmentMedicalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMedicalBinding.inflate(inflater, container, false)
        setupObserver()
        updateUI()
        return binding.root
    }

    private fun setupObserver() = with(binding) {
        viewModel.profileResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        requireContext().toast(it.value.message)
                        findNavController().navigate(
                            MedicalFragmentDirections.actionMedicalFragmentToPillFragment(
                                args.user
                            )
                        )
                    }
                }
                is Resource.Loading -> {
                    progressBar.visible()
                    btnMedical.invisible()
                }
                is Resource.Dismiss -> {
                    progressBar.invisible()
                    btnMedical.visible()
                }
                is Resource.Failure -> handleApiError(it) {
                    medical()
                }
            }
        })
    }

    private fun updateUI() = with(binding) {

        btnMedical.setOnClickListener {
            medical()
        }

    }

    private fun medical() = with(binding) {
        requireContext().alertDialog(
            "Tekan Ya jika anda ingin mengubah puskesmas",
            "Ya",
            "Tidak"
        ) { _, _ ->
            viewModel.user().asLiveData().observe(viewLifecycleOwner, {
                viewModel.medical(
                    "Bearer $it",
                    args.user.id,
                    spnMedical.selectedItem.toString(),
                )
            })
        }

    }

}