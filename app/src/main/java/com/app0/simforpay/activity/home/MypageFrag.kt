package com.app0.simforpay.activity.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.app0.simforpay.R
import com.app0.simforpay.activity.MainAct
import kotlinx.android.synthetic.main.editaccount_dialog.view.*
import kotlinx.android.synthetic.main.frag_mypage.*

class MypageFrag : Fragment() {

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
        return inflater.inflate(R.layout.frag_mypage, container, false)
    }

    override fun onResume() {
        super.onResume()

        btnPwChange.setOnClickListener {
            ShowEditDialog(R.layout.editpw_dialog)
        }

        btnAccountChange.setOnClickListener {
            ShowEditDialog(R.layout.editaccount_dialog)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        val mainAct = activity as MainAct
        mainAct.HideBottomNavi(false)
    }

    fun ShowEditDialog(dialog: Int) {
        val editDialogView = LayoutInflater.from(requireContext()).inflate(dialog, null)
        val editBuilder = AlertDialog.Builder(requireContext()).setView(editDialogView)
        val editDialog = editBuilder.show()

        if(dialog == R.layout.editpw_dialog){ // 비밀번호 변경

        }
        else{ // 계좌번호 변경
            val items = listOf(
                "NH농협", "KB국민", "신한", "우리", "하나", "IBK기업", "SC제일", "씨티", "KDB산업", "SBI저축",
                "새마을", "대구", "광주", "우체국", "신협", "전북", "경남", "부산", "수협", "제주", "카카오뱅크"
            )
            val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
            editDialogView.bank.setAdapter(adapter)
        }

        /*
         --지우면 되는 주석--
        접근할 때 : editDialogView.id
        */

        editDialogView.btnClose.setOnClickListener {
            editDialog.dismiss()
        }
        editDialogView.btnCompl.setOnClickListener { 
            // 완료 버튼 눌리면

            editDialog.dismiss() // dialog 닫기
        }
    }

}