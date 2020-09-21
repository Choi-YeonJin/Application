package com.app0.simforpay.retrofit.domain

data class FriendsSuccess(
    val id: String,
    val userId : String,
    val friendsId: String,
    val friendsName: String,
    val friendsMyid: String,
    val friendsPhoneNum: String,
    val friendsBank: String,
    val friendsAccount: String,
    val favorite: String,
    val block: String
)