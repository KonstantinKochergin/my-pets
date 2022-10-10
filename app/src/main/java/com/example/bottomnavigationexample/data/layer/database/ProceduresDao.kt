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

    @Query("SELECT * from procedures WHERE id = :procedureId")
    fun getProcedureByIdSync(procedureId: Int) : ProcedureEntity

    @Insert
    fun addProcedure(procedure: ProcedureEntity) : Long

    @Update
    fun updateProcedure(procedure: ProcedureEntity)

    @Query("UPDATE procedures SET isOverdue = :isOverdue WHERE id = :id")
    fun updateIsOverdue(id: Int, isOverdue: Boolean)

    @Query("UPDATE procedures SET isOverdue = 0, nextTickMinutesEpoch = :nextTickMinutesEpoch WHERE id = :id")
    fun refreshProcedure(id: Int, nextTickMinutesEpoch: Long)

    @Delete
    fun removeProcedure(procedure: ProcedureEntity)

}