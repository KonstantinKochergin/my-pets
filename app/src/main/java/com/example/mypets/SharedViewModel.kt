package com.example.mypets

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.mypets.data.layer.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SharedViewModel : ViewModel() {

    private var currentPetId: Int = CURRENT_PET_ID_EMPTY_VALUE

    private var currentMealId: Int = CURRENT_MEAL_ID_EMPTY_VALUE

    private var currentProcedureId: Int = CURRENT_PROCEDURE_ID_EMPTY_VALUE

    private var currentMedicineId: Int = CURRENT_MEDICINE_ID_EMPTY_VALUE

    fun getCurrentMedicineId() : Int {
        return currentMedicineId
    }

    fun setCurrentMedicineId(medicineId: Int) {
        currentMedicineId = medicineId
    }

    fun clearCurrentMedicineId() {
        currentMedicineId = CURRENT_MEDICINE_ID_EMPTY_VALUE
    }

    fun setCurrentPetId(petId: Int) {
        currentPetId = petId
    }

    fun getCurrentPetId() : Int {
        return currentPetId
    }

    fun clearCurrentPet() {
        currentPetId = CURRENT_PET_ID_EMPTY_VALUE
    }

    fun getCurrentProcedureId() : Int {
        return currentProcedureId
    }

    fun setCurrentProcedureId(procedureId: Int) {
        currentProcedureId = procedureId
    }

    fun clearCurrentProcedureId() {
        currentProcedureId = CURRENT_PROCEDURE_ID_EMPTY_VALUE
    }

    fun clearCurrentMealId() {
        currentMealId = CURRENT_MEAL_ID_EMPTY_VALUE
    }

    fun setCurrentMealId(mealId: Int) {
        currentMealId = mealId
    }

    fun getCurrentMealId() : Int {
        return currentMealId
    }

    suspend fun getAllPetsUnreactive(context: Context) = withContext(Dispatchers.Default) {
        return@withContext AppDatabase.getDatabase(context).petsDao().getAllUnreactive()
    }

    suspend fun getPetById(context: Context, petId: Int) = withContext(Dispatchers.Default) {
        return@withContext AppDatabase.getDatabase(context).petsDao().getPetById(petId)
    }

    suspend fun getCurrentPetMeals(context: Context) = withContext(Dispatchers.Default) {
        if (currentPetId == CURRENT_PET_ID_EMPTY_VALUE) {
            // TODO: реализовать проверку на отсутствие текущего питомца
        }
        return@withContext AppDatabase.getDatabase(context).mealsDao().getAllByPetId(currentPetId)
    }

    suspend fun getPetMealsUnreactive(context: Context, petId: Int) = withContext(Dispatchers.Default) {
        if (currentPetId == CURRENT_PET_ID_EMPTY_VALUE) {
            // TODO: реализовать проверку на отсутствие текущего питомца
        }
        return@withContext AppDatabase.getDatabase(context).mealsDao().getAllByPetIdUnreactive(petId)
    }

    suspend fun getCurrentPetMealsUnreactive(context: Context) = withContext(Dispatchers.Default) {
        if (currentPetId == CURRENT_PET_ID_EMPTY_VALUE) {
            // TODO: реализовать проверку на отсутствие текущего питомца
        }
        return@withContext AppDatabase.getDatabase(context).mealsDao().getAllByPetIdUnreactive(currentPetId)
    }

    suspend fun getCurrentPetProcedures(context: Context) = withContext(Dispatchers.Default) {
        if (currentPetId == CURRENT_PET_ID_EMPTY_VALUE) {
            // TODO: реализовать проверку на отсутствие текущего питомца
        }
        return@withContext AppDatabase.getDatabase(context).proceduresDao().getAllByPetId(currentPetId)
    }

    suspend fun getPetProceduresUnreactive(context: Context, petId: Int) = withContext(Dispatchers.Default) {
        if (currentPetId == CURRENT_PET_ID_EMPTY_VALUE) {
            // TODO: реализовать проверку на отсутствие текущего питомца
        }
        return@withContext AppDatabase.getDatabase(context).proceduresDao().getAllByPetIdUnreactive(petId)
    }

    suspend fun getCurrentPetProceduresUnreactive(context: Context) = withContext(Dispatchers.Default) {
        if (currentPetId == CURRENT_PET_ID_EMPTY_VALUE) {
            // TODO: реализовать проверку на отсутствие текущего питомца
        }
        return@withContext AppDatabase.getDatabase(context).proceduresDao().getAllByPetIdUnreactive(currentPetId)
    }

    suspend fun getCurrentPetMedicine(context: Context) = withContext(Dispatchers.Default) {
        if (currentPetId == CURRENT_PET_ID_EMPTY_VALUE) {
            // TODO: реализовать проверку на отсутствие текущего питомца
        }
        return@withContext AppDatabase.getDatabase(context).medicineDao().getAllByPetId(currentPetId)
    }

    suspend fun getPetMedicineUnreactive(context: Context, petId: Int) = withContext(Dispatchers.Default) {
        if (currentPetId == CURRENT_PET_ID_EMPTY_VALUE) {
            // TODO: реализовать проверку на отсутствие текущего питомца
        }
        return@withContext AppDatabase.getDatabase(context).medicineDao().getAllByPetIdUnreactive(petId)
    }

    suspend fun getCurrentPetMedicineUnreactive(context: Context) = withContext(Dispatchers.Default) {
        if (currentPetId == CURRENT_PET_ID_EMPTY_VALUE) {
            // TODO: реализовать проверку на отсутствие текущего питомца
        }
        return@withContext AppDatabase.getDatabase(context).medicineDao().getAllByPetIdUnreactive(currentPetId)
    }

    companion object {
        const val CURRENT_PET_ID_EMPTY_VALUE = -1
        const val CURRENT_MEAL_ID_EMPTY_VALUE = -1
        const val CURRENT_PROCEDURE_ID_EMPTY_VALUE = -1
        const val CURRENT_MEDICINE_ID_EMPTY_VALUE = -1
    }
}