package com.app0.simforpay.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
}