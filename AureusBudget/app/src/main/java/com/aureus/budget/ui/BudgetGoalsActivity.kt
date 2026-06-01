package com.aureus.budget.ui

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aureus.budget.data.AppDatabase
import com.aureus.budget.data.entity.BudgetGoal
import com.aureus.budget.databinding.ActivityBudgetGoalsBinding
import com.aureus.budget.utils.CurrencyFormatter
import com.aureus.budget.utils.DateUtils
import com.aureus.budget.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Locale

class BudgetGoalsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBudgetGoalsBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var db: AppDatabase

    private var userId: Long = -1
    private val currentMonth = DateUtils.getCurrentMonth()
    private val currentYear = DateUtils.getCurrentYear()

    // NumberFormat for display
    private val numFormat = NumberFormat.getNumberInstance(Locale("en", "ZA")).apply {
        minimumFractionDigits = 0
        maximumFractionDigits = 0
    }

    // Track which is being updated to avoid infinite loops
    private var updatingMin = false
    private var updatingMax = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBudgetGoalsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        db = AppDatabase.getDatabase(this)
        userId = sessionManager.getUserId()

        binding.tvCurrentMonth.text = DateUtils.getMonthYearLabel(currentMonth, currentYear)

        setupSeekBars()
        setupClickListeners()
        loadExistingGoals()
    }

    private fun setupSeekBars() {
        // --- Minimum SeekBar ---
        binding.seekBarMin.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser && !updatingMin) {
                    updatingMin = true
                    val value = progress.toDouble()
                    binding.tvMinGoalValue.text = "R ${numFormat.format(value)}"
                    binding.etMinGoal.setText(progress.toString())
                    updatingMin = false
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        // Sync EditText -> SeekBar for min
        binding.etMinGoal.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && !updatingMin) {
                syncMinEditToSeekBar()
            }
        }

        // --- Maximum SeekBar ---
        binding.seekBarMax.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser && !updatingMax) {
                    updatingMax = true
                    val value = progress.toDouble()
                    binding.tvMaxGoalValue.text = "R ${numFormat.format(value)}"
                    binding.etMaxGoal.setText(progress.toString())
                    updatingMax = false
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        // Sync EditText -> SeekBar for max
        binding.etMaxGoal.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && !updatingMax) {
                syncMaxEditToSeekBar()
            }
        }
    }

    private fun syncMinEditToSeekBar() {
        val text = binding.etMinGoal.text.toString().trim()
        val value = text.toDoubleOrNull() ?: 0.0
        val clamped = value.toInt().coerceIn(0, binding.seekBarMin.max)
        updatingMin = true
        binding.seekBarMin.progress = clamped
        binding.tvMinGoalValue.text = "R ${numFormat.format(clamped)}"
        updatingMin = false
    }

    private fun syncMaxEditToSeekBar() {
        val text = binding.etMaxGoal.text.toString().trim()
        val value = text.toDoubleOrNull() ?: 0.0
        val clamped = value.toInt().coerceIn(0, binding.seekBarMax.max)
        updatingMax = true
        binding.seekBarMax.progress = clamped
        binding.tvMaxGoalValue.text = "R ${numFormat.format(clamped)}"
        updatingMax = false
    }

    private fun setupClickListeners() {
        binding.ivBack.setOnClickListener { finish() }

        binding.btnSaveGoals.setOnClickListener { saveGoals() }
    }

    private fun loadExistingGoals() {
        lifecycleScope.launch {
            val goal = withContext(Dispatchers.IO) {
                db.budgetGoalDao().getGoalForMonth(userId, currentMonth, currentYear)
            }

            goal?.let { g ->
                val minVal = g.minGoal.toInt().coerceIn(0, binding.seekBarMin.max)
                val maxVal = g.maxGoal.toInt().coerceIn(0, binding.seekBarMax.max)

                updatingMin = true
                binding.seekBarMin.progress = minVal
                binding.tvMinGoalValue.text = "R ${numFormat.format(minVal)}"
                binding.etMinGoal.setText(g.minGoal.toInt().toString())
                updatingMin = false

                updatingMax = true
                binding.seekBarMax.progress = maxVal
                binding.tvMaxGoalValue.text = "R ${numFormat.format(maxVal)}"
                binding.etMaxGoal.setText(g.maxGoal.toInt().toString())
                updatingMax = false
            }
        }
    }

    private fun saveGoals() {
        // Sync text fields before reading
        syncMinEditToSeekBar()
        syncMaxEditToSeekBar()

        val minText = binding.etMinGoal.text.toString().trim()
        val maxText = binding.etMaxGoal.text.toString().trim()

        val minGoal = minText.toDoubleOrNull() ?: binding.seekBarMin.progress.toDouble()
        val maxGoal = maxText.toDoubleOrNull() ?: binding.seekBarMax.progress.toDouble()

        // Validate
        binding.tvGoalError.visibility = View.GONE

        if (minGoal < 0) {
            showError("Minimum goal cannot be negative")
            return
        }

        if (maxGoal <= 0) {
            showError("Please set a maximum spending goal greater than 0")
            return
        }

        if (minGoal >= maxGoal) {
            showError("Minimum goal must be less than the maximum goal")
            return
        }

        binding.btnSaveGoals.isEnabled = false

        val budgetGoal = BudgetGoal(
            userId = userId,
            minGoal = minGoal,
            maxGoal = maxGoal,
            month = currentMonth,
            year = currentYear
        )

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                db.budgetGoalDao().upsertGoal(budgetGoal)
            }
            Toast.makeText(
                this@BudgetGoalsActivity,
                "Goals saved for ${DateUtils.getMonthYearLabel(currentMonth, currentYear)}!",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }

    private fun showError(message: String) {
        binding.tvGoalError.text = message
        binding.tvGoalError.visibility = View.VISIBLE
        binding.btnSaveGoals.isEnabled = true
    }
}
