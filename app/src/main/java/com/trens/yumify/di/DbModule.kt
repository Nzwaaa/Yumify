package com.trens.yumify.di

import android.content.Context
import com.trens.yumify.data.model.db.FoodDatabase
import com.trens.yumify.data.model.db.FoodDao

object DbModule {

    fun provideDatabase(context: Context): FoodDatabase {
        return FoodDatabase.getDatabase(context)
    }

    fun provideFoodDao(database: FoodDatabase): FoodDao {
        return database.foodDao()
    }
}