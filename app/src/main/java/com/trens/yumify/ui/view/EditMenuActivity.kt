package com.trens.yumify.ui.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.trens.yumify.R
import com.trens.yumify.data.model.db.MenuEntity
import com.trens.yumify.viewmodel.MenuViewModel

class EditMenuActivity : AppCompatActivity() {

    private lateinit var menuViewModel: MenuViewModel
    private var menuId: Int? = null
    private var menuImageUri: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_menu)

        menuViewModel = ViewModelProvider(this)[MenuViewModel::class.java]

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarEditMenu)
        val editTextName = findViewById<EditText>(R.id.editTextName)
        val editTextIngredients = findViewById<EditText>(R.id.editTextIngredients)
        val editTextSteps = findViewById<EditText>(R.id.editTextSteps)
        val imageView = findViewById<ImageView>(R.id.imageView)
        val buttonSave = findViewById<Button>(R.id.buttonSave)

        toolbar.setNavigationOnClickListener {
            finish()
        }


        menuId = intent.getIntExtra("menuId", -1)
        if (menuId != -1) {
            val menuName = intent.getStringExtra("menuName") ?: ""
            val menuIngredients = intent.getStringExtra("menuIngredients") ?: ""
            menuImageUri = intent.getStringExtra("menuImageUri") ?: ""
            val menuSteps = intent.getStringExtra("menuSteps") ?: ""

            editTextName.setText(menuName)
            editTextIngredients.setText(menuIngredients)
            editTextSteps.setText(menuSteps)

            if (menuImageUri.isNotEmpty()) {
                Glide.with(this)
                    .load(Uri.parse(menuImageUri))
                    .into(imageView)
            } else {
                Log.d("EditMenuActivity", "URI gambar kosong.")
            }
        }

        imageView.setOnClickListener {
            selectImage()
        }

        buttonSave.setOnClickListener {
            val name = editTextName.text.toString()
            val ingredients = editTextIngredients.text.toString()
            val steps = editTextSteps.text.toString()

            if (name.isNotEmpty() && ingredients.isNotEmpty() && steps.isNotEmpty()) {
                val menu = MenuEntity(
                    id = menuId ?: 0,
                    name = name,
                    imageUri = menuImageUri,
                    ingredients = ingredients,
                    steps = steps
                )
                menuViewModel.updateMenu(menu)
                Toast.makeText(this, "Menu updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            val selectedImageUri = data?.data
            if (selectedImageUri != null) {
                menuImageUri = selectedImageUri.toString()
                Glide.with(this)
                    .load(selectedImageUri)
                    .into(findViewById(R.id.imageView))
            }
        }
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 100
    }
}
