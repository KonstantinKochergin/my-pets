package com.example.mypets.ui.choose.pet.screen

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.mypets.data.layer.database.AppDatabase
import com.example.mypets.data.layer.database.PetEntity
import com.example.mypets.models.PetToRenderOnChoosePet
import kotlinx.coroutines.*

class ChoosePetViewModel() : ViewModel() {

    private val viewModelJob = SupervisorJob()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun getPetsToRender(context: Context) : LiveData<List<PetToRenderOnChoosePet>> {
        return AppDatabase.getDatabase(context).petsDao().getAll().map { it.map { item -> PetToRenderOnChoosePet(item.id, item.name, item.imageUri) } }
    }

    fun addPet(context: Context, pet: PetEntity) {
        uiScope.launch {
            insertPetToDb(context, pet)
        }
    }

    suspend fun insertPetToDb(context: Context, pet: PetEntity) = withContext(Dispatchers.Default) {
        AppDatabase.getDatabase(context).petsDao().addPet(pet)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}