package com.assignment.petamicoassignment.ui.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*


interface Api {

    @GET("api/users")
    suspend fun getUserData(@Query("page") page: Int): UserDetails

    @POST("api/users")
    fun createUser(@Body createUser: CreateUser) : Call<CreatedUserResponse>

    @DELETE("api/users/{id}")
    fun deleteUser(@Path("id") id : Int) : Call<Int>
}