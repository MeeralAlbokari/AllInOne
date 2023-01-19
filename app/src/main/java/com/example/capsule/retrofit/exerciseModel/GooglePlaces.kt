package com.example.capsule.retrofit.exerciseModel

data class GooglePlaces(
    val html_attributions: List<Any>,
    val next_page_token: String,
    val results: List<Result>,
    val status: String
)