package com.aureus.budget.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aureus.budget.data.AppDatabase
import com.aureus.budget.databinding.ActivityLoginBinding
import com.aureus.budget.utils.PasswordHasher
import com.aureus.budget.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        db = AppDatabase.getDatabase(this)

        // Auto-login if session exists
        if (sessionManager.isLoggedIn()) {
            goToMain()
            return
        }

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener { attemptLogin() }

        binding.tvRegisterLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // Allow Enter key to trigger login
        binding.etPassword.setOnEditorActionListener { _, _, _ ->
            attemptLogin()
            true
        }
    }

    private fun attemptLogin() {
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString()

        // Validate inputs
        var isValid = true

        if (username.isEmpty()) {
            binding.tilUsername.error = "Username is required"
            isValid = false
        } else {
            binding.tilUsername.error = null
        }

        if (password.isEmpty()) {
            binding.tilPassword.error = "Password is required"
            isValid = false
        } else {
            binding.tilPassword.error = null
        }

        if (!isValid) return

        // Show loading state
        binding.btnLogin.isEnabled = false
        binding.tvError.visibility = View.GONE

        val passwordHash = PasswordHasher.hash(password)

        lifecycleScope.launch {
            val user = withContext(Dispatchers.IO) {
                db.userDao().login(username, passwordHash)
            }

            if (user != null) {
                sessionManager.saveSession(user.id, user.username)
                goToMain()
            } else {
                binding.tvError.text = "Invalid username or password"
                binding.tvError.visibility = View.VISIBLE
                binding.btnLogin.isEnabled = true
            }
        }
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
