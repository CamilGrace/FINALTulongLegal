package com.example.tulonglegal

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.tulonglegal.databinding.LawyerProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LawyerProfileActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var userEmail: TextView
    private lateinit var userContact: TextView
    private lateinit var userFullName: TextView
    private lateinit var userAddress: TextView
    private lateinit var userGender: TextView
    private lateinit var userAge: TextView
    private lateinit var userAvailability: TextView
    private lateinit var userLegalSpecialization: TextView
    private lateinit var userPrelawDegree: TextView
    private lateinit var userLawSchool: TextView
    private lateinit var userYearsOfExperience: TextView
    private lateinit var userConsultationFee: TextView
    private lateinit var binding: LawyerProfileBinding

    companion object {
        const val EDIT_PROFILE_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LawyerProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()

        // Initialize TextViews for new fields
        userFullName = findViewById(R.id.text_full_name)
        userEmail = findViewById(R.id.user_email)
        userContact = findViewById(R.id.user_contact)
        userAddress = findViewById(R.id.user_address)
        userGender = findViewById(R.id.user_gender)
        userAge = findViewById(R.id.user_age)
        userAvailability = findViewById(R.id.user_availability)
        userLegalSpecialization = findViewById(R.id.user_legal)
        userPrelawDegree = findViewById(R.id.user_prelaw)
        userLawSchool = findViewById(R.id.user_lawschool)
        userYearsOfExperience = findViewById(R.id.user_yearsofexp)
        userConsultationFee = findViewById(R.id.user_ConsultationFee)

        // Fetch and display initial user data
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let { loadUserData(it) }

        // Handle Edit Profile Button
        binding.editProfileBtn.setOnClickListener {
            val intent = Intent(this, LawyerEditProfileActivity::class.java)
            startActivityForResult(intent, EDIT_PROFILE_REQUEST_CODE)
        }

        // Drawer and Navigation setup...
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navigationView: NavigationView = binding.navigationView
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigationView

        binding.imgMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Handle Drawer NavigationItem selection
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_profile -> {
                    // Navigate to Profile Activity
                    startActivity(Intent(this, LawyerProfileActivity::class.java))
                }
                R.id.nav_settings -> {
                    // Navigate to Settings Activity
                    startActivity(Intent(this, ClientSettingsActivity::class.java))
                }

            }

            // Close the drawer after an item is selected
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Handle Bottom Navigation selection
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.person -> {
                    // Navigate to Profile Activity
                    startActivity(Intent(this, LawyerProfileActivity::class.java))
                    true
                }
                R.id.home -> {
                    startActivity(Intent(this, LawyerDashboardActivity::class.java))
                    true
                }
                R.id.settings -> {
                    // Navigate to Settings Activity
                    startActivity(Intent(this, ClientSettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun loadUserData(userId: String) {
        firestore.collection("lawyers").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(LawyerProfile::class.java)
                    user?.let {
                        userFullName.text = it.fullName
                        userEmail.text = it.email
                        userContact.text = it.contactNo
                        userAddress.text = it.address
                        userGender.text = it.gender
                        userAge.text = it.age.toString()
                        userAvailability.text = it.availability
                        userLegalSpecialization.text = it.legalspecialization
                        userPrelawDegree.text = it.prelawdegree
                        userLawSchool.text = it.lawschool
                        userYearsOfExperience.text = it.yearsofexp.toString()
                        userConsultationFee.text = it.consultationfee.toString()
                    }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Handle profile updates after returning from EditProfileActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == EDIT_PROFILE_REQUEST_CODE && resultCode == RESULT_OK) {
            // Update the profile with the data returned from the edit activity
            data?.let {
                userFullName.text = it.getStringExtra("fullName")
                userEmail.text = it.getStringExtra("email")
                userContact.text = it.getStringExtra("contactNo")
                userAddress.text = it.getStringExtra("address")
                userGender.text = it.getStringExtra("gender")
                userAge.text = it.getIntExtra("age", 0).toString()
                userAvailability.text = it.getStringExtra("availability")
                userLegalSpecialization.text = it.getStringExtra("legalspecialization")
                userPrelawDegree.text = it.getStringExtra("prelawdegree")
                userLawSchool.text = it.getStringExtra("lawschool")
                userYearsOfExperience.text = it.getIntExtra("yearsofexp", 0).toString()
                userConsultationFee.text = it.getIntExtra("consultationfee", 0).toString()

                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let { loadUserData(it) }
    }
}