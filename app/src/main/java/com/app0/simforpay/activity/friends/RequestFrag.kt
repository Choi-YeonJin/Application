package com.app0.simforpay.activity.friends

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app0.simforpay.R
import com.app0.simforpay.activity.MainAct
import com.app0.simforpay.adapter.Data
import com.app0.simforpay.adapter.RequestAdapter
import com.app0.simforpay.retrofit.RetrofitHelper
import com.app0.simforpay.retrofit.domain.GetReqFriendsSuccess
import com.app0.simforpay.util.sharedpreferences.Key
import com.app0.simforpay.util.sharedpreferences.MyApplication
import kotlinx.android.synthetic.main.frag_request.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RequestFrag : Fragment() {

    private val Retrofit = RetrofitHelper.getRetrofit()
    private var getReqFriendsList = arrayListOf<GetReqFriendsSuccess>()
    private val Name = mutableListOf<String>()
    private val ID = mutableListOf<String>()

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
        return inflater.inflate(R.layout.frag_request, container, false)
    }

    override fun onResume() {
        super.onResume()

        var id=Integer.parseInt(MyApplication.prefs.getString(Key.LENDER_ID.toString(), ""))
        var cnt = 0

        Retrofit.getReqFreinds(id).enqueue(object : Callback<ArrayList<GetReqFriendsSuccess>> {
            override fun onResponse(call: Call<ArrayList<GetReqFriendsSuccess>>, response: Response<ArrayList<GetReqFriendsSuccess>>)
            {
                getReqFriendsList = response.body()!!

                response.body()?.forEach {
                    Name.add(it.applicantName)
                    ID.add(it.applicantMyid)
                    cnt++
                }

                MyApplication.prefs.setString("RequestFriendsCount",cnt.toString())

                val list = ArrayList<Data>()

                for (i in 0 until cnt) {
                    val item = Data(Name[i], "@"+ID[i])
                    list += item
                }

                rvRequest.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL ,false)
                rvRequest.adapter = RequestAdapter(list, requireContext(), parentFragmentManager,getReqFriendsList)
                rvRequest.setHasFixedSize(true)

                requestCnt.text = cnt.toString() + "명"
            }
            override fun onFailure(call: Call<ArrayList<GetReqFriendsSuccess>>, t: Throwable) {
            }
        }) // 완료 되면 바텀 네비

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