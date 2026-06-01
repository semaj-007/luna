package com.aureus.budget.ui

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.aureus.budget.data.AppDatabase
import com.aureus.budget.data.entity.Category
import com.aureus.budget.data.entity.Expense
import com.aureus.budget.databinding.ActivityAddExpenseBinding
import com.aureus.budget.utils.CurrencyFormatter
import com.aureus.budget.utils.DateUtils
import com.aureus.budget.utils.SessionManager
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddExpenseBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var db: AppDatabase

    private var userId: Long = -1
    private var expenseId: Long = -1L   // -1 = new, else editing
    private var categories: List<Category> = emptyList()
    private var selectedCategoryId: Long? = null
    private var selectedDate: String = DateUtils.getCurrentDate()
    private var selectedStartTime: String = DateUtils.getCurrentTime()
    private var selectedEndTime: String = DateUtils.getCurrentTime()
    private var photoPath: String? = null
    private var cameraPhotoUri: Uri? = null

    // Camera launcher
    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && cameraPhotoUri != null) {
            photoPath = cameraPhotoUri!!.path
            showPhotoPreview(cameraPhotoUri!!)
        }
    }

    // Gallery launcher
    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            // Copy to app private storage
            lifecycleScope.launch {
                val copiedPath = withContext(Dispatchers.IO) { copyUriToFile(it) }
                copiedPath?.let { path ->
                    photoPath = path
                    showPhotoPreview(Uri.fromFile(File(path)))
                }
            }
        }
    }

    // Permission launchers
    private val cameraPermLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> if (granted) launchCamera() }

    private val storagePermLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> if (granted) galleryLauncher.launch("image/*") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        db = AppDatabase.getDatabase(this)
        userId = sessionManager.getUserId()

        expenseId = intent.getLongExtra("expense_id", -1L)

        // Set initial date/time display
        binding.etDate.setText(selectedDate)
        binding.etStartTime.setText(selectedStartTime)
        binding.etEndTime.setText(selectedEndTime)

        loadCategories()
        setupClickListeners()

        if (expenseId != -1L) {
            binding.tvToolbarTitle.text = "Edit Expense"
            binding.ivDelete.visibility = View.VISIBLE
            loadExpense(expenseId)
        }
    }

    private fun loadCategories() {
        lifecycleScope.launch {
            categories = withContext(Dispatchers.IO) {
                db.categoryDao().getCategoriesForUserSync(userId)
            }
            setupCategorySpinner()
        }
    }

    private fun setupCategorySpinner() {
        val categoryNames = categories.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categoryNames)
        (binding.spinnerCategory as AutoCompleteTextView).setAdapter(adapter)

        binding.spinnerCategory.setOnItemClickListener { _, _, position, _ ->
            selectedCategoryId = categories[position].id
            binding.tvCategoryError.visibility = View.GONE
        }

        // If editing, set selected category
        selectedCategoryId?.let { id ->
            val idx = categories.indexOfFirst { it.id == id }
            if (idx >= 0) binding.spinnerCategory.setText(categories[idx].name, false)
        }
    }

    private fun setupClickListeners() {
        binding.ivClose.setOnClickListener { finish() }
        binding.btnCancel.setOnClickListener { finish() }

        // Date picker
        binding.etDate.setOnClickListener { showDatePicker() }
        binding.tilDate.setEndIconOnClickListener { showDatePicker() }

        // Time pickers
        binding.etStartTime.setOnClickListener { showTimePicker(isStart = true) }
        binding.tilStartTime.setEndIconOnClickListener { showTimePicker(isStart = true) }
        binding.etEndTime.setOnClickListener { showTimePicker(isStart = false) }
        binding.tilEndTime.setEndIconOnClickListener { showTimePicker(isStart = false) }

        // Photo
        binding.btnTakePhoto.setOnClickListener { requestCameraPermission() }
        binding.btnChoosePhoto.setOnClickListener { requestStoragePermission() }
        binding.ivRemovePhoto.setOnClickListener { removePhoto() }

        // Save
        binding.btnSave.setOnClickListener { saveExpense() }

        // Delete (edit mode)
        binding.ivDelete.setOnClickListener { confirmDelete() }
    }

    private fun showDatePicker() {
        val parts = selectedDate.split("-")
        val year = parts[0].toInt()
        val month = parts[1].toInt() - 1
        val day = parts[2].toInt()

        DatePickerDialog(this, { _, y, m, d ->
            selectedDate = "%04d-%02d-%02d".format(y, m + 1, d)
            binding.etDate.setText(selectedDate)
        }, year, month, day).show()
    }

    private fun showTimePicker(isStart: Boolean) {
        val timeStr = if (isStart) selectedStartTime else selectedEndTime
        val parts = timeStr.split(":")
        val hour = parts[0].toIntOrNull() ?: 0
        val minute = parts[1].toIntOrNull() ?: 0

        TimePickerDialog(this, { _, h, m ->
            val formatted = "%02d:%02d".format(h, m)
            if (isStart) {
                selectedStartTime = formatted
                binding.etStartTime.setText(formatted)
            } else {
                selectedEndTime = formatted
                binding.etEndTime.setText(formatted)
            }
            binding.tvTimeError.visibility = View.GONE
        }, hour, minute, true).show()
    }

    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED -> launchCamera()
            else -> cameraPermLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun requestStoragePermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES
        else
            Manifest.permission.READ_EXTERNAL_STORAGE

        when {
            ContextCompat.checkSelfPermission(this, permission) ==
                    PackageManager.PERMISSION_GRANTED -> galleryLauncher.launch("image/*")
            else -> storagePermLauncher.launch(permission)
        }
    }

    private fun launchCamera() {
        val photoFile = createImageFile()
        cameraPhotoUri = FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider",
            photoFile
        )
        photoPath = photoFile.absolutePath
        cameraLauncher.launch(cameraPhotoUri)
    }

    private fun createImageFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("EXPENSE_${timestamp}_", ".jpg", storageDir)
    }

    private fun copyUriToFile(uri: Uri): String? {
        return try {
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val file = File(cacheDir, "expense_photo_$timestamp.jpg")
            contentResolver.openInputStream(uri)?.use { input ->
                file.outputStream().use { output -> input.copyTo(output) }
            }
            file.absolutePath
        } catch (e: Exception) {
            null
        }
    }

    private fun showPhotoPreview(uri: Uri) {
        binding.cardPhotoPreview.visibility = View.VISIBLE
        Glide.with(this).load(uri).centerCrop().into(binding.ivPhotoPreview)
    }

    private fun removePhoto() {
        photoPath = null
        cameraPhotoUri = null
        binding.cardPhotoPreview.visibility = View.GONE
    }

    private fun saveExpense() {
        val amountStr = binding.etAmount.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val notes = binding.etNotes.text.toString().trim()

        var isValid = true

        // Validate amount
        val amount = CurrencyFormatter.parse(amountStr)
        if (amountStr.isEmpty()) {
            binding.tvAmountError.text = "Amount is required"
            binding.tvAmountError.visibility = View.VISIBLE
            isValid = false
        } else if (amount == null || amount <= 0) {
            binding.tvAmountError.text = "Enter a valid amount greater than 0"
            binding.tvAmountError.visibility = View.VISIBLE
            isValid = false
        } else {
            binding.tvAmountError.visibility = View.GONE
        }

        // Validate description
        if (description.isEmpty()) {
            binding.tilDescription.error = "Description is required"
            isValid = false
        } else {
            binding.tilDescription.error = null
        }

        // Validate category
        if (selectedCategoryId == null) {
            binding.tvCategoryError.text = "Please select a category"
            binding.tvCategoryError.visibility = View.VISIBLE
            isValid = false
        } else {
            binding.tvCategoryError.visibility = View.GONE
        }

        // Validate time range
        if (selectedEndTime <= selectedStartTime) {
            binding.tvTimeError.text = "End time must be after start time"
            binding.tvTimeError.visibility = View.VISIBLE
            isValid = false
        } else {
            binding.tvTimeError.visibility = View.GONE
        }

        if (!isValid) return

        binding.btnSave.isEnabled = false

        val expense = Expense(
            id = if (expenseId != -1L) expenseId else 0,
            userId = userId,
            categoryId = selectedCategoryId,
            amount = amount!!,
            description = description,
            date = selectedDate,
            startTime = selectedStartTime,
            endTime = selectedEndTime,
            notes = notes,
            photoPath = photoPath
        )

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                if (expenseId != -1L) {
                    db.expenseDao().updateExpense(expense)
                } else {
                    db.expenseDao().insertExpense(expense)
                }
            }
            finish()
        }
    }

    private fun loadExpense(id: Long) {
        lifecycleScope.launch {
            val expense = withContext(Dispatchers.IO) { db.expenseDao().getExpenseById(id) }
            expense?.let { e ->
                binding.etAmount.setText(e.amount.toString())
                binding.etDescription.setText(e.description)
                binding.etNotes.setText(e.notes)

                selectedDate = e.date
                selectedStartTime = e.startTime
                selectedEndTime = e.endTime
                selectedCategoryId = e.categoryId

                binding.etDate.setText(e.date)
                binding.etStartTime.setText(e.startTime)
                binding.etEndTime.setText(e.endTime)

                // Set category spinner
                val catIdx = categories.indexOfFirst { it.id == e.categoryId }
                if (catIdx >= 0) binding.spinnerCategory.setText(categories[catIdx].name, false)

                // Load photo
                e.photoPath?.let { path ->
                    photoPath = path
                    showPhotoPreview(Uri.fromFile(File(path)))
                }
            }
        }
    }

    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setTitle("Delete Expense")
            .setMessage("Are you sure you want to delete this expense?")
            .setPositiveButton("Delete") { _, _ -> deleteExpense() }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteExpense() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                db.expenseDao().getExpenseById(expenseId)?.let { expense ->
                    db.expenseDao().deleteExpense(expense)
                }
            }
            finish()
        }
    }
}
