package com.zerotb.zerotb.ui.home.report.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.zerotb.zerotb.R
import com.zerotb.zerotb.data.responses.data.Report
import com.zerotb.zerotb.databinding.SettingsItemBinding

class ReportAdapter(
    private val callBack: (name: String) -> Unit
) : RecyclerView.Adapter<ReportAdapter.ViewHolder>() {

    private val list = listOf(
        Report("1", "Semua Pasien", R.drawable.ic_people_patient),
        Report("2", "Obat Pasien", R.drawable.ic_healing),
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            SettingsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        with(holder.binding) {
            clSettings.setOnClickListener { callBack.invoke(item.name) }
            txtTitle.text = item.title
            ivIcon.setImageDrawable(ContextCompat.getDrawable(root.context, item.imageId))
        }
    }

    override fun getItemCount(): Int = list.size

    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        val binding = SettingsItemBinding.bind(view)
    }
}