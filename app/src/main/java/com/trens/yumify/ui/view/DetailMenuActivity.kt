package com.trens.yumify.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.trens.yumify.R
import com.trens.yumify.databinding.ActivityDetailMenuBinding

class DetailMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailMenuBinding

    companion object {
        const val EXTRA_MENU_NAME = "menuName"
        const val EXTRA_MENU_INGREDIENTS = "menuIngredients"
        const val EXTRA_MENU_STEPS = "menuSteps"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val menuName = intent?.getStringExtra(EXTRA_MENU_NAME) ?: "Tidak ada nama"
        val menuIngredients = intent?.getStringExtra(EXTRA_MENU_INGREDIENTS) ?: ""
        val menuSteps = intent?.getStringExtra(EXTRA_MENU_STEPS) ?: "Tidak ada langkah"

        val formattedIngredients = menuIngredients.split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .map { "- $it" }
            .joinToString("\n")

        binding.editTextName.text = menuName
        binding.ingredientsText.text = formattedIngredients
        binding.editTextSteps.text = menuSteps

        Glide.with(this)
            .load(R.drawable.cooking)
            .into(binding.imageView)

        binding.backButtonDetail.setOnClickListener {
            finish()
        }
        }
}
