package com.example.mypets.data.layer.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MedicineDao {

    @Query("SELECT * from medicine")
    fun getAll() : LiveData<List<MedicineEntity>>

    @Query("SELECT * from medicine")
    fun getAllUnreactive() : List<MedicineEntity>

    @Query("SELECT * from medicine WHERE petId = :petId")
    fun getAllByPetId(petId: Int) : LiveData<List<MedicineEntity>>

    @Query("SELECT * from medicine WHERE petId = :petId")
    fun getAllByPetIdUnreactive(petId: Int) : List<MedicineEntity>

    @Query("SELECT * from medicine WHERE id = :medicineId")
    fun getMedicineById(medicineId: Int) : LiveData<MedicineEntity>

    @Query("SELECT * from medicine WHERE id = :medicineId")
    fun getMedicineByIdSync(medicineId: Int) : MedicineEntity

    @Insert
    fun addMedicine(medicine: MedicineEntity) : Long

    @Update
    fun updateMedicine(medicine: MedicineEntity)

    @Query("UPDATE medicine SET isOverdue = :isOverdue WHERE id = :id")
    fun updateIsOverdue(id: Int, isOverdue: Boolean)

    @Delete
    fun removeMedicine(medicine: MedicineEntity)

    @Query("DELETE FROM medicine WHERE id = :id")
    fun removeMedicineById(id: Int)
}