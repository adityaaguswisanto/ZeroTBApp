package com.zerotb.zero.ui.home.home.patient.popup.pill.popup

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.zerotb.zero.data.helper.*
import com.zerotb.zero.data.network.Resource
import com.zerotb.zero.databinding.PopupPillBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PillPopup : DialogFragment() {

    private val viewModel by viewModels<PillPopupViewModel>()
    private val args by navArgs<PillPopupArgs>()
    private lateinit var binding: PopupPillBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PopupPillBinding.inflate(inflater, container, false)
        setupObserver()
        updateUI()
        return binding.root
    }

    private fun setupObserver() {
        viewModel.drugResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        requireContext().toast("Obat berhasil dihapus...")
                        findNavController().navigate(
                            PillPopupDirections.actionPillPopupToPillFragment(
                                args.user
                            )
                        )
                    }
                }
                is Resource.Loading -> {
                    Log.i("TAG", "setupObserver: Loading")
                }
                is Resource.Dismiss -> {
                    Log.i("TAG", "setupObserver: Dismiss")
                }
                is Resource.Failure -> handleApiError(it) {
                    drug()
                }
            }
        })
    }

    private fun updateUI() = with(binding) {
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        txtName.text = args.pill.patient
        txtMedical.text = args.pill.medical
        txtDrug.text = args.pill.drug

        btnDeleteDrug.setOnClickListener {
            drug()
        }

        btnUpdateDrug.setOnClickListener {
            findNavController().navigate(
                PillPopupDirections.actionPillPopupToDrugFragment(
                    args.user,
                    args.pill,
                    "update"
                )
            )
        }

    }

    private fun drug() {
        requireContext().alertDialog(
            "Tekan Ya jika anda ingin menghapus obat",
            "Ya",
            "Tidak"
        ) { _, _ ->
            viewModel.user().asLiveData().observe(viewLifecycleOwner, {
                viewModel.drug(
                    "Bearer $it",
                    args.pill.id_drug
                )
            })
        }
    }

}