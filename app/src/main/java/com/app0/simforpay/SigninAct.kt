package com.app0.simforpay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.app0.simforpay.retrofit.RetrofitHelper
import com.app0.simforpay.retrofit.domain.Signin
import com.app0.simforpay.retrofit.domain.SigninSuccess
import kotlinx.android.synthetic.main.act_signin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SigninAct : AppCompatActivity() {

    private val userRetrofit = RetrofitHelper.getUserRetrofit()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_signin)
      
        btnSignin.setOnClickListener{

            val userInfo = Signin(siId.text.toString(), siPw.text.toString())

            userRetrofit.SigninCall(userInfo)
                .enqueue(object : Callback<SigninSuccess>{
                    override fun onResponse(call: Call<SigninSuccess>, response: Response<SigninSuccess>) {
                        if (response.body()?.result=="true"){
                            PreferenceUtil(applicationContext).setString(Key.LENDER_ID.toString(), response.body()?.userId.toString())
                            startActivity(Intent(applicationContext, MainAct::class.java))
                            finish()
                        }
                        else{
                            Toast.makeText(applicationContext, "로그인에 실패하였습니다. 잠시후 재시도 해주세요.", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<SigninSuccess>, t: Throwable) {

                    }
                })
        }

        btnSignup.setOnClickListener{
            startActivity(Intent(applicationContext, PhoneAct::class.java))
            finish()
        }

        TextInput.CheckTwo(btnSignin, siId, siPw)
    }
}