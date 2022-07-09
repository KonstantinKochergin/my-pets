package com.example.bottomnavigationexample.data.layer.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "procedures")
data class ProcedureEntity(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "procedureTime") var procedureTime: String,
    @ColumnInfo(name = "procedureIntervalDays") var procedureIntervalDays: Int, // раз в сколько дней повторять
    @ColumnInfo(name = "petId") var petId: Int,
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    //val currentDay: Int = 0
)
