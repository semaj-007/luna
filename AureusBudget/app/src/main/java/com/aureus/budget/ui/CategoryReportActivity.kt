package com.aureus.budget.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.aureus.budget.data.AppDatabase
import com.aureus.budget.data.dao.CategorySpending
import com.aureus.budget.data.entity.Category
import com.aureus.budget.databinding.ActivityCategoryReportBinding
import com.aureus.budget.ui.adapter.CategoryReportAdapter
import com.aureus.budget.utils.CurrencyFormatter
import com.aureus.budget.utils.DateUtils
import com.aureus.budget.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryReportBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var db: AppDatabase
    private lateinit var reportAdapter: CategoryReportAdapter

    private var userId: Long = -1
    private var fromDate: String = DateUtils.getMonthStartDate(
        DateUtils.getCurrentMonth(), DateUtils.getCurrentYear()
    )
    private var toDate: String = DateUtils.getMonthEndDate(
        DateUtils.getCurrentMonth(), DateUtils.getCurrentYear()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        db = AppDatabase.getDatabase(this)
        userId = sessionManager.getUserId()

        binding.etFromDate.setText(fromDate)
        binding.etToDate.setText(toDate)

        setupRecyclerView()
        setupClickListeners()
        loadReport()
    }

    private fun setupRecyclerView() {
        reportAdapter = CategoryReportAdapter()
        binding.rvCategoryReport.apply {
            layoutManager = LinearLayoutManager(this@CategoryReportActivity)
            adapter = reportAdapter
        }
    }

    private fun setupClickListeners() {
        binding.ivBack.setOnClickListener { finish() }

        binding.etFromDate.setOnClickListener { showDatePicker(isFrom = true) }
        binding.tilFromDate.setEndIconOnClickListener { showDatePicker(isFrom = true) }

        binding.etToDate.setOnClickListener { showDatePicker(isFrom = false) }
        binding.tilToDate.setEndIconOnClickListener { showDatePicker(isFrom = false) }

        binding.btnApplyFilter.setOnClickListener { loadReport() }
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

    private fun loadReport() {
        lifecycleScope.launch {
            val spending = withContext(Dispatchers.IO) {
                db.expenseDao().getCategorySpendingInRange(userId, fromDate, toDate)
            }
            val categories = withContext(Dispatchers.IO) {
                db.categoryDao().getCategoriesForUserSync(userId)
            }
            val categoryMap = categories.associateBy { it.id }
            updateUI(spending, categoryMap)
        }
    }

    private fun updateUI(spending: List<CategorySpending>, categoryMap: Map<Long, Category>) {
        if (spending.isEmpty()) {
            binding.rvCategoryReport.visibility = View.GONE
            binding.tvNoData.visibility = View.VISIBLE
            binding.tvGrandTotal.text = ""
        } else {
            binding.rvCategoryReport.visibility = View.VISIBLE
            binding.tvNoData.visibility = View.GONE

            val grandTotal = spending.sumOf { it.totalAmount }
            binding.tvGrandTotal.text = "Total: ${CurrencyFormatter.format(grandTotal)}"

            reportAdapter.submitData(spending, categoryMap, grandTotal)
        }
    }
}
