package com.example.tulonglegal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.tulonglegal.databinding.ClientDashboardBinding

class ClientDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ClientDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ClientDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                    startActivity(Intent(this, ClientProfileActivity::class.java))
                }
                R.id.nav_settings -> {
                    // Navigate to Settings Activity
                    startActivity(Intent(this, ClientSettingsActivity::class.java))
                }

                R.id.nav_sign_out -> {
                    // Sign out the user and navigate to LoginActivity
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
                    startActivity(Intent(this, ClientProfileActivity::class.java))
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

        // Set OnClickListener for the "Start Matching" button
        binding.containerMatching.setOnClickListener {
            val intent = Intent(this, LawyerMatchingActivity::class.java)
            startActivity(intent)
        }

        // Set OnClickListener for the "Browse Templates" button
        binding.containerFrameLibrary.setOnClickListener {
            val intent = Intent(this, DocumentLibraryActivity::class.java)
            startActivity(intent)
        }
    }

    // Override onResume to set the selected item on bottom navigation
    override fun onResume() {
        super.onResume()

        // Get the current BottomNavigationView
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigationView

        // Manually set the selected item to the correct position when returning
        when (intent?.getIntExtra("SELECTED_ITEM", R.id.home)) {
            R.id.settings -> {
                bottomNavigationView.selectedItemId = R.id.settings
            }
            R.id.person -> {
                bottomNavigationView.selectedItemId = R.id.person
            }
            else -> {
                bottomNavigationView.selectedItemId = R.id.home
            }
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
