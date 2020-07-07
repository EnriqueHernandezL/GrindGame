package com.enriher.grindgame.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.enriher.grindgame.database.GrindDatabaseDao

class TimerViewModelFactory(
    private val dataSource: GrindDatabaseDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimerViewModel::class.java)) {
            return TimerViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}