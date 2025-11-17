package com.example.travelbucket.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CountryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(search: CountrySearchEntity)

    // Per-user history only. Keeps schema explicit.
    @Query("SELECT * FROM country_search WHERE userId = :userId ORDER BY timestamp DESC")
    fun getHistory(userId: String): Flow<List<CountrySearchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountryInfo(info: CountryInfoEntity)

    // Query matches CountryInfoEntity fields
    @Query("SELECT * FROM country_info WHERE countryName = :name AND userId = :userId LIMIT 1")
    suspend fun getCountryInfo(name: String, userId: String): CountryInfoEntity?
}
