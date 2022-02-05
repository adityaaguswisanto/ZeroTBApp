package com.zerotb.zerotb.ui.home.home.patient.popup

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.zerotb.zerotb.data.helper.loadImage
import com.zerotb.zerotb.data.helper.openWhatsAppForPatient
import com.zerotb.zerotb.data.helper.urlAssets
import com.zerotb.zerotb.databinding.PopupPatientBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PatientPopup : DialogFragment() {

    private val args by navArgs<PatientPopupArgs>()
    private lateinit var binding: PopupPatientBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PopupPatientBinding.inflate(inflater, container, false)
        updateUI()
        return binding.root
    }

    private fun updateUI() = with(binding) {
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        txtName.text = args.user.name
        txtMedical.text = args.user.medical

        if (args.user.profile != null) {
            cmvProfile.loadImage("${urlAssets()}${args.user.profile}")
        }

        btnViewDrug.setOnClickListener {
            findNavController().navigate(
                PatientPopupDirections.actionPatientPopupToPillFragment(
                    args.user
                )
            )
        }

        btnWhatsApp.setOnClickListener {
            requireContext().openWhatsAppForPatient(args.user.hp)
        }
    }

}