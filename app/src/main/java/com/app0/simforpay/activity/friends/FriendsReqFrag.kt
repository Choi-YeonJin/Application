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
import com.app0.simforpay.util.sharedpreferences.Key
import com.app0.simforpay.util.sharedpreferences.MyApplication
import kotlinx.android.synthetic.main.frag_friends_req.rvFriends
import kotlinx.android.synthetic.main.frag_request.btnBack
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "name"
private const val ARG_PARAM2 = "friendsNameList"
private const val ARG_PARAM3 = "applicantIdList"
private const val ARG_PARAM4 = "recipientIdList"


class FriendsReqFrag : Fragment() {

    private val Retrofit = RetrofitHelper.getRetrofit()
    private var getFriendsList = listOf<User>()
    private val ID = mutableListOf<String>()
    private var name: String? = null
    private var friendsNameList: String? = null
    private var applicantIdList: String? = null
    private var recipientIdList: String? = null
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
            friendsNameList = it.getString(ARG_PARAM2)
            applicantIdList = it.getString(ARG_PARAM3)
            recipientIdList = it.getString(ARG_PARAM4)
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

        var userFriendsList: List<String>
        var userapplicantIdList: List<String>
        var userrecipientIdList: List<String>

        userFriendsList = friendsNameList!!.split(",")
        userapplicantIdList = applicantIdList!!.split(",")
        userrecipientIdList = recipientIdList!!.split(",")
        Log.d("AAAAAAAAAA",userFriendsList.toString())

        val userInfo = GetUserbyName(name!!)
        var cnt = 0
        Log.d("Name",userInfo.name)

        var id = Integer.parseInt(MyApplication.prefs.getString(Key.LENDER_ID.toString(), ""))
        Retrofit.getUserbyName(userInfo).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                getFriendsList = response.body()!!

                response.body()?.forEach {
                    names.add(it.name)
                    ID.add(it.myId)
                    cnt++
                    for(i in 0 until userFriendsList.size-1){
                        if(userFriendsList[i] == it.name){
                            names.remove(it.name)
                            ID.remove(it.myId)
                            cnt--
                        }
                    }
                    for (i in 0 until userapplicantIdList.size-2) {
                        if (userapplicantIdList[i].toInt() == id) { // 현재 유저가 신청을 보냈는지 확인
                            Log.d("AAAAAAAAAA","현재유저가 신청을 보냄")
                            if (userrecipientIdList[i].toInt() == it.id) {
                                Log.d("AAAAAAAAAA","id가 삭제됨")
                                names.remove(it.name)
                                ID.remove(it.myId)
                                cnt--
                            }
                        } else if (userrecipientIdList[i].toInt() == id) { // 현재 유저가 신청을 받았는지 확인
                            Log.d("AAAAAAAAAA","현재유저가 신청을 받음")
                            if (userapplicantIdList[i].toInt() == it.id) {
                                names.remove(it.name)
                                ID.remove(it.myId)
                                cnt--
                            }
                        }
                    }
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
        fun newInstance(name: String,friendsNameList: String,applicantIdList: String,recipientIdList: String) =
            FriendsReqFrag().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, name)
                    putString(ARG_PARAM2, friendsNameList)
                    putString(ARG_PARAM3, applicantIdList)
                    putString(ARG_PARAM4, recipientIdList)
                }
            }
    }
}