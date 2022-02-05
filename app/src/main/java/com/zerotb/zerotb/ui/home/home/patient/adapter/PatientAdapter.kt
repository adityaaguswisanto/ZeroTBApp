package com.zerotb.zerotb.ui.home.home.patient.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zerotb.zerotb.data.helper.loadImage
import com.zerotb.zerotb.data.helper.urlAssets
import com.zerotb.zerotb.data.responses.data.User
import com.zerotb.zerotb.databinding.PatientItemBinding

class PatientAdapter(
    private val listener: OnItemClickListener
) : PagingDataAdapter<User, PatientAdapter.MyViewHolder>(PatientComparator) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            PatientItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class MyViewHolder(private val binding: PatientItemBinding) :
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

        fun bind(user: User) = with(binding) {
            txtMedical.text = user.medical
            txtNoPatient.text = "No. Pasien : ${user.id}"
            txtName.text = user.name
            if (user.profile != null){
                cmvProfile.loadImage("${urlAssets()}${user.profile}")
            }
        }
    }

    object PatientComparator : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

    interface OnItemClickListener {
        fun onItemClick(user: User)
    }

}