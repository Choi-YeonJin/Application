package com.app0.simforpay.retrofit.domain

data class ContractContentSuccess (
    val id : Int,
    val title : String,
    val borrowDate : String,
    val paybackDate : String,
    val price : Int,
    val lenderName : String,
    val lenderBank : String,
    val lenderAccount : Int,
    val borrower : ArrayList<BorrowerSuccess>,
    val penalty : String,
    val content : String,
    val alarm : Int,
    val state : Int
)