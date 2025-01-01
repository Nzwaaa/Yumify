package com.trens.yumify.data.model.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MenuDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenu(menu: MenuEntity)

    @Update
    suspend fun updateMenu(menu: MenuEntity)

    @Delete
    suspend fun deleteMenu(menu: MenuEntity)

    @Query("SELECT * FROM menu_table")
    fun getAllMenus(): LiveData<List<MenuEntity>>
}
