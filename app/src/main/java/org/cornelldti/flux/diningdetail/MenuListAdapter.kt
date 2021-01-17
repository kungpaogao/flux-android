package org.cornelldti.flux.diningdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.cornelldti.flux.data.MenuCategory
import org.cornelldti.flux.databinding.MenuListItemBinding

class MenuListAdapter :
    RecyclerView.Adapter<MenuListAdapter.ViewHolder>() {

    var data = listOf<MenuCategory>()
        set(value) {
            // TODO: add DiffUtil to avoid refreshing when not necessary
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount() = data.size

    class ViewHolder private constructor(private val binding: MenuListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: MenuCategory,
        ) {
            binding.menuCategory.text = item.category
            binding.menuItemList.text = item.items.joinToString("\n")
        }

        companion object {
            const val TAG = "DiningDetailMenuAdapter.ViewHolder"

            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MenuListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

}
