package com.example.aurafit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aurafit.R
import com.example.aurafit.ui.bottom_nav_fragments.view.physical_fitness.diet_plan.FoodItem

class FoodAdapter(private val foodList: List<FoodItem>) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    class FoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)
        val caloriesTextView: TextView = view.findViewById(R.id.caloriesTextView)
        val proteinTextView: TextView = view.findViewById(R.id.proteinTextView)
        val carbohydratesTextView: TextView = view.findViewById(R.id.carbohydratesTextView)
        val fatsTextView: TextView = view.findViewById(R.id.fatsTextView)
        val vitaminsTextView: TextView = view.findViewById(R.id.vitaminsTextView)
        val mineralsTextView: TextView = view.findViewById(R.id.mineralsTextView)
        val healthBenefitsTextView: TextView = view.findViewById(R.id.healthBenefitsTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = foodList[position]
        holder.nameTextView.text = food.name
        holder.descriptionTextView.text = food.description
        holder.caloriesTextView.text = food.calories
        holder.proteinTextView.text = food.protein
        holder.carbohydratesTextView.text = food.carbohydrates
        holder.fatsTextView.text = food.fats
        holder.vitaminsTextView.text = food.vitamins
        holder.mineralsTextView.text = food.minerals
        holder.healthBenefitsTextView.text = food.healthBenefits

        // Use an image loading library like Glide or Picasso to load images
        Glide.with(holder.itemView.context)
            .load(food.imageUrl)
            .into(holder.imageView)
    }

    override fun getItemCount() = foodList.size
}
