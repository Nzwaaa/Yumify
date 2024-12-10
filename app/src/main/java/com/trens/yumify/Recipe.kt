package com.trens.yumify
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(
    val id: String? = null,
    val image: String? = null,
    val name: String? = null,
    val cookingtime: String? = null,
    val ingredients: String? = null,
    val preparations: String? = null,
) : Parcelable
