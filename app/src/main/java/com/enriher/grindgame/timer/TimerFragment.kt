package com.enriher.grindgame.timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.enriher.grindgame.R
import com.enriher.grindgame.databinding.FragmentTimerBinding


class TimerFragment : Fragment() {

    private lateinit var viewModel: TimerViewModel
    private lateinit var binding: FragmentTimerBinding
    
    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_timer, container, false)

        // New TimerModelFactory
        val viewModelFactory = TimerViewModelFactory()

        // Reference to ViewModel
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TimerViewModel::class.java)

        // Data binding working with LiveData
        binding.timerViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.buttonEdit.setOnClickListener { handleEditButton() }

        return binding.root
    }

    private fun handleEditButton() {
        if(viewModel.editingTime.value ?: false) {
            viewModel.applyEditTime(binding.textEditClock.text.toString())
        } else {
            editTime()
        }
        viewModel.handleEditButton() // Change boolean flag
    }

    private fun editTime() {
        viewModel.editTime()
        // Set text in input to current time
        binding.textEditClock.setText(viewModel.currentTimeString.value)
    }

}