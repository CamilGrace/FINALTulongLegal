package com.example.tulonglegal

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.tulonglegal.databinding.LawyerEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LawyerEditProfileActivity : AppCompatActivity() {
    private lateinit var binding: LawyerEditProfileBinding
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LawyerEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()

        // Enable save button only when the required fields are filled
        binding.userFullName.addTextChangedListener { updateSaveButtonState() }
        binding.userEmail.addTextChangedListener { updateSaveButtonState() }
        binding.userContact.addTextChangedListener { updateSaveButtonState() }
        binding.userAddress.addTextChangedListener { updateSaveButtonState() }
        binding.userGender.addTextChangedListener { updateSaveButtonState() }
        binding.userAge.addTextChangedListener { updateSaveButtonState() }
        binding.userAvailability.addTextChangedListener { updateSaveButtonState() }
        binding.userLegal.addTextChangedListener { updateSaveButtonState() }
        binding.userPrelaw.addTextChangedListener { updateSaveButtonState() }
        binding.userLawschool.addTextChangedListener { updateSaveButtonState() }
        binding.userYearsofexp.addTextChangedListener { updateSaveButtonState() }
        binding.userConsultationFee.addTextChangedListener { updateSaveButtonState() }

        // Save Button Click Listener
        binding.saveButton.setOnClickListener {
            val updatedFullName = binding.userFullName.text.toString()
            val updatedEmail = binding.userEmail.text.toString()
            val updatedContact = binding.userContact.text.toString()
            val updatedAddress = binding.userAddress.text.toString()
            val updatedGender = binding.userGender.text.toString()
            val updatedAge = binding.userAge.text.toString().toIntOrNull() ?: 0
            val updatedAvailability = binding.userAvailability.text.toString()
            val updatedLegalSpecialization = binding.userLegal.text.toString()
            val updatedPrelawDegree = binding.userPrelaw.text.toString()
            val updatedLawSchool = binding.userLawschool.text.toString()
            val updatedYearsOfExperience = binding.userYearsofexp.text.toString().toIntOrNull() ?: 0
            val updatedConsultationFee = binding.userConsultationFee.text.toString().toIntOrNull() ?: 0

            if (updatedFullName.isNotBlank() && updatedEmail.isNotBlank()) {
                // Update Firestore
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                userId?.let {
                    val updatedData = mapOf(
                        "fullName" to updatedFullName,
                        "email" to updatedEmail,
                        "contactNo" to updatedContact,
                        "address" to updatedAddress,
                        "gender" to updatedGender,
                        "age" to updatedAge,
                        "availability" to updatedAvailability,
                        "legalspecialization" to updatedLegalSpecialization,
                        "prelawdegree" to updatedPrelawDegree,
                        "lawschool" to updatedLawSchool,
                        "yearsofexp" to updatedYearsOfExperience,
                        "consultationfee" to updatedConsultationFee
                    )
                    firestore.collection("lawyers").document(it)
                        .update(updatedData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()

                            // Pass updated data back to the profile screen
                            val resultIntent = Intent()
                            resultIntent.putExtra("fullName", updatedFullName)
                            resultIntent.putExtra("email", updatedEmail)
                            resultIntent.putExtra("contactNo", updatedContact)
                            resultIntent.putExtra("address", updatedAddress)
                            resultIntent.putExtra("gender", updatedGender)
                            resultIntent.putExtra("age", updatedAge)
                            resultIntent.putExtra("availability", updatedAvailability)
                            resultIntent.putExtra("legalspecialization", updatedLegalSpecialization)
                            resultIntent.putExtra("prelawdegree", updatedPrelawDegree)
                            resultIntent.putExtra("lawschool", updatedLawSchool)
                            resultIntent.putExtra("yearsofexp", updatedYearsOfExperience)
                            resultIntent.putExtra("consultationfee", updatedConsultationFee)
                            setResult(RESULT_OK, resultIntent)

                            finish() // Close the activity
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error updating profile: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Toast.makeText(this, "Full Name and Email cannot be empty.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateSaveButtonState() {
        val isFullNameFilled = binding.userFullName.text.isNotEmpty()
        val isEmailFilled = binding.userEmail.text.isNotEmpty()
        val isContactFilled = binding.userContact.text.isNotEmpty()
        val isAddressFilled = binding.userAddress.text.isNotEmpty()
        val isGenderFilled = binding.userGender.text.isNotEmpty()
        val isAgeFilled = binding.userAge.text.isNotEmpty()
        val isAvailabilityFilled = binding.userAvailability.text.isNotEmpty()
        val isSpecializationFilled = binding.userLegal.text.isNotEmpty()

        // Enable button if all required fields are filled
        binding.saveButton.isEnabled = isFullNameFilled && isEmailFilled && isContactFilled && isAddressFilled
                && isGenderFilled && isAgeFilled && isAvailabilityFilled && isSpecializationFilled
        binding.saveButton.setBackgroundResource(
            if (binding.saveButton.isEnabled) R.drawable.button_enabled else R.drawable.button_disabled
        )
    }
}
