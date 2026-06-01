package com.aureus.budget.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.aureus.budget.R
import com.aureus.budget.data.AppDatabase
import com.aureus.budget.data.dao.CategorySpending
import com.aureus.budget.data.entity.Expense
import com.aureus.budget.databinding.ActivityMainBinding
import com.aureus.budget.ui.adapter.ExpenseAdapter
import com.aureus.budget.utils.ColorUtils
import com.aureus.budget.utils.CurrencyFormatter
import com.aureus.budget.utils.DateUtils
import com.aureus.budget.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var db: AppDatabase
    private lateinit var expenseAdapter: ExpenseAdapter

    private var userId: Long = -1
    private val currentMonth = DateUtils.getCurrentMonth()
    private val currentYear = DateUtils.getCurrentYear()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        db = AppDatabase.getDatabase(this)
        userId = sessionManager.getUserId()

        if (userId == -1L) {
            goToLogin()
            return
        }

        setupUI()
        setupRecyclerView()
        setupClickListeners()
        observeData()
    }

    private fun setupUI() {
        binding.tvMonthYear.text = DateUtils.getMonthYearLabel(currentMonth, currentYear)
    }

    private fun setupRecyclerView() {
        expenseAdapter = ExpenseAdapter(
            onItemClick = { expense -> openExpenseDetail(expense) },
            getCategoryName = { categoryId -> getCategoryNameSync(categoryId) }
        )
        binding.rvRecentExpenses.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = expenseAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun getCategoryNameSync(categoryId: Long?): String {
        return "Loading..." // Will be resolved via adapter lookup
    }

    private fun setupClickListeners() {
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }

        binding.navBudgets.setOnClickListener {
            startActivity(Intent(this, BudgetGoalsActivity::class.java))
        }

        binding.navReports.setOnClickListener {
            startActivity(Intent(this, CategoryReportActivity::class.java))
        }

        binding.navMore.setOnClickListener {
            showMoreMenu()
        }

        binding.tvViewAll.setOnClickListener {
            startActivity(Intent(this, ExpenseListActivity::class.java))
        }

        binding.tvViewReports.setOnClickListener {
            startActivity(Intent(this, CategoryReportActivity::class.java))
        }

        binding.ivSettings.setOnClickListener {
            showMoreMenu()
        }
    }

    private fun showMoreMenu() {
        val options = arrayOf(
            "📂 Categories",
            "🎯 Budget Goals",
            "📊 Category Report",
            "📋 All Expenses",
            "🚪 Logout"
        )
        AlertDialog.Builder(this)
            .setTitle("More Options")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> startActivity(Intent(this, CategoryActivity::class.java))
                    1 -> startActivity(Intent(this, BudgetGoalsActivity::class.java))
                    2 -> startActivity(Intent(this, CategoryReportActivity::class.java))
                    3 -> startActivity(Intent(this, ExpenseListActivity::class.java))
                    4 -> confirmLogout()
                }
            }
            .show()
    }

    private fun confirmLogout() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { _, _ ->
                sessionManager.clearSession()
                goToLogin()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun observeData() {
        val monthStart = DateUtils.getMonthStartDate(currentMonth, currentYear)
        val monthEnd = DateUtils.getMonthEndDate(currentMonth, currentYear)

        // Observe recent expenses
        lifecycleScope.launch {
            db.expenseDao().getRecentExpenses(userId, 10).collectLatest { expenses ->
                updateRecentExpenses(expenses)
            }
        }

        // Observe monthly total
        val monthPrefix = "%04d-%02d".format(currentYear, currentMonth) + "%"
        lifecycleScope.launch {
            db.expenseDao().getMonthlyTotal(userId, monthPrefix).collectLatest { total ->
                updateBudgetStatus(total)
            }
        }

        // Load category spending
        loadCategorySpending(monthStart, monthEnd)
    }

    private fun updateRecentExpenses(expenses: List<Expense>) {
        if (expenses.isEmpty()) {
            binding.rvRecentExpenses.visibility = View.GONE
            binding.tvNoRecentExpenses.visibility = View.VISIBLE
        } else {
            binding.rvRecentExpenses.visibility = View.VISIBLE
            binding.tvNoRecentExpenses.visibility = View.GONE

            // Load category names then submit
            lifecycleScope.launch {
                val categories = withContext(Dispatchers.IO) {
                    db.categoryDao().getCategoriesForUserSync(userId)
                }
                val categoryMap = categories.associateBy { it.id }
                expenseAdapter.submitListWithCategories(expenses, categoryMap)
            }
        }
    }

    private fun updateBudgetStatus(totalSpent: Double) {
        binding.tvTotalSpent.text = CurrencyFormatter.format(totalSpent)

        lifecycleScope.launch {
            val goal = withContext(Dispatchers.IO) {
                db.budgetGoalDao().getGoalForMonth(userId, currentMonth, currentYear)
            }

            val maxGoal = goal?.maxGoal ?: 0.0
            binding.tvBudgetMax.text = if (maxGoal > 0) "/ ${CurrencyFormatter.format(maxGoal)}" else "/ No limit set"

            if (maxGoal > 0) {
                val percent = ((totalSpent / maxGoal) * 100).toInt().coerceIn(0, 100)
                binding.progressBudget.progress = percent
                binding.tvPercentSpent.text = "$percent% spent"

                val remaining = maxGoal - totalSpent
                binding.tvRemainingLabel.text = "${CurrencyFormatter.format(remaining.coerceAtLeast(0.0))} remaining"
            } else {
                binding.progressBudget.progress = 0
                binding.tvPercentSpent.text = ""
                binding.tvRemainingLabel.text = "Set a budget goal"
            }

            // Today's spending
            val todaySpent = withContext(Dispatchers.IO) {
                db.expenseDao().getTodaySpending(userId, DateUtils.getCurrentDate())
            }
            binding.tvSpentToday.text = CurrencyFormatter.format(todaySpent)

            // Avg daily (days elapsed this month)
            val daysElapsed = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            val avgDaily = if (daysElapsed > 0) totalSpent / daysElapsed else 0.0
            binding.tvAvgDaily.text = CurrencyFormatter.format(avgDaily)
        }
    }

    private fun loadCategorySpending(startDate: String, endDate: String) {
        lifecycleScope.launch {
            val spending = withContext(Dispatchers.IO) {
                db.expenseDao().getCategorySpendingInRange(userId, startDate, endDate)
            }
            updateTopCategories(spending)
        }
    }

    private fun updateTopCategories(spending: List<CategorySpending>) {
        binding.layoutTopCategories.removeAllViews()

        val total = spending.sumOf { it.totalAmount }
        val topItems = spending.take(3)

        if (topItems.isEmpty()) {
            binding.tvNoCategoryData.visibility = View.VISIBLE
            return
        }

        binding.tvNoCategoryData.visibility = View.GONE

        lifecycleScope.launch {
            val categories = withContext(Dispatchers.IO) {
                db.categoryDao().getCategoriesForUserSync(userId)
            }
            val categoryMap = categories.associateBy { it.id }

            topItems.forEach { item ->
                val category = item.categoryId?.let { categoryMap[it] }
                val colorHex = category?.colorHex ?: "#1B3A73"

                val itemView = layoutInflater.inflate(R.layout.item_dashboard_category, null)
                val dot = itemView.findViewById<View>(R.id.viewDot)
                val tvName = itemView.findViewById<TextView>(R.id.tvCategoryName)
                val tvAmount = itemView.findViewById<TextView>(R.id.tvAmount)
                val progress = itemView.findViewById<ProgressBar>(R.id.progressBar)

                dot.setBackgroundColor(ColorUtils.parseColor(colorHex))
                tvName.text = item.categoryName ?: "Uncategorized"
                tvAmount.text = CurrencyFormatter.format(item.totalAmount)
                tvAmount.setTextColor(ColorUtils.parseColor(colorHex))

                val pct = if (total > 0) ((item.totalAmount / total) * 100).toInt() else 0
                progress.progress = pct
                progress.progressTintList =
                    android.content.res.ColorStateList.valueOf(ColorUtils.parseColor(colorHex))

                binding.layoutTopCategories.addView(itemView)

                // Add divider except last
                if (item != topItems.last()) {
                    val divider = View(this@MainActivity)
                    divider.layoutParams =
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1)
                    divider.setBackgroundColor(Color.parseColor("#E0E6F0"))
                    binding.layoutTopCategories.addView(divider)
                }
            }
        }
    }

    private fun openExpenseDetail(expense: Expense) {
        val intent = Intent(this, ExpenseDetailActivity::class.java)
        intent.putExtra("expense_id", expense.id)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        // Refresh data when returning to dashboard
        observeData()
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
