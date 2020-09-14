package com.app0.simforpay.activity.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app0.simforpay.R
import kotlinx.android.synthetic.main.contract_item.view.*
import me.grantland.widget.AutofitTextView

class ContractAdapter(private val contractList: List<ContractData>) : RecyclerView.Adapter<ContractAdapter.ContractViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ContractViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.contract_item, parent, false)

        return ContractViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ContractViewHolder, position: Int) {
        val currentItem = contractList[position]

        holder.name.text = currentItem.name
        holder.content.text = currentItem.content
    }

    override fun getItemCount() = contractList.size

    class ContractViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: AutofitTextView = itemView.contractName
        val content: AutofitTextView = itemView.contractContent
    }
}