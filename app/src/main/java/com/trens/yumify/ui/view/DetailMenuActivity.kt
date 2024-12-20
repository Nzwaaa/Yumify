package com.trens.yumify.ui.view

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.trens.yumify.R

class DetailMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_menu)

        // Ambil referensi elemen dari layout
        val backButton = findViewById<ImageView>(R.id.backButtonDetail)
        val imageView = findViewById<ImageView>(R.id.imageView)
        val recipeName = findViewById<TextView>(R.id.editTextName)
        val recipeInstructions = findViewById<TextView>(R.id.recipeInstructions)
        val ingredientsList = findViewById<RecyclerView>(R.id.editTextIngredients)

        // Ambil data dari Intent
        val menuName = intent.getStringExtra("menuName") ?: "Tidak ada nama"
        val menuIngredients = intent.getStringExtra("menuIngredients") ?: "Tidak ada bahan"
        val menuSteps = intent.getStringExtra("menuSteps") ?: "Tidak ada langkah"
        val menuImageResId = intent.getIntExtra("menuImage", R.drawable.potato) // default placeholder

        // Tampilkan data pada elemen layout
        recipeName.text = menuName
        recipeInstructions.text = menuSteps
        imageView.setImageResource(menuImageResId)

        // TODO: Implementasi untuk ingredients list
        // Anda bisa gunakan Adapter RecyclerView untuk menampilkan daftar bahan-bahan di RecyclerView `editTextIngredients`.

        // Tombol kembali
        backButton.setOnClickListener {
            finish() // Menutup activity ini dan kembali ke yang sebelumnya
        }
    }
}
