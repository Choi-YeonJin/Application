package com.app0.simforpay.retrofit.network

import com.app0.simforpay.retrofit.domain.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {

    @GET("users")
    fun getUsers() : Call<List<User>>

    @POST("sign-in")
    fun SigninCall(@Body userInfo: Signin) : Call<SigninSuccess>

    @POST("user")
    fun SignupCall(@Body userInfo: Signup) : Call<SignupSuccess>

    @POST("valid-userId")
    fun validUserCall(@Body userInfo: validUser) : Call<validUserSuccess>

    @POST("contract")
    fun ContractCall(@Body contractInfo: Contract) : Call<ContractSuccess>

}