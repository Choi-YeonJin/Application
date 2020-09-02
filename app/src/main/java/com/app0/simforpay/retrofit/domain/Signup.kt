package com.app0.simforpay.retrofit.domain

import com.google.gson.annotations.SerializedName

data class Signup(
    @SerializedName("myid")
    val myId : String,
    @SerializedName("password")
    val password: String,
    @SerializedName("name")
    val name : String,
    @SerializedName("image_url")
    val image_url : String = "default",
    @SerializedName("phone_num")
    val phone_num : String
)