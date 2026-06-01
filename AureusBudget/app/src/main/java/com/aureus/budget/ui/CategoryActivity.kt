package com.aureus.budget.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.aureus.budget.data.AppDatabase
import com.aureus.budget.data.entity.Category
import com.aureus.budget.databinding.ActivityCategoryBinding
import com.aureus.budget.ui.adapter.CategoryAdapter
import com.aureus.budget.utils.ColorUtils
import com.aureus.budget.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var db: AppDatabase
    private lateinit var categoryAdapter: CategoryAdapter

    private var userId: Long = -1
    private var selectedColorHex: String = "#1B3A73"
    private var editingCategory: Category? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        db = AppDatabase.getDatabase(this)
        userId = sessionManager.getUserId()

        setupRecyclerView()
        setupColorPicker()
        setupClickListeners()
        observeCategories()
    }

    private fun setupRecyclerView() {
        categoryAdapter = CategoryAdapter(
            onDelete = { category -> confirmDelete(category) }
        )
        binding.rvCategories.apply {
            layoutManager = LinearLayoutManager(this@CategoryActivity)
            adapter = categoryAdapter
        }
    }

    private fun setupColorPicker() {
        binding.layoutColorPicker.removeAllViews()
        ColorUtils.categoryColors.forEach { (hex, _) ->
            val colorView = View(this).apply {
                val size = resources.getDimensionPixelSize(android.R.dimen.app_icon_size) / 2
                layoutParams = android.widget.LinearLayout.LayoutParams(size, size).apply {
                    setMargins(8, 8, 8, 8)
                }
                setBackgroundResource(com.aureus.budget.R.drawable.bg_circle_color)
                background.setTint(ColorUtils.parseColor(hex))
                setOnClickListener {
                    selectedColorHex = hex
                    binding.viewSelectedColor.background.setTint(ColorUtils.parseColor(hex))
                    // Highlight selected
                    binding.layoutColorPicker.findViewWithTag<View>("selected")
                        ?.alpha = 1f
                    tag = "selected"
                    alpha = 0.6f
                }
            }
            binding.layoutColorPicker.addView(colorView)
        }
        // Set initial preview
        binding.viewSelectedColor.background.setTint(ColorUtils.parseColor(selectedColorHex))
    }

    private fun setupClickListeners() {
        binding.ivBack.setOnClickListener { finish() }

        binding.btnAddCategory.setOnClickListener {
            editingCategory = null
            binding.etCategoryName.setText("")
            selectedColorHex = "#1B3A73"
            binding.viewSelectedColor.background.setTint(ColorUtils.parseColor(selectedColorHex))
            binding.cardAddCategory.visibility = View.VISIBLE
            binding.etCategoryName.requestFocus()
        }

        binding.btnCancelCategory.setOnClickListener {
            binding.cardAddCategory.visibility = View.GONE
            binding.tilCategoryName.error = null
        }

        binding.btnSaveCategory.setOnClickListener { saveCategory() }
    }

    private fun saveCategory() {
        val name = binding.etCategoryName.text.toString().trim()

        if (name.isEmpty()) {
            binding.tilCategoryName.error = "Category name is required"
            return
        }
        if (name.length < 2) {
            binding.tilCategoryName.error = "Name must be at least 2 characters"
            return
        }
        binding.tilCategoryName.error = null

        val category = editingCategory?.copy(
            name = name,
            colorHex = selectedColorHex
        ) ?: Category(
            userId = userId,
            name = name,
            colorHex = selectedColorHex
        )

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                db.categoryDao().insertCategory(category)
            }
            binding.cardAddCategory.visibility = View.GONE
            binding.etCategoryName.setText("")
            Toast.makeText(
                this@CategoryActivity,
                "Category saved!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun confirmDelete(category: Category) {
        AlertDialog.Builder(this)
            .setTitle("Delete Category")
            .setMessage("Delete \"${category.name}\"? Expenses in this category won't be deleted but will be uncategorized.")
            .setPositiveButton("Delete") { _, _ ->
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) { db.categoryDao().deleteCategory(category) }
                    Toast.makeText(this@CategoryActivity, "Category deleted", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun observeCategories() {
        lifecycleScope.launch {
            db.categoryDao().getCategoriesForUser(userId).collectLatest { cats ->
                categoryAdapter.submitList(cats)
                binding.rvCategories.visibility = if (cats.isEmpty()) View.GONE else View.VISIBLE
                binding.tvNoCategories.visibility = if (cats.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }
}
