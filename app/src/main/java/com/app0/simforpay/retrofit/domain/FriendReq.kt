package com.app0.simforpay.retrofit.domain

import com.google.gson.annotations.SerializedName

data class FriendReq (
    @SerializedName("applicant_id")
    val applicant_id: Int,
    @SerializedName("recipient_id")
    val recipient_id: Int,
    @SerializedName("recipient_name")
    val recipient_name: String
)
