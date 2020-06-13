package com.enriher.grindgame

import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.enriher.grindgame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var seconds = 0
    var timerRunning = false
    var editingTime = false

    private var handler = Handler()
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.time = secondsToTime()

        binding.buttonStart.setOnClickListener { handleStartButton() }
        binding.buttonEdit.setOnClickListener { handleEditButton() }
    }

    private fun handleEditButton() {
        if(editingTime) {
            applyEditTime()
        } else {
            editTime()
        }
        editingTime = !editingTime
    }

    private fun editTime() {
        // If timer running, pause
        if(timerRunning) {
            handleStartButton()
        }
        // Set text in input to current time
        binding.textEditClock.setText(secondsToTime())

        // Change Button texts/UI elements visibility
        binding.buttonEdit.setText(R.string.apply)
        binding.textClock.visibility = View.INVISIBLE
        binding.textEditClock.visibility = View.VISIBLE
    }

    private fun applyEditTime() {
        val input = binding.textEditClock.text.toString().split(":")
        if(input.size != 3) {
            // TODO: Handle error
            // TODO: Handle no Int inputs
            return
        }

        seconds = input[0].toInt() * 3600 + input[1].toInt() * 60 + input[2].toInt()
        binding.textClock.text = secondsToTime()

        // Change Button texts/UI elements visibility
        binding.buttonEdit.setText(R.string.edit)
        binding.textClock.visibility = View.VISIBLE
        binding.textEditClock.visibility = View.GONE
    }

    private fun handleStartButton() {
        if(timerRunning) {
            pauseTimer()
        } else {
            startTimer()
        }
        timerRunning = !timerRunning
    }

    private fun startTimer() {
        binding.buttonStart.setText(R.string.pause)
        // Create the runnable action
        runnable = Runnable {
            seconds++
            binding.time = secondsToTime()
            handler.postDelayed(runnable, 1000)
        }

        handler.post(runnable)
    }

    private fun pauseTimer() {
        binding.buttonStart.setText(R.string.start)
        handler.removeCallbacks(runnable)
    }

    private fun secondsToTime(): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val seconds = seconds % 60
        return "${String.format("%02d", hours)}:${String.format("%02d", minutes)}:${String.format("%02d",seconds)}"
    }
}