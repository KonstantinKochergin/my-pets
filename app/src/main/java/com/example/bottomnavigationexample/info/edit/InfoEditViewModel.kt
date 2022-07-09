package com.example.bottomnavigationexample.info.edit

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.bottomnavigationexample.data.layer.database.AppDatabase
import com.example.bottomnavigationexample.data.layer.database.PetEntity
import kotlinx.coroutines.*

class InfoEditViewModel : ViewModel() {

    suspend fun insertPetToDb(context: Context, pet: PetEntity) = withContext(Dispatchers.Default)  {
        return@withContext AppDatabase.getDatabase(context).petsDao().addPet(pet)
    }

    suspend fun updatePet(context: Context, pet: PetEntity) = withContext(Dispatchers.Default) {
        Log.d("kosty update pet name", pet.name)
        AppDatabase.getDatabase(context).petsDao().updatePet(pet)
    }
}