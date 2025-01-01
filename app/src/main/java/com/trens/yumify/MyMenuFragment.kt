package com.trens.yumify

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.trens.yumify.ui.adapter.MenuAdapter
import com.trens.yumify.ui.view.AddMenuActivity
import com.trens.yumify.ui.view.DetailMenuActivity
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
        menuViewModel = ViewModelProvider(this)[MenuViewModel::class.java]

        val recyclerView = view.findViewById<RecyclerView>(R.id.recipeRV)

        adapter = MenuAdapter(
            onMenuClick = { menu ->
                val intent = Intent(requireContext(), DetailMenuActivity::class.java).apply {
                    putExtra(DetailMenuActivity.EXTRA_MENU_NAME, menu.name)
                    putExtra(DetailMenuActivity.EXTRA_MENU_INGREDIENTS, menu.ingredients)
                    putExtra(DetailMenuActivity.EXTRA_MENU_STEPS, menu.steps)
                }
                startActivity(intent)
            },
            onEditClick = { menu ->
                val intent = Intent(requireContext(), EditMenuActivity::class.java).apply {
                    putExtra("menuId", menu.id)
                    putExtra("menuName", menu.name)
                    putExtra("menuIngredients", menu.ingredients)
                    putExtra("menuSteps", menu.steps)
                    putExtra("menuImageUri", menu.imageUri) // Tambahkan imageUri
                }
                startActivity(intent)
            },
            onDeleteClick = { menu ->
                val alertDialog = android.app.AlertDialog.Builder(requireContext())
                alertDialog.setTitle("Delete Menu")
                alertDialog.setMessage("Are you sure you want to delete this menu?")
                alertDialog.setPositiveButton("Yes") { _, _ ->
                    menuViewModel.deleteMenu(menu)
                    Toast.makeText(requireContext(), "${menu.name} has been deleted", Toast.LENGTH_SHORT).show()
                }
                alertDialog.setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                alertDialog.create().show()
            }

        )

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        menuViewModel.allMenus.observe(viewLifecycleOwner) { menus ->
            adapter.submitList(menus)
        }


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
