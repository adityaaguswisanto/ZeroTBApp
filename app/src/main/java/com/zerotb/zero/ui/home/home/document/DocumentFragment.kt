package com.zerotb.zero.ui.home.home.document

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.zerotb.zero.data.helper.LoadAdapter
import com.zerotb.zero.data.helper.toast
import com.zerotb.zero.data.helper.visible
import com.zerotb.zero.data.responses.data.Pill
import com.zerotb.zero.data.store.UserStore
import com.zerotb.zero.databinding.FragmentDocumentBinding
import com.zerotb.zero.ui.home.home.patient.popup.pill.PillViewModel
import com.zerotb.zero.ui.home.home.patient.popup.pill.adapter.PillAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DocumentFragment : Fragment(), PillAdapter.OnItemClickListener {

    private val viewModel by viewModels<PillViewModel>()
    private lateinit var binding: FragmentDocumentBinding

    @Inject
    lateinit var userStore: UserStore

    private val pillAdapter = PillAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDocumentBinding.inflate(inflater, container, false)
        updateUI()
        return binding.root
    }

    private fun updateUI() = with(binding) {

        pills()

        rvDocument.apply {
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

    }

    private fun pills() {
        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            lifecycleScope.launch {
                viewModel.pill(
                    "Bearer $it",
                    userStore.userId.first()!!
                ).collectLatest {
                    pillAdapter.submitData(it)
                }
            }
        })
    }

    override fun onItemClick(pill: Pill) {
        Log.i("TAG", "onItemClick: Clicked")
    }

}