package com.enriher.grindgame.timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.enriher.grindgame.R
import com.enriher.grindgame.database.GrindDatabase
import com.enriher.grindgame.databinding.FragmentTimerBinding
import kotlin.concurrent.timer


class TimerFragment : Fragment() {

    private lateinit var timerViewModel: TimerViewModel
    private lateinit var binding: FragmentTimerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_timer, container, false
        )

        // Reference to application, needed for database
        val application = requireNotNull(this.activity).application

        // Reference to dao
        val dataSource = GrindDatabase.getInstance(application).grindDatabaseDao

        // New TimerModelFactory
        val viewModelFactory = TimerViewModelFactory(dataSource)

        // Reference to ViewModel
        timerViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(TimerViewModel::class.java)

        // Data binding working with LiveData
        binding.lifecycleOwner = viewLifecycleOwner
        binding.timerViewModel = timerViewModel


        timerViewModel.editTimeEvent.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding.textEditClock.setText(timerViewModel.currentTimeString.value)
                timerViewModel.doneEditTimeEvent()
            }
        })

        timerViewModel.applyEditTimeEvent.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                timerViewModel.applyEditTime(binding.textEditClock.text.toString())
                timerViewModel.doneApplyEditTimeEvent()
            }
        })

        return binding.root
    }

}