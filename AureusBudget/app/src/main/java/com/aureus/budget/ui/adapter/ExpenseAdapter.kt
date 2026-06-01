package com.aureus.budget.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aureus.budget.data.entity.Category
import com.aureus.budget.data.entity.Expense
import com.aureus.budget.databinding.ItemExpenseBinding
import com.aureus.budget.utils.ColorUtils
import com.aureus.budget.utils.CurrencyFormatter
import com.aureus.budget.utils.DateUtils

class ExpenseAdapter(
    private val onItemClick: (Expense) -> Unit,
    private val getCategoryName: (Long?) -> String
) : ListAdapter<ExpenseWithCategory, ExpenseAdapter.ExpenseViewHolder>(DIFF_CALLBACK) {

    private var categoryMap: Map<Long, Category> = emptyMap()

    fun submitListWithCategories(expenses: List<Expense>, categories: Map<Long, Category>) {
        categoryMap = categories
        val items = expenses.map { ExpenseWithCategory(it, categories[it.categoryId]) }
        submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ItemExpenseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }

    class ExpenseViewHolder(private val binding: ItemExpenseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ExpenseWithCategory, onItemClick: (Expense) -> Unit) {
            val expense = item.expense
            val category = item.category

            binding.tvDescription.text = expense.description
            binding.tvAmount.text = CurrencyFormatter.format(expense.amount)
            binding.tvDate.text = DateUtils.formatDisplayDate(expense.date)
            binding.tvTime.text = "${expense.startTime}–${expense.endTime}"
            binding.tvCategory.text = category?.name ?: "Uncategorized"

            val colorHex = category?.colorHex ?: "#1B3A73"
            val color = ColorUtils.parseColor(colorHex)
            binding.viewCategoryColor.setBackgroundColor(color)
            binding.tvAmount.setTextColor(color)

            // Photo indicator
            if (!expense.photoPath.isNullOrEmpty()) {
                binding.layoutPhotoIndicator.visibility = View.VISIBLE
            } else {
                binding.layoutPhotoIndicator.visibility = View.GONE
            }

            binding.root.setOnClickListener { onItemClick(expense) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ExpenseWithCategory>() {
            override fun areItemsTheSame(
                oldItem: ExpenseWithCategory,
                newItem: ExpenseWithCategory
            ) = oldItem.expense.id == newItem.expense.id

            override fun areContentsTheSame(
                oldItem: ExpenseWithCategory,
                newItem: ExpenseWithCategory
            ) = oldItem == newItem
        }
    }
}

data class ExpenseWithCategory(
    val expense: Expense,
    val category: Category?
)
