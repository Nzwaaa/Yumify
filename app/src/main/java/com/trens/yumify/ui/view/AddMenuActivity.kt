package com.trens.yumify.ui.view

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
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_menu)

        menuViewModel = ViewModelProvider(this).get(MenuViewModel::class.java)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.backButtonAddMenu)
        val editTextName = findViewById<EditText>(R.id.editTextName)
        val editTextIngredients = findViewById<EditText>(R.id.editTextIngredients)
        val editTextSteps = findViewById<EditText>(R.id.editTextSteps)
        val imageView = findViewById<ImageView>(R.id.imageView)
        val buttonSave = findViewById<Button>(R.id.buttonSave)

        // Atur toolbar sebagai action bar
        setSupportActionBar(toolbar)

        // Tambahkan fungsi back pada tombol navigasi
        toolbar.setNavigationOnClickListener {
            finish() // Menutup aktivitas dan kembali ke layar sebelumnya
        }

        // Set up image picker
        val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imageUri = uri
                imageView.setImageURI(uri)
            }
        }

        imageView.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        buttonSave.setOnClickListener {
            val name = editTextName.text.toString()
            val ingredients = editTextIngredients.text.toString()
            val steps = editTextSteps.text.toString()

            // Validate empty fields
            when {
                name.isEmpty() -> showToast("Menu name cannot be empty")
                ingredients.isEmpty() -> showToast("Ingredients cannot be empty")
                steps.isEmpty() -> showToast("Steps cannot be empty")
                imageUri == null -> showToast("Please select an image for the menu")

                else -> {
                    val menu = MenuEntity(
                        name = name,
                        imageUri = imageUri.toString(), // Save the image URI as a string
                        steps = steps,
                        ingredients = ingredients
                    )
                    menuViewModel.insertMenu(menu)

                    // Show success toast
                    Toast.makeText(this, "Menu added successfully", Toast.LENGTH_SHORT).show()

                    // Finish AddMenuActivity and return to previous screen
                    finish()
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
