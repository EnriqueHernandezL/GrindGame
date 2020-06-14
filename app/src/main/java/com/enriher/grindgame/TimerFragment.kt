package com.enriher.grindgame

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.enriher.grindgame.databinding.FragmentTimerBinding


class TimerFragment : Fragment() {

    private lateinit var binding: FragmentTimerBinding

    var seconds = 0
    var timerRunning = false
    var editingTime = false

    private var handler = Handler()
    private lateinit var runnable: Runnable

    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_timer, container, false)

        binding.time = secondsToTime()

        binding.buttonStart.setOnClickListener { handleStartButton() }
        binding.buttonEdit.setOnClickListener { handleEditButton() }
        return binding.root
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