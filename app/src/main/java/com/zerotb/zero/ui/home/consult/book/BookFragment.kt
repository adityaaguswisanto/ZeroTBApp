package com.zerotb.zero.ui.home.consult.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.zerotb.zero.data.helper.*
import com.zerotb.zero.data.network.Resource
import com.zerotb.zero.databinding.FragmentBookBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookFragment : Fragment() {

    private val viewModel by viewModels<BookViewModel>()
    private lateinit var binding: FragmentBookBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookBinding.inflate(inflater, container, false)
        setupObserver()
        updateUI()
        return binding.root
    }

    private fun setupObserver() = with(binding) {
        viewModel.bookResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        requireContext().toast(it.value.message)
                        findNavController().navigate(
                            BookFragmentDirections.actionBookFragmentToConsultFragment()
                        )
                    }
                }
                is Resource.Loading -> {
                    progressBar.visible()
                    btnConsult.invisible()
                }
                is Resource.Dismiss -> {
                    progressBar.invisible()
                    btnConsult.visible()
                }
                is Resource.Failure -> handleApiError(it) {
                    book()
                }
            }
        })
    }

    private fun updateUI() = with(binding) {

        edtName.addTextChangedListener {
            checkRequiredField()
        }

        edtConsult.addTextChangedListener {
            checkRequiredField()
        }

        btnConsult.setOnClickListener {
            book()
        }

    }

    private fun checkRequiredField() = with(binding) {
        btnConsult.isEnabled =
            edtName.text.toString().trim().isNotEmpty() &&
                    edtConsult.text.toString().trim().isNotEmpty()
    }

    private fun book() = with(binding) {
        requireContext().alertDialog(
            "Tekan Ya jika anda ingin mengirim konsultasi",
            "Ya",
            "Tidak"
        ) { _, _ ->
            viewModel.user().asLiveData().observe(viewLifecycleOwner, {
                lifecycleScope.launch {
                    viewModel.book(
                        "Bearer $it",
                        edtName.text.toString().trim(),
                        edtConsult.text.toString().trim(),
                        viewModel.userName().first()!!
                    )
                }
            })
        }
    }


}