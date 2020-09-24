package com.app0.simforpay.activity.login

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

        var requestPermissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        val permissionCheck = SigninAct.PermissionCheck(this, requestPermissions)
        permissionCheck.permissionCheck()

        btnSignin.setOnClickListener{

            val userInfo = Signin(siId.text.toString(), siPw.text.toString())

            Retrofit.SigninCall(userInfo)
                .enqueue(object : Callback<ResUesrSuccess>{
                    override fun onResponse(call: Call<ResUesrSuccess>, response: Response<ResUesrSuccess>) {
                        if (response.body()?.result=="true"){
                            MyApplication.prefs.setBoolean(Key.AutoLogin.toString(), cbAutoLogin.isChecked) // 자동 로그인 체크 여부 확인
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

    class PermissionCheck(val permissionActivity: Activity, val requirePermissions: Array<String>) {

        private val permissionRequestCode = 100

        //권한 체크용
        public fun permissionCheck() {
            var failRequestPermissionList = ArrayList<String>()

            for(permission in  requirePermissions) {
                if(ContextCompat.checkSelfPermission(
                        permissionActivity.applicationContext,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED) {
                    failRequestPermissionList.add(permission)
                }
            }

            if(failRequestPermissionList.isNotEmpty()) {
                val array = arrayOfNulls<String>(failRequestPermissionList.size)
                ActivityCompat.requestPermissions(
                    permissionActivity, failRequestPermissionList.toArray(
                        array
                    ), permissionRequestCode
                )
            }
        }
    }
}