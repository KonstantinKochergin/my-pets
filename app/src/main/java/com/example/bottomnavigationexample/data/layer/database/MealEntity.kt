package com.example.bottomnavigationexample.data.layer.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class MealEntity(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "product") var product: String,
    @ColumnInfo(name = "productWeight") var productWeight: Int,
    @ColumnInfo(name = "mealTime") var mealTime: String,
    @ColumnInfo(name = "petId") var petId: Int,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
)