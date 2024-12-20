package com.trens.yumify.ui.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.trens.yumify.R
import com.trens.yumify.data.model.db.MenuEntity
import com.trens.yumify.viewmodel.MenuViewModel

class EditMenuActivity : AppCompatActivity() {

    private lateinit var menuViewModel: MenuViewModel
    private var menuId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_menu)

        menuViewModel = ViewModelProvider(this)[MenuViewModel::class.java]

        val editTextName = findViewById<EditText>(R.id.editTextName)
        val editTextIngredients = findViewById<EditText>(R.id.editTextIngredients)
        val editTextSteps = findViewById<EditText>(R.id.editTextSteps)
        val buttonSave = findViewById<Button>(R.id.buttonSave)

        // Ambil data dari intent
        menuId = intent.getIntExtra("menuId", -1)
        if (menuId != -1) {
            val menuName = intent.getStringExtra("menuName") ?: ""
            val menuIngredients = intent.getStringExtra("menuIngredients") ?: ""
            val menuSteps = intent.getStringExtra("menuSteps") ?: ""

            // Isi field
            editTextName.setText(menuName)
            editTextIngredients.setText(menuIngredients)
            editTextSteps.setText(menuSteps)
        }

        // Tombol simpan
        buttonSave.setOnClickListener {
            val name = editTextName.text.toString()
            val ingredients = editTextIngredients.text.toString()
            val steps = editTextSteps.text.toString()

            if (name.isNotEmpty() && ingredients.isNotEmpty() && steps.isNotEmpty()) {
                val menu = MenuEntity(
                    id = menuId ?: 0,
                    name = name,
                    imageUri = "", // Atur jika ada URI
                    ingredients = ingredients,
                    steps = steps
                )
                menuViewModel.updateMenu(menu)
                Toast.makeText(this, "Menu berhasil diperbarui", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
