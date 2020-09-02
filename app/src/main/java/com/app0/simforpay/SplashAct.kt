package com.app0.simforpay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

private val SPLASH_TIME_OUT:Long = 2000

class SplashAct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_splash)

        Handler().postDelayed({

            if(MyApplication.prefs.getString("id", "") == ""){
                startActivity(Intent(this,SigninAct::class.java))
            }else{
                startActivity(Intent(this,MainAct::class.java))
            }

            finish()
        }, SPLASH_TIME_OUT)
    }
}