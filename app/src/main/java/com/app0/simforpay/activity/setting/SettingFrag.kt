package com.app0.simforpay.activity.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app0.simforpay.R
import com.app0.simforpay.activity.login.SigninAct
import com.app0.simforpay.util.dialog.CustomDialog
import com.app0.simforpay.util.sharedpreferences.MyApplication
import kotlinx.android.synthetic.main.frag_setting.*

class SettingFrag : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_setting, container, false)
    }

    override fun onResume() {
        super.onResume()

        privacyPolicy.setOnClickListener{
            requireFragmentManager().beginTransaction().replace(R.id.layFull, PrivacypolicyFrag()).addToBackStack(null).commit()
        }

        opensourceLicense.setOnClickListener {
            requireFragmentManager().beginTransaction().replace(R.id.layFull, OpensourceFrag()).addToBackStack(null).commit()
        }

        logout.setOnClickListener {
            ShowAlertDialog()
        }

        withdrawal.setOnClickListener {
            requireFragmentManager().beginTransaction().replace(R.id.layFull, WithdrawalFrag()).addToBackStack(null).commit()
        }
    }

    fun ShowAlertDialog() {
        val dialog = CustomDialog.CustomDialogBuilder()
            .setSubTitle("로그아웃하시겠습니까?")
            .setNegativeBtnText("취소")
            .setPositiveBtnText("확인")
            .setBtnClickListener(object : CustomDialog.CustomDialogListener {
                override fun onClickPositiveBtn() {
                    MyApplication.prefs.clear()

                    val intent = Intent(context, SigninAct::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            }).create()
        dialog.show(parentFragmentManager, dialog.tag)
    }
}