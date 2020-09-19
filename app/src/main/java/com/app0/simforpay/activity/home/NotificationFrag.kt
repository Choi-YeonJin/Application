package com.app0.simforpay.activity.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app0.simforpay.R
import com.app0.simforpay.activity.MainAct
import com.app0.simforpay.adapter.Data
import com.app0.simforpay.adapter.NotificationAdapter
import kotlinx.android.synthetic.main.frag_notification.*

class NotificationFrag : Fragment() {
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
        return inflater.inflate(R.layout.frag_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notiList = generateDummyList(3)

        rvNotification.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        rvNotification.adapter = NotificationAdapter(notiList)
        rvNotification.setHasFixedSize(true)
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

    private fun generateDummyList(size: Int): List<Data> {
        val list = ArrayList<Data>()

        for (i in 0 until size) {
            val item = Data("a", "b")
            list += item
        }

        return list
    }
}