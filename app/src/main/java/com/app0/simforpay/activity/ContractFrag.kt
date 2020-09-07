package com.app0.simforpay.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app0.simforpay.R
import com.app0.simforpay.global.TextInput
import com.app0.simforpay.global.sharedpreferences.PreferenceUtil
import com.app0.simforpay.retrofit.RetrofitHelper
import com.app0.simforpay.retrofit.domain.Contract
import com.app0.simforpay.retrofit.domain.ContractSuccess
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.frag_contract.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.*

class ContractFrag : Fragment() {

    private val userRetrofit = RetrofitHelper.getUserRetrofit()
    val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_contract, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bank Dropdown
        val items = listOf("NH농협", "KB국민", "신한", "우리", "하나", "IBK기업", "SC제일", "씨티", "KDB산업", "SBI저축",
            "새마을", "대구", "광주", "우체국", "신협", "전북", "경남", "부산", "수협", "제주", "카카오뱅크")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        bank.setAdapter(adapter)

        // Enable Switch
        swAlert.isEnabled = false
        if(!swAlert.isEnabled){
            swAlert.setOnClickListener {
                Toast.makeText(context, "완료일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        TextInput.CheckFive(btnSave, contractName, tradeDay, price, lender, borrower1)
    }

    override fun onResume() {
        super.onResume()
        var cnt = 1 // borrower cnt
        val borrowerPrices = listOf(borrowerPrice1, borrowerPrice2, borrowerPrice3, borrowerPrice4, borrowerPrice5)

        // Click Back Button
        btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_fragContract_to_fragHome)
        }

        // Datepicker
        tradeDay.setOnClickListener {
            ShowDatePickerDialog(tradeDay, "trade")
        }

        complDay.setOnClickListener {
            swAlert.isEnabled = true

            if(!tradeDay.text.isNullOrEmpty())
                ShowDatePickerDialog(complDay, "compl")
            else
                Toast.makeText(context, "거래일을 입력해주세요.", Toast.LENGTH_SHORT).show()
        }

        btnDelComplDay.setOnClickListener {
            complDay.setText("")
            swAlert.isChecked = false
            swAlert.isEnabled = false
        }

        // Set individual Price
        price.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                TextBorrowerPrice(cnt, borrowerPrices)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        cbN1.setOnCheckedChangeListener { buttonView, isChecked ->
            TextBorrowerPrice(cnt, borrowerPrices)
        }

        // Add & Delete Borrower
        btnAddBorrower.setOnClickListener {
            cnt++
            VisibilBorrower(cnt, 0)
            LayBorrower(cnt)

            TextBorrowerPrice(cnt, borrowerPrices)
        }

        btnDelBorrower.setOnClickListener {
            VisibilBorrower(cnt, 1)
            cnt--
            LayBorrower(cnt)

            TextBorrowerPrice(cnt, borrowerPrices)
        }

        btnSave.setOnClickListener{
            val title = contractName.text.toString()
            val borrow_date = tradeDay.text.toString()
            val payback_date = complDay.text.toString()
            val price = Integer.parseInt(price.text.toString())
            val lender_id = Integer.parseInt(PreferenceUtil(this.requireContext()).getString(Key.LENDER_ID.toString(), ""))
            val lender_name = lender.text.toString()
            val penalty = penalty.text.toString()
            val alarm = if (swAlert.isChecked) 1 else 0

            val contractInfo = Contract(title, borrow_date, payback_date, price, lender_id, lender_name, penalty, alarm)

            userRetrofit.ContractCall(contractInfo)
                .enqueue(object : Callback<ContractSuccess> {
                    override fun onResponse(call: Call<ContractSuccess>, response: Response<ContractSuccess>) {
                        if(response.body()?.result=="true")
                            findNavController().navigate(R.id.action_fragContract_to_fragHome)
                        else
                            Toast.makeText(context, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<ContractSuccess>, t: Throwable) {

                    }
                })

        }
    }

    fun ShowDatePickerDialog(editText: EditText, str: String) {
        val listener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            editText.setText("${year}년 ${month + 1}월 ${dayOfMonth}일")

            if (str == "trade") {
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
        }

        DatePickerDialog(
            this.requireContext(),
            listener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            if (str == "trade")
                datePicker.maxDate = System.currentTimeMillis()
            else
                datePicker.minDate = calendar.time.time
        }.show()
    }

    fun VisibilBorrower(cnt:Int, btnState:Int){
        var layBorrower = layBorrower2 as TextInputLayout
        var borrowerPrice = borrowerPrice2 as TextView

        when(cnt){
            2 -> {
                if(btnState == 0)
                    btnDelBorrower.visibility = View.VISIBLE
                else
                    btnDelBorrower.visibility = View.INVISIBLE

                layBorrower = layBorrower2
                borrowerPrice = borrowerPrice2
            }

            3 -> {
                layBorrower = layBorrower3
                borrowerPrice = borrowerPrice3
            }

            4 -> {
                layBorrower = layBorrower4
                borrowerPrice = borrowerPrice4

                btnAddBorrower.isEnabled = true
            }

            5 -> {
                layBorrower = layBorrower5
                borrowerPrice = borrowerPrice5

                btnAddBorrower.isEnabled = false
            }
        }

        if(btnState == 0){
            layBorrower.visibility = View.VISIBLE
            borrowerPrice.visibility = View.VISIBLE
        }
        else{
            layBorrower.visibility = View.GONE
            borrowerPrice.visibility = View.GONE
        }
    }

    fun LayBorrower(cnt:Int){
        val params = btnDelBorrower.layoutParams as ConstraintLayout.LayoutParams
        var layBorrowerId = layBorrower2.id

        when(cnt){
            2 -> layBorrowerId = layBorrower2.id
            3 -> layBorrowerId = layBorrower3.id
            4 -> layBorrowerId = layBorrower4.id
            5 -> layBorrowerId = layBorrower5.id
        }

        params.topToTop = layBorrowerId
        params.bottomToBottom = layBorrowerId
        btnDelBorrower.requestLayout()
    }

    fun TextBorrowerPrice(cnt: Int, borrowerPrices: List<TextView>){
        var borrowerPrice = 0

        borrowerPrice = if(cbN1.isChecked) price.text.toString().toInt()/cnt else price.text.toString().toInt()

        for(textView in borrowerPrices){
            textView.text = NumberFormat.getInstance(Locale.KOREA).format(borrowerPrice) + "원"
        }
    }
}