package com.example.mypets.ui.medicine.add

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
import com.example.mypets.data.layer.database.MedicineEntity
import com.example.mypets.databinding.FragmentMedicineAddBinding
import com.example.mypets.utils.DateTimeUtils
import com.example.mypets.workers.NotificationsWorker
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.TimeUnit

class MedicineAddFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: FragmentMedicineAddBinding? = null

    private val binding get() = _binding!!

    private lateinit var medicineAddViewModel: MedicineAddViewModel

    private lateinit var navController: NavController

    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMedicineAddBinding.inflate(inflater, container, false)

        medicineAddViewModel = ViewModelProvider(this).get(MedicineAddViewModel::class.java)

        navController = findNavController()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (sharedViewModel.getCurrentMedicineId() != SharedViewModel.CURRENT_MEDICINE_ID_EMPTY_VALUE) {
            uiScope.launch(Dispatchers.IO) {
                val currentProcedureLiveData = medicineAddViewModel.getMedicineById(view.context, sharedViewModel.getCurrentMedicineId())
                withContext(Dispatchers.Main){
                    currentProcedureLiveData.observe(viewLifecycleOwner){medicine ->
                        binding.medicineNameValue.setText(medicine.name)
                        binding.medicineTimeValue.setText(medicine.time)
                        binding.medicineDateValue.setText(medicine.date)
                    }
                }
            }
        }

        binding.saveButton.setOnClickListener {
            uiScope.launch(Dispatchers.IO) {
                val medicineName = binding.medicineNameValue.text.toString()
                val medicineTime = binding.medicineTimeValue.text.toString()
                val medicineDate = binding.medicineDateValue.text.toString()
                val nextTickMinutesEpoch = DateTimeUtils.parseTimeAndDateToMinutes(medicineTime, medicineDate)
                val petId = sharedViewModel.getCurrentPetId()
                if (sharedViewModel.getCurrentMedicineId() == SharedViewModel.CURRENT_MEDICINE_ID_EMPTY_VALUE) {
                    val medicine = MedicineEntity(medicineName, medicineTime, medicineDate, nextTickMinutesEpoch, false, petId)
                    val taskId = medicineAddViewModel.addMedicine(view.context, medicine)
                    val currentTimeMinutes = Date().time / 1000 / 60
                    val workerData = Data.Builder()
                    workerData.putInt(NotificationsWorker.TASK_TYPE_KEY, TaskType.HEALTH.value)
                    workerData.putInt(NotificationsWorker.TASK_ID_KEY, taskId.toInt())
                    workerData.putInt(NotificationsWorker.PET_ID_KEY, petId)
                    // Планируем задачу
                    val medWorkRequest = OneTimeWorkRequestBuilder<NotificationsWorker>()
                        .setInitialDelay(nextTickMinutesEpoch - currentTimeMinutes, TimeUnit.MINUTES)
                        .setInputData(workerData.build())
                        .build()
                    WorkManager.getInstance(view.context).enqueue(medWorkRequest)
                }
                else {
                    val medicine = MedicineEntity(medicineName, medicineTime, medicineDate, nextTickMinutesEpoch, false, petId, sharedViewModel.getCurrentMedicineId())
                    medicineAddViewModel.updateMedicine(view.context, medicine)
                }
                withContext(Dispatchers.Main) {
                    sharedViewModel.clearCurrentMedicineId()
                    navController.navigate(R.id.action_medicineAddFragment_to_navigation_health)
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