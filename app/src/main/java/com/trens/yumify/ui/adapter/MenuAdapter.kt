package com.trens.yumify.ui.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.trens.yumify.R
import com.trens.yumify.data.model.db.MenuEntity

class MenuAdapter(
    private val onMenuClick: (MenuEntity) -> Unit,
    private val onEditClick: (MenuEntity) -> Unit,
    private val onDeleteClick: (MenuEntity) -> Unit
) : ListAdapter<MenuEntity, MenuAdapter.MenuViewHolder>(MenuDiffCallback()) {

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val menuImageUri: ImageView = itemView.findViewById(R.id.menuImage)
        private val menuName: TextView = itemView.findViewById(R.id.menuName)
        private val menuDescription: TextView = itemView.findViewById(R.id.menuDescription)
        private val buttonEdit: ImageView = itemView.findViewById(R.id.buttonEdit)
        private val buttonDelete: ImageView = itemView.findViewById(R.id.buttonDelete)

        fun bind(menu: MenuEntity) {
            // Set nama dan deskripsi menu
            menuName.text = menu.name
            menuDescription.text = "Ingredients: ${menu.ingredients}"

            // Debug: log URI gambar
            Log.d("MenuAdapter", "Image URI: ${menu.imageUri}")

            if (!menu.imageUri.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(Uri.parse(menu.imageUri)) // URI gambar dari database
                    .placeholder(R.drawable.baseline_image_search_24) // Placeholder jika gambar belum tersedia
                    .into(menuImageUri)
            } else {
                menuImageUri.setImageResource(R.drawable.baseline_image_search_24) // Placeholder jika URI kosong
            }

            // Tambahkan listener untuk tombol edit
            buttonEdit.setOnClickListener {
                onEditClick(menu)
            }

            // Tambahkan listener untuk tombol delete
            buttonDelete.setOnClickListener {
                onDeleteClick(menu)
            }

            // Klik pada seluruh item untuk melihat detail
            itemView.setOnClickListener {
                onMenuClick(menu)
            }
        }
    }

    // MenuDiffCallback berada di luar ViewHolder
    class MenuDiffCallback : DiffUtil.ItemCallback<MenuEntity>() {
        override fun areItemsTheSame(oldItem: MenuEntity, newItem: MenuEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MenuEntity, newItem: MenuEntity): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val currentMenu = getItem(position)
        holder.bind(currentMenu)
    }
}
