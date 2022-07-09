package com.example.bottomnavigationexample.ui.add.meal

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.bottomnavigationexample.data.layer.database.AppDatabase
import com.example.bottomnavigationexample.data.layer.database.MealEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddMealViewModel : ViewModel() {

    suspend fun getMealById(context: Context, mealId: Int) = withContext(Dispatchers.Default) {
        return@withContext AppDatabase.getDatabase(context).mealsDao().getMealById(mealId)
    }

    suspend fun addMeal(context: Context, mealEntity: MealEntity) = withContext(Dispatchers.Default) {
        return@withContext AppDatabase.getDatabase(context).mealsDao().addMeal(mealEntity)
    }

    suspend fun updateMeal(context: Context, mealEntity: MealEntity) = withContext(Dispatchers.Default) {
        return@withContext AppDatabase.getDatabase(context).mealsDao().updateMeal(mealEntity)
    }
}