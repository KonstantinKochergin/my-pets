package com.example.mypets.ui.edit.procedure

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
import com.example.mypets.data.layer.database.ProcedureEntity
import com.example.mypets.databinding.FragmentEditProcedureBinding
import com.example.mypets.utils.DateTimeUtils
import com.example.mypets.workers.NotificationsWorker
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.TimeUnit

class EditProcedureFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: FragmentEditProcedureBinding? = null

    private val binding get() = _binding!!

    private lateinit var editProcedureViewModel: EditProcedureViewModel

    private lateinit var navController: NavController

    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProcedureBinding.inflate(inflater, container, false)

        editProcedureViewModel = ViewModelProvider(this).get(EditProcedureViewModel::class.java)

        navController = findNavController()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (sharedViewModel.getCurrentProcedureId() != SharedViewModel.CURRENT_PROCEDURE_ID_EMPTY_VALUE) {
            uiScope.launch(Dispatchers.IO) {
                val currentProcedureLiveData = editProcedureViewModel.getProcedureById(view.context, sharedViewModel.getCurrentProcedureId())
                withContext(Dispatchers.Main){
                    currentProcedureLiveData.observe(viewLifecycleOwner){procedure ->
                        binding.procedureNameValue.setText(procedure.name)
                        binding.procedureTimeValue.setText(procedure.procedureTime)
                        binding.procedureIntervalValue.setText(procedure.procedureIntervalDays.toString())
                    }
                }
            }
        }

        binding.saveButton.setOnClickListener {
            uiScope.launch(Dispatchers.IO) {
                val procedureName = binding.procedureNameValue.text.toString()
                val procedureTime = binding.procedureTimeValue.text.toString()
                var intervalDays = 0
                if (binding.procedureIntervalValue.text.toString() != "") {
                    intervalDays = binding.procedureIntervalValue.text.toString().toInt()
                }
                val nextTickMinutesEpoch = DateTimeUtils.parseTimeAndDateToMinutes(procedureTime)
                val currentTimeMinutes = Date().time / 1000 / 60
                val petId = sharedViewModel.getCurrentPetId()
                if (sharedViewModel.getCurrentProcedureId() == SharedViewModel.CURRENT_PROCEDURE_ID_EMPTY_VALUE) {
                    val procedure = ProcedureEntity(procedureName, procedureTime, intervalDays, nextTickMinutesEpoch, false, petId)
                    val taskId = editProcedureViewModel.addProcedure(view.context, procedure)
                    // Планирование задачи (уведомление + isOverdue = true)
                    val workerData = Data.Builder()
                    workerData.putInt(NotificationsWorker.TASK_TYPE_KEY, TaskType.CARE.value)
                    workerData.putInt(NotificationsWorker.TASK_ID_KEY, taskId.toInt())
                    workerData.putInt(NotificationsWorker.PET_ID_KEY, petId)
                    // Планируем задачу
                    val procedureWorkRequest = OneTimeWorkRequestBuilder<NotificationsWorker>()
                        .setInitialDelay(nextTickMinutesEpoch - currentTimeMinutes, TimeUnit.MINUTES)
                        .setInputData(workerData.build())
                        .build()
                    WorkManager.getInstance(view.context).enqueue(procedureWorkRequest)

                }
                else {
                    val procedure = ProcedureEntity(procedureName, procedureTime, intervalDays, nextTickMinutesEpoch, false, petId, sharedViewModel.getCurrentProcedureId())
                    editProcedureViewModel.updateProcedure(view.context, procedure)
                }
                withContext(Dispatchers.Main) {
                    sharedViewModel.clearCurrentProcedureId()
                    navController.navigate(R.id.action_editProcedureFragment_to_navigation_care)
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