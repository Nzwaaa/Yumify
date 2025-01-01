package com.trens.yumify.data.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_table")
data class FoodEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String?
)