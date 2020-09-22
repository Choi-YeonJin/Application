package com.app0.simforpay.activity.setting

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.app0.simforpay.R
import com.app0.simforpay.activity.MainAct
import kotlinx.android.synthetic.main.frag_opensource.*
import kotlinx.android.synthetic.main.frag_privacypolicy.btnBack

class OpensourceFrag : Fragment() {

    private lateinit var callback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Press Back Button
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                fragmentManager?.popBackStackImmediate()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainAct = activity as MainAct
        mainAct.HideBottomNavi(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_opensource, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wbOpensource.loadUrl("file:///android_asset/www/opensource.html")
    }

    override fun onResume() {
        super.onResume()

        btnBack.setOnClickListener {
            fragmentManager?.popBackStackImmediate()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        val mainAct = activity as MainAct
        mainAct.HideBottomNavi(false)
    }
}