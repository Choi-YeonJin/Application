package com.app0.simforpay.adapter
import android.content.Context
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
import com.app0.simforpay.activity.contract.ContractShareFrag
import com.app0.simforpay.activity.friends.FriendsFrag
import com.app0.simforpay.retrofit.RetrofitHelper
import com.app0.simforpay.retrofit.domain.*
import com.app0.simforpay.util.sharedpreferences.Key
import com.app0.simforpay.util.sharedpreferences.MyApplication
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.frag_contract.*
import kotlinx.android.synthetic.main.friends_item.view.frId
import kotlinx.android.synthetic.main.friends_item.view.frName
import kotlinx.android.synthetic.main.friends_item.view.imgFr
import kotlinx.android.synthetic.main.friends_req_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendsReqAdapter(private val freindsReqList: List<Data>, context: Context, fragmentManager: FragmentManager, getFriendsList: List<User>) : RecyclerView.Adapter<FriendsReqAdapter.FriendsReqViewHolder>() {

    private val context = context
    private val fragmentManager = fragmentManager
    private val Retrofit = RetrofitHelper.getRetrofit()
    private val getFriendsList = getFriendsList

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
            var applicant_id = Integer.parseInt(MyApplication.prefs.getString(Key.LENDER_ID.toString(), ""))
            val recipient_id = getFriendsList[position].id
            val recipient_name = getFriendsList[position].name
            val friendsReqInfo = FriendReq(applicant_id, recipient_id,recipient_name)
            Retrofit.FriendsReqCall(friendsReqInfo)
                .enqueue(object : Callback<FriendReqSuccess> {
                    override fun onResponse(
                        call: Call<FriendReqSuccess>,
                        response: Response<FriendReqSuccess>
                    ) {
                        if (response.body()?.result == "true") {
                            fragmentManager!!.beginTransaction().replace(R.id.layFull, FriendsFrag()).commit()

                        } else
                            Toast.makeText(context, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<FriendReqSuccess>, t: Throwable) {

                    }
                })
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