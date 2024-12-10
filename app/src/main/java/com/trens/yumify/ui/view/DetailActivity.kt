package com.trens.yumify.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.trens.yumify.R
import com.trens.yumify.data.model.response.MealsListResponse
import com.trens.yumify.data.repository.MainRepository
import com.trens.yumify.databinding.ActivityDetailBinding
import com.trens.yumify.di.ApiModule
import com.trens.yumify.di.DbModule
import com.trens.yumify.data.model.db.FoodEntity
import com.trens.yumify.ui.adapter.IngredientsAdapter
import com.trens.yumify.utilities.DataStatus
import com.trens.yumify.viewmodel.DetailViewModel
import com.trens.yumify.viewmodel.DetailViewModelFactory

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var ingredientsAdapter: IngredientsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = MainRepository(
            ApiModule.provideApiService(),
            DbModule.provideFoodDao(DbModule.provideDatabase(applicationContext))
        )

        detailViewModel = ViewModelProvider(this, DetailViewModelFactory(repository))[DetailViewModel::class.java]

        val mealId = intent.getStringExtra("MEAL_ID")?.toInt() ?: -1
        if (mealId != -1) {
            detailViewModel.getFoodDetail(mealId)
        }

        setupRecyclerView()
        setupObservers()

        binding.backButtonDetail.setOnClickListener {
            onBackPressed()
        }

        binding.FavoriteButton.setOnClickListener {
            val meal = detailViewModel.foodDetail.value?.data?.meals?.firstOrNull()
            meal?.let {
                toggleFavorite(it)
            }
        }
    }

    private fun setupRecyclerView() {
        ingredientsAdapter = IngredientsAdapter()
        binding.ingredientsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@DetailActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = ingredientsAdapter
        }
    }

    private fun setupObservers() {
        detailViewModel.foodDetail.observe(this) { status ->
            when (status.status) {
                DataStatus.Status.LOADING -> {}
                DataStatus.Status.SUCCESS -> {
                    status.data?.let { response ->
                        val meal = response.meals?.firstOrNull()
                        meal?.let { bindDetails(it) }
                    }
                }
                DataStatus.Status.ERROR -> {}
            }
        }

        detailViewModel.isFavorite.observe(this) { isFavorite ->
            if (isFavorite) {
                binding.FavoriteButton.setImageResource(R.drawable.ic_favorite)
            } else {
                binding.FavoriteButton.setImageResource(R.drawable.ic_favorite)
            }
        }
    }

    private fun bindDetails(meal: MealsListResponse.Meal) {
        binding.apply {
            recipeImage.load(meal.strMealThumb)
            recipeTitle.text = meal.strMeal
            recipeCatgory.text = meal.strCategory
            recipeInstructions.text = meal.strInstructions
            ingredientsAdapter.setData(getIngredientsList(meal))
        }
    }

    private fun getIngredientsList(meal: MealsListResponse.Meal): List<Pair<String, String?>> {
        val ingredients = mutableListOf<Pair<String, String?>>()
        if (!meal.strIngredient1.isNullOrEmpty()) ingredients.add(meal.strIngredient1 to meal.strMeasure1)
        if (!meal.strIngredient2.isNullOrEmpty()) ingredients.add(meal.strIngredient2 to meal.strMeasure2)
        if (!meal.strIngredient3.isNullOrEmpty()) ingredients.add(meal.strIngredient3 to meal.strMeasure3)
        if (!meal.strIngredient4.isNullOrEmpty()) ingredients.add(meal.strIngredient4 to meal.strMeasure4)
        if (!meal.strIngredient5.isNullOrEmpty()) ingredients.add(meal.strIngredient5 to meal.strMeasure5)
        if (!meal.strIngredient6.isNullOrEmpty()) ingredients.add(meal.strIngredient6 to meal.strMeasure6)
        return ingredients
    }

    private fun toggleFavorite(meal: MealsListResponse.Meal) {
        detailViewModel.isFavorite.observe(this) { isFavorite ->
            if (isFavorite) {
                detailViewModel.deleteFavorite(FoodEntity(meal.idMeal.toString(),
                    meal.strMeal.toString(), meal.strMealThumb))
            } else {
                detailViewModel.saveFavorite(FoodEntity(meal.idMeal.toString(),
                    meal.strMeal.toString(), meal.strMealThumb))
            }
        }
        detailViewModel.checkFavoriteStatus(meal.idMeal)
    }
}
