package com.app0.simforpay.activity.home

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.app0.simforpay.R
import com.app0.simforpay.activity.MainAct
import com.app0.simforpay.retrofit.RetrofitHelper
import com.app0.simforpay.retrofit.domain.ResResultSuccess
import com.app0.simforpay.retrofit.domain.UpdateAccount
import com.app0.simforpay.retrofit.domain.UpdateUser
import com.app0.simforpay.retrofit.domain.User
import com.app0.simforpay.util.ImgUrl
import com.app0.simforpay.util.RegularExpression
import com.app0.simforpay.util.sharedpreferences.Key
import com.app0.simforpay.util.sharedpreferences.MyApplication
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.editaccount_dialog.view.*
import kotlinx.android.synthetic.main.editaccount_dialog.view.btnClose
import kotlinx.android.synthetic.main.editaccount_dialog.view.btnCompl
import kotlinx.android.synthetic.main.editpw_dialog.view.*
import kotlinx.android.synthetic.main.frag_mypage.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger
import java.security.MessageDigest

private const val ARG_PARAM1 = "name"
private const val ARG_PARAM2 = "id"
private const val ARG_PARAM3 = "phone"
private const val ARG_PARAM4 = "bank"
private const val ARG_PARAM5 = "account"
private const val ARG_PARAM6 = "imgUrl"

class MypageFrag : Fragment() {

    private val Retrofit = RetrofitHelper.getRetrofit()
    var oldPwCheck = false // 현재 비밀번호 일치 여부(bool)
    var pwCheck = false // pw 정규식 만족 여부(bool)
    var pwAgainCheck = false // pw와 pwAgain 일치 여부(bool)
    private var name: String? = null
    private var id: String? = null
    private var phone: String? = null
    private var bank: String? = null
    private var account: String? = null
    private var imgUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainAct = activity as MainAct
        mainAct.HideBottomNavi(true)

        arguments?.let {
            name = it.getString(ARG_PARAM1)
            id = it.getString(ARG_PARAM2)
            phone = it.getString(ARG_PARAM3)
            bank = it.getString(ARG_PARAM4)
            account = it.getString(ARG_PARAM5)
            imgUrl = it.getString(ARG_PARAM6)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_mypage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    @ExperimentalStdlibApi
    override fun onResume() {
        super.onResume()

        myName.text = name.toString()
        myId.text = id
        myPhone.text = phone
        myBank.text = bank
        myAccountNum.text = account
        if(imgUrl != "Default")
        {
            val Image = ImgUrl.StringToBitmap(imgUrl.toString())
            Glide.with(requireContext()).load(Image).circleCrop().into(imgProfile)
        }

        btnBack.setOnClickListener{
            fragmentManager?.popBackStackImmediate()
        }

//        btnAddImgProfile.setOnClickListener {
//             /* java.lang.RuntimeException: android.os.TransactionTooLargeException: data parcel size 11474292 bytes error */
//
//            TedImagePicker.with(requireContext())
//                .start { uri ->
//                    val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
//                    imageUri = bitmap
//                    Glide.with(this).load(uri).circleCrop().into(imgProfile)
//                }
//        }

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

    @ExperimentalStdlibApi
    fun ShowEditDialog(dialog: Int) {
        val editDialogView = LayoutInflater.from(requireContext()).inflate(dialog, null)
        val editBuilder = AlertDialog.Builder(requireContext()).setView(editDialogView)
        val editDialog = editBuilder.show()

        if (dialog == R.layout.editpw_dialog) { // 비밀번호 변경
            editDialogView.oldPw.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    editDialogView.layOldPw.error = null // 비밀번호 인증 실패 후 edittext 바뀌면 error 제거
                    val oldpw = editDialogView.oldPw.text.toString()
                        val md = MessageDigest.getInstance("MD5")
                        val md5PW =  BigInteger(1, md.digest(oldpw.encodeToByteArray())).toString(16).padStart(32, '0')
                    checkOldPw(md5PW, editDialogView.layOldPw) //현재 비밀번호 확인
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            })

            editDialogView.newPw.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    editDialogView.layNewPw.error = null // 일치 실패 후 edittext 바뀌면 error 제거
                    pwCheck = RegularExpression.Vaild(editDialogView.newPw)
                    Log.d("test", pwCheck.toString())
                    if (pwCheck) ChangeIcon(editDialogView.layNewPw) else DefaultIcon(editDialogView.layNewPw)

                    // pw와 pwAgain 일치 여부 검사가 완료되었는데 pw가 바뀌면 pwAgain 일치 여부도 다시 검사해야 함
                    if (pwAgainCheck)
                        DefaultIcon(editDialogView.layPwAgain)
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            })

            editDialogView.newPwAgain.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    editDialogView.layPwAgain.error = null // 일치 실패 후 edittext 바뀌면 error 제거

