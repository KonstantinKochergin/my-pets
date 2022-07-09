package com.example.bottomnavigationexample.data.layer.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProceduresDao {

    @Query("SELECT * from procedures")
    fun getAll() : LiveData<List<ProcedureEntity>>

    @Query("SELECT * from procedures WHERE petId = :petId")
    fun getAllByPetId(petId: Int) : LiveData<List<ProcedureEntity>>

    @Query("SELECT * from procedures WHERE petId = :petId")
    fun getAllByPetIdUnreactive(petId: Int) : List<ProcedureEntity>

    @Query("SELECT * from procedures WHERE id = :procedureId")
    fun getProcedureById(procedureId: Int) : LiveData<ProcedureEntity>

    @Insert
    fun addProcedure(procedure: ProcedureEntity) : Long

    @Update
    fun updateProcedure(procedure: ProcedureEntity)

    @Delete
    fun removeProcedure(procedure: ProcedureEntity)

}