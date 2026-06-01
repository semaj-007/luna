package com.aureus.budget.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aureus.budget.data.AppDatabase
import com.aureus.budget.data.entity.Expense
import com.aureus.budget.databinding.ActivityExpenseDetailBinding
import com.aureus.budget.utils.ColorUtils
import com.aureus.budget.utils.CurrencyFormatter
import com.aureus.budget.utils.DateUtils
import com.aureus.budget.utils.SessionManager
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class ExpenseDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExpenseDetailBinding
    private lateinit var db: AppDatabase
    private var expenseId: Long = -1L
    private var expense: Expense? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpenseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getDatabase(this)
        expenseId = intent.getLongExtra("expense_id", -1L)

        if (expenseId == -1L) { finish(); return }

        binding.ivBack.setOnClickListener { finish() }
        binding.ivEdit.setOnClickListener { openEditActivity() }

        loadExpense()
    }

    private fun loadExpense() {
        lifecycleScope.launch {
            expense = withContext(Dispatchers.IO) { db.expenseDao().getExpenseById(expenseId) }

            expense?.let { e ->
                binding.tvAmount.text = CurrencyFormatter.format(e.amount)
                binding.tvDescription.text = e.description
                binding.tvDate.text = DateUtils.formatDisplayDate(e.date)
                binding.tvTime.text = "${e.startTime} – ${e.endTime}"

                // Load category
                val category = e.categoryId?.let { id ->
                    withContext(Dispatchers.IO) { db.categoryDao().getCategoryById(id) }
                }
                binding.tvCategory.text = category?.name ?: "Uncategorized"
                val colorHex = category?.colorHex ?: "#1B3A73"
                binding.tvCategory.background.setTint(ColorUtils.parseColor(colorHex))

                // Notes
                if (e.notes.isNotEmpty()) {
                    binding.layoutNotes.visibility = View.VISIBLE
                    binding.tvNotes.text = e.notes
                } else {
                    binding.layoutNotes.visibility = View.GONE
                }

                // Photo
                e.photoPath?.let { path ->
                    val file = File(path)
                    if (file.exists()) {
                        binding.cardPhoto.visibility = View.VISIBLE
                        Glide.with(this@ExpenseDetailActivity)
                            .load(Uri.fromFile(file))
                            .centerCrop()
                            .into(binding.ivExpensePhoto)
                    }
                }
            }
        }
    }

    private fun openEditActivity() {
        val intent = Intent(this, AddExpenseActivity::class.java)
        intent.putExtra("expense_id", expenseId)
        startActivityForResult(intent, 1002)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1002) {
            // Reload after edit
            loadExpense()
        }
    }
}
