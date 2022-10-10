package com.example.mypets.ui.health

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mypets.R
import com.example.mypets.SharedViewModel
import com.example.mypets.adapters.MedicineAdapter
import com.example.mypets.databinding.FragmentHealthBinding
import kotlinx.coroutines.*

class HealthFragment : Fragment() {

    private var _binding: FragmentHealthBinding? = null

    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    private lateinit var navController: NavController

    private lateinit var medicineRv: RecyclerView
    private lateinit var medicineAdapter: MedicineAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        navController = findNavController()

        _binding = FragmentHealthBinding.inflate(inflater, container, false)
        val root: View = binding.root

        medicineRv = binding.medicineRv
        medicineAdapter = MedicineAdapter(navController, sharedViewModel)

        binding.addMedicine.setOnClickListener {
            navController.navigate(R.id.action_navigation_health_to_medicineAddFragment)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        medicineRv.layoutManager = LinearLayoutManager(view.context)
        medicineRv.adapter = medicineAdapter

        uiScope.launch(Dispatchers.IO) {
            val medicineLiveData = sharedViewModel.getCurrentPetMedicine(view.context)
            withContext(Dispatchers.Main){
                medicineLiveData.observe(viewLifecycleOwner){medicine ->
                    medicineAdapter.setMedicineList(medicine)
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