package com.zerotb.zero.ui.home.home.patient

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.zerotb.zero.R
import com.zerotb.zero.data.helper.LoadAdapter
import com.zerotb.zero.data.helper.toast
import com.zerotb.zero.data.helper.visible
import com.zerotb.zero.data.responses.data.User
import com.zerotb.zero.databinding.FragmentPatientBinding
import com.zerotb.zero.ui.home.home.patient.adapter.PatientAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PatientFragment : Fragment(), PatientAdapter.OnItemClickListener {

    private val viewModel by viewModels<PatientViewModel>()
    private lateinit var binding: FragmentPatientBinding
    private val patientAdapter = PatientAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPatientBinding.inflate(inflater, container, false)
        updateUI()
        return binding.root
    }

    private fun updateUI() = with(binding) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)

        patient()

        rvPatient.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = patientAdapter.withLoadStateFooter(
                footer = LoadAdapter { patientAdapter.retry() },
            )
        }

        patientAdapter.addLoadStateListener {
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
            patientAdapter.retry()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Cari nama atau no. pasien"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    viewModel.user().asLiveData().observe(viewLifecycleOwner, {
                        lifecycleScope.launch {
                            viewModel.patient(
                                "Bearer $it",
                                newText
                            ).collectLatest {
                                patientAdapter.submitData(it)
                            }
                        }
                    })
                } else {
                    patient()
                }
                return true
            }

        })
    }

    private fun patient() {
        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            lifecycleScope.launch {
                viewModel.patient(
                    "Bearer $it",
                    ""
                ).collectLatest {
                    patientAdapter.submitData(it)
                }
            }
        })
    }

    override fun onItemClick(user: User) {
        findNavController().navigate(
            PatientFragmentDirections.actionPatientFragmentToPatientPopup(
                user
            )
        )
    }

}