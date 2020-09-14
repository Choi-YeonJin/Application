package com.app0.simforpay.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app0.simforpay.R
import com.app0.simforpay.activity.recyclerview.ContractData

class HomeFrag : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val contractList = generateDummyList(3)
//
//        rvContract.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL ,false)
//        rvContract.adapter = ContractAdapter(contractList)
//
//        rvContract.setHasFixedSize(true)
    }

    private fun generateDummyList(size: Int): List<ContractData> {
        val list = ArrayList<ContractData>()

        for(i in 0 until size){
            val item = ContractData("a", "b")
            list += item
        }

        return list
    }
}