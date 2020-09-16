package com.app0.simforpay.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText

object TextInput{

    fun CheckOne(button: Button, editText1: EditText){
        editText1.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val et1 = editText1.text.toString().trim()

                button.isEnabled = et1.isNotEmpty()
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(
                s: Editable
            ) {
            }
        })
    }

    fun CheckTwo(button: Button, editText1: EditText, editText2: EditText){
        val editTexts = listOf(editText1, editText2)
        for (editText in editTexts) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    val et1 = editText1.text.toString().trim()
                    val et2 = editText2.text.toString().trim()

                    button.isEnabled = et1.isNotEmpty()
                            && et2.isNotEmpty()
                }

                override fun beforeTextChanged(
                    s: CharSequence, start: Int, count: Int, after: Int) {
                }

                override fun afterTextChanged(
                    s: Editable
                ) {
                }
            })
        }
    }

    fun CheckFive(button: Button, editText1: EditText, editText2: EditText, editText3: EditText, editText4: EditText, editText5: EditText){
        val editTexts = listOf(editText1, editText2, editText3, editText4, editText5)
        for (editText in editTexts) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    val et1 = editText1.text.toString().trim()
                    val et2 = editText2.text.toString().trim()
                    val et3 = editText3.text.toString().trim()
                    val et4 = editText4.text.toString().trim()
                    val et5 = editText5.text.toString().trim()

                    button.isEnabled = et1.isNotEmpty()
                            && et2.isNotEmpty()
                            && et3.isNotEmpty()
                            && et4.isNotEmpty()
                            && et5.isNotEmpty()
                }

                override fun beforeTextChanged(
                    s: CharSequence, start: Int, count: Int, after: Int) {
                }

                override fun afterTextChanged(
                    s: Editable
                ) {
                }
            })
        }
    }
}