package com.example.mypets.data.layer

import androidx.lifecycle.LiveData
import com.example.mypets.data.layer.database.AppDatabase
import com.example.mypets.data.layer.database.PetEntity

class PetsRepository(private val appDb: AppDatabase) {

    public fun getAllPets(): LiveData<List<PetEntity>> {
        return appDb.petsDao().getAll()
    }

    public fun addPet(pet: PetEntity) {
        appDb.petsDao().addPet(pet)
    }

    public fun removePet() {

    }
}