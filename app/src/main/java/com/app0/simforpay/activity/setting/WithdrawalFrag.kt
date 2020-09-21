package com.app0.simforpay.activity.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.app0.simforpay.R
import com.app0.simforpay.activity.MainAct
import com.app0.simforpay.activity.login.SigninAct
import com.app0.simforpay.retrofit.RetrofitHelper
import com.app0.simforpay.retrofit.domain.ResResultSuccess
import com.app0.simforpay.util.sharedpreferences.Key
import com.app0.simforpay.util.sharedpreferences.MyApplication
import kotlinx.android.synthetic.main.frag_notification.btnBack
import kotlinx.android.synthetic.main.frag_withdrawal.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WithdrawalFrag : Fragment() {

    private val Retrofit = RetrofitHelper.getRetrofit()
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
        return inflater.inflate(R.layout.frag_withdrawal, container, false)
    }

    override fun onResume() {
        super.onResume()

        btnBack.setOnClickListener {
            fragmentManager?.popBackStackImmediate()
        }

        cbAgree.setOnCheckedChangeListener { buttonView, isChecked ->
            btnWithdrawal.isEnabled = isChecked
        }

        btnWithdrawal.setOnClickListener {
            var id = Integer.parseInt(MyApplication.prefs.getString(Key.LENDER_ID.toString(), ""))

            Retrofit.DeleteUser(id).enqueue(object :
                Callback<ResResultSuccess> {
                override fun onResponse(
                    call: Call<ResResultSuccess>,
                    response: Response<ResResultSuccess>
                ) {
                    if (response.body()?.result == "true") {
                        val intent = Intent(context, SigninAct::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                    else{
                        Toast.makeText(context, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResResultSuccess>, t: Throwable) {}

            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        val mainAct = activity as MainAct
        mainAct.HideBottomNavi(false)
    }
}