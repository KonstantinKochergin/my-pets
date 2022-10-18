package com.example.mypets.data.layer.firebase.database

import com.example.mypets.data.layer.database.MealEntity
import com.example.mypets.data.layer.database.MedicineEntity
import com.example.mypets.data.layer.database.PetEntity
import com.example.mypets.data.layer.database.ProcedureEntity

data class AllUserDataDto(
    val pets: List<PetEntity>,
    val meals: List<MealEntity>,
    val procedures: List<ProcedureEntity>,
    val medicines: List<MedicineEntity>
)