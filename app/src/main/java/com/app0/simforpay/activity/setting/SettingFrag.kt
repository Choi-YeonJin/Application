package com.app0.simforpay.activity.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app0.simforpay.R
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

        }

        opensourceLicense.setOnClickListener {

        }

        logout.setOnClickListener {

        }

        withdrawal.setOnClickListener {
            requireFragmentManager().beginTransaction().replace(R.id.layFull, WithdrawalFrag()).addToBackStack(null).commit()
        }
    }
}