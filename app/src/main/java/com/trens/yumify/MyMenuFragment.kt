package com.trens.yumify

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.trens.yumify.data.model.db.MenuEntity
import com.trens.yumify.ui.adapter.MenuAdapter
import com.trens.yumify.ui.view.AddMenuActivity
import com.trens.yumify.ui.view.EditMenuActivity
import com.trens.yumify.viewmodel.MenuViewModel

class MyMenuFragment : Fragment() {

    private lateinit var menuViewModel: MenuViewModel
    private lateinit var adapter: MenuAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_menu, container, false)
        menuViewModel = ViewModelProvider(this).get(MenuViewModel::class.java)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recipeRV)

        // Inisialisasi adapter dengan onEditClick
        adapter = MenuAdapter { menu ->
            val intent = Intent(requireContext(), EditMenuActivity::class.java)
            intent.putExtra("menuId", menu.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Mengamati perubahan data di ViewModel dan mengupdate daftar menu di adapter
        menuViewModel.allMenus.observe(viewLifecycleOwner) { menus ->
            adapter.submitList(menus)
        }

        // Tambahkan Intent untuk navigasi ke AddMenuActivity
        view.findViewById<FloatingActionButton>(R.id.fabAddMenu).setOnClickListener {
            try {
                val intent = Intent(requireContext(), AddMenuActivity::class.java)
                startActivity(intent)
                Log.d("Navigation", "Successfully navigated to AddMenuActivity")
            } catch (e: Exception) {
                Log.e("Navigation", "Navigation error: ${e.message}")
            }
        }

        return view
    }
}
