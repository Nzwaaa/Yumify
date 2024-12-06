package com.trens.yumify.ui.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.trens.yumify.FavoriteFragment
import com.trens.yumify.HomeFragment
import com.trens.yumify.MyMenuFragment
import com.trens.yumify.ProfileFragment
import com.trens.yumify.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        @Suppress("DEPRECATION")
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homepage -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.activity2 -> {
                    loadFragment(MyMenuFragment())
                    true
                }
                R.id.activity3 -> {
                    loadFragment(FavoriteFragment())
                    true
                }
                R.id.profilpage -> {
                    loadFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }

        // Membuka fragment default saat pertama kali
        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.homepage
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_container, fragment)
            .commit()
    }


}