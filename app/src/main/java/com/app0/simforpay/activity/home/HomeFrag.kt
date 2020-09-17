package com.app0.simforpay.activity.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.app0.simforpay.R
import com.app0.simforpay.activity.Key
import com.app0.simforpay.adapter.ContractAdapter
import com.app0.simforpay.adapter.Data
import com.app0.simforpay.retrofit.RetrofitHelper
import kotlinx.android.synthetic.main.frag_home.*
import com.app0.simforpay.retrofit.domain.ContractContentSuccess
import com.app0.simforpay.util.sharedpreferences.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFrag : Fragment() {

    private val Retrofit = RetrofitHelper.getRetrofit()
    private val Title = mutableListOf<String>()
    private val Content = mutableListOf<String>()
    var cnt=0

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
        var id=Integer.parseInt(MyApplication.prefs.getString(Key.LENDER_ID.toString(), ""))

        Retrofit.getContracts(id).enqueue(object : Callback<List<ContractContentSuccess>> {
            override fun onResponse(call: Call<List<ContractContentSuccess>>, response: Response<List<ContractContentSuccess>>) {
                response.body()?.forEach {
                    Title.add(it.title)
                    Content.add(it.content)
                    cnt++
                }
                val list = ArrayList<Data>()
                for(i in 0 until cnt){
                    val item = Data(Title[i], Content[i])
                    list += item
                }
                vpContract.adapter = ContractAdapter(list, requireContext())
            }

            override fun onFailure(call: Call<List<ContractContentSuccess>>, t: Throwable) {}

        })

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
            requireFragmentManager().beginTransaction().add(R.id.layFull, SearchFrag()).commit()
        }

        btnNotification.setOnClickListener {
            requireFragmentManager().beginTransaction().add(R.id.layFull, NotificationFrag()).commit()
        }

        btnMypage.setOnClickListener {
            requireFragmentManager().beginTransaction().add(R.id.layFull, MypageFrag()).commit()
        }
    }
}