package com.enriher.grindgame.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="timer_table")
data class GrindTimer(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "timeInSeconds")
    var timeInSeconds: Int = 0
)