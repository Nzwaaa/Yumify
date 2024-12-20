package com.trens.yumify.data.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu_table")
data class MenuEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val imageUri: String, // Untuk menyimpan URI gambar
    val steps: String, // Langkah-langkah memasak
    val ingredients: String // Daftar bahan
)
