package com.example.bottomnavigationexample.ui.edit.procedure

import android.os.Bundle
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
import com.example.bottomnavigationexample.data.layer.database.ProcedureEntity
import com.example.bottomnavigationexample.databinding.FragmentAddMealBinding
import com.example.bottomnavigationexample.databinding.FragmentEditProcedureBinding
import com.example.bottomnavigationexample.ui.add.meal.AddMealViewModel
import kotlinx.coroutines.*

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
                val petId = sharedViewModel.getCurrentPetId()
                if (sharedViewModel.getCurrentProcedureId() == SharedViewModel.CURRENT_PROCEDURE_ID_EMPTY_VALUE) {
                    val procedure = ProcedureEntity(procedureName, procedureTime, intervalDays, petId)
                    editProcedureViewModel.addProcedure(view.context, procedure)
                }
                else {
                    val procedure = ProcedureEntity(procedureName, procedureTime, intervalDays,petId, sharedViewModel.getCurrentProcedureId())
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