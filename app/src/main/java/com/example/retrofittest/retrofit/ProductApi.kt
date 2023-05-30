package com.example.retrofittest.retrofit

import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApi {
    @GET("product/{id}")
    suspend fun getProductById(@Path("id") id: Int):Product
}