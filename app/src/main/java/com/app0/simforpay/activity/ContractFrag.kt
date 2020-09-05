package com.app0.simforpay

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app0.simforpay.retrofit.RetrofitHelper
import com.app0.simforpay.retrofit.domain.Contract
import com.app0.simforpay.retrofit.domain.ContractSuccess
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.frag_contract.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.math.min

class ContractFrag : Fragment() {

    private val userRetrofit = RetrofitHelper.getUserRetrofit()

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.frag_contract, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                .enqueue(object : Callback<ContractSuccess>{
                    override fun onResponse(call: Call<ContractSuccess>, response: Response<ContractSuccess>) {
                        if(response.body()?.result=="true"){
                            Toast.makeText(context, "계약서 작성 성공", Toast.LENGTH_LONG).show()
                        }else{

                        }
                    }

                    override fun onFailure(call: Call<ContractSuccess>, t: Throwable) {

                    }
                })

        }

        // bank dropdown
        val items = listOf("NH농협", "KB국민", "신한", "우리", "하나", "IBK기업", "SC제일", "씨티", "KDB산업", "SBI저축",
            "새마을", "대구", "광주", "우체국", "신협", "전북", "경남", "부산", "수협", "제주", "카카오뱅크")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        bank.setAdapter(adapter)

        val calendar = Calendar.getInstance()
        val complDayListener = DatePickerDialog.OnDateSetListener {
                view, year, month, dayOfMonth -> complDay.setText("${year}년 ${month+1}월 ${dayOfMonth}일")
        }
        val complDayDatePickerDialog = DatePickerDialog(this.requireContext(), complDayListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

        val tradeDayListener = DatePickerDialog.OnDateSetListener{view, year, month, dayOfMonth ->

            tradeDay.setText("${year}년 ${month+1}월 ${dayOfMonth}일")

            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            complDayDatePickerDialog.datePicker.minDate = calendar.time.time
        }

        val tradeDayDatePickerDialog = DatePickerDialog(this.requireContext(), tradeDayListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

        tradeDay.setOnClickListener {

            tradeDayDatePickerDialog.show()
        }

        complDay.setOnClickListener {

            if(!tradeDay.text.isNullOrEmpty()){

                complDayDatePickerDialog.show()

            }else{
                Toast.makeText(context, "거래일을 입력해주세요.", Toast.LENGTH_LONG).show()
            }
        }

        btnDelComplDay.setOnClickListener {
            complDay.setText("")
        }

        btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_fragContract_to_fragHome)
        }

        TextInput.CheckFive(btnSave, contractName, tradeDay, price, lender, borrower1)
    }

    override fun onStart() {
        super.onStart()

        var cnt = 1

        btnAddBorrower.setOnClickListener {
            cnt++
            VisibilBorrower(cnt, 0)
            LayBorrower(cnt)
        }

        btnDelBorrower.setOnClickListener {
            VisibilBorrower(cnt, 1)
            cnt--
            LayBorrower(cnt)
        }
    }

    fun showDatePickerDialog() : Calendar {
        var selectDate = null as Calendar

        val calendar: Calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            R.style.Theme_MaterialComponents_Dialog,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                selectDate = Calendar.getInstance().apply { set(year, monthOfYear, dayOfMonth) }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = System.currentTimeMillis()
        }.show()

        return selectDate;
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
            2 -> {
                layBorrowerId = layBorrower2.id
            }

            3 -> {
                layBorrowerId = layBorrower3.id
            }

            4 -> {
                layBorrowerId = layBorrower4.id
            }

            5 -> {
                layBorrowerId = layBorrower5.id
            }
        }

        params.topToTop = layBorrowerId
        params.bottomToBottom = layBorrowerId
        btnDelBorrower.requestLayout()
    }
}