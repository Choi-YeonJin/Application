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
import com.app0.simforpay.R
import com.app0.simforpay.activity.MainAct
import com.app0.simforpay.activity.contract.ContractShareFrag
import com.app0.simforpay.retrofit.RetrofitHelper
import com.app0.simforpay.retrofit.domain.ContractContentSuccess
import com.app0.simforpay.util.sharedpreferences.Key
import com.app0.simforpay.util.sharedpreferences.MyApplication
import kotlinx.android.synthetic.main.frag_contract.*
import kotlinx.android.synthetic.main.frag_home.*
import kotlinx.android.synthetic.main.frag_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchFrag : Fragment() {

    private val Retrofit = RetrofitHelper.getRetrofit()
    lateinit var Title: ArrayList<String>
    lateinit var adapter: ArrayAdapter<String>

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
        return inflater.inflate(R.layout.frag_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var id=Integer.parseInt(MyApplication.prefs.getString(Key.LENDER_ID.toString(), ""))

//        contentTitle = ArrayList()

        Retrofit.getContracts(id).enqueue(object : Callback<ArrayList<ContractContentSuccess>> {
            override fun onResponse(
                call: Call<ArrayList<ContractContentSuccess>>,
                response: Response<ArrayList<ContractContentSuccess>>
            ) {
                Title = ArrayList()
                response.body()?.forEach {
                    Title.add(it.title)
                }
                adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, Title)
                listView.adapter = adapter

            }

            override fun onFailure(call: Call<ArrayList<ContractContentSuccess>>, t: Throwable) {}

        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.i("Test", "Llego al querysubmit : " + query)
                if (Title.contains(query)) {
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

        listView.setOnItemClickListener { parent, view, position, id ->
            val element = adapter.getItemId(position) // The item that was clicked

            requireFragmentManager().beginTransaction().replace(R.id.layFull,
                HomeFrag.newInstance(element.toInt())
            ).commit()
        }


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

}