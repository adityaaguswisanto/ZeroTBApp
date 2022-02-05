package com.zerotb.zerotb.ui.home.consult.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zerotb.zerotb.data.helper.formatDateWithTime
import com.zerotb.zerotb.data.responses.data.Consult
import com.zerotb.zerotb.databinding.ConsultItemBinding

class ConsultAdapter(
    private val listener: OnItemClickListener
) : PagingDataAdapter<Consult, ConsultAdapter.MyViewHolder>(PatientComparator) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ConsultItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class MyViewHolder(private val binding: ConsultItemBinding) :
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

        fun bind(consult: Consult) = with(binding) {
            txtName.text = consult.patient
            txtDate.text = "Waktu Konsultasi : ${formatDateWithTime(consult.created_at)}"
            txtConsult.text = consult.consult
            txtDoctor.text = consult.doctor
        }
    }

    object PatientComparator : DiffUtil.ItemCallback<Consult>() {
        override fun areItemsTheSame(oldItem: Consult, newItem: Consult): Boolean {
            return oldItem.id_consult == newItem.id_consult
        }

        override fun areContentsTheSame(oldItem: Consult, newItem: Consult): Boolean {
            return oldItem == newItem
        }
    }

    interface OnItemClickListener {
        fun onItemClick(consult: Consult)
    }

}