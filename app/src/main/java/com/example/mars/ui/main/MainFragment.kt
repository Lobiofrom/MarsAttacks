package com.example.mars.ui.main

import android.app.AlertDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mars.R
import com.example.mars.databinding.FragmentMainBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private var selectedDate = ""
    private var selectedRover = ""
    private val rovers = arrayOf("Curiosity", "Opportunity", "Spirit", "Perseverance")
    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("yyyy-M-dd", Locale.getDefault())


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)

        val constraints = CalendarConstraints.Builder()
            .setOpenAt(calendar.timeInMillis)
            .build()

        val dateDialog = MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(constraints)
            .setTitleText("Choose the date of Mars pictures")
            .build()

        dateDialog.addOnPositiveButtonClickListener { date ->
            calendar.timeInMillis = date
            selectedDate = dateFormat.format(calendar.time)

            showRoverSelector(requireContext(), rovers) {
                selectedRover = it.lowercase()
                val bundle = Bundle()
                bundle.putString("date", selectedDate)
                bundle.putString("rover", selectedRover)
                findNavController().navigate(R.id.action_mainFragment_to_secondFragment, bundle)
            }
        }
        dateDialog.show(childFragmentManager, "date")
        return binding.root
    }

    private fun showRoverSelector(context: Context, roverOptions: Array<String>, onRoverSelected: (String) -> Unit) {
        val roverSelector = AlertDialog.Builder(context)
        roverSelector.setTitle("Select Rover")
        roverSelector.setItems(roverOptions) { _, which ->
            onRoverSelected.invoke(roverOptions[which])
        }
        roverSelector.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}