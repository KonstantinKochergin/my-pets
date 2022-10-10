package com.example.mypets.data.layer.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "procedures")
data class ProcedureEntity(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "procedureTime") var procedureTime: String,
    @ColumnInfo(name = "procedureIntervalDays") var procedureIntervalDays: Int, // раз в сколько дней повторять
    @ColumnInfo(name = "nextTickMinutesEpoch") var nextTickMinutesEpoch: Long,     // время процедеры в минутах(кол-во минут от 01.01.1970)
    @ColumnInfo(name = "isOverdue") var isOverdue: Boolean,
    @ColumnInfo(name = "petId") var petId: Int,
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
)
