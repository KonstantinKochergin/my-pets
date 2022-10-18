package com.example.mypets.data.layer.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MealsDao {

    @Query("SELECT * from meals")
    fun getAll() : LiveData<List<MealEntity>>

    @Query("SELECT * from meals")
    fun getAllUnreactive() : List<MealEntity>

    @Query("SELECT * from meals WHERE petId = :petId")
    fun getAllByPetId(petId: Int) : LiveData<List<MealEntity>>

    @Query("SELECT * from meals WHERE petId = :petId")
    fun getAllByPetIdUnreactive(petId: Int) : List<MealEntity>

    @Query("SELECT * from meals WHERE id = :mealId")
    fun getMealById(mealId: Int) : LiveData<MealEntity>

    @Query("SELECT * from meals WHERE id = :mealId")
    fun getMealByIdSync(mealId: Int) : MealEntity

    @Insert
    fun addMeal(meal: MealEntity) : Long

    @Update
    fun updateMeal(meal: MealEntity)

    @Query("UPDATE meals SET isOverdue = :isOverdue WHERE id = :id")
    fun updateIsOverdue(id: Int, isOverdue: Boolean)

    @Query("UPDATE meals SET isOverdue = 0, nextTickMinutesEpoch = :nextTickMinutesEpoch WHERE id = :id")
    fun refreshMeal(id: Int, nextTickMinutesEpoch: Long)

    @Delete
    fun removeMeal(meal: MealEntity)
}

