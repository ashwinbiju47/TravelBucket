package com.example.travelbucket.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "country_search")
data class CountrySearchEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val countryName: String,
    val population: Int,
    val currency: String,
    val estimatedCost: Double = 0.0,
    val timestamp: Long = System.currentTimeMillis()
)
