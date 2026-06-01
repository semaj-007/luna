package com.aureus.budget.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aureus.budget.data.AppDatabase
import com.aureus.budget.data.entity.User
import com.aureus.budget.databinding.ActivityRegisterBinding
import com.aureus.budget.utils.PasswordHasher
import com.aureus.budget.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        db = AppDatabase.getDatabase(this)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnRegister.setOnClickListener { attemptRegister() }
        binding.tvLoginLink.setOnClickListener { finish() }
    }

    private fun attemptRegister() {
        val username = binding.etUsername.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        var isValid = true

        binding.tilUsername.error = null
        binding.tilEmail.error = null
        binding.tilPassword.error = null
        binding.tilConfirmPassword.error = null
        binding.tvError.visibility = View.GONE

        if (username.isEmpty()) {
            binding.tilUsername.error = "Username is required"
            isValid = false
        } else if (username.length < 3) {
            binding.tilUsername.error = "Username must be at least 3 characters"
            isValid = false
        }

        if (email.isEmpty()) {
            binding.tilEmail.error = "Email is required"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = "Enter a valid email address"
            isValid = false
        }

        if (password.isEmpty()) {
            binding.tilPassword.error = "Password is required"
            isValid = false
        } else if (password.length < 6) {
            binding.tilPassword.error = "Password must be at least 6 characters"
            isValid = false
        }

        if (confirmPassword.isEmpty()) {
            binding.tilConfirmPassword.error = "Please confirm your password"
            isValid = false
        } else if (password != confirmPassword) {
            binding.tilConfirmPassword.error = "Passwords do not match"
            isValid = false
        }

        if (!isValid) return

        binding.btnRegister.isEnabled = false

        lifecycleScope.launch {
            // Check if username already taken
            val existing = withContext(Dispatchers.IO) {
                db.userDao().getUserByUsername(username)
            }

            if (existing != null) {
                binding.tilUsername.error = "Username already taken"
                binding.btnRegister.isEnabled = true
                return@launch
            }

            // Create user
            val user = User(
                username = username,
                passwordHash = PasswordHasher.hash(password),
                email = email
            )

            val userId = withContext(Dispatchers.IO) {
                db.userDao().insertUser(user)
            }

            if (userId > 0) {
                sessionManager.saveSession(userId, username)

                // Navigate to main
                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                binding.tvError.text = "Registration failed. Please try again."
                binding.tvError.visibility = View.VISIBLE
                binding.btnRegister.isEnabled = true
            }
        }
    }
}
