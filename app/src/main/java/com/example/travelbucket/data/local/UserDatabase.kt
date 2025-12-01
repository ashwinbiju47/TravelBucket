package com.example.travelbucket.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [User::class, CountryInfoEntity::class, CountrySearchEntity::class, DreamDestination::class],
    version = 2, // Increment version
    exportSchema = false
)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun countryDao(): CountryDao
    abstract fun dreamDestinationDao(): DreamDestinationDao

    companion object {
        @Volatile private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_db"
                )
                    .fallbackToDestructiveMigration()   // ok for dev; consider proper migrations later
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
