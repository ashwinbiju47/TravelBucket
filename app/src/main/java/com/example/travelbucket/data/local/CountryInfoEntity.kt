package com.example.travelbucket.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "country_info")
data class CountryInfoEntity(
    @PrimaryKey(autoGenerate = false)
    val countryName: String,        // column name must match DAO query
    val userId: String,
    val population: Long = 0L,
    val currency: String? = null,
    val description: String? = null,
    val estimatedCost: Double = 0.0
)
