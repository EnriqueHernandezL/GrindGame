package com.enriher.grindgame.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GrindTimer::class], version = 1, exportSchema = false)
abstract class GrindDatabase: RoomDatabase() {
    abstract val grindDatabaseDao: GrindDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: GrindDatabase? = null

        fun getInstance(context: Context): GrindDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if(instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext, GrindDatabase::class.java, "database")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}