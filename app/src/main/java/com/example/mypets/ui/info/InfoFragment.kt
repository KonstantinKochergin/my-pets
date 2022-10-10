package com.example.mypets.ui.info

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.mypets.R
import com.example.mypets.SharedViewModel
import com.example.mypets.databinding.FragmentInfoBinding
import kotlinx.coroutines.*

class InfoFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var infoViewModel: InfoViewModel

    private lateinit var navController: NavController

    private var _binding: FragmentInfoBinding? = null

    private val binding get() = _binding!!

    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        infoViewModel = ViewModelProvider(this).get(InfoViewModel::class.java)

        navController = findNavController()

        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        uiScope.launch(Dispatchers.IO) {
            val petLiveData = sharedViewModel.getPetById(view.context, sharedViewModel.getCurrentPetId())
            withContext(Dispatchers.Main) {
                petLiveData.observe(viewLifecycleOwner){currentPet ->
                    binding.petNameTv.text = currentPet.name
                    binding.ageValue.text = currentPet.age.toString()
                    binding.sexValue.text = currentPet.sex
                    binding.behaviourValue.text = currentPet.behavior
                    binding.furColorValue.text = currentPet.furColor
                    binding.castrationValue.text = currentPet.castration
                    binding.allergyValue.text = currentPet.allergy
                    if (currentPet.imageUri != "") {
                        binding.petPhoto.setImageURI(Uri.parse(currentPet.imageUri))
                    }
                    else {
                        binding.petPhoto.setImageDrawable(resources.getDrawable(R.drawable.dog_placeholder))
                    }
                }
            }
        }

        binding.editButton.setOnClickListener {
            navController.navigate(R.id.action_navigation_info_to_documentsEditFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        job.cancel()
    }
}