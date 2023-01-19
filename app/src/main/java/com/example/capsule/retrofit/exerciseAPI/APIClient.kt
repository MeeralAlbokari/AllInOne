package com.example.capsule.retrofit.exerciseAPI

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIClient {


    private var retrofit : Retrofit? = null

    fun getClient(): Retrofit?{
        retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }//getClient fun
}