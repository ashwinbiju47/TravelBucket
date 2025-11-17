package com.example.travelbucket.data.repository

import com.example.travelbucket.data.local.CountryDao
import com.example.travelbucket.data.local.CountryInfoEntity
import com.example.travelbucket.data.local.CountrySearchEntity
import com.example.travelbucket.data.remote.CountryApi

class CountryRepository(
    private val api: CountryApi,
    private val dao: CountryDao,
    private val userId: String
) {

    suspend fun searchCountry(name: String): CountryInfoEntity {

        // 1. Check user-specific cache first
//        val cached = dao.getCountryInfo(name, userId)
//        if (cached != null) return cached

        // 2. Fetch from REST Countries API
        val remoteList = api.getCountryByName(name)

        // Safety guard: avoid crashes if API returns empty
        val remote = remoteList.firstOrNull()
            ?: throw IllegalStateException("Country not found for: $name")

        val currency = remote.currencies?.keys?.firstOrNull() ?: "N/A"
        val population = remote.population ?: 0L

        // 3. Build entity
        val entity = CountryInfoEntity(
            countryName = name,
            userId = userId,
            population = population,
            currency = currency,
            description = remote.region ?: "Unknown region"
        )

        // 4. Insert user-specific info into DB
        dao.insertCountryInfo(entity)

        // 5. Track user-specific search history
        dao.insertSearch(
            CountrySearchEntity(
                userId = userId,
                countryName = name,
                population = population.toInt(),
                currency = currency
            )
        )

        // 6. Return the inserted data
        return entity
    }

    // Flow<List<CountrySearchEntity>>
    fun getHistory() = dao.getHistory(userId)
}
