package com.example.mypets.ui.add.meal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.mypets.R
import com.example.mypets.SharedViewModel
import com.example.mypets.data.layer.TaskType
import com.example.mypets.data.layer.database.MealEntity
import com.example.mypets.databinding.FragmentAddMealBinding
import com.example.mypets.utils.DateTimeUtils
import com.example.mypets.workers.NotificationsWorker
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.TimeUnit

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
                val nextTickMinutesEpoch = DateTimeUtils.parseTimeAndDateToMinutes(mealTime)
                val currentTimeMinutes = Date().time / 1000 / 60
                if (sharedViewModel.getCurrentMealId() == SharedViewModel.CURRENT_MEAL_ID_EMPTY_VALUE) {
                    val meal = MealEntity(mealName, product, productWeight, mealTime, nextTickMinutesEpoch,false, petId)
                    val taskId = addMealViewModel.addMeal(view.context, meal)
                    // Планирование задачи (уведомление + isOverdue = true)
                    val workerData = Data.Builder()
                    workerData.putInt(NotificationsWorker.TASK_TYPE_KEY, TaskType.FOOD.value)
                    workerData.putInt(NotificationsWorker.TASK_ID_KEY, taskId.toInt())
                    workerData.putInt(NotificationsWorker.PET_ID_KEY, petId)
                    // Планируем задачу
                    val mealWorkRequest = OneTimeWorkRequestBuilder<NotificationsWorker>()
                        .setInitialDelay(nextTickMinutesEpoch - currentTimeMinutes, TimeUnit.MINUTES)
                        .setInputData(workerData.build())
                        .build()
                    WorkManager.getInstance(view.context).enqueue(mealWorkRequest)
                }
                else {
                    val meal = MealEntity(mealName, product, productWeight, mealTime, nextTickMinutesEpoch, false, petId, sharedViewModel.getCurrentMealId())
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