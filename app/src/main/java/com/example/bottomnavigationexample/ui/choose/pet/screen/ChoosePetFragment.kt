package com.example.bottomnavigationexample.ui.choose.pet.screen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomnavigationexample.App
import com.example.bottomnavigationexample.R
import com.example.bottomnavigationexample.SharedViewModel
import com.example.bottomnavigationexample.adapters.PetsChoosePetScreenAdapter
import com.example.bottomnavigationexample.data.layer.database.PetEntity
import com.example.bottomnavigationexample.databinding.FragmentChoosePetBinding

class ChoosePetFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: FragmentChoosePetBinding? = null

    private val binding get() = _binding!!

    lateinit var navController: NavController

    lateinit var choosePetViewModel: ChoosePetViewModel

    lateinit var petsAdapter: PetsChoosePetScreenAdapter

    lateinit var petsRv: RecyclerView

    lateinit var addPetButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChoosePetBinding.inflate(inflater, container, false)

        navController = findNavController()

        choosePetViewModel = ViewModelProvider(this).get(ChoosePetViewModel::class.java)

        petsAdapter = PetsChoosePetScreenAdapter(emptyList(), navController, sharedViewModel, resources.getDrawable(R.drawable.dog_placeholder))

        petsRv = binding.petsRv
        addPetButton = binding.addPetButton

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        sharedViewModel.clearCurrentPet()

        petsRv.layoutManager = LinearLayoutManager(view.context)
        petsRv.adapter = petsAdapter

        choosePetViewModel.getPetsToRender(view.context).observe(viewLifecycleOwner){pets ->
            petsAdapter.setPetsList(pets)
        }

        addPetButton.setOnClickListener{
            navController.navigate(R.id.action_choosePetFragment_to_documentsEditFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}