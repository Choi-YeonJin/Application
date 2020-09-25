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
import kotlin.system.exitProcess

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

        val FriendsName = mutableListOf<String>()
        val applicantId = mutableListOf<String>()
        val recipientId = mutableListOf<String>()
        var cnt = 0

        var id = Integer.parseInt(MyApplication.prefs.getString(Key.LENDER_ID.toString(), ""))

        Retrofit.getFreinds(id).enqueue(object : Callback<ArrayList<FriendsSuccess>> {
            override fun onResponse(
                call: Call<ArrayList<FriendsSuccess>>,
                response: Response<ArrayList<FriendsSuccess>>
            ) {
                response.body()?.forEach {
                    FriendsName.add(it.friendsName)
                }
            }

            override fun onFailure(call: Call<ArrayList<FriendsSuccess>>, t: Throwable) {
            }
        })

        Retrofit.getAllReqFreinds(id)
            .enqueue(object : Callback<ArrayList<GetAllReqFriendsSuccess>> {
                override fun onResponse(
                    call: Call<ArrayList<GetAllReqFriendsSuccess>>,
                    response: Response<ArrayList<GetAllReqFriendsSuccess>>
                ) {
                    response.body()?.forEach {
                        applicantId.add(it.applicantId)
                        recipientId.add(it.recipientId)
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<GetAllReqFriendsSuccess>>,
                    t: Throwable
                ) {
                }
            })

        if (pageName == "HomeFrag") {
            Retrofit.getContracts(id).enqueue(object : Callback<ArrayList<ContractContentSuccess>> {
                override fun onResponse(
                    call: Call<ArrayList<ContractContentSuccess>>,
                    response: Response<ArrayList<ContractContentSuccess>>
                ) {
                    loading_image.visibility = View.GONE



                    List = ArrayList()
                    response.body()?.forEach {
                        List.add(it.title)
                    }

                    adapter =
                        ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, List)
                    listView?.adapter = adapter
                }

                override fun onFailure(
                    call: Call<ArrayList<ContractContentSuccess>>,
                    t: Throwable
                ) {
                    loading_image.visibility = View.GONE
                    List = ArrayList()
                    List.add("작성된 계약서가 없습니다.")

                    adapter =
                        ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, List)
                    listView?.adapter = adapter
                }
            })
            listView.setOnItemClickListener { parent, view, position, id ->
                val element = adapter.getItemId(position) // The item that was clicked

                requireFragmentManager().beginTransaction()
                    .replace(R.id.layFull, HomeFrag.newInstance(element.toInt())).commit()
            }
        } else if (pageName == "FriendsFrag") {
            searchView.queryHint = "찾고싶은 친구를 입력해주세요"
            Retrofit.getUsers().enqueue(object : Callback<List<User>> {
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    getUserList = response.body()!!
                    loading_image.visibility = View.GONE
                    List = ArrayList()

                    response.body()?.forEach { //전체 유저 갯수만큼 foreach()
//                        Log.d("Friends",FriendsName.size.toString())
                        List.add(it.name)
                        if (FriendsName.size != 0) { // 현재 친구리스트에 친구가 있다면
                            Log.d("validFriends","have friends")
                            for (i in 0 until FriendsName.size) {
                                if (applicantId.size != 0) { // 현재 유저가 신청 받거나, 신청 보내는 사람중에 잇는지 확인
                                    Log.d("Applicant","Sure")
                                    for (x in 0 until applicantId.size) {
                                        if (applicantId[x].toInt() == id) { // 현재 유저가 신청을 보냈는지 확인
                                            if (it.id == id) List.remove(it.name)
                                            if (it.name == "admin")List.remove(it.name)
                                            if (it.name == FriendsName[i])List.remove(it.name)
                                            if (it.id == recipientId[x].toInt()) List.remove(it.name)
                                        }
                                        else if (recipientId[x].toInt() == id) { // 현재 유저가 신청을 받았는지 확인
                                            if (it.id == id) List.remove(it.name)
                                            if (it.name == "admin")List.remove(it.name)
                                            if (it.name == FriendsName[i])List.remove(it.name)
                                            if (it.id == applicantId[x].toInt()) List.remove(it.name)
                                        }
                                    }
                                }else // 요청을 보냈거나, 받은 게 없는 경우
                                {
                                    if (it.id == id) List.remove(it.name)
                                    if (it.name == "admin")List.remove(it.name)
                                    if (it.name == FriendsName[i])List.remove(it.name)
                                }
                            }
                        } else {// 현재 친구리스트에 친구가 없다면
                            Log.d("validFriends", "Not have friends")
                            if (it.id == id) List.remove(it.name)
                            if (it.name == "admin")List.remove(it.name)
                        }
                    }
                    adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, List)
                    listView?.adapter = adapter
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    loading_image.visibility = View.GONE
                    List = ArrayList()
                    List.add("유저가 없습니다.")

                    adapter =
                        ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, List)
                    listView?.adapter = adapter
                }

            })
            listView.setOnItemClickListener { parent, view, position, id ->
                val element = adapter.getItemId(position) // The item that was clicked
                Log.d("Nameeeeeeeeeeeeeeeee", List[element.toInt()])
                requireFragmentManager().beginTransaction()
                    .replace(R.id.layFull, FriendsReqFrag.newInstance(List[element.toInt()]))
                    .commit()

//                Toast.makeText(context,List[element.toInt()],Toast.LENGTH_SHORT).show()
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.i("Test", "Llego al querysubmit : $query")
                if (List.contains(query)) {
                    adapter.filter.filter(query)
                } else {
                    Toast.makeText(context, "No Match found", Toast.LENGTH_LONG).show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
                Log.i("Test", "Llego al querytextchange : $newText")
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
