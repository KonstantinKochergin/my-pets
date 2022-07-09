package com.example.bottomnavigationexample.ui.home

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomnavigationexample.SharedViewModel
import com.example.bottomnavigationexample.adapters.MergedTasksAdapter
import com.example.bottomnavigationexample.data.layer.MergedTaskItem
import com.example.bottomnavigationexample.data.layer.TaskType
import com.example.bottomnavigationexample.databinding.FragmentHomeBinding
import com.example.bottomnavigationexample.utils.DateTimeUtils
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var homeViewModel: HomeViewModel

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private lateinit var mergedTasksRv: RecyclerView

    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mergedTasksRv = binding.mergedItemsRv

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        uiScope.launch(Dispatchers.IO) {
            if (sharedViewModel.getCurrentPetId() != SharedViewModel.CURRENT_PET_ID_EMPTY_VALUE) {
                val petLiveData = sharedViewModel.getPetById(view.context, sharedViewModel.getCurrentPetId())
                withContext(Dispatchers.Main) {
                    petLiveData.observe(viewLifecycleOwner){pet ->
                        if (pet.imageUri != "") {
                            binding.petPhoto.setImageURI(Uri.parse(pet.imageUri))
                        }
                        binding.petNameTv.text = pet.name
                        binding.ageValue.text = pet.age.toString()
                        binding.sexValue.text = pet.sex
                    }
                }
            }
        }

        mergedTasksRv.layoutManager = LinearLayoutManager(view.context)

        uiScope.launch(Dispatchers.IO) {
            val meals = sharedViewModel.getCurrentPetMealsUnreactive(view.context)
            val procedures = sharedViewModel.getCurrentPetProceduresUnreactive(view.context)
            val medicines = sharedViewModel.getCurrentPetMedicineUnreactive(view.context)

            val mergedTaskItems: MutableList<MergedTaskItem> = mutableListOf()

            val sdf = SimpleDateFormat("hh:mm")
            val currentTime = sdf.format(Date())
            val currentTimeMinutes = DateTimeUtils.timeStringToMinutes(currentTime)
            for (mealIndex in meals.indices) {
                val meal = meals[mealIndex]
                val mealTimeMinutes = DateTimeUtils.timeStringToMinutes(meal.mealTime)
                if (mealTimeMinutes <= currentTimeMinutes) {     // ещё впереди
                    val restTimeMinutes = currentTimeMinutes - mealTimeMinutes
                    mergedTaskItems.add(MergedTaskItem(TaskType.FOOD, meal.name, restTimeMinutes, DateTimeUtils.minutesToHHMM(restTimeMinutes)))
                }
                else {
                    val restTimeMinutes = DateTimeUtils.daysToMinutes(1) - mealTimeMinutes + currentTimeMinutes
                    mergedTaskItems.add(MergedTaskItem(TaskType.FOOD, meal.name, restTimeMinutes, DateTimeUtils.minutesToHHMM(restTimeMinutes)))
                }
            }

            // TODO: учитывать procedureIntervalDays
            for (procedureIndex in procedures.indices) {
                val procedure = procedures[procedureIndex]
                val procedureTimeMinutes = DateTimeUtils.timeStringToMinutes(procedure.procedureTime)
                if (procedureTimeMinutes <= currentTimeMinutes) {
                    val restTimeMinutes = currentTimeMinutes - procedureTimeMinutes
                    mergedTaskItems.add(MergedTaskItem(TaskType.CARE, procedure.name, restTimeMinutes, DateTimeUtils.minutesToHHMM(restTimeMinutes)))
                }
                else {
                    val restTimeMinutes = DateTimeUtils.daysToMinutes(1) - procedureTimeMinutes + currentTimeMinutes
                    mergedTaskItems.add(MergedTaskItem(TaskType.CARE, procedure.name, restTimeMinutes, DateTimeUtils.minutesToHHMM(restTimeMinutes)))
                }
            }

            /*
            val sdfDays = SimpleDateFormat("dd")
            val currentDay = sdfDays.format(Date()).toInt()
            for (medicineIndex in medicines.indices) {
                val medicine = medicines[medicineIndex]
                val daysDiff = DateTimeUtils.getDaysFromDDMMYYYY(medicine.date) - currentDay
                val procedureTimeMinutes = DateTimeUtils.timeStringToMinutes(medicine.procedureTime)
                if (procedureTimeMinutes <= currentTimeMinutes) {
                    val restTimeMinutes = currentTimeMinutes - procedureTimeMinutes
                    mergedTaskItems.add(MergedTaskItem(TaskType.CARE, procedure.name, restTimeMinutes, DateTimeUtils.minutesToHHMM(restTimeMinutes)))
                }
            }
            */
            for (medicineIndex in medicines.indices) {
                val medicine = medicines[medicineIndex]
                val medicineTimeMinutes = DateTimeUtils.timeStringToMinutes(medicine.time)
                if (medicineTimeMinutes <= currentTimeMinutes) {
                    val restTimeMinutes = currentTimeMinutes - medicineTimeMinutes
                    mergedTaskItems.add(MergedTaskItem(TaskType.HEALTH, medicine.name, restTimeMinutes, DateTimeUtils.minutesToHHMM(restTimeMinutes)))
                }
                else {
                    val restTimeMinutes = DateTimeUtils.daysToMinutes(1) - medicineTimeMinutes + currentTimeMinutes
                    mergedTaskItems.add(MergedTaskItem(TaskType.HEALTH, medicine.name, restTimeMinutes, DateTimeUtils.minutesToHHMM(restTimeMinutes)))
                }
            }

            withContext(Dispatchers.Main) {
                mergedTasksRv.adapter = MergedTasksAdapter(mergedTaskItems, view.context)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        job.cancel()
    }
}