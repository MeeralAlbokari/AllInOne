package com.example.capsule.retrofit.exerciseAPI

import com.example.capsule.retrofit.exerciseModel.GooglePlaces
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {

    @GET("maps/api/place/nearbysearch/json")
    fun getNearBy(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("type") type: String,
//        @Query("keyword") keyword: String?,
        @Query("key") key: String
    ): Call<GooglePlaces>
}