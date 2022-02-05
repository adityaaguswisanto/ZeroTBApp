package com.zerotb.zero.ui.home.consult

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.zerotb.zero.data.helper.LoadAdapter
import com.zerotb.zero.data.helper.toast
import com.zerotb.zero.data.helper.visible
import com.zerotb.zero.data.responses.data.Consult
import com.zerotb.zero.databinding.FragmentConsultBinding
import com.zerotb.zero.ui.home.consult.adapter.ConsultAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ConsultFragment : Fragment(), ConsultAdapter.OnItemClickListener {

    private val viewModel by viewModels<ConsultViewModel>()
    private lateinit var binding: FragmentConsultBinding

    private val consultAdapter = ConsultAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConsultBinding.inflate(inflater, container, false)
        updateUI()
        return binding.root
    }

    private fun updateUI() = with(binding) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)

        consult()

        rvConsult.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = consultAdapter.withLoadStateFooter(
                footer = LoadAdapter { consultAdapter.retry() },
            )
        }

        consultAdapter.addLoadStateListener {
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
            consultAdapter.retry()
        }

        fabConsult.setOnClickListener {
            findNavController().navigate(
                ConsultFragmentDirections.actionConsultFragmentToBookFragment()
            )
        }

    }

    private fun consult() {
        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            lifecycleScope.launch {
                viewModel.consult(
                    "Bearer $it",
                ).collectLatest {
                    consultAdapter.submitData(it)
                }
            }
        })
    }

    override fun onItemClick(consult: Consult) {
        Log.i("TAG", "onItemClick: clicked")
    }

}