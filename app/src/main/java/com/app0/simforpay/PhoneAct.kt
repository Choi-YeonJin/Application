package com.app0.simforpay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.act_phone.*
import kotlinx.android.synthetic.main.act_phone.btnBack
import kotlinx.android.synthetic.main.act_signup.*

class PhoneAct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_phone)

        btnBack.setOnClickListener{
            val intent = Intent(applicationContext, SigninAct::class.java)
            startActivity(intent)
            finish()
        }

        btnNext.setOnClickListener{
            val intent = Intent(applicationContext, SignupAct::class.java)
            intent.putExtra("phoneNumber", phoneNum.text.toString())
            startActivity(intent)
            finish()
        }

        btnSend.setOnClickListener {
            btnSend.text = "재전송"
        }
    }
}