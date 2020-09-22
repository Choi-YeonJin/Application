package com.app0.simforpay.retrofit.domain

import com.google.gson.annotations.SerializedName

data class DeleteFriends(
    @SerializedName ("user_id")
    val user_id: Int,
    @SerializedName ("friends_id")
    val friends_id: Int
)