package com.app0.simforpay.activity.login

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.app0.simforpay.R
import com.app0.simforpay.global.ImgUrl
import com.app0.simforpay.global.RegularExpression
import com.app0.simforpay.global.TextInput
import com.app0.simforpay.retrofit.RetrofitHelper
import com.app0.simforpay.retrofit.domain.Signup
import com.app0.simforpay.retrofit.domain.SignupSuccess
import com.app0.simforpay.retrofit.domain.validUser
import com.app0.simforpay.retrofit.domain.validUserSuccess
import com.google.android.material.textfield.TextInputLayout
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.android.synthetic.main.act_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignupAct : AppCompatActivity() {

    private val userRetrofit = RetrofitHelper.getUserRetrofit()
    private var imageUri: Bitmap?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_signup)

        var idOverlap = false // id 중복 체크(bool)
        var idCheck = false // id 정규식 만족 여부(bool)
        var pwCheck = false // pw 정규식 만족 여부(bool)
        var pwAgainCheck = false // pw와 pwAgain 일치 여부(bool)

        // Click Back Button
        btnBack.setOnClickListener{
            finish()
        }

        // Select Profile Image
        btnAddImgProfile.setOnClickListener {
            TedImagePicker.with(this)
                .start { uri ->
//                    Log.d("test", uri.toString())
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    imageUri = bitmap
                    imgProfile.setImageURI(uri)
                }
        }

        // Change layName StartIcon
        name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(name.text.toString().trim() != "")
                    ChangeIcon(layName)
                else
                    DefaultIcon(layName)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })


        suId.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                layId.error = null // 중복체크 실패 후 edittext 바뀌면 error 제거

                idCheck = RegularExpression.Vaild(suId)

                // 중복체크 성공 후 edittext 바뀌면 다시 중복체크 해야하므로 icon 변경
                if(idOverlap){
                    DefaultIcon(layId)
                    idOverlap = false
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        // ID 중복 체크
        btnCheckId.setOnClickListener {
            val userId = validUser(suId.text.toString())
            userRetrofit.validUserCall(userId)
                .enqueue(object : Callback<validUserSuccess> {
                    override fun onResponse(call: Call<validUserSuccess>, response: Response<validUserSuccess>){
                        if(response.body()?.result=="true"){
                            idOverlap = true
                            ChangeIcon(layId)

                            layId.error = null
                        }
                        else {
                            idOverlap = false
                            layId.error = "이미 사용중인 아이디입니다."
                        }
                    }

                    override fun onFailure(call: Call<validUserSuccess>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
        }

        suBtnCompl.setOnClickListener{
            if(!idOverlap)
                layId.error = "아이디 중복 확인을 해주세요."
            if(!idCheck)
                layId.error = "6~12자 이내, 영문/숫자만 사용 가능합니다."
            if(!pwCheck)
                layPw.error = "8~20자 이내, 영문/숫자/특수문자 필수 사용해주세요."
            if(!pwAgainCheck)
                layPwAgain.error = "비밀번호가 일치하지 않습니다."

            if(idOverlap && idCheck && pwCheck && pwAgainCheck){
                val imgUrl = if (imageUri != null) ImgUrl.BitmapToString(imageUri!!) else ""

                val userInfo = Signup(
                    suId.text.toString(),
                    suPw.text.toString(),
                    name.text.toString(),
                    imgUrl,
                    intent.getStringExtra("phoneNumber").toString()
                )

                userRetrofit.SignupCall(userInfo)
                    .enqueue(object : Callback<SignupSuccess> {
                        override fun onResponse(call: Call<SignupSuccess>, response: Response<SignupSuccess>){
                            if(response.body()?.result=="true"){
                                startActivity(Intent(applicationContext, SigninAct::class.java))
                                finish()
                            }else {
                                Toast.makeText(applicationContext, "회원가입에 실패하셨습니다. 잠시후 재시도 해주세요.", Toast.LENGTH_LONG).show()
                            }
                        }

                        override fun onFailure(call: Call<SignupSuccess>, t: Throwable) {
                            t.printStackTrace()
                        }
                    })
            }
        }

        suPw.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    layPw.error = null // 중복체크 실패 후 edittext 바뀌면 error 제거

                    pwCheck = RegularExpression.Vaild(suPw)

                    if(pwCheck)
                        ChangeIcon(layPw)
                    else{
                        DefaultIcon(layPw)
                    }

                    // pw와 pwAgain 일치 여부 검사가 완료되었는데 pw가 바뀌면 pwAgain 일치 여부도 다시 검사해야 함
                    if(pwAgainCheck)
                        DefaultIcon(layPwAgain)
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
        })

        pwAgain.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                layPwAgain.error = null // 중복체크 실패 후 edittext 바뀌면 error 제거

                pwAgainCheck = pwAgain.text.toString() != "" && pwAgain.text.toString() == suPw.text.toString()// pwAgain edittext가 비어있지 않고, pw와 일치하는가

                if(pwAgainCheck)
                    ChangeIcon(layPwAgain)
                else
                    DefaultIcon(layPwAgain)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        TextInput.CheckFour(suBtnCompl, name, suId, suPw, pwAgain) // 모든 eidttext에 text가 있어야 Button 활성화
    }

    fun DefaultIcon(textInputLayout: TextInputLayout){
        if(textInputLayout.id == R.id.layName)
            textInputLayout.setStartIconDrawable(R.drawable.ic_textfield)
        else if(textInputLayout.id == R.id.layId)
            textInputLayout.setStartIconDrawable(R.drawable.ic_person)
        else
            textInputLayout.setStartIconDrawable(R.drawable.ic_lock)

        textInputLayout.setStartIconTintList(ColorStateList.valueOf(resources.getColor(R.color.icon)))
    }

    fun ChangeIcon(textInputLayout: TextInputLayout){
        textInputLayout.setStartIconDrawable(R.drawable.ic_check_circle)
        textInputLayout.setStartIconTintList(ContextCompat.getColorStateList(applicationContext, R.color.green))
    }
}

