package com.app0.simforpay.retrofit.domain

import com.google.gson.annotations.SerializedName

data class Borrower(
      @SerializedName("borrower_id")
      val borrower_id: Int?,
      @SerializedName("userName")
      val userName: String,
      @SerializedName("price")
      val price: Int?,
      @SerializedName("payback_state")
      val payback_state:Int
)