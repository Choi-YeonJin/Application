package com.app0.simforpay.retrofit.domain

import com.google.gson.annotations.SerializedName

data class UpdateAccount(
    @SerializedName("bank")
    val myId : String,
    @SerializedName("account")
    val password: Int
)