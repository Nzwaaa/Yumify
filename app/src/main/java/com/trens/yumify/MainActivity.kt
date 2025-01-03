package com.trens.yumify

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Setup padding untuk insets sistem
        val mainView = findViewById<View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(
                left = systemBars.left,
                top = systemBars.top,
                right = systemBars.right,
                bottom = systemBars.bottom
            )
            insets
        }

        // Setup BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
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

    /**
     * Fungsi untuk memuat fragment ke dalam container
     */
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_container, fragment)
            .commit()
    }

    /**
     * Mengaktifkan mode Edge-to-Edge
     */
    private fun enableEdgeToEdge() {
        // Aktifkan edge-to-edge untuk aplikasi
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}
