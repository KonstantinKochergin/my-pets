package com.example.bottomnavigationexample.data.layer.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MedicineDao {

    @Query("SELECT * from medicine")
    fun getAll() : LiveData<List<MedicineEntity>>

    @Query("SELECT * from medicine WHERE petId = :petId")
    fun getAllByPetId(petId: Int) : LiveData<List<MedicineEntity>>

    @Query("SELECT * from medicine WHERE petId = :petId")
    fun getAllByPetIdUnreactive(petId: Int) : List<MedicineEntity>

    @Query("SELECT * from medicine WHERE id = :medicineId")
    fun getMedicineById(medicineId: Int) : LiveData<MedicineEntity>

    @Insert
    fun addMedicine(medicine: MedicineEntity) : Long

    @Update
    fun updateMedicine(medicine: MedicineEntity)

    @Delete
    fun removeMedicine(medicine: MedicineEntity)
}