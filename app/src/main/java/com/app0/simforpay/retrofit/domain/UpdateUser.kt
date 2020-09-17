package com.app0.simforpay.retrofit.domain

import com.google.gson.annotations.SerializedName

data class UpdateUser(
    @SerializedName("imageUrl")
    val imageUrl : String,
    @SerializedName("password")
    val password: String?
)