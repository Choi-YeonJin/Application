package com.app0.simforpay.activity.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.app0.simforpay.R
import com.app0.simforpay.util.RegularExpression
import com.app0.simforpay.util.TextInput
import kotlinx.android.synthetic.main.act_phone.*

class PhoneAct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_phone)

        btnBack.setOnClickListener{
            finish()
        }

        phoneNum.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                layPhoneNum.error = null // 중복체크 실패 후 edittext 바뀌면 error 제거
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        btnSend.setOnClickListener {
            btnSend.text = "재전송"
        }

        btnNext.setOnClickListener{
            if(RegularExpression.Vaild(phoneNum)){
                val intent = Intent(applicationContext, SignupAct::class.java)
                intent.putExtra("phoneNumber", phoneNum.text.toString())
                startActivity(intent)
            }
            else {
                layPhoneNum.error = "전화번호 형식이 틀렸습니다."
            }

        }

        TextInput.CheckOne(btnNext, phoneNum)
    }
}