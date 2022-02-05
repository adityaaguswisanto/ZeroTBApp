package com.zerotb.zerotb.ui.home.home.patient.popup.pill

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.zerotb.zerotb.data.helper.LoadAdapter
import com.zerotb.zerotb.data.helper.toast
import com.zerotb.zerotb.data.helper.visible
import com.zerotb.zerotb.data.responses.data.Pill
import com.zerotb.zerotb.databinding.FragmentPillBinding
import com.zerotb.zerotb.ui.home.home.patient.popup.pill.adapter.PillAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PillFragment : Fragment(), PillAdapter.OnItemClickListener {

    private val viewModel by viewModels<PillViewModel>()
    private val args by navArgs<PillFragmentArgs>()
    private lateinit var binding: FragmentPillBinding

    private val pillAdapter = PillAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPillBinding.inflate(inflater, container, false)
        updateUI()
        return binding.root
    }

    private fun updateUI() = with(binding) {

        pills()

        rvPill.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = pillAdapter.withLoadStateFooter(
                footer = LoadAdapter { pillAdapter.retry() },
            )
        }

        pillAdapter.addLoadStateListener {
            progressBar.visible(it.source.refresh is LoadState.Loading)
            ivRetry.visible(it.source.refresh is LoadState.Error)

            val errorState = it.source.append as? LoadState.Error
                ?: it.source.prepend as? LoadState.Error
                ?: it.append as? LoadState.Error
                ?: it.prepend as? LoadState.Error
            errorState?.let { state ->
                requireContext().toast("\uD83D\uDE28 Wooops ${state.error}")
            }
        }

        ivRetry.setOnClickListener {
            pillAdapter.retry()
        }

        fabMedical.setOnClickListener {
            findNavController().navigate(
                PillFragmentDirections.actionPillFragmentToMedicalFragment(
                    args.user
                )
            )
        }

        fabDrug.setOnClickListener {
            findNavController().navigate(
                PillFragmentDirections.actionPillFragmentToDrugFragment(
                    args.user,
                    null,
                    "add"
                )
            )
        }
    }

    private fun pills() {
        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            lifecycleScope.launch {
                viewModel.pill(
                    "Bearer $it",
                    args.user.id
                ).collectLatest {
                    pillAdapter.submitData(it)
                }
            }
        })
    }

    override fun onItemClick(pill: Pill) {
        findNavController().navigate(
            PillFragmentDirections.actionPillFragmentToPillPopup(
                pill,
                args.user
            )
        )
    }

}