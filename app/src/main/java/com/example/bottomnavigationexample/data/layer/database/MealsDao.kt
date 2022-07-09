package com.example.bottomnavigationexample.data.layer.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MealsDao {

    @Query("SELECT * from meals")
    fun getAll() : LiveData<List<MealEntity>>

    @Query("SELECT * from meals WHERE petId = :petId")
    fun getAllByPetId(petId: Int) : LiveData<List<MealEntity>>

    @Query("SELECT * from meals WHERE petId = :petId")
    fun getAllByPetIdUnreactive(petId: Int) : List<MealEntity>

    @Query("SELECT * from meals WHERE id = :mealId")
    fun getMealById(mealId: Int) : LiveData<MealEntity>

    @Insert
    fun addMeal(meal: MealEntity) : Long

    @Update
    fun updateMeal(meal: MealEntity)

    @Delete
    fun removeMeal(meal: MealEntity)
}

