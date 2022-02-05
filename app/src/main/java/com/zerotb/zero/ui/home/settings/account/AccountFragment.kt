package com.zerotb.zero.ui.home.settings.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.zerotb.zero.data.helper.*
import com.zerotb.zero.data.network.Resource
import com.zerotb.zero.databinding.FragmentAccountBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private val args by navArgs<AccountFragmentArgs>()
    private val viewModel by viewModels<AccountViewModel>()
    private lateinit var binding: FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
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
                    }
                }
                is Resource.Loading -> {
                    progressBar.visible()
                    btnUpdateAccount.invisible()
                }
                is Resource.Dismiss -> {
                    progressBar.invisible()
                    btnUpdateAccount.visible()
                }
                is Resource.Failure -> handleApiError(it) {
                    profile()
                }
            }
        })
    }

    private fun updateUI() = with(binding) {
        edtName.setText(args.user.name)
        edtUsername.setText(args.user.username)
        edtHp.setText(args.user.hp)
        edtEmail.setText(args.user.email)

        if (args.user.gender == "L") {
            rbMan.isChecked = true
            gender = "L"
        } else {
            rbWomen.isChecked = true
            gender = "P"
        }

        edtName.addTextChangedListener {
            checkRequiredField()
        }
        edtUsername.addTextChangedListener {
            checkRequiredField()
        }
        edtHp.addTextChangedListener {
            checkRequiredField()
        }
        edtEmail.addTextChangedListener {
            checkRequiredField()
        }

        rbMan.setOnClickListener {
            rbMan.isChecked = true
            gender = "L"
        }

        rbWomen.setOnClickListener {
            rbWomen.isChecked = true
            gender = "P"
        }

        btnUpdateAccount.setOnClickListener {
            profile()
        }

    }

    private fun checkRequiredField() = with(binding) {
        btnUpdateAccount.isEnabled =
            edtName.text.toString().trim().isNotEmpty() && edtUsername.text.toString().trim()
                .isNotEmpty() && edtHp.text.toString().trim()
                .isNotEmpty() && edtEmail.text.toString().trim()
                .isNotEmpty() && rbMan.isChecked || rbWomen.isChecked
    }

    private fun profile() = with(binding) {
        if (!isValidEmail(edtEmail.text.toString().trim())) {
            requireContext().toast("Email anda tidak valid !")
        } else {
            requireContext().alertDialog(
                "Tekan Ya jika anda ingin mengubah akun",
                "Ya",
                "Tidak"
            ) { _, _ ->
                viewModel.user().asLiveData().observe(viewLifecycleOwner, {
                    viewModel.profile(
                        "Bearer $it",
                        args.user.id,
                        edtName.text.toString().trim(),
                        edtUsername.text.toString().trim(),
                        edtEmail.text.toString().trim(),
                        gender,
                        edtHp.text.toString().trim()
                    )
                })
            }
        }
    }

    companion object {
        private lateinit var gender: String
    }

}