package com.app0.simforpay.global

import android.widget.EditText
import com.app0.simforpay.R


object RegularExpression {
    val num = "(?=.*\\d)" // =(?=.*[0-9])
    val eng = "(?=.*[a-zA-Z])"
    val special = "(?=.*\\W)" // =(?=.*[!@#\$%^*+=-])
    val pwLength = ".{8,20}" // 8~20
    val idLength = ".{6,12}" // 6~12
    val phone = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}\$" // 010XXXXXXXX

    fun Vaild(editText: EditText) : Boolean {
        var pattern = ""

        if(editText.id == R.id.phoneNum)
            pattern = phone
        else if(editText.id == R.id.suId)
            pattern = num + eng + idLength
        else if(editText.id == R.id.suPw)
            pattern = num + eng + special + pwLength

//        Log.e("pattern : ", pattern)

        return Regex(pattern).containsMatchIn(editText.text.toString())
    }
}