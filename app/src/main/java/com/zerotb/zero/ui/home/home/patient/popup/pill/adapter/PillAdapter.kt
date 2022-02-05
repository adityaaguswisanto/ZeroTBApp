package com.zerotb.zero.ui.home.home.patient.popup.pill.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zerotb.zero.data.responses.data.Pill
import com.zerotb.zero.databinding.PillItemBinding

class PillAdapter(
    private val listener: OnItemClickListener
) : PagingDataAdapter<Pill, PillAdapter.MyViewHolder>(PillComparator) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            PillItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class MyViewHolder(private val binding: PillItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }
            }
        }

        fun bind(pill: Pill) = with(binding) {
            txtName.text = pill.patient
            txtNoPatientAndMedical.text =
                "No. Pasien : ${pill.user} | ${pill.medical}"
            txtHour.text = "Waktu minum : ${pill.hour}"
            txtDrug.text = pill.drug
        }
    }

    object PillComparator : DiffUtil.ItemCallback<Pill>() {
        override fun areItemsTheSame(oldItem: Pill, newItem: Pill): Boolean {
            return oldItem.id_drug == newItem.id_drug
        }

        override fun areContentsTheSame(oldItem: Pill, newItem: Pill): Boolean {
            return oldItem == newItem
        }
    }

    interface OnItemClickListener {
        fun onItemClick(pill: Pill)
    }

}