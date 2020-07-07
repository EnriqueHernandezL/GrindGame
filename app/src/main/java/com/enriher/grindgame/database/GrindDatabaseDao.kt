package com.enriher.grindgame.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface GrindDatabaseDao {
    @Insert
    fun insert(timer: GrindTimer)

    @Update
    fun update(timer: GrindTimer)

    @Query("SELECT * FROM timer_table LIMIT 1")
    fun getGrindTimer(): GrindTimer?
}