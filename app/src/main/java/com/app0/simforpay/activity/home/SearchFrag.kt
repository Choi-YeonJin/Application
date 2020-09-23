package com.app0.simforpay.activity.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import com.app0.simforpay.R
import com.app0.simforpay.activity.MainAct
import com.app0.simforpay.activity.contract.ContractShareFrag
import com.app0.simforpay.activity.friends.FriendsReqFrag
import com.app0.simforpay.activity.friends.RequestFrag
import com.app0.simforpay.adapter.Data
import com.app0.simforpay.adapter.FriendsAdapter
import com.app0.simforpay.retrofit.RetrofitHelper
import com.app0.simforpay.retrofit.domain.ContractContentSuccess
import com.app0.simforpay.retrofit.domain.FriendsSuccess
import com.app0.simforpay.retrofit.domain.User
import com.app0.simforpay.util.sharedpreferences.Key
import com.app0.simforpay.util.sharedpreferences.MyApplication
import com.hendraanggrian.appcompat.widget.Mention
import kotlinx.android.synthetic.main.frag_contract.*
import kotlinx.android.synthetic.main.frag_friends.*
import kotlinx.android.synthetic.main.frag_home.*
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

        lateinit var searchViewText: String

        var id=Integer.parseInt(MyApplication.prefs.getString(Key.LENDER_ID.toString(), ""))

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

                val mainAct = activity as MainAct
                mainAct.HideBottomNavi(false)
                requireFragmentManager().beginTransaction().replace(R.id.layFull, HomeFrag())
                    .addToBackStack(null).commit()
            }
            MyApplication.prefs.setString("contractPosition", "0")

        }
        else if(pageName == "FriendsFrag"){
            Retrofit.getUsers().enqueue(object : Callback<List<User>> {
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    getUserList = response.body()!!
                    List = ArrayList()
                    response.body()?.forEach {
                        List.add(it.name)
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

                Toast.makeText(context,List[element.toInt()],Toast.LENGTH_SHORT).show()
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