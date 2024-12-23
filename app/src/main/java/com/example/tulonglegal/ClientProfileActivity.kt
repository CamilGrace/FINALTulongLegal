package com.example.tulonglegal

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.tulonglegal.databinding.ClientProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ClientProfileActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var userEmail: TextView
    private lateinit var userContact: TextView
    private lateinit var userFullName: TextView
    private lateinit var userAddress: TextView
    private lateinit var binding: ClientProfileBinding

    companion object {
        const val EDIT_PROFILE_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ClientProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()

        // Initialize UI components for user data
        userFullName = findViewById(R.id.text_full_name)
        userEmail = findViewById(R.id.user_email)
        userContact = findViewById(R.id.user_contact)
        userAddress = findViewById(R.id.user_address)

        // Fetch and display initial user data
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let { loadUserData(it) }

        // Handle Edit Profile Button
        binding.editProfileBtn.setOnClickListener {
            val intent = Intent(this, ClientEditProfileActivity::class.java)
            startActivity(intent)
        }

        // Drawer and Navigation setup
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
                    startActivity(Intent(this, ClientProfileActivity::class.java))
                }
                R.id.nav_inbox -> {
                    startActivity(Intent(this, ClientInboxActivity::class.java))
                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, ClientSettingsActivity::class.java))
                }
                R.id.nav_signout -> {
                    signOutUser()
                }
            }

            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        bottomNavigationView.selectedItemId = 0

        // Handle Bottom Navigation selection
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.person -> {
                    startActivity(Intent(this, ClientInboxActivity::class.java))
                    true
                }
                R.id.home -> {
                    startActivity(Intent(this, ClientDashboardActivity::class.java))
                    true
                }
                R.id.settings -> {
                    startActivity(Intent(this, ClientSettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun loadUserData(userId: String) {
        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(UserProfile::class.java)
                    user?.let {
                        // Update UI with user data
                        userFullName.text = it.fullName
                        userEmail.text = it.email
                        userContact.text = it.contactNo
                        userAddress.text = it.address

                        // Also update the navigation drawer header with the full name
                        val navigationView: NavigationView = binding.navigationView
                        val headerView = navigationView.getHeaderView(0)
                        val userNameTextView: TextView = headerView.findViewById(R.id.user_name)
                        userNameTextView.text = it.fullName
                    }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onResume() {
        super.onResume()
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let { loadUserData(it) }
    }

    private fun signOutUser() {
        // Clear user session data (if using SharedPreferences or other storage)
        val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        // Redirect to LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear activity stack
        startActivity(intent)
        finish() // Close the current activity
    }
}
