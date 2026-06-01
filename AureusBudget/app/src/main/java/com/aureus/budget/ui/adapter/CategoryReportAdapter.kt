package com.aureus.budget.ui.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aureus.budget.data.dao.CategorySpending
import com.aureus.budget.data.entity.Category
import com.aureus.budget.databinding.ItemCategoryReportBinding
import com.aureus.budget.utils.ColorUtils
import com.aureus.budget.utils.CurrencyFormatter

class CategoryReportAdapter :
    RecyclerView.Adapter<CategoryReportAdapter.ReportViewHolder>() {

    private var items: List<CategorySpendingWithColor> = emptyList()
    private var grandTotal: Double = 0.0

    fun submitData(
        spending: List<CategorySpending>,
        categoryMap: Map<Long, Category>,
        total: Double
    ) {
        grandTotal = total
        items = spending.map { s ->
            val cat = s.categoryId?.let { categoryMap[it] }
            CategorySpendingWithColor(s, cat?.colorHex ?: "#1B3A73")
        }
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val binding = ItemCategoryReportBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ReportViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        holder.bind(items[position], grandTotal)
    }

    class ReportViewHolder(private val binding: ItemCategoryReportBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CategorySpendingWithColor, total: Double) {
            val spending = item.spending
            val color = ColorUtils.parseColor(item.colorHex)

            binding.tvCategoryName.text = spending.categoryName ?: "Uncategorized"
            binding.tvAmount.text = CurrencyFormatter.format(spending.totalAmount)
            binding.tvAmount.setTextColor(color)
            binding.viewColor.background.setTint(color)

            val txCount = spending.expenseCount
            binding.tvCount.text = "$txCount ${if (txCount == 1) "expense" else "expenses"}"

            val pct = if (total > 0) ((spending.totalAmount / total) * 100) else 0.0
            binding.progressCategory.progress = pct.toInt()
            binding.progressCategory.progressTintList = ColorStateList.valueOf(color)

            binding.tvPercentage.text = "%.1f%% of total spending".format(pct)
        }
    }
}

data class CategorySpendingWithColor(
    val spending: CategorySpending,
    val colorHex: String
)
