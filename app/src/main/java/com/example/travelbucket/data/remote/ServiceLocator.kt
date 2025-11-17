package com.example.travelbucket.data.remote

import android.content.Context
import com.example.travelbucket.data.local.UserDatabase
import com.example.travelbucket.data.repository.CountryRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object ServiceLocator {

    private const val BASE_URL = "https://restcountries.com/"

    val countryApi: CountryApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CountryApi::class.java)
    }

    fun provideDatabase(context: Context) =
        UserDatabase.getDatabase(context)

    fun provideCountryRepository(context: Context, userId: String): CountryRepository {
        val db = provideDatabase(context)
        return CountryRepository(
            api = countryApi,
            dao = db.countryDao(),
            userId = userId
        )
    }
}

