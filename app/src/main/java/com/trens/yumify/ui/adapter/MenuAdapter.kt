package com.trens.yumify.ui.adapter

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

class MenuAdapter(private val onEditClick: (MenuEntity) -> Unit) :
    ListAdapter<MenuEntity, MenuAdapter.MenuViewHolder>(MenuDiffCallback()) {

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val menuImage: ImageView = itemView.findViewById(R.id.menuImage)
        private val menuName: TextView = itemView.findViewById(R.id.menuName)
        private val menuDescription: TextView = itemView.findViewById(R.id.menuDescription)
        private val buttonEdit: ImageView = itemView.findViewById(R.id.buttonEdit)

        fun bind(menu: MenuEntity) {
            menuName.text = menu.name
            menuDescription.text = "Ingredients: ${menu.ingredients}"

            // Menggunakan Glide untuk memuat gambar dari URI
            Glide.with(itemView.context)
                .load(menu.imageUri)
                .placeholder(R.drawable.baseline_image_search_24) // Gambar placeholder
                .into(menuImage)

            // Tambahkan listener untuk tombol edit
            buttonEdit.setOnClickListener {
                onEditClick(menu)
            }
        }
    }

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
