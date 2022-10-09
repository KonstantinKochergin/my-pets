package com.example.bottomnavigationexample.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.bottomnavigationexample.helpers.NotificationsHelper

class NotificationsWorker(val context: Context, val params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        NotificationsHelper(context).createNotification("Время пришло!", "Пришло время позаботиться о питомце")
        return  Result.success()
    }
}