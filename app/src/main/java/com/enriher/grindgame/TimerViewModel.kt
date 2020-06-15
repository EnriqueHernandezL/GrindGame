package com.enriher.grindgame

import android.os.Handler
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {
    var seconds = 0
    var timerRunning = false
    var editingTime = false

    init {

    }

    fun secondsToTime(): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val seconds = seconds % 60
        return "${String.format("%02d", hours)}:${String.format("%02d", minutes)}:${String.format("%02d",seconds)}"
    }
}