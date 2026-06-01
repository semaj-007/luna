package com.aureus.budget.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.aureus.budget.data.AppDatabase
import com.aureus.budget.data.entity.Category
import com.aureus.budget.data.entity.Expense
import com.aureus.budget.databinding.ActivityExpenseListBinding
import com.aureus.budget.ui.adapter.ExpenseAdapter
import com.aureus.budget.utils.CurrencyFormatter
import com.aureus.budget.utils.DateUtils
import com.aureus.budget.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExpenseListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExpenseListBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var db: AppDatabase
    private lateinit var expenseAdapter: ExpenseAdapter

    private var userId: Long = -1
    private var fromDate: String = DateUtils.getMonthStartDate(
        DateUtils.getCurrentMonth(), DateUtils.getCurrentYear()
    )
    private var toDate: String = DateUtils.getMonthEndDate(
        DateUtils.getCurrentMonth(), DateUtils.getCurrentYear()
    )
    private var categoryMap: Map<Long, Category> = emptyMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpenseListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        db = AppDatabase.getDatabase(this)
        userId = sessionManager.getUserId()

        // Set initial date range display
        binding.etFromDate.setText(fromDate)
        binding.etToDate.setText(toDate)

        setupRecyclerView()
        setupClickListeners()
        loadCategories()
        loadExpenses()
    }

    private fun setupRecyclerView() {
        expenseAdapter = ExpenseAdapter(
            onItemClick = { expense -> openDetail(expense) },
            getCategoryName = { id -> categoryMap[id]?.name ?: "Uncategorized" }
        )
        binding.rvExpenses.apply {
            layoutManager = LinearLayoutManager(this@ExpenseListActivity)
            adapter = expenseAdapter
        }
    }

    private fun setupClickListeners() {
        binding.ivBack.setOnClickListener { finish() }

        binding.etFromDate.setOnClickListener { showDatePicker(isFrom = true) }
        binding.tilFromDate.setEndIconOnClickListener { showDatePicker(isFrom = true) }

        binding.etToDate.setOnClickListener { showDatePicker(isFrom = false) }
        binding.tilToDate.setEndIconOnClickListener { showDatePicker(isFrom = false) }

        binding.btnApplyFilter.setOnClickListener {
            if (!DateUtils.isValidDateRange(fromDate, toDate)) {
                binding.tvDateRangeError.text = "End date must be after or equal to start date"
                binding.tvDateRangeError.visibility = View.VISIBLE
            } else {
                binding.tvDateRangeError.visibility = View.GONE
                loadExpenses()
            }
        }
    }

    private fun showDatePicker(isFrom: Boolean) {
        val dateStr = if (isFrom) fromDate else toDate
        val parts = dateStr.split("-")
        val year = parts[0].toInt()
        val month = parts[1].toInt() - 1
        val day = parts[2].toInt()

        DatePickerDialog(this, { _, y, m, d ->
            val formatted = "%04d-%02d-%02d".format(y, m + 1, d)
            if (isFrom) {
                fromDate = formatted
                binding.etFromDate.setText(formatted)
            } else {
                toDate = formatted
                binding.etToDate.setText(formatted)
            }
        }, year, month, day).show()
    }

    private fun loadCategories() {
        lifecycleScope.launch {
            val cats = withContext(Dispatchers.IO) {
                db.categoryDao().getCategoriesForUserSync(userId)
            }
            categoryMap = cats.associateBy { it.id }
        }
    }

    private fun loadExpenses() {
        lifecycleScope.launch {
            db.expenseDao().getExpensesInRange(userId, fromDate, toDate)
                .collectLatest { expenses ->
                    updateUI(expenses)
                }
        }
    }

    private fun updateUI(expenses: List<Expense>) {
        if (expenses.isEmpty()) {
            binding.rvExpenses.visibility = View.GONE
            binding.tvNoExpenses.visibility = View.VISIBLE
            binding.tvTotalAmount.text = "R 0.00"
        } else {
            binding.rvExpenses.visibility = View.VISIBLE
            binding.tvNoExpenses.visibility = View.GONE

            expenseAdapter.submitListWithCategories(expenses, categoryMap)

            val total = expenses.sumOf { it.amount }
            binding.tvTotalAmount.text = CurrencyFormatter.format(total)
        }
    }

    private fun openDetail(expense: Expense) {
        val intent = Intent(this, ExpenseDetailActivity::class.java)
        intent.putExtra("expense_id", expense.id)
        startActivityForResult(intent, REQUEST_EDIT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_EDIT) loadExpenses()
    }

    companion object {
        private const val REQUEST_EDIT = 1001
    }
}
