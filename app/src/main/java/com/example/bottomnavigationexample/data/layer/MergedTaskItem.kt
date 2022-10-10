package com.example.bottomnavigationexample.data.layer

data class MergedTaskItem(
    val taskId: Int,
    val petId: Int,
    val taskType: TaskType,
    val name: String,
    val restTimeMinutes: Int,
    var isOverdue: Boolean
)
