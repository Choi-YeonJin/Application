package com.app0.simforpay.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.PagerAdapter
import com.app0.simforpay.R
import com.app0.simforpay.activity.contract.ContractFrag
import com.app0.simforpay.activity.home.HomeFrag
import com.app0.simforpay.retrofit.RetrofitHelper
import com.app0.simforpay.retrofit.domain.Borrower
import com.app0.simforpay.retrofit.domain.ContractContentSuccess
import com.app0.simforpay.retrofit.domain.UpdateSuccess
import com.app0.simforpay.util.CustomBottomSheetDialog
import kotlinx.android.synthetic.main.contract_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ContractAdapter( models: List<Data>, context: Context, fragmentManager: FragmentManager, getContractContent: List<ContractContentSuccess> ): PagerAdapter() {
    private val Retrofit = RetrofitHelper.getRetrofit()
    private var context: Context? = null
    private val fragmentManager = fragmentManager
    private val getContractContent = getContractContent
    private var models:List<Data> = emptyList()

    override fun getCount(): Int {
        return models.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val view: View = LayoutInflater.from(context).inflate(
            R.layout.contract_item,
            container,
            false
        )

        view.contractName.text = models[position].main
        view.contractContent.text = models[position].sub
        if(getContractContent[position].state == 1)
            view.contractComplState.visibility = view.visibility

        view.btnContractSetting.setOnClickListener{
            val dialog = CustomBottomSheetDialog.CustomBottomSheetDialogBuilder()
                .setBtnClickListener(object :
                    CustomBottomSheetDialog.CustomBottomSheetDialogListener {
                    override fun onClickMenu1Btn() {

                        fragmentManager.beginTransaction().remove(HomeFrag.newInstance(0)).commit()
                        fragmentManager.beginTransaction().replace(R.id.layFull,ContractFrag.newInstance(
                            getContractContent[position].id,
                            getContractContent[position].title,
                            getContractContent[position].borrowDate,
                            getContractContent[position].paybackDate,
                            getContractContent[position].price,
                            getContractContent[position].lenderName,
                            getContractContent[position].lenderBank,
                            getContractContent[position].lenderAccount,
                            getContractContent[position].borrower,
                            getContractContent[position].penalty,
                            getContractContent[position].content,
                            getContractContent[position].alarm,
                            getContractContent[position].state)
                        ).commit()

                        Toast.makeText(context, "First Button Clicked", Toast.LENGTH_SHORT).show()
                    }

                    override fun onClickMenu2Btn() {
                        Retrofit.DeleteContract(getContractContent[position].id).enqueue(object :
                            Callback<UpdateSuccess> {
                            override fun onResponse(
                                call: Call<UpdateSuccess>,
                                response: Response<UpdateSuccess>
                            ) {
                                if (response.body()?.result == "true") {
                                    fragmentManager.beginTransaction().replace(R.id.layFull, HomeFrag.newInstance(0))
                                        .addToBackStack(null).commit()
                                }
                            }

                            override fun onFailure(call: Call<UpdateSuccess>, t: Throwable) {}

                        })
                    }

                    override fun onClickMenu3Btn() {
                        Retrofit.ContractCompl(getContractContent[position].id).enqueue(object :
                            Callback<UpdateSuccess> {
                            override fun onResponse(
                                call: Call<UpdateSuccess>,
                                response: Response<UpdateSuccess>
                            ) {
                                if (response.body()?.result == "true") {
                                    view.contractComplState.visibility = view.visibility
                                    HomeFrag.newInstance(0)
                                }
                            }

                            override fun onFailure(call: Call<UpdateSuccess>, t: Throwable) {}

                        })
                    }
                }).create()
            dialog.show(fragmentManager, dialog.tag)
            Log.d("Content", getContractContent[position].borrower[0].toString())
//            Toast.makeText(context, position.toString() + "View Clicked", Toast.LENGTH_SHORT).show()
        }

        container.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    init {
        this.context = context
        this.models = models
    }
}
