package com.app0.simforpay.adapter
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.app0.simforpay.R
import com.app0.simforpay.activity.friends.FriendsFrag
import com.app0.simforpay.retrofit.RetrofitHelper
import com.app0.simforpay.retrofit.domain.*
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.request_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RequestAdapter(private val requestList: List<Data>, context: Context, fragmentManager: FragmentManager, getFriendsList: ArrayList<GetReqFriendsSuccess>) : RecyclerView.Adapter<RequestAdapter.RequestViewHolder>() {

    private val context = context
    private val fragmentManager = fragmentManager
    private val getFriendsList = getFriendsList
    private val Retrofit = RetrofitHelper.getRetrofit()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RequestViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.request_item, parent, false)

        return RequestViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val currentItem = requestList[position]

        Glide.with(context).load(R.drawable.img_profile).circleCrop().into(holder.img) // image circle crop
        holder.name.text = currentItem.main
        holder.id.text = currentItem.sub

        val getReqFriendsId = getFriendsList[position]
        holder.btnConfirm.setOnClickListener {
            val idInfo = id(getReqFriendsId.id.toInt())
            Retrofit.AcceptReqFriend(idInfo)
                .enqueue(object : Callback<ResResultSuccess> {
                    override fun onResponse(
                        call: Call<ResResultSuccess>,
                        response: Response<ResResultSuccess>
                    ) {
                        if (response.body()?.result == "true") {
                            fragmentManager!!.beginTransaction().replace(R.id.layFull, FriendsFrag()).commit()

                        } else
                            Toast.makeText(context, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<ResResultSuccess>, t: Throwable) {
                        Log.d("Fail","false")

                    }
                })
        }
        holder.btnDelete.setOnClickListener {
            val idInfo = id(getReqFriendsId.id.toInt())
            Retrofit.RefuseReqFriend(idInfo)
                .enqueue(object : Callback<ResResultSuccess> {
                    override fun onResponse(
                        call: Call<ResResultSuccess>,
                        response: Response<ResResultSuccess>
                    ) {
                        if (response.body()?.result == "true") {
                            fragmentManager!!.beginTransaction().replace(R.id.layFull, FriendsFrag()).commit()

                        } else
                            Toast.makeText(context, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<ResResultSuccess>, t: Throwable) {
                        Log.d("Fail","false")

                    }
                })
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