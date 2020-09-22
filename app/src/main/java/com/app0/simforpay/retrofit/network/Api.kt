package com.app0.simforpay.retrofit.network

import com.app0.simforpay.retrofit.domain.*
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @GET("users")
    fun getUsers() : Call<List<User>>

    @GET("user/{id}")
    fun getUser(@Path("id") id: Int): Call<User>

//    @GET("contract-contents/{id}")
//    fun getContractContents(@Path("id") id: Int): Call<List<ContractContentSuccess>>

    @GET("contracts/{id}")
    fun getContracts(@Path("id") id: Int): Call<ArrayList<ContractContentSuccess>>

    @GET("friends/{id}")
    fun getFreinds(@Path("id") id: Int): Call<ArrayList<FriendsSuccess>>

    @POST("sign-in")
    fun SigninCall(@Body userInfo: Signin) : Call<ResUesrSuccess>

    @POST("user")
    fun SignupCall(@Body userInfo: Signup) : Call<ResUesrSuccess>

    @POST("valid-userId")
    fun vaildUserCall(@Body userInfo: VaildUser) : Call<ResResultSuccess>

    @POST("contract")
    fun ContractCall(@Body contractInfo: Contract) : Call<ContractSuccess>

    @PUT("user-pw/{id}")
    fun UpdateUser(@Path("id") id: Int,@Body updateUserInfo: UpdateUser) : Call<ResResultSuccess>

    @PUT("bank-registration/{id}")
    fun UpdateUserAccount(@Path("id") id: Int,@Body updateUserInfo: UpdateAccount) : Call<ResResultSuccess>

    @PUT("contract-complete/{id}")
    fun ContractCompl(@Path("id") id: Int) : Call<ResResultSuccess>

    @PUT("contract/{id}")
    fun UpdateContract(@Path("id") id: Int?,@Body updateContractInfo: Contract) : Call<ResResultSuccess>

    @DELETE("contract/{id}")
    fun DeleteContract(@Path("id") id: Int) : Call<ResResultSuccess>

    @DELETE("user/{id}")
    fun DeleteUser(@Path("id") id: Int) : Call<ResResultSuccess>

    @HTTP(method = "DELETE", path = "friends", hasBody = true)
    fun DeleteFriends(@Body DeleteFriendsInfo: DeleteFriends) : Call<ResResultSuccess>

}