package com.example.mypets.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mypets.R
import com.example.mypets.SharedViewModel
import com.example.mypets.data.layer.database.AppDatabase
import com.example.mypets.data.layer.database.MedicineEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MedicineAdapter(
    private val navController: NavController,
    private val sharedViewModel: SharedViewModel
) :
    RecyclerView.Adapter<MedicineAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root: View
        val medicineName: TextView
        val medicineTime: TextView
        val medicineDate: TextView
        val editButton: Button
        val removeButton: Button

        init {
            medicineName = view.findViewById(R.id.medicine_name)
            medicineTime = view.findViewById(R.id.medicine_time_value)
            medicineDate = view.findViewById(R.id.medicine_date_value)
            editButton = view.findViewById(R.id.edit_button)
            removeButton = view.findViewById(R.id.remove_button)
            root = view
        }
    }

    private var medicineList: List<MedicineEntity> = emptyList()

    fun setMedicineList(list: List<MedicineEntity>) {
        medicineList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.medicine_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.medicineName.text = medicineList.get(position).name
        holder.medicineTime.text = medicineList.get(position).time
        holder.medicineDate.text = medicineList.get(position).date

        holder.editButton.setOnClickListener{
            sharedViewModel.setCurrentMedicineId(medicineList.get(position).id)
            navController.navigate(R.id.action_navigation_health_to_medicineAddFragment)
        }

        holder.removeButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                AppDatabase.getDatabase(holder.root.context).medicineDao().removeMedicine(medicineList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return medicineList.size
    }
}