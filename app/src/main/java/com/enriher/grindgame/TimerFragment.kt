package com.enriher.grindgame

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.enriher.grindgame.databinding.FragmentTimerBinding


class TimerFragment : Fragment() {

    private lateinit var viewModel: TimerViewModel

    private lateinit var binding: FragmentTimerBinding

    private var handler = Handler()

    private lateinit var runnable: Runnable

    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_timer, container, false)
        viewModel = ViewModelProviders.of(this).get(TimerViewModel::class.java)

        if(viewModel.timerRunning) {
            startTimer()
        }
        if(viewModel.editingTime) {
            editTime()
        }

        binding.time = viewModel.secondsToTime()
        binding.buttonStart.setOnClickListener { handleStartButton() }
        binding.buttonEdit.setOnClickListener { handleEditButton() }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if(viewModel.timerRunning) {
            handler.removeCallbacks(runnable)
        }
    }

    private fun handleEditButton() {
        if(viewModel.editingTime) {
            applyEditTime()
        } else {
            editTime()
        }
        viewModel.editingTime = !viewModel.editingTime
    }

    private fun handleStartButton() {
        if(viewModel.timerRunning) {
            pauseTimer()
        } else {
            startTimer()
        }
        viewModel.timerRunning = !viewModel.timerRunning
    }

    private fun startTimer() {
        binding.buttonStart.setText(R.string.pause)
        // Create the runnable action
        runnable = Runnable {
            viewModel.seconds++
            binding.time = viewModel.secondsToTime()
            handler.postDelayed(runnable, 1000)
        }

        handler.post(runnable)
    }

    private fun pauseTimer() {
        binding.buttonStart.setText(R.string.start)
        handler.removeCallbacks(runnable)
    }

    private fun editTime() {
        // If timer running, pause
        if(viewModel.timerRunning) {
            handleStartButton()
        }
        // Set text in input to current time
        binding.textEditClock.setText(viewModel.secondsToTime())

        // Change Button texts/UI elements visibility
        binding.buttonEdit.setText(R.string.apply)
        binding.textClock.visibility = View.INVISIBLE
        binding.textEditClock.visibility = View.VISIBLE
    }

    private fun applyEditTime() {
        // TODO extract in method and move to viewModel
        val input = binding.textEditClock.text.toString().split(":")
        if(input.size != 3) {
            // TODO: Handle error
            // TODO: Handle no Int inputs
            return
        }
        viewModel.seconds = input[0].toInt() * 3600 + input[1].toInt() * 60 + input[2].toInt()

        binding.textClock.text = viewModel.secondsToTime()

        // Change Button texts/UI elements visibility
        binding.buttonEdit.setText(R.string.edit)
        binding.textClock.visibility = View.VISIBLE
        binding.textEditClock.visibility = View.GONE
    }

}