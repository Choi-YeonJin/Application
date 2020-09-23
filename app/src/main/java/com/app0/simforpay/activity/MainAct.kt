package com.app0.simforpay.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.app0.simforpay.R
import com.app0.simforpay.databinding.ActMainBinding

class MainAct : AppCompatActivity() {
    private lateinit var binding: ActMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initNavigation()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun initNavigation() {
        val navController = findNavController(R.id.navi_host)
        binding.navi.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.fragContract) {
                binding.navi.visibility = View.GONE
            } else {
                binding.navi.visibility = View.VISIBLE
            }
        }
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_main)
        binding.lifecycleOwner = this
    }

    fun HideBottomNavi(state: Boolean){
        if(state) binding.navi.visibility = View.GONE else binding.navi.visibility = View.VISIBLE
    }

    fun getNotificationBuilder(id: String, name: String) : NotificationCompat.Builder {
        val builder: NotificationCompat.Builder?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // 8.0 오레오 버전
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT)// 중요도에 따라 메시지가 보이는 순서가 달라진다.
            channel.enableLights(true) //
            channel.lightColor = R.color.mainBlue
            channel.enableVibration(true)
            manager.createNotificationChannel(channel)
            builder = NotificationCompat.Builder(this, id)
        } else { // 그 외의 버전s
            builder = NotificationCompat.Builder(this)
        }

        return builder
    }
}