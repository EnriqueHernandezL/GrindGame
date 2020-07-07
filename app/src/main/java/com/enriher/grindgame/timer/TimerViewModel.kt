package com.enriher.grindgame.timer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.enriher.grindgame.database.GrindDatabaseDao
import com.enriher.grindgame.database.GrindTimer
import kotlinx.coroutines.*

class TimerViewModel(
    val database: GrindDatabaseDao
) : ViewModel() {

    // TODO: Set in the app
    private val dailyGoal = 28800 // 8 hours in seconds

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

    val dailyProgess = Transformations.map(seconds) { seconds ->
        (100 * seconds) / dailyGoal
    }

    // Edit time event
    private val _editTimeEvent = MutableLiveData<Boolean>()
    val editTimeEvent: LiveData<Boolean>
        get() = _editTimeEvent

    // Apply Edit time event
    private val _applyEditTimeEvent = MutableLiveData<Boolean>()
    val applyEditTimeEvent: LiveData<Boolean>
        get() = _applyEditTimeEvent

    private var timerJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + timerJob)

    init {
        uiScope.launch {
            println(getTimerFromDatabase())
            _seconds.value = getTimerFromDatabase()?.timeInSeconds ?: 0
        }
        _timerRunning.value = false
        _editingTime.value = false
        _editTimeEvent.value = false
    }

    private suspend fun getTimerFromDatabase(): GrindTimer? {
        return withContext(Dispatchers.IO) {
            database.getGrindTimer()
        }
    }

    fun handleEditButton() {
        if (_editingTime.value ?: false) {
            _applyEditTimeEvent.value = true
            // applyEditTime() will be called back with the fragment with the inputted value
        } else {
            editTime()
        }
        _editingTime.value = !(_editingTime.value ?: false)
    }

    fun doneEditTimeEvent() {
        _editTimeEvent.value = false
    }

    fun doneApplyEditTimeEvent() {
        _applyEditTimeEvent.value = false
    }

    private fun editTime() {
        // Fire edit time event
        // TextEditView will be updated in the timerFragment
        _editTimeEvent.value = true

        // If timer running, pause
        if (timerRunning.value == true) {
            handleStartButton()
        }

    }

    fun applyEditTime(time: String) {
        val input = time.split(":")
        if (input.size != 3) {
            // TODO: Handle error
            // TODO: Handle no Int inputs
            return
        }
        _seconds.value = input[0].toInt() * 3600 + input[1].toInt() * 60 + input[2].toInt()
    }

    fun handleStartButton() {
        if (_timerRunning.value == true) {
            pauseTimer()
        } else {
            startTimer()
        }
        _timerRunning.value = !(_timerRunning.value ?: false)
    }

    private fun startTimer() {
        uiScope.launch {
            delay(1000)
            while (timerRunning.value == true) {
                _seconds.value = _seconds.value?.plus(1)
//                println("Seconds: " + seconds.value)
                updateTimeInDatabase()
                delay(1000)
            }
        }
    }

    private fun pauseTimer() {
        // TODO: Do something?
    }

    fun finishDay() {
        // Stop timer if running
        if (timerRunning.value == true) {
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

    override fun onCleared() {
        println("onCleared called")
//        _timerRunning.value = false
//
//        // Update database
//        uiScope.launch {
//            updateTimeInDatabase()
//        }

        // TODO: Cancel job in only view destroyed (i.e. quit app)
        timerJob.cancel()

        super.onCleared()
    }

    private suspend fun updateTimeInDatabase() {
        println("Updating database")
        val timerInDatabase = getTimerFromDatabase()
        if (timerInDatabase != null) {
            timerInDatabase.timeInSeconds = _seconds.value!!
            update(timerInDatabase)
        } else {
            insert(GrindTimer(timeInSeconds = seconds.value!!))
        }
        println("Database updated")
    }

    private suspend fun insert(grindTimer: GrindTimer) {
        withContext(Dispatchers.IO) {
            database.insert(grindTimer)
        }
    }

    private suspend fun update(grindTimer: GrindTimer) {
        withContext(Dispatchers.IO) {
            database.update(grindTimer)
        }
    }
}