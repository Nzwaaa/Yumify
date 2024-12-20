package com.trens.yumify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.trens.yumify.data.model.db.MenuDatabase
import com.trens.yumify.data.model.db.MenuEntity
import kotlinx.coroutines.launch

class MenuViewModel(application: Application) : AndroidViewModel(application) {
    private val menuDao = MenuDatabase.getDatabase(application).menuDao()
    val allMenus: LiveData<List<MenuEntity>> = menuDao.getAllMenus()

    fun insertMenu(menu: MenuEntity) = viewModelScope.launch {
        menuDao.insertMenu(menu)
    }

    fun updateMenu(menu: MenuEntity) = viewModelScope.launch {
        menuDao.updateMenu(menu)
    }

    fun deleteMenu(menu: MenuEntity) = viewModelScope.launch {
        menuDao.deleteMenu(menu)
    }
}
