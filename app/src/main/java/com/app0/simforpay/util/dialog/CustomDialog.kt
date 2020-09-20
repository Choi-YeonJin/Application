package com.app0.simforpay.util.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.app0.simforpay.R
import kotlinx.android.synthetic.main.custom_dialog.view.*

class CustomDialog(): DialogFragment() {

    var title: String? = null
    var message: String? = null
    var btnPositiveText: String? = null
    var btnNegativeText: String? = null
    var listener: CustomDialogListener? = null

    interface CustomDialogListener {
        fun onClickPositiveBtn() {}
        fun onClickNegativeBtn() {}
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.custom_dialog, container, false)
        return view.rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.apply {
            dialogTitle.text = title
            dialogMessage.text = message
            btnNegative.text = btnNegativeText
            btnNegative.setOnClickListener {
                dismiss()
                listener?.onClickNegativeBtn()
            }
            btnPositive.text = btnPositiveText
            btnPositive.setOnClickListener {
                dismiss()
                listener?.onClickPositiveBtn()
            }
        }
    }

    class CustomDialogBuilder {
        private val dialog = CustomDialog()

        fun setTitle(title: String): CustomDialogBuilder {
            dialog.title = title
            return this
        }

        fun setMessage(message: String): CustomDialogBuilder {
            dialog.message = message
            return this
        }

        fun setNegativeBtnText(text: String): CustomDialogBuilder {
            dialog.btnNegativeText = text
            return this
        }

        fun setPositiveBtnText(text: String): CustomDialogBuilder {
            dialog.btnPositiveText = text
            return this
        }

        fun setBtnClickListener(listener: CustomDialogListener): CustomDialogBuilder {
            dialog.listener = listener
            return this
        }

        fun create(): CustomDialog {
            return dialog
        }
    }
}