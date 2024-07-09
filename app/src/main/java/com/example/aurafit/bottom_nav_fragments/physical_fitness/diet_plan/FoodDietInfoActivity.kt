package com.example.aurafit.bottom_nav_fragments.physical_fitness.diet_plan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aurafit.adapters.FoodAdapter
import com.example.aurafit.databinding.ActivityFoodDietInfoBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.io.InputStream

data class FoodItem(
    val imageUrl: String,
    val name: String,
    val description: String,
    val calories: String,
    val protein: String,
    val carbohydrates: String,
    val fats: String,
    val vitamins: String,
    val minerals: String,
    val healthBenefits: String
)

data class FoodItemsWrapper(
    val foodItems: List<FoodItem>
)

class FoodDietInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoodDietInfoBinding
    private lateinit var foodAdapter: FoodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDietInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load JSON data and parse
        val json = loadJSONFromAsset("food_data.json")
        if (json != null) {
            val foodItems = parseFoodJson(json)
            setupRecyclerView(foodItems)
        }
    }

    private fun parseFoodJson(json: String): List<FoodItem> {
        val gson = Gson()
        val type = object : TypeToken<FoodItemsWrapper>() {}.type
        val foodItemsWrapper: FoodItemsWrapper = gson.fromJson(json, type)
        return foodItemsWrapper.foodItems
    }

    private fun setupRecyclerView(foodItems: List<FoodItem>) {
        foodAdapter = FoodAdapter(foodItems)
        binding.recyclerView.adapter = foodAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun loadJSONFromAsset(fileName: String): String? {
        return try {
            val inputStream: InputStream = assets.open(fileName)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
    }
}
