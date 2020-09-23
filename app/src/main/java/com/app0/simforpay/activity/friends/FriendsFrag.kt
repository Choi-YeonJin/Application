package com.app0.simforpay.activity.friends

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app0.simforpay.R
import com.app0.simforpay.activity.home.SearchFrag
import com.app0.simforpay.adapter.Data
import com.app0.simforpay.adapter.FriendsAdapter
import com.app0.simforpay.retrofit.RetrofitHelper
import com.app0.simforpay.retrofit.domain.FriendsSuccess
import com.app0.simforpay.util.sharedpreferences.Key
import com.app0.simforpay.util.sharedpreferences.MyApplication
import kotlinx.android.synthetic.main.frag_friends.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendsFrag : Fragment() {

    private val Retrofit = RetrofitHelper.getRetrofit()
    private var getFriendsList = arrayListOf<FriendsSuccess>()
    private val Name = mutableListOf<String>()
    private val ID = mutableListOf<String>()
    var cnt = 0

    lateinit var List: ArrayList<String>
    lateinit var adapter: ArrayAdapter<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_friends, container, false)
    }

    override fun onResume() {
        super.onResume()

        Log.d("RequestFriendsCount",MyApplication.prefs.getString("RequestFriendsCount", "0"))
        notiCnt.setText(MyApplication.prefs.getString("RequestFriendsCount", "0"))

        var id=Integer.parseInt(MyApplication.prefs.getString(Key.LENDER_ID.toString(), ""))

        Retrofit.getFreinds(id).enqueue(object : Callback<ArrayList<FriendsSuccess>> {
            override fun onResponse(call: Call<ArrayList<FriendsSuccess>>, response: Response<ArrayList<FriendsSuccess>>) {
                getFriendsList = response.body()!!

                response.body()?.forEach {
                    Name.add(it.friendsName)
                    ID.add(it.friendsMyid)
                    cnt++
                }

                val list = ArrayList<Data>()

                for (i in 0 until cnt)  {
                    val item = Data(Name[i], ID[i])
                    list += item
                }
                rvFriends.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL ,false)
                rvFriends.adapter = FriendsAdapter(list, requireContext(), parentFragmentManager,getFriendsList)
                rvFriends.setHasFixedSize(true)

                friendsCnt.text = cnt.toString() + "명"
            }

            override fun onFailure(call: Call<ArrayList<FriendsSuccess>>, t: Throwable) {
            }

        })

        btnReqFriends.setOnClickListener {
            requireFragmentManager().beginTransaction().replace(R.id.layFull, RequestFrag())
                .addToBackStack(null).commit()
        }

        btnSearch.setOnClickListener {
            requireFragmentManager().beginTransaction().replace(R.id.layFull, SearchFrag.newInstance("FriendsFrag"))
                .addToBackStack(null).commit()
        }
    }

}