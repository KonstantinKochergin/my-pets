package com.example.mypets.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.mypets.data.layer.TaskType
import com.example.mypets.data.layer.database.AppDatabase
import com.example.mypets.helpers.NotificationsHelper

class NotificationsWorker(val context: Context, val params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val taskType = params.inputData.getInt(TASK_TYPE_KEY, 1)
        val taskId = params.inputData.getInt(TASK_ID_KEY, -1)
        val petId = params.inputData.getInt(PET_ID_KEY, -1)
        var notificationTitle = "Время позаботиться о питомце!"
        var notificationDescription = ""
        if (taskId != -1 && petId != -1) {
            val currentPet = AppDatabase.getDatabase(context).petsDao().getPetByIdSync(petId)
            if (taskType == TaskType.CARE.value) {
                val currentProcedure = AppDatabase.getDatabase(context).proceduresDao().getProcedureByIdSync(taskId)
                AppDatabase.getDatabase(context).proceduresDao().updateIsOverdue(taskId, true)
                notificationDescription = "Питомец: ${currentPet.name}\n Уход: ${currentProcedure.name}"
            }
            else if (taskType == TaskType.HEALTH.value) {
                val currentMedicine = AppDatabase.getDatabase(context).medicineDao().getMedicineByIdSync(taskId)
                AppDatabase.getDatabase(context).medicineDao().updateIsOverdue(taskId, true)
                notificationDescription = "Питомец: ${currentPet.name}\n Медицина: ${currentMedicine.name}"
            }
            else if (taskType == TaskType.FOOD.value) {
                val currentMeal = AppDatabase.getDatabase(context).mealsDao().getMealByIdSync(taskId)
                AppDatabase.getDatabase(context).mealsDao().updateIsOverdue(taskId, true)
                notificationDescription = "Питомец: ${currentPet.name}\n Питание: ${currentMeal.name}"
            }
        }
        NotificationsHelper(context).createNotification(notificationTitle, notificationDescription, petId)
        return  Result.success()
    }

    companion object {
        val TASK_TYPE_KEY = "taskType"
        val TASK_ID_KEY = "taskId"
        val PET_ID_KEY = "petId"
    }
}