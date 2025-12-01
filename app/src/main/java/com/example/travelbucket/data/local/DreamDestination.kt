package com.example.travelbucket.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dream_destinations")
data class DreamDestination(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val name: String,
    val notes: String?,
    val photoPath: String,
    val timestamp: Long
)
