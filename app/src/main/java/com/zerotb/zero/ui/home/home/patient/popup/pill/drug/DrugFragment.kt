package com.zerotb.zero.ui.home.home.patient.popup.pill.drug

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.zerotb.zero.data.helper.*
import com.zerotb.zero.data.network.Resource
import com.zerotb.zero.databinding.FragmentDrugBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DrugFragment : Fragment() {

    private val args by navArgs<DrugFragmentArgs>()
    private val viewModel by viewModels<DrugViewModel>()
    private lateinit var binding: FragmentDrugBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDrugBinding.inflate(inflater, container, false)
        setupObserver()
        updateUI()
        return binding.root
    }

    private fun setupObserver() = with(binding) {
        viewModel.drugResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        requireContext().toast("Obat berhasil dikirim....")
                        findNavController().navigate(
                            DrugFragmentDirections.actionDrugFragmentToPillFragment(
                                args.user
                            )
                        )
                    }
                }
                is Resource.Loading -> {
                    progressBar.visible()
                    btnAddDrug.invisible()
                }
                is Resource.Dismiss -> {
                    progressBar.invisible()
                    btnAddDrug.visible()
                }
                is Resource.Failure -> handleApiError(it) {
                    drug()
                    update()
                }
            }
        })
    }

    private fun updateUI() = with(binding) {

        if (args.status == "add") {
            edtName.setText(args.user.name)
            edtNoPatient.setText("No. Pasien : ${args.user.id}")

            edtHour.addTextChangedListener {
                btnAddDrug.enable(it.toString().isNotEmpty())
            }

            edtHour.setOnClickListener {
                val picker = TimePickerDialog(
                    requireContext(),
                    { _, sHour, sMinute ->
                        edtHour.setText("$sHour:$sMinute")
                    }, hour, minutes, true
                )
                picker.show()
            }
            btnAddDrug.setOnClickListener {
                drug()
            }
        } else {
            toolbar.title = "Update Obat"
            edtName.setText(args.user.name)
            edtNoPatient.setText("No. Pasien : ${args.user.id}")
            edtHour.setText(args.pill!!.hour)
            edtHour.addTextChangedListener {
                btnAddDrug.enable(it.toString().isNotEmpty())
            }
            edtHour.setOnClickListener {
                val picker = TimePickerDialog(
                    requireContext(),
                    { _, sHour, sMinute ->
                        edtHour.setText("$sHour:$sMinute")
                    }, hour, minutes, true
                )
                picker.show()
            }
            btnAddDrug.text = "Update Obat"
            btnAddDrug.setOnClickListener {
                update()
            }
        }

    }

    private fun drug() = with(binding) {
        requireContext().alertDialog(
            "Tekan Ya jika anda ingin menambahkan obat",
            "Ya",
            "Tidak"
        ) { _, _ ->
            viewModel.user().asLiveData().observe(viewLifecycleOwner, {
                lifecycleScope.launch {
                    viewModel.drug(
                        "Bearer $it",
                        spnDrug.selectedItem.toString(),
                        edtHour.text.toString().trim(),
                        args.user.id,
                        args.user.medical,
                        args.user.name,
                        viewModel.userName().first()!!
                    )
                }
            })
        }
    }

    private fun update() = with(binding) {
        requireContext().alertDialog(
            "Tekan Ya jika anda ingin mengubah obat",
            "Ya",
            "Tidak"
        ) { _, _ ->
            viewModel.user().asLiveData().observe(viewLifecycleOwner, {
                lifecycleScope.launch {
                    viewModel.drug(
                        "Bearer $it",
                        args.pill!!.id_drug,
                        spnDrug.selectedItem.toString(),
                        edtHour.text.toString().trim(),
                        args.user.id,
                        args.user.medical,
                        args.user.name,
                        viewModel.userName().first()!!
                    )
                }
            })
        }
    }

    companion object {
        private const val hour = 0
        private const val minutes = 0
    }

}