package com.app0.simforpay.retrofit.domain

data class GetReqFriendsSuccess(
    val id: String,
    val requestTime:String,
    val applicantId : String,
    val applicantName: String,
    val applicantMyid: String,
    val recipientId: String,
    val recipientName: String,
    val acceptState: String,
    val acceptTime: String
)