package com.trens.yumify.viewmodel

import androidx.lifecycle.*
import com.trens.yumify.data.model.db.FoodEntity
import com.trens.yumify.data.model.response.MealsListResponse
import com.trens.yumify.data.repository.MainRepository
import com.trens.yumify.utilities.DataStatus
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: MainRepository) : ViewModel() {

    private val _foodDetail = MutableLiveData<DataStatus<MealsListResponse>>()
    val foodDetail: LiveData<DataStatus<MealsListResponse>> get() = _foodDetail

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    fun getFoodDetail(id: Int) = viewModelScope.launch {
        repository.getFoodDetail(id).collect {
            _foodDetail.value = it
            checkFavoriteStatus(id.toString())
        }
    }

    fun saveFavorite(foodEntity: FoodEntity) = viewModelScope.launch {
        repository.saveFood(foodEntity)
        checkFavoriteStatus(foodEntity.id)
    }

    fun deleteFavorite(foodEntity: FoodEntity) = viewModelScope.launch {
        repository.deleteFood(foodEntity)
        checkFavoriteStatus(foodEntity.id)
    }

    fun checkFavoriteStatus(id: String?) = viewModelScope.launch {
        if (id != null) {
            repository.existsFood(id).collect {
                _isFavorite.value = it
            }
        }
    }

//    suspend fun isFavorite(id: Int): LiveData<Boolean> = repository.existsFood(id.toString()).asLiveData()
}
