package com.example.retrofittest.retrofit

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Repository {
    @GET("product/{id}")
    suspend fun getProductById(@Path("id") id: Int):Product

    @POST("auth/login")
    suspend fun oAuth(@Body authRequest: AuthRequest): User

    @GET("product")
    suspend fun getAllProduct(): Products
}