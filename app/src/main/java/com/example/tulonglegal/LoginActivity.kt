package com.example.tulonglegal

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.tulonglegal.databinding.UserLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: UserLoginBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        // Adding TextWatcher to username and password input fields
        binding.usernameEditText.addTextChangedListener(loginTextWatcher)
        binding.passwordEditText.addTextChangedListener(loginTextWatcher)

        // Set up the listener for "Login for Legal Professionals"
        binding.lawyerLogin.setOnClickListener {
            // Navigate to LawyerLoginActivity
            val intent = Intent(this, LawyerLoginActivity::class.java)
            startActivity(intent)
        }

        // Handle Login Button Click
        binding.loginButton.setOnClickListener {
            val usernameInput = binding.usernameEditText.text.toString().trim()
            val passwordInput = binding.passwordEditText.text.toString().trim()

            // Check if input fields are valid
            if (usernameInput.isNotEmpty() && passwordInput.isNotEmpty()) {
                // Show Progress Bar
                showProgressBar(true)

                // Simulate login verification (replace with actual logic)
                val storedPassword = sharedPreferences.getString(usernameInput, null)
                if (storedPassword == passwordInput) {
                    // Navigate to ClientDashboardActivity
                    val intent = Intent(this, ClientDashboardActivity::class.java)
                    startActivity(intent)
                    finish()  // Close the login screen
                } else {
                    // Handle login failure
                    Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
                }

                // Hide Progress Bar after login attempt
                showProgressBar(false)
            } else {
                // Handle empty username/password
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Navigate to Registration Screen
        binding.signupText.setOnClickListener {
            val intent = Intent(this, ClientRegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    // TextWatcher to enable or disable login button based on input
    private val loginTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val usernameInput = binding.usernameEditText.text.toString().trim()
            val passwordInput = binding.passwordEditText.text.toString().trim()

            // Enable the login button only if both fields are not empty
            val isInputValid = usernameInput.isNotEmpty() && passwordInput.isNotEmpty()
            binding.loginButton.isEnabled = isInputValid

            // Change button background based on its state
            val buttonBackground = if (isInputValid) {
                R.drawable.button_enabled // Your enabled button background
            } else {
                R.drawable.button_disabled // Your disabled button background
            }
            binding.loginButton.background = ContextCompat.getDrawable(this@LoginActivity, buttonBackground)

            // Change button text color based on its state
            val buttonTextColor = if (isInputValid) {
                ContextCompat.getColor(this@LoginActivity, android.R.color.white) // White text when enabled
            } else {
                ContextCompat.getColor(this@LoginActivity, android.R.color.black) // Dark gray text when disabled
            }
            binding.loginButton.setTextColor(buttonTextColor)
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    // Function to show or hide the progress bar
    private fun showProgressBar(show: Boolean) {
        if (show) {
            binding.progressBar.visibility = View.VISIBLE
            binding.loginButton.isEnabled = false // Disable login button while loading
        } else {
            binding.progressBar.visibility = View.GONE
            binding.loginButton.isEnabled = true // Enable login button after loading
        }
    }
}
