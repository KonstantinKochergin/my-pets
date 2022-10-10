package com.example.mypets.data.layer.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicine")
data class MedicineEntity(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "time") var time: String,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "nextTickMinutesEpoch") var nextTickMinutesEpoch: Long,     // время процедеры в минутах(кол-во минут от 01.01.1970)
    @ColumnInfo(name = "isOverdue") var isOverdue: Boolean,
    @ColumnInfo(name = "petId") var petId: Int,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
)
