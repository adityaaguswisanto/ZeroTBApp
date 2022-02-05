package com.zerotb.zerotb.ui.home.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.zerotb.zerotb.data.helper.ZeroBroadcast
import com.zerotb.zerotb.data.helper.openWhatsAppForDoctor
import com.zerotb.zerotb.data.store.ZeroSharedPreferences
import com.zerotb.zerotb.databinding.FragmentUserBinding
import com.zerotb.zerotb.ui.home.home.adapter.UserAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment() {

    private lateinit var binding: FragmentUserBinding
    private lateinit var userAdapter: UserAdapter

    private lateinit var zeroBroadcast: ZeroBroadcast
    private lateinit var zeroSharedPreferences: ZeroSharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        updateUI()
        return binding.root
    }

    private fun updateUI() = with(binding) {

        zeroBroadcast = ZeroBroadcast()
        zeroSharedPreferences = ZeroSharedPreferences(requireContext())

        onNotificationBroadcast()

        userAdapter = UserAdapter {
            when (it) {
                "1" -> {
                    findNavController().navigate(UserFragmentDirections.actionUserFragmentToDocumentFragment())
                }
                "2" -> {
                    requireContext().openWhatsAppForDoctor("+6281235660549")
                }
            }
        }
        rvUser.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext())
            adapter = userAdapter
        }
    }

    private fun onNotificationBroadcast() {
        zeroSharedPreferences.setTimeDaily(timeDaily)
        zeroSharedPreferences.setDailyMessage(messageDaily)
        zeroBroadcast.setAlarm(requireContext(), "reminderDaily", timeDaily, messageDaily)
    }

    companion object {
        private var timeDaily = "16:45"
        private var messageDaily = "Daily Reminder"
    }

}