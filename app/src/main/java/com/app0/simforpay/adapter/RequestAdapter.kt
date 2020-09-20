package com.app0.simforpay.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app0.simforpay.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.request_item.view.*

class RequestAdapter(private val requestList: List<Data>, context: Context) : RecyclerView.Adapter<RequestAdapter.RequestViewHolder>() {

    private val context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RequestViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.request_item, parent, false)

        return RequestViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val currentItem = requestList[position]

        Glide.with(context).load(R.drawable.img_profile).circleCrop().into(holder.img) // image circle crop
        holder.name.text = currentItem.main
        holder.id.text = currentItem.sub
        holder.btnConfirm.setOnClickListener {
            // 확인 버튼 클릭
        }
        holder.btnDelete.setOnClickListener {
            // 삭제 버튼 클릭
        }
    }

    override fun getItemCount() = requestList.size

    class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.imgFr
        val name: TextView = itemView.frName
        val id: TextView = itemView.frId
        val btnConfirm: Button = itemView.btnConfirm
        val btnDelete: Button = itemView.btnDelete
    }
}