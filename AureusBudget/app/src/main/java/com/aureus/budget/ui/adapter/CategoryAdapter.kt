package com.aureus.budget.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aureus.budget.data.entity.Category
import com.aureus.budget.databinding.ItemCategoryBinding
import com.aureus.budget.utils.ColorUtils

class CategoryAdapter(
    private val onDelete: (Category) -> Unit
) : ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.tvCategoryName.text = category.name
            binding.viewColor.background.setTint(ColorUtils.parseColor(category.colorHex))
            binding.ivDelete.setOnClickListener { onDelete(category) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(old: Category, new: Category) = old.id == new.id
            override fun areContentsTheSame(old: Category, new: Category) = old == new
        }
    }
}
