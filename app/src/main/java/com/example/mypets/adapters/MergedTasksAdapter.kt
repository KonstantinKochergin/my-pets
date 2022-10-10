package com.example.mypets.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.mypets.R
import com.example.mypets.data.layer.MergedTaskItem
import com.example.mypets.data.layer.TaskType
import com.example.mypets.data.layer.database.AppDatabase
import com.example.mypets.ui.general.schedule.screen.GeneralScheduleFragment
import com.example.mypets.ui.home.HomeFragment
import com.example.mypets.utils.DateTimeUtils
import com.example.mypets.workers.NotificationsWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.TimeUnit

//A64543
class MergedTasksAdapter(
    val mergedTasks: List<MergedTaskItem>,
    val context: Context,
    val homeFragment: HomeFragment?,
    val generalSheduleFragment: GeneralScheduleFragment?,
    val viewFromFragment: View
) : RecyclerView.Adapter<MergedTasksAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root: ConstraintLayout
        val typeImage: ImageView
        val name: TextView
        val restTime: TextView
        val petName: TextView

        init {
            root = view.findViewById(R.id.merged_item_root)
            typeImage = view.findViewById(R.id.item_type_icon)
            name = view.findViewById(R.id.name)
            restTime = view.findViewById(R.id.rest_time)
            petName = view.findViewById(R.id.pet_name)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.merged_tasks_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mergedTask = mergedTasks.get(position)
        holder.name.text = mergedTask.name
        val restTimeString = DateTimeUtils.minutesToHHMM(mergedTask.restTimeMinutes)
        holder.restTime.text = restTimeString

        if (mergedTask.isOverdue) {
            holder.root.setBackgroundColor(Color.parseColor("#A64543"))
            holder.restTime.visibility = View.GONE
        }

        if (mergedTask.petName != null) {
            holder.petName.text = mergedTask.petName
        }
        else {
            holder.petName.visibility = View.GONE
        }

        if (mergedTask.taskType == TaskType.FOOD) {
            holder.typeImage.setImageDrawable(context.getDrawable(R.drawable.ic_inactive_food))
        }
        else if (mergedTask.taskType == TaskType.CARE) {
            holder.typeImage.setImageDrawable(context.getDrawable(R.drawable.ic_inactive_care_icon))
        }
        else if (mergedTask.taskType == TaskType.HEALTH) {
            holder.typeImage.setImageDrawable(context.getDrawable(R.drawable.ic_inactive_health_icon))
        }

        holder.root.setOnClickListener{
            if (mergedTask.isOverdue) {
                CoroutineScope(Dispatchers.IO).launch {
                    val currentDate = Date()
                    val currentTimeMinutes = Date().time / 1000 / 60
                    if (mergedTask.taskType == TaskType.FOOD) {
                        val currentMeal = AppDatabase.getDatabase(context).mealsDao().getMealByIdSync(mergedTask.taskId)
                        val nextDate = DateTimeUtils.getFormattedDatePlusIntervalString(currentDate, 1)
                        val nextTickMinutesEpoch = DateTimeUtils.parseTimeAndDateToMinutes(currentMeal.mealTime, nextDate)
                        AppDatabase.getDatabase(holder.root.context).mealsDao().refreshMeal(mergedTask.taskId, nextTickMinutesEpoch)
                        // Планирование задачи (уведомление + isOverdue = true)
                        val workerData = Data.Builder()
                        workerData.putInt(NotificationsWorker.TASK_TYPE_KEY, TaskType.FOOD.value)
                        workerData.putInt(NotificationsWorker.TASK_ID_KEY, mergedTask.taskId)
                        workerData.putInt(NotificationsWorker.PET_ID_KEY, mergedTask.petId)
                        // Планируем задачу
                        val mealWorkRequest = OneTimeWorkRequestBuilder<NotificationsWorker>()
                            .setInitialDelay(nextTickMinutesEpoch - currentTimeMinutes, TimeUnit.MINUTES)
                            .setInputData(workerData.build())
                            .build()
                        WorkManager.getInstance(holder.root.context).enqueue(mealWorkRequest)
                        withContext(Dispatchers.Main) {
                            if (homeFragment != null) {
                                homeFragment.getAndRenderMergedItems(viewFromFragment)
                            }
                            else if (generalSheduleFragment != null) {
                                generalSheduleFragment.getAndRenderMergedItems(viewFromFragment)
                            }
                        }
                    }
                    else if (mergedTask.taskType == TaskType.CARE) {
                        val currentProcedure = AppDatabase.getDatabase(holder.root.context).proceduresDao().getProcedureByIdSync(mergedTask.taskId)
                        val nextDate = DateTimeUtils.getFormattedDatePlusIntervalString(currentDate, currentProcedure.procedureIntervalDays)
                        val nextTickMinutesEpoch = DateTimeUtils.parseTimeAndDateToMinutes(currentProcedure.procedureTime, nextDate)
                        AppDatabase.getDatabase(holder.root.context).proceduresDao().refreshProcedure(mergedTask.taskId, nextTickMinutesEpoch)
                        // Планирование задачи (уведомление + isOverdue = true)
                        val workerData = Data.Builder()
                        workerData.putInt(NotificationsWorker.TASK_TYPE_KEY, TaskType.CARE.value)
                        workerData.putInt(NotificationsWorker.TASK_ID_KEY, mergedTask.taskId)
                        workerData.putInt(NotificationsWorker.PET_ID_KEY, mergedTask.petId)
                        // Планируем задачу
                        val procedureWorkRequest = OneTimeWorkRequestBuilder<NotificationsWorker>()
                            .setInitialDelay(nextTickMinutesEpoch - currentTimeMinutes, TimeUnit.MINUTES)
                            .setInputData(workerData.build())
                            .build()
                        WorkManager.getInstance(holder.root.context).enqueue(procedureWorkRequest)
                        withContext(Dispatchers.Main) {
                            if (homeFragment != null) {
                                homeFragment.getAndRenderMergedItems(viewFromFragment)
                            }
                            else if (generalSheduleFragment != null) {
                                generalSheduleFragment.getAndRenderMergedItems(viewFromFragment)
                            }
                        }
                    }
                    else if (mergedTask.taskType == TaskType.HEALTH) {
                        AppDatabase.getDatabase(holder.root.context).medicineDao().removeMedicineById(mergedTask.taskId)
                        withContext(Dispatchers.Main) {
                            if (homeFragment != null) {
                                homeFragment.getAndRenderMergedItems(viewFromFragment)
                            }
                            else if (generalSheduleFragment != null) {
                                generalSheduleFragment.getAndRenderMergedItems(viewFromFragment)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mergedTasks.size
    }
}