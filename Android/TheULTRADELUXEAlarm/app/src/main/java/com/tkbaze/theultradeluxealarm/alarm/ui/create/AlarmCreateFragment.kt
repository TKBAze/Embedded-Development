package com.tkbaze.theultradeluxealarm.alarm.ui.create

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD
import com.google.android.material.timepicker.TimeFormat
import com.tkbaze.theultradeluxealarm.R
import com.tkbaze.theultradeluxealarm.alarm.AlarmApplication
import com.tkbaze.theultradeluxealarm.alarm.AlarmViewModel
import com.tkbaze.theultradeluxealarm.alarm.AlarmViewModelFactory
import com.tkbaze.theultradeluxealarm.alarm.ui.alarmlist.AlarmListFragment
import com.tkbaze.theultradeluxealarm.databinding.FragmentAlarmCreateBinding


class AlarmCreateFragment : Fragment() {

    private val viewModel: AlarmViewModel by activityViewModels {
        AlarmViewModelFactory(
            activity?.application!!,
            (activity?.application as AlarmApplication).database.alarmDao()
        )
    }

    private var _binding: FragmentAlarmCreateBinding? = null
    private val binding get() = _binding!!

    // Placeholder variables
    private val tempId = 3808

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlarmCreateBinding.inflate(layoutInflater)

        val picker =
            MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).setHour(8).setMinute(0)
                .setTitleText(R.string.sel_time).setInputMode(INPUT_MODE_KEYBOARD).build()

        picker.addOnPositiveButtonClickListener {
            binding.textTemp.text = String.format("%02d:%02d", picker.hour, picker.minute)
        }

        binding.buttonSetAlarm.setOnClickListener {
            Log.d("AlarmViewModel", "add button")

            viewModel.addNewAlarm(
                picker.hour,
                picker.minute,
                binding.switchRecurring.isChecked,
                binding.switchScheduled.isChecked
            )

            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container, AlarmListFragment())
            fragmentTransaction.commit()
        }

        binding.textTemp.setOnClickListener {
            picker.show(childFragmentManager, "tag")
        }
        picker.show(childFragmentManager, "tag")
        return binding.root
    }

}