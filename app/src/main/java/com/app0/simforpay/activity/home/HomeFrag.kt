package com.app0.simforpay.activity.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.app0.simforpay.R
import com.app0.simforpay.adapter.ContractAdapter
import com.app0.simforpay.adapter.Data
import com.app0.simforpay.util.CustomBottomSheetDialog
import kotlinx.android.synthetic.main.frag_home.*


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

        val contractList = generateDummyList(3) // viewpager에 들어갈 정보

        vpContract.adapter = ContractAdapter(contractList, requireContext())

        // display 비율에 맞춰 padding과 margin setting
        val dpValue = 80
        val displaySize = resources.displayMetrics.density
        val margin = (dpValue * displaySize).toInt()
        vpContract.setPadding(margin/2, 0, margin+margin/2, 0)
        vpContract.pageMargin = margin/2

        vpContract.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }
            override fun onPageSelected(position: Int) {
                // 첫페이지와 끝페이지 padding, margin 값 조절
                when (position) {
                    0 -> vpContract.setPadding(margin/2, 0, margin+margin/2, 0)
                    vpContract.size-1 -> vpContract.setPadding(margin+margin/2, 0, margin/2, 0)
                    else -> vpContract.setPadding(margin, 0, margin, 0)
                }

            }
        })
    }

    override fun onResume() {
        super.onResume()

        btnSearch.setOnClickListener {
//            requireFragmentManager().beginTransaction().add(R.id.layFull, SearchFrag()).commit()

            val dialog = CustomBottomSheetDialog.CustomBottomSheetDialogBuilder()
                .setBtnClickListener(object : CustomBottomSheetDialog.CustomBottomSheetDialogListener {
                    override fun onClickMenu1Btn() {
                        Toast.makeText(context, "First Button Clicked", Toast.LENGTH_SHORT).show()
                    }
                    override fun onClickMenu2Btn() {
                        Toast.makeText(context, "Second Button Clicked", Toast.LENGTH_SHORT).show()
                    }
                    override fun onClickMenu3Btn() {
                        Toast.makeText(context, "Third Button Clicked", Toast.LENGTH_SHORT).show()
                    }
                }).create()
            dialog.show(parentFragmentManager, dialog.tag)
        }

        btnNotification.setOnClickListener {
            requireFragmentManager().beginTransaction().add(R.id.layFull, NotificationFrag()).commit()
        }

        btnMypage.setOnClickListener {
            requireFragmentManager().beginTransaction().add(R.id.layFull, MypageFrag()).commit()
        }
    }

    private fun generateDummyList(size: Int): List<Data> {
        val list = ArrayList<Data>()

        for(i in 0 until size){
            val item = Data("a", "b")
            list += item
        }

        return list
    }
}