package com.example.bottomnavigationexample.ui.start.screen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.bottomnavigationexample.R
import com.example.bottomnavigationexample.databinding.FragmentStartScreenBinding

class StartScreenFragment : Fragment() {

    private var _binding: FragmentStartScreenBinding? = null

    private val binding get() = _binding!!

    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentStartScreenBinding.inflate(inflater, container, false)

        navController = findNavController()

        val buttonToPetsList = binding.choosePetButton
        val buttonToGeneralSchedule = binding.generalScheduleButton

        buttonToPetsList.setOnClickListener{
            navController.navigate(R.id.action_startScreenFragment_to_choosePetFragment)
        }

        buttonToGeneralSchedule.setOnClickListener{
            navController.navigate(R.id.action_startScreenFragment_to_generalScheduleFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}