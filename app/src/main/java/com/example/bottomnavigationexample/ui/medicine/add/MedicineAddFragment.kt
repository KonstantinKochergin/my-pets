package com.example.bottomnavigationexample.ui.medicine.add

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
import com.example.bottomnavigationexample.data.layer.database.MedicineEntity
import com.example.bottomnavigationexample.data.layer.database.ProcedureEntity
import com.example.bottomnavigationexample.databinding.FragmentEditProcedureBinding
import com.example.bottomnavigationexample.databinding.FragmentMedicineAddBinding
import com.example.bottomnavigationexample.ui.edit.procedure.EditProcedureViewModel
import kotlinx.coroutines.*

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
                val petId = sharedViewModel.getCurrentPetId()
                if (sharedViewModel.getCurrentMedicineId() == SharedViewModel.CURRENT_MEDICINE_ID_EMPTY_VALUE) {
                    val medicine = MedicineEntity(medicineName, medicineTime, medicineDate, petId)
                    medicineAddViewModel.addMedicine(view.context, medicine)
                }
                else {
                    val medicine = MedicineEntity(medicineName, medicineTime, medicineDate, petId, sharedViewModel.getCurrentMedicineId())
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