package com.example.tulonglegal

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tulonglegal.databinding.ClientRegistrationBinding

class ClientRegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ClientRegistrationBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ClientRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        // Initially disable the Register button
        binding.registerButton.isEnabled = false

        // Set up TextWatchers for input validation
        val fields = arrayOf(
            binding.nameEditText,
            binding.contactEditText,
            binding.emailEditText,
            binding.unameEditText,
            binding.passEditText,
            binding.confirmpassEditText
        )
        fields.forEach { field ->
            field.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    binding.registerButton.isEnabled = isValid()
                }
                override fun afterTextChanged(s: Editable?) {}
            })
        }

        // Enable Register button if checkbox is checked
        binding.checkBox.setOnCheckedChangeListener { _, _ -> binding.registerButton.isEnabled = isValid() }

        // Register button click logic
        binding.registerButton.setOnClickListener {
            val username = binding.unameEditText.text.toString().trim()
            val password = binding.passEditText.text.toString().trim()

            if (sharedPreferences.contains(username)) {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show()
            } else {
                // Show progress bar while registering
                showProgressBar(true)

                // Save user data
                val editor = sharedPreferences.edit()
                editor.putString(username, password)
                editor.putString("${username}_name", binding.nameEditText.text.toString().trim())
                editor.putString("${username}_contact", binding.contactEditText.text.toString().trim())
                editor.putString("${username}_email", binding.emailEditText.text.toString().trim())
                editor.apply()

                // Show success message
                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                showProgressBar(false)

                // Finish activity and go back to LoginActivity
                finish()
            }
        }
    }

    // Check if all fields are valid
    private fun isValid(): Boolean {
        val name = binding.nameEditText.text.toString().trim()
        val contact = binding.contactEditText.text.toString().trim()
        val email = binding.emailEditText.text.toString().trim()
        val username = binding.unameEditText.text.toString().trim()
        val password = binding.passEditText.text.toString().trim()
        val confirmPassword = binding.confirmpassEditText.text.toString().trim()

        if (name.isEmpty() || contact.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) return false
        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!binding.checkBox.isChecked) {
            Toast.makeText(this, "You must accept the terms and conditions", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    // Function to show or hide the progress bar
    private fun showProgressBar(show: Boolean) {
        if (show) {
            binding.progressBar.visibility = View.VISIBLE
            binding.registerButton.isEnabled = false  // Disable register button during loading
        } else {
            binding.progressBar.visibility = View.GONE
            binding.registerButton.isEnabled = true  // Enable register button after loading
        }
    }
}
