package com.zerotb.zerotb.ui.home.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.zerotb.zerotb.R
import com.zerotb.zerotb.data.responses.data.Patient
import com.zerotb.zerotb.databinding.HomeItemBinding

class UserAdapter (
    private val callBack: (name: String) -> Unit
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private val list = listOf(
        Patient("1", "Dokumentasi", R.drawable.doctor),
        Patient("2", "Video Call\nVia WhatsApp", R.drawable.videocall),
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