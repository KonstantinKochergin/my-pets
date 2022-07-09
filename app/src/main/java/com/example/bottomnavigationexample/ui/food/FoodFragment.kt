package com.example.bottomnavigationexample.ui.food

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
import com.example.bottomnavigationexample.databinding.FragmentFoodBinding
import kotlinx.coroutines.*

class FoodFragment : Fragment() {

    private var _binding: FragmentFoodBinding? = null

    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    private lateinit var navController: NavController

    private lateinit var mealsRv: RecyclerView
    private lateinit var mealsAdapter: MealsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        navController = findNavController()

        _binding = FragmentFoodBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mealsRv = binding.mealsRv
        mealsAdapter = MealsAdapter(navController, sharedViewModel)

        binding.addMealButton.setOnClickListener {
            navController.navigate(R.id.action_navigation_food_to_addMealFragment)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mealsRv.layoutManager = LinearLayoutManager(view.context)
        mealsRv.adapter = mealsAdapter

        uiScope.launch(Dispatchers.IO) {
            val mealsLiveData = sharedViewModel.getCurrentPetMeals(view.context)
            withContext(Dispatchers.Main){
                mealsLiveData.observe(viewLifecycleOwner){meals ->
                    mealsAdapter.setMealsList(meals)
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