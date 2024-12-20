package com.trens.yumify.ui.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.trens.yumify.R
import com.trens.yumify.data.model.db.MenuEntity
import com.trens.yumify.viewmodel.MenuViewModel

class AddMenuActivity : AppCompatActivity() {
    private lateinit var menuViewModel: MenuViewModel
    private var imageUri: Uri? = null // Menyimpan URI gambar yang diupload

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_menu)

        menuViewModel = ViewModelProvider(this).get(MenuViewModel::class.java)

        val editTextName = findViewById<EditText>(R.id.editTextName)
        val editTextIngredients = findViewById<EditText>(R.id.editTextIngredients)
        val editTextSteps = findViewById<EditText>(R.id.editTextSteps)
        val imageView = findViewById<ImageView>(R.id.imageView)
        val buttonSave = findViewById<Button>(R.id.buttonSave)

        // Intent untuk memilih gambar dari galeri
        val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imageUri = uri
                imageView.setImageURI(uri)
            }
        }

        // Klik pada gambar untuk membuka galeri
        imageView.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        buttonSave.setOnClickListener {
            val name = editTextName.text.toString()
            val ingredients = editTextIngredients.text.toString()
            val steps = editTextSteps.text.toString()

            // Validasi kolom kosong
            when {
                name.isEmpty() -> showToast("Nama menu tidak boleh kosong")
                ingredients.isEmpty() -> showToast("Bahan-bahan tidak boleh kosong")
                steps.isEmpty() -> showToast("Langkah-langkah tidak boleh kosong")
                imageUri == null -> showToast("Silakan pilih gambar untuk menu")
                else -> {
                    val menu = MenuEntity(
                        name = name,
                        imageUri = imageUri.toString(),
                        steps = steps,
                        ingredients = ingredients
                    )
                    menuViewModel.insertMenu(menu)
                    Toast.makeText(this, "Success add menu", Toast.LENGTH_SHORT).show()
                    finish() // Menutup activity
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
