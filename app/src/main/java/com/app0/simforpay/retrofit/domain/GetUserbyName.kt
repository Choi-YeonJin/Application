package com.app0.simforpay.retrofit.domain

import com.google.gson.annotations.SerializedName

data class GetUserbyName(
    @SerializedName ("name")
    val name: String
)