package com.example.tulonglegal

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.tulonglegal.databinding.ClientDashboardBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ClientDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ClientDashboardBinding
    private lateinit var userNameTextView: TextView
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ClientDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()

        // Get the DrawerLayout and NavigationView from the layout
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navigationView: NavigationView = binding.navigationView
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigationView

        // Initialize the TextView for the user's full name in the NavigationView header
        val headerView = navigationView.getHeaderView(0)
        userNameTextView = headerView.findViewById(R.id.user_name)

        // Fetch and display the user's full name
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let { loadUserData(it) }

        // Open the Drawer when the menu icon is clicked
        binding.imgMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Handle Drawer NavigationItem selection
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_profile -> {
                    // Navigate to Profile Activity
                    startActivity(Intent(this, ClientProfileActivity::class.java))
                }
                R.id.nav_inbox -> {
                    // Navigate to Inbox Activity
                    startActivity(Intent(this, ClientInboxActivity::class.java))
                }
                R.id.nav_settings -> {
                    // Navigate to Settings Activity
                    startActivity(Intent(this, ClientSettingsActivity::class.java))
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
                    // Navigate to Profile Activity
                    startActivity(Intent(this, ClientInboxActivity::class.java))
                    true
                }
                R.id.home -> {
                    // Stay on Home, no action needed
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

        // Set OnClickListener for other buttons
        binding.containerMatching.setOnClickListener {
            val intent = Intent(this, LawyerMatchingActivity::class.java)
            startActivity(intent)
        }

        binding.containerFrameLibrary.setOnClickListener {
            val intent = Intent(this, DocumentLibraryActivity::class.java)
            startActivity(intent)
        }
    }

    // Load user's data from Firestore
    private fun loadUserData(userId: String) {
        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(UserProfile::class.java)
                    user?.let {
                        userNameTextView.text = it.fullName // Set the user's full name in the drawer header
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

    // Sign out the user and redirect to LoginActivity
    private fun signOutUser() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // Close the current activity
    }
}
