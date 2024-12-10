package com.example.tulonglegal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tulonglegal.databinding.ClientSettingsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class LawyerSettingsActivity : AppCompatActivity() {

    private lateinit var binding: ClientSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ClientSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle Back Button click
        binding.imageBack.setOnClickListener {
            finish() // Close the activity and go back to the previous screen
        }

        // Handle Bottom Navigation selection
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.person -> {
                    // Navigate to Profile Activity
                    startActivity(Intent(this, LawyerInboxActivity::class.java))
                    true
                }
                R.id.home -> {
                    // Navigate to Dashboard Activity
                    startActivity(Intent(this, LawyerDashboardActivity::class.java))
                    true
                }
                R.id.settings -> {
                    // Stay on Settings Activity; no action needed
                    true
                }
                else -> false
            }
        }

        // Set the default selected item in Bottom Navigation to Settings
        bottomNavigationView.selectedItemId = R.id.settings
    }

    override fun onResume() {
        super.onResume()

        // Ensure the Bottom Navigation highlights the Settings icon
        binding.bottomNavigationView.selectedItemId = R.id.settings
    }
}
