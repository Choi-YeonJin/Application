package com.app0.simforpay.retrofit.network

import com.app0.simforpay.retrofit.domain.*
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @GET("users")
    fun getUsers() : Call<List<User>>

    @GET("user/{id}")
    fun getUser(@Path("id") id: Int): Call<User>

    @GET("contract-contents/{id}")
    fun getContracts(@Path("id") id: Int): Call<List<ContractContentSuccess>>

    @POST("sign-in")
    fun SigninCall(@Body userInfo: Signin) : Call<SigninSuccess>

    @POST("user")
    fun SignupCall(@Body userInfo: Signup) : Call<SignupSuccess>

    @POST("valid-userId")
    fun validUserCall(@Body userInfo: validUser) : Call<validUserSuccess>

    @POST("contract")
    fun ContractCall(@Body contractInfo: Contract) : Call<ContractSuccess>

    @PUT("user-pw/{id}")
    fun UpdateUser(@Path("id") id: Int,@Body updateUserInfo: UpdateUser) : Call<UpdateUserSuccess>

}