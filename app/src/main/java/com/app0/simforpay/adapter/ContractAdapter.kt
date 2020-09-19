package com.app0.simforpay.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.PagerAdapter
import com.app0.simforpay.R
import com.app0.simforpay.util.CustomBottomSheetDialog
import kotlinx.android.synthetic.main.contract_item.view.*

class ContractAdapter(private val models: List<Data>, context: Context, fragmentManager: FragmentManager) : PagerAdapter() {
    private var context: Context? = null
    private val fragmentManager = fragmentManager

    override fun getCount(): Int {
        return models.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val view: View = LayoutInflater.from(context).inflate(R.layout.contract_item, container, false)

        view.contractName.text = models[position].main
        view.contractContent.text = models[position].sub

        view.btnContractSetting.setOnClickListener{
            val dialog = CustomBottomSheetDialog.CustomBottomSheetDialogBuilder()
                .setBtnClickListener(object : CustomBottomSheetDialog.CustomBottomSheetDialogListener {
                    override fun onClickMenu1Btn() {
                        Toast.makeText(context, "First Button Clicked", Toast.LENGTH_SHORT).show()
                    }
                    override fun onClickMenu2Btn() {
                        Toast.makeText(context, "Second Button Clicked", Toast.LENGTH_SHORT).show()
                    }
                    override fun onClickMenu3Btn() {
                        Toast.makeText(context, "Third Button Clicked", Toast.LENGTH_SHORT).show()
                    }
                }).create()
            dialog.show(fragmentManager, dialog.tag)
        }

        container.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    init {
        this.context = context
    }
}
