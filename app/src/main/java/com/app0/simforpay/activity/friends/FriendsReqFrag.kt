package com.app0.simforpay.activity.friends

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app0.simforpay.R
import com.app0.simforpay.activity.MainAct
import com.app0.simforpay.adapter.Data
import com.app0.simforpay.adapter.FriendsReqAdapter
import com.app0.simforpay.retrofit.RetrofitHelper
import com.app0.simforpay.retrofit.domain.GetUserbyName
import com.app0.simforpay.retrofit.domain.User
import kotlinx.android.synthetic.main.frag_friends_req.rvFriends
import kotlinx.android.synthetic.main.frag_request.btnBack
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "name"

class FriendsReqFrag : Fragment() {

    private val Retrofit = RetrofitHelper.getRetrofit()
    private var getFriendsList = listOf<User>()
    private val ID = mutableListOf<String>()
    private var name: String? = null
    private val names = mutableListOf<String>()

    private lateinit var callback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Press Back Button
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireFragmentManager().beginTransaction().replace(R.id.layFull, FriendsFrag()).commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainAct = activity as MainAct
        mainAct.HideBottomNavi(true)

        arguments?.let {
            name = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_friends_req, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userInfo = GetUserbyName(name!!)
        var cnt = 0
        Log.d("Name",userInfo.name)
        Retrofit.getUserbyName(userInfo).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                getFriendsList = response.body()!!

                response.body()?.forEach {
                    names.add(it.name)
                    ID.add(it.myId)
                    cnt++
                }

                val list = ArrayList<Data>()

                for (i in 0 until cnt)  {
                    val item = Data(names[i], ID[i])
                    list += item
                }
                rvFriends.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL ,false)
                rvFriends.adapter = FriendsReqAdapter(list, requireContext(),parentFragmentManager, getFriendsList)
                rvFriends.setHasFixedSize(true)
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {}

        })


    }

    override fun onResume() {
        super.onResume()

        btnBack.setOnClickListener {
            requireFragmentManager().beginTransaction().replace(R.id.layFull, FriendsFrag()).commit()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(name: String) =
            FriendsReqFrag().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, name)
                }
            }
    }
}