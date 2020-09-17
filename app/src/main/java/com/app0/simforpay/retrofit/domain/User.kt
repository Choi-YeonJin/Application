package com.app0.simforpay.retrofit.domain

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("myid")
    val myId : String,
    @SerializedName("password")
    val password: String,
    @SerializedName("name")
    val name : String,
    @SerializedName("imageUrl")
    val image_url : String = "default",
    @SerializedName("phoneNum")
    val phone_num : String,
    @SerializedName("bank")
    val bank : String,
    @SerializedName("account")
    val account : String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)