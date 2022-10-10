package com.example.mypets.ui.general.schedule.screen

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mypets.SharedViewModel
import com.example.mypets.adapters.MergedTasksAdapter
import com.example.mypets.data.layer.MergedTaskItem
import com.example.mypets.data.layer.TaskType
import com.example.mypets.databinding.FragmentGeneralScheduleBinding
import kotlinx.coroutines.*
import java.util.*

class GeneralScheduleFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: FragmentGeneralScheduleBinding? = null

    private val binding get() = _binding!!

    private lateinit var mergedTasksRv: RecyclerView

    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentGeneralScheduleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mergedTasksRv = binding.mergedItemsRv

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getAndRenderMergedItems(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        job.cancel()
    }

    fun getAndRenderMergedItems(view: View) {
        mergedTasksRv.layoutManager = LinearLayoutManager(view.context)
        val thisFragment = this

        uiScope.launch(Dispatchers.IO) {
            val allPets = sharedViewModel.getAllPetsUnreactive(view.context)
            var mergedTaskItems: List<MergedTaskItem> = listOf()
            for (pet in allPets) {
                val petItems = getMergedItemsForPet(view.context, pet.id, pet.name)
                mergedTaskItems = mergedTaskItems + petItems
            }

            val sortedMergedTasks = mergedTaskItems.sortedWith(compareBy { it.restTimeMinutes })

            withContext(Dispatchers.Main) {
                mergedTasksRv.adapter = MergedTasksAdapter(sortedMergedTasks, view.context, null, thisFragment, view)
            }
        }
    }

    private suspend fun getMergedItemsForPet(context: Context, petId: Int, petName: String) : MutableList<MergedTaskItem> {
        val meals = sharedViewModel.getPetMealsUnreactive(context, petId)
        val procedures = sharedViewModel.getPetProceduresUnreactive(context, petId)
        val medicines = sharedViewModel.getPetMedicineUnreactive(context, petId)

        val mergedTaskItems: MutableList<MergedTaskItem> = mutableListOf()

        val currentTimeMinutes = Date().time / 1000 / 60

        for (mealIndex in meals.indices) {
            val meal = meals[mealIndex]
            val restTimeMinutes = meal.nextTickMinutesEpoch - currentTimeMinutes
            mergedTaskItems.add(MergedTaskItem(meal.id, meal.petId, petName, TaskType.FOOD, meal.name, restTimeMinutes.toInt(), meal.isOverdue))
        }

        for (procedureIndex in procedures.indices) {
            val procedure = procedures[procedureIndex]
            val restTimeMinutes = procedure.nextTickMinutesEpoch - currentTimeMinutes
            mergedTaskItems.add(MergedTaskItem(procedure.id, procedure.petId, petName, TaskType.CARE, procedure.name, restTimeMinutes.toInt(), procedure.isOverdue))
        }

        for (medicineIndex in medicines.indices) {
            val medicine = medicines[medicineIndex]
            val restTimeMinutes = medicine.nextTickMinutesEpoch - currentTimeMinutes
            mergedTaskItems.add(MergedTaskItem(medicine.id, medicine.petId, petName, TaskType.HEALTH, medicine.name, restTimeMinutes.toInt(), medicine.isOverdue))
        }

        return mergedTaskItems
    }
}