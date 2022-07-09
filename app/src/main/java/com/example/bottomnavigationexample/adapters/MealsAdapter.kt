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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MealsAdapter(
    private val navController: NavController,
    private val sharedViewModel: SharedViewModel
) :
    RecyclerView.Adapter<MealsAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root: View
        val mealName: TextView
        val mealTime: TextView
        val product: TextView
        val productWeight: TextView
        val editButton: Button
        val removeButton: Button

        init {
            mealName = view.findViewById(R.id.meal_name)
            mealTime = view.findViewById(R.id.meal_time)
            product = view.findViewById(R.id.product)
            productWeight = view.findViewById(R.id.product_weight)
            editButton = view.findViewById(R.id.edit_button)
            removeButton = view.findViewById(R.id.remove_button)
            root = view
        }
    }

    private var mealsList: List<MealEntity> = emptyList()

    fun setMealsList(list: List<MealEntity>) {
        mealsList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.meal_item, parent, false)
        return MealsAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mealName.text = mealsList.get(position).name
        holder.mealTime.text = mealsList.get(position).mealTime
        holder.product.text = mealsList.get(position).product
        holder.productWeight.text = mealsList.get(position).productWeight.toString()

        holder.editButton.setOnClickListener{
            sharedViewModel.setCurrentMealId(mealsList.get(position).id)
            navController.navigate(R.id.action_navigation_food_to_addMealFragment)
        }

        holder.removeButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                AppDatabase.getDatabase(holder.root.context).mealsDao().removeMeal(mealsList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }
}