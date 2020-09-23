package com.app0.simforpay.retrofit.domain

data class GetAllReqFriendsSuccess(
    val id: String,
    val requestTime:String,
    val applicantId : String,
    val applicantName: String,
    val recipientId: String,
    val recipientName: String,
    val acceptState: String,
    val acceptTime: String
)