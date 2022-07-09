package com.example.bottomnavigationexample.data.layer.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pets")
data class PetEntity(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "imageUri") var imageUri: String,
    @ColumnInfo(name = "age") var age: Int,
    @ColumnInfo(name = "sex") var sex: String,
    @ColumnInfo(name = "behavior") var behavior: String,
    @ColumnInfo(name = "furColor") var furColor: String,
    @ColumnInfo(name = "castration") var castration: String,
    @ColumnInfo(name = "allergy") var allergy: String,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
)