                    val newPwAgain = editDialogView.newPwAgain.text.toString()
                    val newPw = editDialogView.newPw.text.toString()
                    if (newPwAgain == newPw) {
                        pwAgainCheck = true
                        ChangeIcon(editDialogView.layPwAgain)
                    } else DefaultIcon(editDialogView.layPwAgain)
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            })

            editDialogView.btnClose.setOnClickListener {
                editDialog.dismiss()
            }
            editDialogView.btnCompl.setOnClickListener {
                // 완료 버튼 눌리면
                if (!oldPwCheck)
                    editDialogView.layOldPw.error = "현재 비밀번호가 일치하지 않습니다."
                if (!pwCheck)
                    editDialogView.layNewPw.error = "8~20자 이내, 영문/숫자/특수문자 필수 사용해주세요."
                if (!pwAgainCheck)
                    editDialogView.layPwAgain.error = "비밀번호가 일치하지 않습니다."
                if (oldPwCheck && pwCheck && pwAgainCheck) {
                    val newPw = editDialogView.newPw.text.toString()
                    updateUserPw(newPw)
                    editDialog.dismiss() // dialog 닫기
                }
            }

        } else { // 계좌번호 변경
            val items = listOf(
                "NH농협", "KB국민", "신한", "우리", "하나", "IBK기업", "SC제일", "씨티", "KDB산업", "SBI저축",
                "새마을", "대구", "광주", "우체국", "신협", "전북", "경남", "부산", "수협", "제주", "카카오뱅크"
            )
            val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
            editDialogView.bank.setAdapter(adapter)

            editDialogView.btnClose.setOnClickListener {
                editDialog.dismiss()
            }
            editDialogView.btnCompl.setOnClickListener {
                // 완료 버튼 눌리면
                updateUserAccount(editDialogView.bank, editDialogView.accountNum)
                editDialog.dismiss() // dialog 닫기}
            }
        }
    }

    private fun updateUserAccount(bank: AutoCompleteTextView?, accountNum: TextInputEditText?) {
        var id = Integer.parseInt(MyApplication.prefs.getString(Key.LENDER_ID.toString(), ""))

        val bank = bank!!.text.toString()
        val account = Integer.parseInt(accountNum!!.text.toString())
        val updateUserInfo = UpdateAccount(bank, account)

        Retrofit.UpdateUserAccount(id, updateUserInfo)
            .enqueue(object : Callback<ResResultSuccess> {
                override fun onResponse(
                    call: Call<ResResultSuccess>,
                    response: Response<ResResultSuccess>
                ) {
                    if (response.body()?.result == "true") {
//                        Toast.makeText(context, "계좌정보가 정상적으로 업데이트 되었습니다.", Toast.LENGTH_SHORT).show()
                            myBank.text = bank
                            myAccountNum.setText(account.toString())
                    }
                }

                override fun onFailure(call: Call<ResResultSuccess>, t: Throwable) {}

            })
    }

    private fun checkOldPw(oldpw: String, layOldPw: TextInputLayout?) {
        var id = Integer.parseInt(MyApplication.prefs.getString(Key.LENDER_ID.toString(), ""))

        Retrofit.getUser(id).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                val pw = response.body()?.password
                if (pw == oldpw) {
                    oldPwCheck = true
                    ChangeIcon(layOldPw!!)
                } else
                    DefaultIcon(layOldPw!!)
            }

            override fun onFailure(call: Call<User>, t: Throwable) {}

        })
    }

    private fun updateUserPw(newPw: String) {
        var id = Integer.parseInt(MyApplication.prefs.getString(Key.LENDER_ID.toString(), ""))
        val imageUrl = imgProfile.toString()
        val updateUserInfo = UpdateUser(imageUrl, newPw)

        Retrofit.UpdateUser(id, updateUserInfo).enqueue(object : Callback<ResResultSuccess> {
            override fun onResponse(
                call: Call<ResResultSuccess>,
                response: Response<ResResultSuccess>
            ) {
                if (response.body()?.result == "true") {
//                    Toast.makeText(context, "비밀번호가 정상적으로 업데이트 되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResResultSuccess>, t: Throwable) {}

        })
    }

    fun DefaultIcon(textInputLayout: TextInputLayout) {
        textInputLayout.setStartIconDrawable(R.drawable.ic_lock)

        textInputLayout.setStartIconTintList(ColorStateList.valueOf(resources.getColor(R.color.icon)))
    }

    fun ChangeIcon(textInputLayout: TextInputLayout) {
        textInputLayout.setStartIconDrawable(R.drawable.ic_check_circle)
        textInputLayout.setStartIconTintList(
            ContextCompat.getColorStateList(
                requireContext(),
                R.color.green
            )
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(name: String, id: String, phone: String, bank: String, account: String, imgUrl: String) =
            MypageFrag().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, name)
                    putString(ARG_PARAM2, id)
                    putString(ARG_PARAM3, phone)
                    putString(ARG_PARAM4, bank)
                    putString(ARG_PARAM5, account)
                    putString(ARG_PARAM6, imgUrl)
                }
            }
    }

}