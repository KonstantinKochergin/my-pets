package com.example.mypets.ui.synchronization

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.mypets.R
import com.example.mypets.data.layer.SynchronizationConstants
import com.example.mypets.data.layer.database.AppDatabase
import com.example.mypets.data.layer.firebase.database.AllUserDataDto
import com.example.mypets.data.layer.firebase.database.FirebaseDatabaseHelper
import com.example.mypets.databinding.FragmentStartScreenBinding
import com.example.mypets.databinding.FragmentSynchronizationBinding
import kotlinx.coroutines.*
import java.util.*

class SynchronizationFragment : Fragment() {

    private var _binding: FragmentSynchronizationBinding? = null

    private val binding get() = _binding!!

    lateinit var navController: NavController

    lateinit var buttonCreate: Button
    lateinit var buttonConnect: Button
    lateinit var userIdTV: TextView
    lateinit var userIdEditText: EditText

    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSynchronizationBinding.inflate(inflater, container, false)

        navController = findNavController()

        buttonCreate = binding.generateKeyButton
        buttonConnect = binding.connectButton
        userIdTV = binding.generatedKeyTv
        userIdEditText = binding.keyInput

        val prefs = binding.root.context.getSharedPreferences(SynchronizationConstants.PREFS_FILE_NAME, Context.MODE_PRIVATE)
        val userId = prefs.getString(SynchronizationConstants.USER_ID_PREFS_KEY, null)

        if (userId != null) {
            lockButtons()
            userIdTV.text = userId
        }

        buttonCreate.setOnClickListener {
            // Генерируем UUID и сохраняем данные в firebase
            uiScope.launch(Dispatchers.IO) {
                val nextUserId = UUID.randomUUID().toString()
                val allPets = AppDatabase.getDatabase(binding.root.context).petsDao().getAllUnreactive()
                val allMeals = AppDatabase.getDatabase(binding.root.context).mealsDao().getAllUnreactive()
                val allProcedures = AppDatabase.getDatabase(binding.root.context).proceduresDao().getAllUnreactive()
                val allMedicines = AppDatabase.getDatabase(binding.root.context).medicineDao().getAllUnreactive()
                val allDataDto = AllUserDataDto(allPets, allMeals, allProcedures, allMedicines)
                FirebaseDatabaseHelper.saveUserData(nextUserId, allDataDto)
                withContext(Dispatchers.Main) {
                    val prefsEditor = prefs.edit()
                    prefsEditor.putString(SynchronizationConstants.USER_ID_PREFS_KEY, nextUserId)
                    prefsEditor.commit()
                    lockButtons()
                    userIdTV.text = nextUserId
                }
            }
        }

        userIdTV.setOnClickListener {
            if (userId != null) {
                val clipboard = binding.root.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("plain text", userId)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(binding.root.context, "User ID copied!", Toast.LENGTH_SHORT).show()
            }
        }

        buttonConnect.setOnClickListener {
            val userIdFromInput = userIdEditText.text.toString()
            if (userIdFromInput != "") {
                val prefsEditor = prefs.edit()
                prefsEditor.putString(SynchronizationConstants.USER_ID_PREFS_KEY, userIdFromInput)
                prefsEditor.commit()
                lockButtons()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        job.cancel()
    }

    private fun lockButtons() {
        buttonCreate.isEnabled = false
        buttonCreate.isClickable = false
        buttonConnect.isEnabled = false
        buttonConnect.isClickable = false
    }
}