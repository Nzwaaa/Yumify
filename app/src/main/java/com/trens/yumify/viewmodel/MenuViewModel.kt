package com.trens.yumify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.trens.yumify.data.model.db.MenuDatabase
import com.trens.yumify.data.model.db.MenuEntity
import kotlinx.coroutines.launch

class MenuViewModel(application: Application) : AndroidViewModel(application) {

    // Accessing the MenuDao from MenuDatabase
    private val menuDao = MenuDatabase.getDatabase(application).menuDao()

    // LiveData to observe the list of menus
    val allMenus: LiveData<List<MenuEntity>> = menuDao.getAllMenus()

    /**
     * Inserts a new menu into the database.
     * This function is executed asynchronously using viewModelScope.
     */
    fun insertMenu(menu: MenuEntity) {
        viewModelScope.launch {
            try {
                menuDao.insertMenu(menu)
            } catch (e: Exception) {
                // Handle any error that occurs during the insertion
                e.printStackTrace()
            }
        }
    }

    /**
     * Updates an existing menu in the database.
     * This function is executed asynchronously using viewModelScope.
     */
    fun updateMenu(menu: MenuEntity) {
        viewModelScope.launch {
            try {
                menuDao.updateMenu(menu)
            } catch (e: Exception) {
                // Handle any error that occurs during the update
                e.printStackTrace()
            }
        }
    }

    /**
     * Deletes a menu from the database.
     * This function is executed asynchronously using viewModelScope.
     */
    fun deleteMenu(menu: MenuEntity) {
        viewModelScope.launch {
            try {
                menuDao.deleteMenu(menu)
            } catch (e: Exception) {
                // Handle any error that occurs during the delete
                e.printStackTrace()
            }
        }
    }
}
