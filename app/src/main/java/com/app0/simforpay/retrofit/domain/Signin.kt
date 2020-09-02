package com.app0.simforpay.retrofit.domain

import com.google.gson.annotations.SerializedName

data class Signin(
    @SerializedName("myid")
    val myid : String,
    @SerializedName("password")
    val password: String
)