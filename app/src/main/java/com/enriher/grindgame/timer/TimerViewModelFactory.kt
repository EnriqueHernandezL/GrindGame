package com.enriher.grindgame.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class TimerViewModelFactory: ViewModelProvider.Factory {
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TimerViewModel::class.java)) {
            return TimerViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}