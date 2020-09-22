package com.app0.simforpay.util.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app0.simforpay.R
import com.app0.simforpay.retrofit.domain.FriendsSuccess
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.friends_bottomsheet.*

private lateinit var FriendInfo:FriendsSuccess

class CustomFriendsBottomSheetDialog : BottomSheetDialogFragment() {

    var listener: CustomBottomSheetDialogListener? = null


    interface CustomBottomSheetDialogListener {
        fun onClickMenu3Btn() {}
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.friends_bottomsheet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        view?.apply {
            menu1.setOnClickListener {
                dismiss()
                listener?.onClickMenu3Btn()
            }
            Log.d("B",FriendInfo.toString())
            frName.setText(FriendInfo.friendsName)
            frId.setText("@" + FriendInfo.friendsMyid)
            frPhone.setText(FriendInfo.friendsPhoneNum)
            frAccount.setText(FriendInfo.friendsBank + " " + FriendInfo.friendsAccount)

        }
    }

    class CustomBottomSheetDialogBuilder() {
        private val dialog = CustomFriendsBottomSheetDialog()

        fun setBtnClickListener(listener: CustomBottomSheetDialogListener, getFriendsList:FriendsSuccess): CustomBottomSheetDialogBuilder {
            dialog.listener = listener
            FriendInfo = getFriendsList
            return this
        }

        fun create(): CustomFriendsBottomSheetDialog {
            return dialog
        }
    }
}