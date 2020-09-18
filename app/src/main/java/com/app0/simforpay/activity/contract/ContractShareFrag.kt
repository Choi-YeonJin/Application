package com.app0.simforpay.activity.contract

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app0.simforpay.R
import kotlinx.android.synthetic.main.frag_contract_share.*
import java.io.File
import java.io.FileOutputStream

private const val ARG_PARAM1 = "name"
private const val ARG_PARAM2 = "content"

class ContractShareFrag : Fragment() {

    private lateinit var callback: OnBackPressedCallback
    private var name: String? = null
    private var content: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Press Back Button
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_fragContract_to_fragHome)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            name = it.getString(ARG_PARAM1)
            content = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_contract_share, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title.text = name
        contractName.text = name
        contractContent.text = content
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
        val uploadFolder = Environment.getExternalStoragePublicDirectory("/DCIM/SimforPay/")
        if (!uploadFolder.exists()) uploadFolder.mkdir() //폴더 생성

        // 저장할 주소
        val path = Environment.getExternalStorageDirectory().absolutePath + "/DCIM/SimforPay/" //저장 경로 (String Type 변수)

        try {
            val filePath = "$path${System.currentTimeMillis()}.jpeg"
            val file: File = File(filePath)
            val fos = FileOutputStream(filePath) // 경로 + 제목 + .jpg로 FileOutputStream Setting
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos)
            requireContext().sendBroadcast(Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)))
            fos.flush()
            fos.close()

            Toast.makeText(requireContext(), "이미지 저장 완료", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()

            Toast.makeText(requireContext(), "저장공간 접근 권한이 비활성화되어 있습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(name: String, content: String) =
            ContractShareFrag().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, name)
                    putString(ARG_PARAM2, content)
                }
            }
    }
}