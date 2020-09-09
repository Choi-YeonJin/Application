package com.app0.simforpay.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app0.simforpay.R
import kotlinx.android.synthetic.main.fragment_contract_share.*
import java.io.File
import java.io.FileOutputStream


class ContractShareFrag : Fragment() {

    private lateinit var callback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contract_share, container, false)
    }

    override fun onResume() {
        super.onResume()

        shBtnCompl.setOnClickListener {
            findNavController().navigate(R.id.action_fragContract_to_fragHome)
        }

        // save image
        val captureLayout = layContract // 캡쳐할 레이아웃

        btnSaveImg.setOnClickListener {
            Capture(captureLayout) //지정한 Layout 영역 사진첩 저장 요청
        }
    }

    fun Capture(view: View?) {
        //Null Point Exception ERROR 방지
        if (view == null) {
            Log.e("save image", "view null error")
            return
        }

        // 캡쳐 파일 저장
        view.buildDrawingCache()
        val bitmap = view.drawingCache

        // 저장할 폴더
        val uploadFolder: File =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!uploadFolder.exists())
            uploadFolder.mkdir() //폴더 생성

        // 저장할 주소
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        try {
            val fos = FileOutputStream("$path.${System.currentTimeMillis()}.jpeg") // 경로 + 제목 + .jpg로 FileOutputStream Setting
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos)
            fos.flush()
            fos.close()

            Log.e("save image", "success")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}