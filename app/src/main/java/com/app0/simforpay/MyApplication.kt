package com.app0.simforpay

import android.app.Application

class MyApplication : Application() {
    companion object {
        lateinit var prefs: PreferenceUtil }
    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)
        super.onCreate()
    }
}