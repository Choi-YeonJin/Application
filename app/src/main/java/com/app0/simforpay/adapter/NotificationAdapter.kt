package com.app0.simforpay.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app0.simforpay.R
import kotlinx.android.synthetic.main.notification_item.view.*

class NotificationAdapter(private val notiList: List<Data>) : RecyclerView.Adapter<NotificationAdapter.NotiViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : NotiViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.notification_item, parent, false)

        return NotiViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotiViewHolder, position: Int) {
        val currentItem = notiList[position]

        holder.img.setImageResource(R.drawable.logo_blue)
        holder.main.text = currentItem.main
        holder.sub.text = currentItem.sub
        holder.btn.setOnClickListener { 
            // 삭제 버튼 클릭
        }
    }

    override fun getItemCount() = notiList.size

    class NotiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.imgNoti
        val main: TextView = itemView.main
        val sub: TextView = itemView.sub
        val btn: ImageButton = itemView.btnClose
    }
}