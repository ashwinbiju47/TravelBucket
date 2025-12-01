package com.example.travelbucket.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DreamDestinationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDestination(destination: DreamDestination)

    @Query("SELECT * FROM dream_destinations WHERE userId = :userId ORDER BY timestamp DESC")
    fun getDestinationsByUser(userId: String): Flow<List<DreamDestination>>

    @Query("DELETE FROM dream_destinations WHERE id = :id")
    suspend fun deleteDestination(id: Int)
}
