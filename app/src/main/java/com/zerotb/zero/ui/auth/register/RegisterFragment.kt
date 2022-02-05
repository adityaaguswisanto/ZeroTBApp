package com.zerotb.zero.ui.auth.register

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.zerotb.zero.data.helper.*
import com.zerotb.zero.data.network.Resource
import com.zerotb.zero.databinding.FragmentRegisterBinding
import com.zerotb.zero.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val viewModel by viewModels<AuthViewModel>()
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        setupObserver()
        updateUI()
        return binding.root
    }

    private fun setupObserver() = with(binding) {
        viewModel.registerResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        requireContext().toast(it.value.message)
                        findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
                    }
                }
                is Resource.Loading -> {
                    progressBar.visible()
                    btnRegister.invisible()
                }
                is Resource.Dismiss -> {
                    progressBar.invisible()
                    btnRegister.visible()
                }
                is Resource.Failure -> handleApiError(it) {
                    register()
                }
            }
        })
    }

    private fun updateUI() = with(binding) {
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
        edtPassword.addTextChangedListener {
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

        cbPassword.setOnClickListener {
            if (cbPassword.isChecked) edtPassword.transformationMethod =
                HideReturnsTransformationMethod.getInstance() else edtPassword.transformationMethod =
                PasswordTransformationMethod.getInstance()
        }

        btnRegister.setOnClickListener {
            register()
        }

        txtLogin.setOnClickListener {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }
    }

    private fun checkRequiredField() = with(binding) {
        btnRegister.isEnabled =
            edtName.text.toString().trim().isNotEmpty() && edtUsername.text.toString().trim()
                .isNotEmpty() && edtHp.text.toString().trim()
                .isNotEmpty() && edtEmail.text.toString().trim()
                .isNotEmpty() && edtPassword.text.toString().trim()
                .isNotEmpty() && rbMan.isChecked || rbWomen.isChecked
    }

    private fun register() = with(binding) {
        if (!isValidEmail(edtEmail.text.toString().trim())) {
            requireContext().toast("Email anda tidak valid !")
        } else {
            requireContext().alertDialog(
                "Tekan Ya jika anda ingin membuat akun",
                "Ya",
                "Tidak"
            ) { _, _ ->
                viewModel.register(
                    edtName.text.toString().trim(),
                    edtUsername.text.toString().trim(),
                    edtEmail.text.toString().trim(),
                    gender,
                    edtHp.text.toString().trim(),
                    edtPassword.text.toString().trim(),
                    "2",
                    "Belum Terdaftar"
                )
            }
        }
    }

    companion object {
        private lateinit var gender: String
    }

}