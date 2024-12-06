package com.trens.yumify.viewmodel

import androidx.lifecycle.*
import com.trens.yumify.data.model.db.FoodEntity
import com.trens.yumify.data.repository.MainRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: MainRepository) : ViewModel() {

    val favoriteList: LiveData<List<FoodEntity>> = repository.getDbFoodList().asLiveData()

    fun saveFavorite(foodEntity: FoodEntity) = viewModelScope.launch {
        repository.saveFood(foodEntity)
    }

    fun deleteFavorite(foodEntity: FoodEntity) = viewModelScope.launch {
        repository.deleteFood(foodEntity)
    }

    suspend fun isFavorite(id: Int): LiveData<Boolean> = repository.existsFood(id.toString()).asLiveData()
}
