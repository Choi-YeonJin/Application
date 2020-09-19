package com.app0.simforpay.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app0.simforpay.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.contract_bottomsheet.view.*

class CustomBottomSheetDialog : BottomSheetDialogFragment() {

    var listener: CustomBottomSheetDialogListener? = null

    interface CustomBottomSheetDialogListener {
        fun onClickMenu1Btn() {}
        fun onClickMenu2Btn() {}
        fun onClickMenu3Btn() {}
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.contract_bottomsheet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        view?.apply {
            menu1.setOnClickListener {
                dismiss()
                listener?.onClickMenu1Btn()
            }
            menu2.setOnClickListener {
                dismiss()
                listener?.onClickMenu2Btn()
            }
            menu3.setOnClickListener {
                dismiss()
                listener?.onClickMenu3Btn()
            }
        }
    }

    class CustomBottomSheetDialogBuilder() {
        private val dialog = CustomBottomSheetDialog()

        fun setBtnClickListener(listener: CustomBottomSheetDialogListener): CustomBottomSheetDialogBuilder {
            dialog.listener = listener
            return this
        }

        fun create(): CustomBottomSheetDialog {
            return dialog
        }
    }
}