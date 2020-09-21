package com.app0.simforpay.activity.home

import android.os.Bundle
import android.util.Log
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
import com.app0.simforpay.retrofit.RetrofitHelper
import com.app0.simforpay.retrofit.domain.ContractContentSuccess
import com.app0.simforpay.retrofit.domain.User
import com.app0.simforpay.util.sharedpreferences.Key
import com.app0.simforpay.util.sharedpreferences.MyApplication
import kotlinx.android.synthetic.main.frag_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFrag : Fragment() {

    private val Retrofit = RetrofitHelper.getRetrofit()
    private val Title = mutableListOf<String>()
    private val Content = mutableListOf<String>()
    var cnt = 0
    private var User = arrayOf<String?>()
    private var getContractContent = arrayListOf<ContractContentSuccess>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var id = Integer.parseInt(MyApplication.prefs.getString(Key.LENDER_ID.toString(), ""))

        Retrofit.getUser(id).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                User = arrayOf(
                    response.body()?.name,
                    "@" + response.body()?.myId,
                    response.body()?.phone_num,
                    response.body()?.bank,
                    response.body()?.account,
                    response.body()?.image_url
                )
            }

            override fun onFailure(call: Call<User>, t: Throwable) {}

        })

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

        // display 비율에 맞춰 padding과 margin setting
//        val display = activity?.windowManager?.defaultDisplay
//        val size = Point()
//        display?.getSize(size)

        val dpValue = 80
        val displaySize = resources.displayMetrics.density
        val margin = (dpValue * displaySize).toInt()
        vpContract.setPadding(margin / 2, 0, margin + margin / 2, 0)
        vpContract.pageMargin = margin / 2

        vpContract.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                // 첫페이지와 끝페이지 padding, margin 값 조절
                Log.d("page", position.toString())
                when (position) {
                    0 -> vpContract.setPadding(margin / 2, 0, margin + margin / 2, 0)
                    vpContract.size - 1 -> vpContract.setPadding(
                        margin + margin / 2,
                        0,
                        margin / 2,
                        0
                    )
                    else -> vpContract.setPadding(margin, 0, margin, 0)
                }

            }
        })
    }

    override fun onResume() {
        super.onResume()

        var id = Integer.parseInt(MyApplication.prefs.getString(Key.LENDER_ID.toString(), ""))

        Retrofit.getContracts(id).enqueue(object : Callback<ArrayList<ContractContentSuccess>> {
            override fun onResponse( call: Call<ArrayList<ContractContentSuccess>>, response: Response<ArrayList<ContractContentSuccess>> ) {
                getContractContent = response.body()!!

                response.body()?.forEach {
                    Title.add(it.title)
                    Content.add(it.content)
                    cnt++
                }

                val list = ArrayList<Data>()

                for (i in 0 until cnt) {
                    val item = Data(Title[i], Content[i])
                    list += item
                }
                vpContract.adapter = ContractAdapter(list, requireContext(), parentFragmentManager, getContractContent)
                val position = MyApplication.prefs.getString("contractPosition", "0")
                vpContract.setCurrentItem(position.toInt()!!)
            }

            override fun onFailure(call: Call<ArrayList<ContractContentSuccess>>, t: Throwable) {}

        })

        btnSearch.setOnClickListener {
            requireFragmentManager().beginTransaction().replace(R.id.layFull, SearchFrag.newInstance("HomeFrag"))
                .addToBackStack(null).commit()
        }

        btnNotification.setOnClickListener {
            requireFragmentManager().beginTransaction().replace(R.id.layFull, NotificationFrag())
                .addToBackStack(null).commit()
        }

        btnMypage.setOnClickListener {
//            for(i in 0..5){
//                Log.e("User ${i} : ", User[i].toString())
//            }
            if(User.isNotEmpty()){
                requireFragmentManager().beginTransaction().replace(
                    R.id.layFull,
                    MypageFrag.newInstance(
                        User[0]!!,
                        User[1]!!,
                        User[2]!!,
                        User[3]!!,
                        User[4]!!,
                        User[5]!!
                    )
                ).addToBackStack(null).commit()
            }
            else Toast.makeText(requireContext(), "잠시후 재시도 해주세요.", Toast.LENGTH_SHORT).show()
        }
    }
}
