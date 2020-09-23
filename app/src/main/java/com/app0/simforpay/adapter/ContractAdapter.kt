package com.app0.simforpay.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.PagerAdapter
import com.app0.simforpay.R
import com.app0.simforpay.activity.contract.ContractFrag
import com.app0.simforpay.activity.home.HomeFrag
import com.app0.simforpay.retrofit.RetrofitHelper
import com.app0.simforpay.retrofit.domain.ContractContentSuccess
import com.app0.simforpay.retrofit.domain.ResResultSuccess
import com.app0.simforpay.util.dialog.CustomContractBottomSheetDialog
import kotlinx.android.synthetic.main.contract_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ContractAdapter( models: List<Data>, context: Context, fragmentManager: FragmentManager, getContractContent: ArrayList<ContractContentSuccess> ): PagerAdapter() {
    private val Retrofit = RetrofitHelper.getRetrofit()
    private var context: Context? = null
    private val fragmentManager = fragmentManager
    private val getContractContent = getContractContent
    private var models:List<Data> = emptyList()
    private var BorrowerString: String = ""

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
        if(getContractContent[position].state == 1)
            view.contractComplState.visibility = view.visibility

        view.btnContractSetting.setOnClickListener{
            val dialog = CustomContractBottomSheetDialog.CustomBottomSheetDialogBuilder()
                .setBtnClickListener(object :
                    CustomContractBottomSheetDialog.CustomBottomSheetDialogListener {
                    override fun onClickMenu1Btn() {
                        for(i in 0 until getContractContent[position].borrower.size)
                        {
                            BorrowerString += getContractContent[position].borrower[i].id.toString() + ","
                            BorrowerString += getContractContent[position].borrower[i].contractId.toString() + ","
                            BorrowerString += getContractContent[position].borrower[i].borrowerId.toString() + ","
                            BorrowerString += getContractContent[position].borrower[i].userName + ","
                            BorrowerString += getContractContent[position].borrower[i].price.toString() + ","
                            BorrowerString += getContractContent[position].borrower[i].paybackState.toString() + "!"
                        }

                        fragmentManager.beginTransaction().replace(R.id.layFull,ContractFrag.newInstance(
                            getContractContent[position].id,
                            getContractContent[position].title,
                            getContractContent[position].borrowDate,
                            getContractContent[position].paybackDate,
                            getContractContent[position].price,
                            getContractContent[position].lenderName,
                            getContractContent[position].lenderBank,
                            getContractContent[position].lenderAccount,
                            BorrowerString,
                            getContractContent[position].penalty,
                            getContractContent[position].content,
                            getContractContent[position].alarm,
                            getContractContent[position].state)
                        ).addToBackStack(null).commit()

//                        Toast.makeText(context, "로딩 중", Toast.LENGTH_SHORT).show()
                    }

                    override fun onClickMenu2Btn() {
                        Retrofit.DeleteContract(getContractContent[position].id).enqueue(object :
                            Callback<ResResultSuccess> {
                            override fun onResponse(
                                call: Call<ResResultSuccess>,
                                response: Response<ResResultSuccess>
                            ) {
                                if (response.body()?.result == "true") {
                                    fragmentManager.beginTransaction().replace(R.id.layFull, HomeFrag())
                                        .addToBackStack(null).commit()
                                }
                            }

                            override fun onFailure(call: Call<ResResultSuccess>, t: Throwable) {}

                        })
                    }

                    override fun onClickMenu3Btn() {
                        Retrofit.ContractCompl(getContractContent[position].id).enqueue(object :
                            Callback<ResResultSuccess> {
                            override fun onResponse(
                                call: Call<ResResultSuccess>,
                                response: Response<ResResultSuccess>
                            ) {
                                if (response.body()?.result == "true") {
                                    view.contractComplState.visibility = view.visibility
                                    fragmentManager.beginTransaction().replace(R.id.layFull, HomeFrag())
                                        .addToBackStack(null).commit()
                                }
                            }

                            override fun onFailure(call: Call<ResResultSuccess>, t: Throwable) {}

                        })
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
        this.models = models
    }
}
