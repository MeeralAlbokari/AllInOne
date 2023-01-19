package com.example.capsule.retrofit.foodAPI


import com.example.capsule.data.foodEntity.Category
import com.example.capsule.data.foodEntity.Meal
import com.example.capsule.data.foodEntity.MealResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIInterface {
    @GET("categories.php")
    fun getCategoryList(): Call<Category>

    @GET("filter.php")
    fun getMealList(@Query("c") category: String): Call<Meal>

    @GET("lookup.php")
    fun getSpecificItem(@Query("i") id: String): Call<MealResponse>


}