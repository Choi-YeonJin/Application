package com.app0.simforpay.retrofit.domain

data class BorrowerSuccess (
    val id : Int,
    val contractId : Int,
    val borrowerId : Int,
    val userName : String,
    val price : Int,
    val paybackState : Int
)