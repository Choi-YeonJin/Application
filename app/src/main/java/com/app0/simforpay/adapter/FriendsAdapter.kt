package com.app0.simforpay.adapter
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView
import com.app0.simforpay.R
import com.app0.simforpay.retrofit.domain.ContractContentSuccess
import com.app0.simforpay.retrofit.domain.FriendsSuccess
import com.app0.simforpay.util.dialog.CustomFriendsBottomSheetDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.contract_item.view.*
import kotlinx.android.synthetic.main.friends_bottomsheet.view.*
import kotlinx.android.synthetic.main.friends_item.view.*
import kotlinx.android.synthetic.main.friends_item.view.frId
import kotlinx.android.synthetic.main.friends_item.view.frName

class FriendsAdapter(private val freindsList: List<Data>, context: Context, fragmentManager: FragmentManager, getFriendsList: ArrayList<FriendsSuccess>) : RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder>() {

    private val context = context
    private val fragmentManager = fragmentManager
    private val getFriendsList = getFriendsList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : FriendsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.friends_item, parent, false)

        return FriendsViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        val currentItem = freindsList[position]

        Glide.with(context).load(R.drawable.img_profile).circleCrop().into(holder.img) // image circle crop
        holder.name.text = currentItem.main
        holder.id.text = currentItem.sub
        holder.btn.setOnClickListener {
            val dialog = CustomFriendsBottomSheetDialog.CustomBottomSheetDialogBuilder()
                .setBtnClickListener(object :
                    CustomFriendsBottomSheetDialog.CustomBottomSheetDialogListener {
                    override fun onClickMenu3Btn() {

                    }
                }).create()
            dialog.show(fragmentManager, dialog.tag)
        }
    }

    override fun getItemCount() = freindsList.size

    class FriendsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.imgFr
        val name: TextView = itemView.frName
        val id: TextView = itemView.frId
        val btn: ImageButton = itemView.btnFrSetting
    }
}