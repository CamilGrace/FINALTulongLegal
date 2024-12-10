package com.example.tulonglegal

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.tulonglegal.databinding.LawyerDashboardBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LawyerDashboardActivity : AppCompatActivity() {
    private lateinit var binding: LawyerDashboardBinding
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LawyerDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()

        // Get the DrawerLayout and NavigationView from the layout
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navigationView: NavigationView = binding.navigationView
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigationView

        // Open the Drawer when the menu icon is clicked
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
                R.id.nav_inbox -> {
                    // Navigate to Inbox Activity
                    startActivity(Intent(this, LawyerInboxActivity::class.java))
                }
                R.id.nav_settings -> {
                    // Navigate to Settings Activity
                    startActivity(Intent(this, LawyerSettingsActivity::class.java))
                }
                R.id.nav_signout -> {
                    signOutUser()
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
                    // Navigate to Inbox Activity
                    startActivity(Intent(this, LawyerInboxActivity::class.java))
                    true
                }
                R.id.home -> {
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

        // Load user data and update navigation header
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let { loadUserData(it) }
    }

    private fun loadUserData(userId: String) {
        firestore.collection("lawyers").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(LawyerProfile::class.java)
                    user?.let {
                        // Update the navigation header with the user's full name
                        val headerView = binding.navigationView.getHeaderView(0)
                        val userNameTextView: TextView = headerView.findViewById(R.id.user_name)
                        userNameTextView.text = it.fullName
                    }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
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
