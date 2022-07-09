package com.example.bottomnavigationexample.data.layer

data class MergedTaskItem(
    val taskType: TaskType,
    val name: String,
    val restTimeMinutes: Int,
    var restTime: String    // 1ч 20м
)
