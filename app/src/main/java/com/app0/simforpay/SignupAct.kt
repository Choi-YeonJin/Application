package com.app0.simforpay

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

        btnCheckId.setOnClickListener {
            val userId = validUser(suId.text.toString())
            userRetrofit.validUserCall(userId)
                .enqueue(object : Callback<validUserSuccess> {
                    override fun onResponse(call: Call<validUserSuccess>, response: Response<validUserSuccess>){
                        if(response.body()?.result=="true"){
                            val idOverlap=true;
                            Toast.makeText(applicationContext, "사용가능한 아이디입니다.", Toast.LENGTH_LONG).show()
                        }else {
                            val idOverlap=false;
                            Toast.makeText(applicationContext, "이미 사용중인 아이디입니다.", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<validUserSuccess>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
        }

        TextInput.CheckFour(btnCompl, name, suId, suPw, pwAgain)

        btnCompl.setOnClickListener(View.OnClickListener {
            if(!pwVaild())
                layPw.error = "8~20자 이내, 영문/숫자/특수문자 필수 사용"

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
        })
    }

    override fun onStart() {
        super.onStart()

        suPw.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(pwVaild()){
                    layPw.endIconMode = TextInputLayout.END_ICON_CUSTOM
                    layPw.setEndIconDrawable(R.drawable.ic_check)
//                    layPw.setEndIconTintMode(R.color.green)
                }
                else
                    layPw.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

    }

    fun pwVaild() : Boolean {
        val pwPattern = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[\$@!%*#?&]).{8,20}.\$"
        return Regex(pwPattern).containsMatchIn(suPw.text.toString())

//        (? =. * [0-9]) # 숫자는 한 번 이상 발생해야합니다
//        (? =. * [a-z]) # 소문자는 한 번 이상 발생해야합니다
//        (? =. * [A-Z]) # 대문자는 한 번 이상 발생해야합니다
//        (? =. [-@ % [} + '!/# $ ^? :;, ( ") ~`. = & {>] <_]) # 특수 문자는 적어도 한 번은 반드시 특수 문자로 대체해야합니다
//        (? =\S + $) # 전체 문자열에 공백이 허용되지 않습니다.
//        {8,} # 최소한 6 자리 이상
    }

    private fun bitmapToString(bitmap: Bitmap): String {

        val byteArrayOutputStream = ByteArrayOutputStream()
        val byteArray = byteArrayOutputStream.toByteArray()

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)

        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun stringToBitmap(encodedString: String): Bitmap {
        val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)

        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    }
}

