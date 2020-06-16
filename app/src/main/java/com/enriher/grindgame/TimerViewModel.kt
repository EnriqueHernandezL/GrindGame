package com.enriher.grindgame

import android.os.Handler
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {

    // Fragment data

    // TODO: Set in the app
    private val dailyGoal = 28800

    private val _seconds = MutableLiveData<Int>()
    val seconds: LiveData<Int>
        get() = _seconds

    private val _timerRunning = MutableLiveData<Boolean>()
    val timerRunning: LiveData<Boolean>
        get() = _timerRunning

    private val _editingTime = MutableLiveData<Boolean>()
    val editingTime: LiveData<Boolean>
        get() = _editingTime

    // Data processed to UI
    val buttonStartText = Transformations.map(_timerRunning) { running ->
        if (running) "Pause" else "Start"
    }
    val buttonEditText = Transformations.map(_editingTime) { editing ->
        if (editing) "Apply" else "Edit"
    }

    val currentTimeString = Transformations.map(seconds) { seconds ->
        secondsToTime(seconds)
    }

    val dailyProgess = Transformations.map(seconds) {seconds ->
        (100 * seconds) / dailyGoal
    }

    private var handler = Handler()
    private lateinit var runnable: Runnable

    init {
        _seconds.value = 0
        _timerRunning.value = false
        _editingTime.value = false
    }

    fun handleEditButton() {
        _editingTime.value = !(_editingTime.value ?: false)
    }

    fun editTime() {
        // If timer running, pause
        if(timerRunning.value == true) {
            handleStartButton()
        }
    }

    fun applyEditTime(time: String) {
        // TODO extract in method and move to viewModel
        val input = time.split(":")
        if(input.size != 3) {
            // TODO: Handle error
            // TODO: Handle no Int inputs
            return
        }
        _seconds.value = input[0].toInt() * 3600 + input[1].toInt() * 60 + input[2].toInt()
    }

    fun handleStartButton() {
        if(_timerRunning.value == true) {
            pauseTimer()
        } else {
            startTimer()
        }
        _timerRunning.value = !(_timerRunning.value ?: false)
    }


    private fun startTimer() {
        println("startTimer")
        // Create the runnable action
        runnable = Runnable {
            println("_seconds value" + _seconds.value)
            println("seconds value" + seconds.value)
            _seconds.value = _seconds.value?.plus(1)
            handler.postDelayed(runnable, 1000)
        }

        // Start Timer
        handler.post(runnable)
    }

    private fun pauseTimer() {
        handler.removeCallbacks(runnable)
    }

    fun finishDay() {
        // Stop timer if running
        if(timerRunning.value == true) {
            handleStartButton()
        }

        // TODO: Save seconds in database
        _seconds.value = 0
    }

    private fun secondsToTime(secondsArg: Int): String {
        val hours = secondsArg / 3600
        val minutes = (secondsArg % 3600) / 60
        val seconds = secondsArg % 60
        return "${String.format("%02d", hours)}:${String.format(
            "%02d",
            minutes
        )}:${String.format("%02d", seconds)}"
    }
}