package com.example.bottomnavigationexample.data.layer.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PetsDao {

    @Query("SELECT * from pets")
    fun getAll() : LiveData<List<PetEntity>>

    @Query("SELECT * from pets WHERE id=:id")
    fun getPetById(id: Int) : LiveData<PetEntity>

    @Insert
    fun addPet(pet: PetEntity) : Long

    @Update
    fun updatePet(pet: PetEntity)

    @Delete
    fun removePet(pet: PetEntity)
}