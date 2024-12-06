package com.example.tulonglegal

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.addTextChangedListener
import com.example.tulonglegal.databinding.LawyerLoginBinding
import com.example.tulonglegal.databinding.LawyerMatchingBinding

class LawyerMatchingActivity : AppCompatActivity() {
    private lateinit var binding: LawyerMatchingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lawyer_matching)

        // Set up the "Back" button functionality
        binding.imageBack.setOnClickListener {
            finish() // Close this activity and return to the previous one
        }

        // Initialize the spinner for legal categories
        val spinnerLegalCategory = findViewById<Spinner>(R.id.spinnerLegalCategory)

        // List of legal categories
        val categories = listOf("Criminal", "Civil", "Labor", "Immigration", "Family")

        // Set up the spinner with an ArrayAdapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLegalCategory.adapter = adapter

        // Initialize the additional details EditText and button
        val additionalDetailsEditText = findViewById<EditText>(R.id.editTextAdditionalDetails)
        val btnFindMyLawyer = findViewById<AppCompatButton>(R.id.btnFindMyLawyer)

        // Disable "Find My Lawyer" button initially
        btnFindMyLawyer.isEnabled = false

        // Set the item selected listener for the spinner
        spinnerLegalCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                checkInputAndEnableButton(additionalDetailsEditText, btnFindMyLawyer)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle the case when no category is selected
                Toast.makeText(applicationContext, "No category selected", Toast.LENGTH_SHORT).show()
            }
        }

        // Watch for text input in the additional details EditText
        additionalDetailsEditText.addTextChangedListener {
            checkInputAndEnableButton(additionalDetailsEditText, btnFindMyLawyer)
        }

        // Handle Find My Lawyer button click
        btnFindMyLawyer.setOnClickListener {
            // Navigate to the MatchesFoundActivity
            val intent = Intent(this, MatchesFoundActivity::class.java)
            startActivity(intent)
        }
    }

    // Function to check if both inputs are filled and enable the button
    private fun checkInputAndEnableButton(additionalDetailsEditText: EditText, btnFindMyLawyer: AppCompatButton) {
        val selectedCategory = findViewById<Spinner>(R.id.spinnerLegalCategory).selectedItem.toString()
        val additionalDetails = additionalDetailsEditText.text.toString()

        // Enable button only when both legal category and additional details have input
        btnFindMyLawyer.isEnabled = selectedCategory.isNotEmpty() && additionalDetails.isNotEmpty()
    }
}
