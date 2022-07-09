package com.example.bottomnavigationexample.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomnavigationexample.R
import com.example.bottomnavigationexample.SharedViewModel
import com.example.bottomnavigationexample.data.layer.database.AppDatabase
import com.example.bottomnavigationexample.data.layer.database.MealEntity
import com.example.bottomnavigationexample.data.layer.database.ProcedureEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProceduresAdapter(
    private val navController: NavController,
    private val sharedViewModel: SharedViewModel
) :
    RecyclerView.Adapter<ProceduresAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root: View
        val procedureName: TextView
        val procedureTime: TextView
        val procedureInterval: TextView
        val editButton: Button
        val removeButton: Button

        init {
            procedureName = view.findViewById(R.id.procedure_name)
            procedureTime = view.findViewById(R.id.procedure_time_value)
            procedureInterval = view.findViewById(R.id.procedure_interval_value)
            editButton = view.findViewById(R.id.edit_button)
            removeButton = view.findViewById(R.id.remove_button)
            root = view
        }
    }

    private var proceduresList: List<ProcedureEntity> = emptyList()

    fun setProceduresList(list: List<ProcedureEntity>) {
        proceduresList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProceduresAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.procedure_item, parent, false)
        return ProceduresAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProceduresAdapter.ViewHolder, position: Int) {
        holder.procedureName.text = proceduresList.get(position).name
        holder.procedureTime.text = proceduresList.get(position).procedureTime
        holder.procedureInterval.text = proceduresList.get(position).procedureIntervalDays.toString()

        holder.editButton.setOnClickListener{
            sharedViewModel.setCurrentProcedureId(proceduresList.get(position).id)
            navController.navigate(R.id.action_navigation_care_to_editProcedureFragment)
        }

        holder.removeButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                AppDatabase.getDatabase(holder.root.context).proceduresDao().removeProcedure(proceduresList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return proceduresList.size
    }
}