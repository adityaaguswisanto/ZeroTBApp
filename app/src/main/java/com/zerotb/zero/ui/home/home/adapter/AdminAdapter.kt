package com.zerotb.zero.ui.home.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.zerotb.zero.R
import com.zerotb.zero.data.responses.data.Doctor
import com.zerotb.zero.databinding.HomeItemBinding

class AdminAdapter(
    private val callBack: (name: String) -> Unit
) : RecyclerView.Adapter<AdminAdapter.ViewHolder>() {

    private val list = listOf(
        Doctor("1", "Input Data\nPasien", R.drawable.operator),
        Doctor("2", "Semua Data\nPasien", R.drawable.doctor),
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            HomeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        with(holder.binding) {
            clHome.setOnClickListener { callBack.invoke(item.name) }
            txtTitle.text = item.title
            ivIcon.setImageDrawable(ContextCompat.getDrawable(root.context, item.imageId))
        }
    }

    override fun getItemCount(): Int = list.size

    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        val binding = HomeItemBinding.bind(view)
    }
}