package com.example.bottomnavigationexample.adapters

import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomnavigationexample.R
import com.example.bottomnavigationexample.SharedViewModel
import com.example.bottomnavigationexample.models.PetToRenderOnChoosePet

class PetsChoosePetScreenAdapter(
    private val petsRenderItems: List<PetToRenderOnChoosePet>,
    private val navController: NavController,
    private val sharedViewModel: SharedViewModel,
    private val drawablePlaceholder: Drawable
) :
    RecyclerView.Adapter<PetsChoosePetScreenAdapter.ViewHolder>() {

    private var petsList = petsRenderItems

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root: View
        val photoContainer: ImageView
        val nameTv: TextView

        init {
            root = view
            photoContainer = view.findViewById(R.id.pet_photo)
            nameTv = view.findViewById(R.id.pet_name_tv)
        }
    }

    fun setPetsList(_petsList: List<PetToRenderOnChoosePet>) {
        petsList = _petsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.choose_pet_pet_item, parent, false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameTv.text = petsList.get(position).name
        if (petsList.get(position).imageUri != "") {
            holder.photoContainer.setImageURI(Uri.parse(petsList.get(position).imageUri))
        }
        else {
            holder.photoContainer.setImageDrawable(drawablePlaceholder)
        }
        holder.root.setOnClickListener{
            sharedViewModel.setCurrentPetId(petsList.get(position).id)
            navController.navigate(R.id.action_choosePetFragment_to_navigation_home)
        }
    }

    override fun getItemCount(): Int {
        return petsList.size
    }
}