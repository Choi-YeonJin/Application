package com.app0.simforpay.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.app0.simforpay.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.friends_item.view.frId
import kotlinx.android.synthetic.main.friends_item.view.frName
import kotlinx.android.synthetic.main.friends_item.view.imgFr
import kotlinx.android.synthetic.main.friends_req_item.view.*

class FriendsReqAdapter(private val freindsReqList: List<Data>, context: Context, fragmentManager: FragmentManager) : RecyclerView.Adapter<FriendsReqAdapter.FriendsReqViewHolder>() {

    private val context = context
    private val fragmentManager = fragmentManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : FriendsReqViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.friends_req_item, parent, false)

        return FriendsReqViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: FriendsReqViewHolder, position: Int) {
        val currentItem = freindsReqList[position]

        Glide.with(context).load(R.drawable.img_profile).circleCrop().into(holder.img) // image circle crop
        holder.name.text = currentItem.main
        holder.id.text = currentItem.sub
        holder.btn.setOnClickListener {
            // 친구 추가 버튼 클릭
        }
    }

    override fun getItemCount() = freindsReqList.size

    class FriendsReqViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.imgFr
        val name: TextView = itemView.frName
        val id: TextView = itemView.frId
        val btn: Button = itemView.btnAddFriends
    }
}