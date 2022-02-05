package com.zerotb.zero.ui.home.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.zerotb.zero.databinding.FragmentAdminBinding
import com.zerotb.zero.ui.home.home.adapter.AdminAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminFragment : Fragment() {

    private lateinit var binding: FragmentAdminBinding
    private lateinit var adminAdapter: AdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminBinding.inflate(inflater, container, false)
        updateUI()
        return binding.root
    }

    private fun updateUI() = with(binding) {
        adminAdapter = AdminAdapter {
            when (it) {
                "1" -> {
                    findNavController().navigate(AdminFragmentDirections.actionAdminFragmentToEntryFragment())
                }
                "2" -> {
                    findNavController().navigate(AdminFragmentDirections.actionAdminFragmentToPatientFragment())
                }
            }
        }
        rvAdmin.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext())
            adapter = adminAdapter
        }
    }

}