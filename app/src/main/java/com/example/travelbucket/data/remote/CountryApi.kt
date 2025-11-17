package com.example.travelbucket.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface CountryApi {

    @GET("v3.1/name/{country}?fullText=true")
    suspend fun getCountryByName(
        @Path("country") country: String
    ): List<CountryResponse>
}
