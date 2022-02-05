package com.zerotb.zerotb.ui.home.report

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.zerotb.zerotb.data.helper.alertDialog
import com.zerotb.zerotb.data.helper.pdfDrug
import com.zerotb.zerotb.data.helper.pdfPatient
import com.zerotb.zerotb.databinding.FragmentReportBinding
import com.zerotb.zerotb.ui.home.report.adapter.ReportAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportFragment : Fragment() {

    private lateinit var binding: FragmentReportBinding
    private lateinit var reportAdapter: ReportAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportBinding.inflate(inflater, container, false)
        updateUI()
        return binding.root
    }

    private fun updateUI() = with(binding) {
        reportAdapter = ReportAdapter {
            when (it) {
                "1" -> {
                    requireContext().alertDialog(
                        "Tekan Ya jika anda ingin mencetak semua data pasien",
                        "Ya",
                        "Tidak"
                    ) { _, _ ->
                        val request = DownloadManager.Request(Uri.parse(pdfPatient()))
                            .setTitle("Download Data Pasien")
                            .setDescription("Downloading....")
                            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Data Pasien.pdf")
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            .setAllowedOverMetered(true)

                        val dm =
                            requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                        dm.enqueue(request)
                    }
                }
                "2" -> {
                    requireContext().alertDialog(
                        "Tekan Ya jika anda ingin mencetak obat pasien",
                        "Ya",
                        "Tidak"
                    ) { _, _ ->
                        val request = DownloadManager.Request(Uri.parse(pdfDrug()))
                            .setTitle("Download Data Obat Pasien")
                            .setDescription("Downloading....")
                            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Data Obat Pasien.pdf")
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            .setAllowedOverMetered(true)

                        val dm =
                            requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                        dm.enqueue(request)
                    }
                }
            }
        }
        rvReport.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext())
            adapter = reportAdapter
        }
    }

}