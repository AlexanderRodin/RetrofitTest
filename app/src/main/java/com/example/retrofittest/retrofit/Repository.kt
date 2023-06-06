package com.example.retrofittest.retrofit

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface Repository {
    @GET("product/{id}")
    suspend fun getProductById(@Path("id") id: Int): Product

    @POST("auth/login")
    suspend fun oAuth(@Body authRequest: AuthRequest): Response<User>

//    @GET("product")
//    suspend fun getAllProduct(): Products

    // TODO Authorization
    @Headers("Content-Type: application/json")
    @GET("auth/products")
    suspend fun getAllProducts(@Header("Authorization") token: String): Products

    @Headers("Content-Type: application/json")
    @GET("auth/product/search")
    suspend fun getProductByName(
        @Header("Authorization") token: String,
        @Query("q") name: String
    ): Products
}