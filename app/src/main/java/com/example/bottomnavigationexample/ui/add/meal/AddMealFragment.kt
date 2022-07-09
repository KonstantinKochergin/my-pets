package com.example.bottomnavigationexample.ui.add.meal

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.bottomnavigationexample.R
import com.example.bottomnavigationexample.SharedViewModel
import com.example.bottomnavigationexample.data.layer.database.MealEntity
import com.example.bottomnavigationexample.databinding.FragmentAddMealBinding
import com.example.bottomnavigationexample.databinding.FragmentFoodBinding
import com.example.bottomnavigationexample.info.edit.InfoEditViewModel
import kotlinx.coroutines.*

class AddMealFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: FragmentAddMealBinding? = null

    private val binding get() = _binding!!

    private lateinit var addMealViewModel: AddMealViewModel

    private lateinit var navController: NavController

    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddMealBinding.inflate(inflater, container, false)

        addMealViewModel = ViewModelProvider(this).get(AddMealViewModel::class.java)

        navController = findNavController()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (sharedViewModel.getCurrentMealId() != SharedViewModel.CURRENT_MEAL_ID_EMPTY_VALUE) {
            uiScope.launch(Dispatchers.IO) {
                val currentMealLiveData = addMealViewModel.getMealById(view.context, sharedViewModel.getCurrentMealId())
                withContext(Dispatchers.Main){
                    currentMealLiveData.observe(viewLifecycleOwner){meal ->
                        binding.mealNameValue.setText(meal.name)
                        binding.productNameValue.setText(meal.product)
                        binding.productWeightValue.setText(meal.productWeight.toString())
                        binding.mealTimeValue.setText(meal.mealTime)
                    }
                }
            }
        }

        binding.saveButton.setOnClickListener {
            uiScope.launch(Dispatchers.IO) {
                val mealName = binding.mealNameValue.text.toString()
                val product = binding.productNameValue.text.toString()
                var weight = 0
                if (binding.productWeightValue.text.toString() != "") {
                    weight = binding.productWeightValue.text.toString().toInt()
                }
                val productWeight = weight
                val mealTime = binding.mealTimeValue.text.toString()
                val petId = sharedViewModel.getCurrentPetId()
                if (sharedViewModel.getCurrentMealId() == SharedViewModel.CURRENT_MEAL_ID_EMPTY_VALUE) {
                    val meal = MealEntity(mealName, product, productWeight, mealTime, petId)
                    addMealViewModel.addMeal(view.context, meal)
                }
                else {
                    val meal = MealEntity(mealName, product, productWeight, mealTime, petId, sharedViewModel.getCurrentMealId())
                    addMealViewModel.updateMeal(view.context, meal)
                }
                withContext(Dispatchers.Main) {
                    sharedViewModel.clearCurrentMealId()
                    navController.navigate(R.id.action_addMealFragment_to_navigation_food)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        job.cancel()
    }
}