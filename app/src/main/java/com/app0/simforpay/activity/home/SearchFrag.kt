package com.app0.simforpay.activity.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app0.simforpay.R
import com.app0.simforpay.activity.MainAct
import com.app0.simforpay.activity.friends.FriendsReqFrag
import com.app0.simforpay.adapter.Data
import com.app0.simforpay.adapter.FriendsAdapter
import com.app0.simforpay.adapter.RequestAdapter
import com.app0.simforpay.retrofit.RetrofitHelper
import com.app0.simforpay.retrofit.domain.*
import com.app0.simforpay.util.sharedpreferences.Key
import com.app0.simforpay.util.sharedpreferences.MyApplication
import kotlinx.android.synthetic.main.frag_friends.*
import kotlinx.android.synthetic.main.frag_request.*
import kotlinx.android.synthetic.main.frag_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "pageName"

class SearchFrag : Fragment() {

    private val Retrofit = RetrofitHelper.getRetrofit()
    lateinit var List: ArrayList<String>
    lateinit var adapter: ArrayAdapter<String>
    private var getUserList = listOf<User>()

    private var pageName: String? = null

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

        arguments?.let {
            pageName = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val Name = mutableListOf<String>()
        val applicantId = mutableListOf<String>()
        val recipientId = mutableListOf<String>()
        var cnt = 0

        var id=Integer.parseInt(MyApplication.prefs.getString(Key.LENDER_ID.toString(), ""))

        Retrofit.getFreinds(id).enqueue(object : Callback<ArrayList<FriendsSuccess>> {
            override fun onResponse(call: Call<ArrayList<FriendsSuccess>>, response: Response<ArrayList<FriendsSuccess>>) {
                response.body()?.forEach {
                    Name.add(it.friendsName)
                }
            }
            override fun onFailure(call: Call<ArrayList<FriendsSuccess>>, t: Throwable) {
            }
        })

        Retrofit.getAllReqFreinds().enqueue(object : Callback<ArrayList<GetAllReqFriendsSuccess>> {
            override fun onResponse(call: Call<ArrayList<GetAllReqFriendsSuccess>>, response: Response<ArrayList<GetAllReqFriendsSuccess>>)
            {
                response.body()?.forEach {
                    applicantId.add(it.applicantId)
                    recipientId.add(it.recipientId)
                }
            }
            override fun onFailure(call: Call<ArrayList<GetAllReqFriendsSuccess>>, t: Throwable) {
            }
        })

        if(pageName == "HomeFrag"){
            Retrofit.getContracts(id).enqueue(object : Callback<ArrayList<ContractContentSuccess>> {
                override fun onResponse(
                    call: Call<ArrayList<ContractContentSuccess>>,
                    response: Response<ArrayList<ContractContentSuccess>>
                ) {
                    List = ArrayList()
                    response.body()?.forEach {
                        List.add(it.title)
                    }
                    adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, List)
                    listView?.adapter = adapter
                }

                override fun onFailure(call: Call<ArrayList<ContractContentSuccess>>, t: Throwable) {

                }
            })
            listView.setOnItemClickListener { parent, view, position, id ->
                val element = adapter.getItemId(position) // The item that was clicked
                MyApplication.prefs.setString("contractPosition", element.toString())

                fragmentManager?.popBackStackImmediate()
            }
            MyApplication.prefs.setString("contractPosition", "0")

        }
        else if(pageName == "FriendsFrag"){
            Retrofit.getUsers().enqueue(object : Callback<List<User>> {
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    getUserList = response.body()!!

                    List = ArrayList()

                    response.body()?.forEach { //전체 유저 갯수만큼 foreach(5)
//                        Log.d("FrendsListSize",Name.size.toString()) //현재 친구의 갯구
//                        Log.d("FrendsList",Name.toString())// 현재 친구 이름 list
                        if(Name.size != 0){
                            for(i in 0 until Name.size){
                                if(Name[i] == it.name) Log.d("Noti","Same")
                                else if(Name[i] != it.name) Log.d("Noti","Not Same")

                                if(Name[i] != it.name){
                                    Log.d("result","cnt : " + cnt)
                                    cnt++
                                }
                                if(cnt == Name.size) {
                                    Log.d("result","List add : " + it.name)
                                    if(applicantId.size != 0){
                                        for(i in 0 until applicantId.size){
                                            if(it.id == id)
                                            else if((applicantId[i] == id.toString() && recipientId[i] == it.id.toString()) || (recipientId[i] == id.toString() && applicantId[i] == it.id.toString())){
                                                Log.d("app","conflict")
                                            }
                                            else List.add(it.name)
                                        }
                                    }else{
                                        if(it.id == id)
                                        else List.add(it.name)
                                    }
                                }else{
                                    Log.d("result","Not add")
                                }
                            }
                            cnt=0
                        }else{
                            if(applicantId.size != 0){
                                for(i in 0 until applicantId.size){
                                    if(it.id == id)
                                    else if((applicantId[i] == id.toString() && recipientId[i] == it.id.toString()) || (recipientId[i] == id.toString() && applicantId[i] == it.id.toString())){
                                        Log.d("app","conflict")
                                    }
                                    else List.add(it.name)
                                }
                            }else{
                                if(it.id == id)
                                else List.add(it.name)
                            }
                        }
//                        else List.add(it.name)

                    }
                    Log.d("List",List.toString())
                    adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, List)
                    listView?.adapter = adapter
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {}

            })
            listView.setOnItemClickListener { parent, view, position, id ->
                val element = adapter.getItemId(position) // The item that was clicked
                requireFragmentManager().beginTransaction().replace(R.id.layFull, FriendsReqFrag.newInstance(List[element.toInt()])).commit()

//                Toast.makeText(context,List[element.toInt()],Toast.LENGTH_SHORT).show()
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.i("Test", "Llego al querysubmit : " + query)
                if (List.contains(query)) {
                    adapter.filter.filter(query)
                } else {
                    Toast.makeText(context, "No Match found", Toast.LENGTH_LONG).show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
                Log.i("Test", "Llego al querytextchange : " + newText)
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()

        btnCancel.setOnClickListener {
            fragmentManager?.popBackStackImmediate()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        val mainAct = activity as MainAct
        mainAct.HideBottomNavi(false)
    }

    companion object {
        @JvmStatic
        fun newInstance(pageName: String) =
            SearchFrag().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, pageName)
                }
            }
    }

}