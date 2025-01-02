package com.trens.yumify.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.trens.yumify.data.model.db.MenuDao
import com.trens.yumify.data.model.db.MenuDatabase
import com.trens.yumify.data.model.db.MenuEntity

class MenuRepository(application: Application) {

    private val menuDao: MenuDao = MenuDatabase.getDatabase(application).menuDao()

    // Fungsi untuk update menu (tanpa coroutine di sini)
    suspend fun updateMenu(menu: MenuEntity) {
        menuDao.updateMenu(menu)
        Log.d("MenuRepository", "Menu diperbarui di database dengan URI: ${menu.imageUri}")
    }


    // Mendapatkan semua menu
    fun getAllMenus(): LiveData<List<MenuEntity>> {
        return menuDao.getAllMenus()
    }

    // Menghapus menu
    suspend fun deleteMenu(menu: MenuEntity) {
        menuDao.deleteMenu(menu)
    }
}
