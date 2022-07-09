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

class MergedTasksAdapter(val mergedTasks: List<MergedTaskItem>, val context: Context) : RecyclerView.Adapter<MergedTasksAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val typeImage: ImageView
        val name: TextView
        val restTime: TextView

        init {
            typeImage = view.findViewById(R.id.item_type_icon)
            name = view.findViewById(R.id.name)
            restTime = view.findViewById(R.id.rest_time)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.merged_tasks_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = mergedTasks.get(position).name
        holder.restTime.text = mergedTasks.get(position).restTime
        if (mergedTasks.get(position).taskType == TaskType.FOOD) {
            holder.typeImage.setImageDrawable(context.getDrawable(R.drawable.ic_inactive_food))
        }
        else if (mergedTasks.get(position).taskType == TaskType.CARE) {
            holder.typeImage.setImageDrawable(context.getDrawable(R.drawable.ic_inactive_care_icon))
        }
        else if (mergedTasks.get(position).taskType == TaskType.HEALTH) {
            holder.typeImage.setImageDrawable(context.getDrawable(R.drawable.ic_inactive_health_icon))
        }
    }

    override fun getItemCount(): Int {
        return mergedTasks.size
    }
}