package com.example.bottomnavigationexample.ui.care

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomnavigationexample.R
import com.example.bottomnavigationexample.SharedViewModel
import com.example.bottomnavigationexample.adapters.MealsAdapter
import com.example.bottomnavigationexample.adapters.ProceduresAdapter
import com.example.bottomnavigationexample.databinding.FragmentCareBinding
import com.example.bottomnavigationexample.databinding.FragmentFoodBinding
import kotlinx.coroutines.*

class CareFragment : Fragment() {

    private var _binding: FragmentCareBinding? = null

    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    private lateinit var navController: NavController

    private lateinit var proceduresRv: RecyclerView
    private lateinit var proceduresAdapter: ProceduresAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        navController = findNavController()

        _binding = FragmentCareBinding.inflate(inflater, container, false)
        val root: View = binding.root

        proceduresRv = binding.proceduresRv
        proceduresAdapter = ProceduresAdapter(navController, sharedViewModel)

        binding.addProcedure.setOnClickListener {
            navController.navigate(R.id.action_navigation_care_to_editProcedureFragment)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        proceduresRv.layoutManager = LinearLayoutManager(view.context)
        proceduresRv.adapter = proceduresAdapter

        uiScope.launch(Dispatchers.IO) {
            val proceduresLiveData = sharedViewModel.getCurrentPetProcedures(view.context)
            withContext(Dispatchers.Main){
                proceduresLiveData.observe(viewLifecycleOwner){procedures ->
                    proceduresAdapter.setProceduresList(procedures)
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