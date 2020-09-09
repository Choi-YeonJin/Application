package com.app0.simforpay.retrofit.domain

import com.google.gson.annotations.SerializedName

data class Contract(
    @SerializedName ("title")
    val title: String,
    @SerializedName("borrow_date")
    val borrow_date: String,
    @SerializedName("payback_date")
    val payback_date: String,
    @SerializedName("price")
    val price: Int,
    @SerializedName("lender_id")
    val lender_id: Int,
    @SerializedName("lender_name")
    val lender_name: String,
    @SerializedName("lender_bank")
    val lender_bank: String,
    @SerializedName("lender_account")
    val lender_account: Int,
    @SerializedName("penalty")
    val penalty: String,
    @SerializedName("alarm")
    val alarm: Int
)