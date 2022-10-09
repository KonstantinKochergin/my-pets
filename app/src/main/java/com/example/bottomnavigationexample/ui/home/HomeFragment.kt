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
import com.example.bottomnavigationexample.helpers.NotificationsHelper
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

            val currentTimeMinutes = Date().time / 1000 / 60

            for (mealIndex in meals.indices) {
                val meal = meals[mealIndex]
                val restTimeMinutes = meal.nextTickMinutesEpoch - currentTimeMinutes
                mergedTaskItems.add(MergedTaskItem(TaskType.FOOD, meal.name, restTimeMinutes.toInt(), meal.isOverdue))
            }

            // TODO: учитывать procedureIntervalDays
            for (procedureIndex in procedures.indices) {
                val procedure = procedures[procedureIndex]
                val restTimeMinutes = procedure.nextTickMinutesEpoch - currentTimeMinutes
                mergedTaskItems.add(MergedTaskItem(TaskType.CARE, procedure.name, restTimeMinutes.toInt(), procedure.isOverdue))
            }

            for (medicineIndex in medicines.indices) {
                val medicine = medicines[medicineIndex]
                val restTimeMinutes = medicine.nextTickMinutesEpoch - currentTimeMinutes
                mergedTaskItems.add(MergedTaskItem(TaskType.HEALTH, medicine.name, restTimeMinutes.toInt(), medicine.isOverdue))
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