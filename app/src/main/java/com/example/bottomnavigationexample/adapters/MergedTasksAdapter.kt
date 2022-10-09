package com.example.bottomnavigationexample.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomnavigationexample.R
import com.example.bottomnavigationexample.data.layer.MergedTaskItem
import com.example.bottomnavigationexample.data.layer.TaskType
import com.example.bottomnavigationexample.utils.DateTimeUtils

class MergedTasksAdapter(val mergedTasks: List<MergedTaskItem>, val context: Context) : RecyclerView.Adapter<MergedTasksAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val typeImage: ImageView
        val name: TextView
        val restTime: TextView
        val overdue: TextView

        init {
            typeImage = view.findViewById(R.id.item_type_icon)
            name = view.findViewById(R.id.name)
            restTime = view.findViewById(R.id.rest_time)
            overdue = view.findViewById(R.id.overdue_value)
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
        holder.overdue.text = if (mergedTask.isOverdue) "true" else "false"
        if (mergedTask.taskType == TaskType.FOOD) {
            holder.typeImage.setImageDrawable(context.getDrawable(R.drawable.ic_inactive_food))
        }
        else if (mergedTask.taskType == TaskType.CARE) {
            holder.typeImage.setImageDrawable(context.getDrawable(R.drawable.ic_inactive_care_icon))
        }
        else if (mergedTask.taskType == TaskType.HEALTH) {
            holder.typeImage.setImageDrawable(context.getDrawable(R.drawable.ic_inactive_health_icon))
        }
    }

    override fun getItemCount(): Int {
        return mergedTasks.size
    }
}