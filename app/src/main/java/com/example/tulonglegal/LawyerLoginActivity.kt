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
import com.example.tulonglegal.databinding.LawyerLoginBinding

class LawyerLoginActivity : AppCompatActivity() {

    private lateinit var binding: LawyerLoginBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = LawyerLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("LawyerPrefs", MODE_PRIVATE)

        // Set up the "Back" button functionality
        binding.imageBack.setOnClickListener {
            finish() // Close this activity and return to the previous one
        }

        // Add TextWatchers for the username and password fields
        binding.usernameEditText.addTextChangedListener(loginTextWatcher)
        binding.passwordEditText.addTextChangedListener(loginTextWatcher)

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
                    // Save login state in SharedPreferences
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("isLoggedIn", true)  // Set user as logged in
                    editor.putString("username", usernameInput)  // Save username
                    editor.apply()

                    // Navigate to LawyerDashboardActivity
                    val intent = Intent(this, LawyerDashboardActivity::class.java)
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

        // Set the click listener for "Sign up here" text
        binding.signupText.setOnClickListener {
            val intent = Intent(this, LawyerRegistrationActivity::class.java)
            startActivity(intent)  // Start LawyerRegistrationActivity
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
            binding.loginButton.background = ContextCompat.getDrawable(this@LawyerLoginActivity, buttonBackground)

            // Change button text color based on its state
            val buttonTextColor = if (isInputValid) {
                ContextCompat.getColor(this@LawyerLoginActivity, android.R.color.white) // White text when enabled
            } else {
                ContextCompat.getColor(this@LawyerLoginActivity, android.R.color.black) // Dark gray text when disabled
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
