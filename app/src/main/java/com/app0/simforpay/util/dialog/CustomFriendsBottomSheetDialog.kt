package com.app0.simforpay.util.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app0.simforpay.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.friends_bottomsheet.*

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
        }
    }

    class CustomBottomSheetDialogBuilder() {
        private val dialog = CustomFriendsBottomSheetDialog()

        fun setBtnClickListener(listener: CustomBottomSheetDialogListener): CustomBottomSheetDialogBuilder {
            dialog.listener = listener
            return this
        }

        fun create(): CustomFriendsBottomSheetDialog {
            return dialog
        }
    }
}