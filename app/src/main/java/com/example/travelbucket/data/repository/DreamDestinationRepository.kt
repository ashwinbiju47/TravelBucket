package com.example.travelbucket.data.repository

import com.example.travelbucket.data.local.DreamDestination
import com.example.travelbucket.data.local.DreamDestinationDao
import kotlinx.coroutines.flow.Flow

class DreamDestinationRepository(
    private val dao: DreamDestinationDao,
    private val userId: String
) {
    val allDestinations: Flow<List<DreamDestination>> = dao.getDestinationsByUser(userId)

    suspend fun addDestination(name: String, notes: String?, photoPath: String) {
        val destination = DreamDestination(
            userId = userId,
            name = name,
            notes = notes,
            photoPath = photoPath,
            timestamp = System.currentTimeMillis()
        )
        dao.insertDestination(destination)
    }

    suspend fun deleteDestination(id: Int) {
        dao.deleteDestination(id)
    }
}
