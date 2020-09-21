package com.app0.simforpay.activity.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app0.simforpay.R
import com.app0.simforpay.activity.MainAct
import com.app0.simforpay.retrofit.RetrofitHelper
import com.app0.simforpay.retrofit.domain.ResUesrSuccess
import com.app0.simforpay.retrofit.domain.Signin
import com.app0.simforpay.util.TextInput
import com.app0.simforpay.util.sharedpreferences.Key
import com.app0.simforpay.util.sharedpreferences.MyApplication
import kotlinx.android.synthetic.main.act_signin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SigninAct : AppCompatActivity() {

    private val Retrofit = RetrofitHelper.getRetrofit()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_signin)
      
        btnSignin.setOnClickListener{

            val userInfo = Signin(siId.text.toString(), siPw.text.toString())

            Retrofit.SigninCall(userInfo)
                .enqueue(object : Callback<ResUesrSuccess>{
                    override fun onResponse(call: Call<ResUesrSuccess>, response: Response<ResUesrSuccess>) {
                        if (response.body()?.result=="true"){
                            MyApplication.prefs.setString(Key.LENDER_ID.toString(), response.body()?.userId.toString())
                            startActivity(Intent(applicationContext, MainAct::class.java))
                            finish()
                        }
                        else{
                            Toast.makeText(applicationContext, "로그인에 실패했습니다. 아이디 혹은 비밀번호를 확인해주세요.", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<ResUesrSuccess>, t: Throwable) {

                    }
                })
        }

        btnSignup.setOnClickListener{
//            startActivity(Intent(applicationContext, PhoneAct::class.java))
            startActivity(Intent(applicationContext, SignupAct::class.java))
        }

        TextInput.CheckTwo(btnSignin, siId, siPw)
    }
}