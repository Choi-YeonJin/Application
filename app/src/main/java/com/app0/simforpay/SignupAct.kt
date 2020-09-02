package com.app0.simforpay

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
import java.io.ByteArrayOutputStream


class SignupAct : AppCompatActivity() {

    private val userRetrofit = RetrofitHelper.getUserRetrofit()
    private var imageUri: Bitmap?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_signup)

        btnBack.setOnClickListener{
            val intent = Intent(applicationContext, PhoneAct::class.java)
            startActivity(intent)
            finish()
        }

        btnAddImgProfile.setOnClickListener {
            TedImagePicker.with(this)
                .start { uri ->
//                    Log.d("test", uri.toString())
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    imageUri = bitmap
                    imgProfile.setImageURI(uri)
                }
        }

        var idOverlap = false;

        suId.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(idOverlap){
                    layId.setStartIconDrawable(R.drawable.ic_person)
                    layId.setStartIconTintMode(PorterDuff.Mode.SRC_IN)
                    idOverlap = false
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })


        btnCheckId.setOnClickListener {
            val userId = validUser(suId.text.toString())
            userRetrofit.validUserCall(userId)
                .enqueue(object : Callback<validUserSuccess> {
                    override fun onResponse(call: Call<validUserSuccess>, response: Response<validUserSuccess>){
                        if(response.body()?.result=="true"){
                            idOverlap=true;
                            ChangeIcon(layId)

                        }else {
                            idOverlap = false;
                            layId.error = "이미 사용중인 아이디입니다."
                        }
                    }

                    override fun onFailure(call: Call<validUserSuccess>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
        }

        TextInput.CheckFour(btnCompl, name, suId, suPw, pwAgain)

        btnCompl.setOnClickListener{
            if(!idOverlap)
                layId.error = "아이디 중복 확인을 해주세요."

//                Log.d("phoneNumber", intent.extras!!.getString("phoneNumber", ""))
//                Log.d("phoneNumber", intent.getStringExtra("phoneNumber").toString())

            val imageEncodeUri = if (imageUri != null) bitmapToString(imageUri!!) else ""
            val userInfo = Signup(suId.text.toString(), suPw.text.toString(), name.text.toString(), imageEncodeUri, intent.getStringExtra("phoneNumber").toString())
            
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

        suPw.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(pwVaild())
                    ChangeIcon(layPw)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        pwAgain.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(suPw.text.toString() == pwAgain.text.toString())
                    ChangeIcon(layPwAgain)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    fun ChangeIcon(textInputLayout: TextInputLayout){
        textInputLayout.setStartIconDrawable(R.drawable.ic_check_circle)
        textInputLayout.setStartIconTintList(ContextCompat.getColorStateList(applicationContext, R.color.starticon_selector))
    }

    fun pwVaild() : Boolean {
        val pwPattern = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[!@#\$%^*+=-]).{8,20}.\$"

        Log.e("loggggggg", Regex(pwPattern).containsMatchIn(suPw.text.toString()).toString())

        return Regex(pwPattern).containsMatchIn(suPw.text.toString())
    }

    private fun bitmapToString(bitmap: Bitmap): String {

        val byteArrayOutputStream = ByteArrayOutputStream()
        val byteArray = byteArrayOutputStream.toByteArray()

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)

        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}

