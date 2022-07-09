package com.example.bottomnavigationexample.info.edit

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.bottomnavigationexample.R
import com.example.bottomnavigationexample.SharedViewModel
import com.example.bottomnavigationexample.data.layer.database.PetEntity
import com.example.bottomnavigationexample.databinding.FragmentInfoEditBinding
import com.example.bottomnavigationexample.ui.choose.pet.screen.ChoosePetViewModel
import com.example.bottomnavigationexample.utils.PetsUtils
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class InfoEditFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: FragmentInfoEditBinding? = null

    private val binding get() = _binding!!

    private lateinit var infoEditViewModel: InfoEditViewModel

    private lateinit var navController: NavController

    private lateinit var photoContainer: ImageView

    private lateinit var buttonSave: Button

    private var imageFilePath = ""

    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentInfoEditBinding.inflate(inflater, container, false)

        navController = findNavController()

        infoEditViewModel = ViewModelProvider(this).get(InfoEditViewModel::class.java)

        photoContainer = binding.petPhoto
        buttonSave = binding.saveButton

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sexSpinner = binding.sexValue
        ArrayAdapter.createFromResource(view.context,  R.array.spinner_sex_items, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sexSpinner.adapter = adapter
        }

        val behaviorSpinner = binding.behaviourValue
        ArrayAdapter.createFromResource(view.context,  R.array.spinner_behaviour_items, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            behaviorSpinner.adapter = adapter
        }

        val castrationSpinner = binding.castrationValue
        ArrayAdapter.createFromResource(view.context,  R.array.spinner_castration_items, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            castrationSpinner.adapter = adapter
        }

        if (ContextCompat.checkSelfPermission(view.context, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
            PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_PERMISSION)
        }

        if (sharedViewModel.getCurrentPetId() != SharedViewModel.CURRENT_PET_ID_EMPTY_VALUE) {
            val sexItems = resources.getStringArray(R.array.spinner_sex_items)
            val behaviorItems = resources.getStringArray(R.array.spinner_behaviour_items)
            val castrationItems = resources.getStringArray(R.array.spinner_castration_items)
            uiScope.launch(Dispatchers.IO) {
                val petLiveData = sharedViewModel.getPetById(view.context, sharedViewModel.getCurrentPetId())
                withContext(Dispatchers.Main) {
                    petLiveData.observe(viewLifecycleOwner){currentPet ->
                        if (currentPet.imageUri != "") {
                            binding.petPhoto.setImageURI(Uri.parse(currentPet.imageUri))
                        }
                        else {
                            binding.petPhoto.setImageDrawable(resources.getDrawable(R.drawable.dog_placeholder))
                        }
                        binding.petNameEd.setText(currentPet.name)
                        binding.ageValue.setText(currentPet.age.toString())
                        binding.sexValue.setSelection(PetsUtils.getItemIndex(sexItems, currentPet.sex))
                        binding.behaviourValue.setSelection(PetsUtils.getItemIndex(behaviorItems, currentPet.behavior))
                        binding.furColorValue.setText(currentPet.furColor)
                        binding.castrationValue.setSelection(PetsUtils.getItemIndex(castrationItems, currentPet.castration))
                        binding.allergyValue.setText(currentPet.allergy)
                        imageFilePath = currentPet.imageUri
                    }
                }
            }
        }

        photoContainer.setOnClickListener{
            openCameraIntent(view.context)
        }

        buttonSave.setOnClickListener {
            val petName = binding.petNameEd.text.toString()
            var age = 0
            if (binding.ageValue.text.toString() != "") {
                age = binding.ageValue.text.toString().toInt()
            }
            val sex = binding.sexValue.selectedItem.toString()
            val behavior = binding.behaviourValue.selectedItem.toString()
            val furColor = binding.furColorValue.text.toString()
            val castration = binding.castrationValue.selectedItem.toString()
            val allergy = binding.allergyValue.text.toString()

            val petObj = PetEntity(petName, imageFilePath, age, sex, behavior, furColor, castration, allergy)
            uiScope.launch(Dispatchers.IO) {
                if (sharedViewModel.getCurrentPetId() == SharedViewModel.CURRENT_PET_ID_EMPTY_VALUE) {
                    val nextPetId = infoEditViewModel.insertPetToDb(view.context, petObj)
                    sharedViewModel.setCurrentPetId(nextPetId.toInt())
                }
                else {
                    petObj.id = sharedViewModel.getCurrentPetId()
                    infoEditViewModel.updatePet(view.context, petObj)
                }
                withContext(Dispatchers.Main) {
                    navController.navigate(R.id.action_documentsEditFragment_to_navigation_info)
                }
            }
        }
    }

    private fun openCameraIntent(context: Context) {
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (pictureIntent.resolveActivity(context.packageManager) != null) {
            var photoFile: File? = null
            photoFile = try {
                createImageFile(context)
            } catch (e: IOException) {
                e.printStackTrace()
                return
            }
            val photoUri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", photoFile!!)
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            startActivityForResult(pictureIntent, REQUEST_IMAGE)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(context: Context): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "IMG_" + timeStamp + "_"
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image: File = File.createTempFile(imageFileName, ".jpg", storageDir)
        imageFilePath = image.getAbsolutePath()
        return image
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION && grantResults.size > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Разрешения получены", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                photoContainer.setImageURI(Uri.parse(imageFilePath))
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(context, "You cancelled the operation", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        job.cancel()
    }

    companion object {
        const val REQUEST_IMAGE = 100
        const val REQUEST_PERMISSION = 200
    }
}