package com.example.tulonglegal

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class DocumentLibraryActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var imgMenu: ImageView
    private lateinit var searchBar: EditText
    private lateinit var employmentContractCard: LinearLayout
    private lateinit var leaseAgreementCard: LinearLayout
    private lateinit var terminationCard: LinearLayout
    private lateinit var ceaseAndDesistCard: LinearLayout
    private lateinit var linearTulongLegal: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.legal_document_library)

        // Initialize Views
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        imgMenu = findViewById(R.id.img_menu)
        searchBar = findViewById(R.id.searchBar)
        employmentContractCard = findViewById(R.id.employmentContractCard)
        leaseAgreementCard = findViewById(R.id.leaseAgreement)
        terminationCard = findViewById(R.id.terminationCard)
        ceaseAndDesistCard = findViewById(R.id.ceaseanddesistCard)
        linearTulongLegal = findViewById(R.id.linear_tulong_legal)

        // Open navigation drawer when menu icon is clicked
        imgMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

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

        // Set click listeners for card components
        employmentContractCard.setOnClickListener {
            Toast.makeText(this, "Employment Contract selected", Toast.LENGTH_SHORT).show()
        }

        leaseAgreementCard.setOnClickListener {
            Toast.makeText(this, "Lease Agreement selected", Toast.LENGTH_SHORT).show()
        }

        terminationCard.setOnClickListener {
            Toast.makeText(this, "Notice of Termination selected", Toast.LENGTH_SHORT).show()
        }

        ceaseAndDesistCard.setOnClickListener {
            Toast.makeText(this, "Cease and Desist Letter selected", Toast.LENGTH_SHORT).show()
        }

        // Handle search bar input
        searchBar.setOnEditorActionListener { _, _, _ ->
            val query = searchBar.text.toString()
            Toast.makeText(this, "Searching for: $query", Toast.LENGTH_SHORT).show()
            true
        }

        // Handle "Tulong Legal" banner click
        linearTulongLegal.setOnClickListener {
            Toast.makeText(this, "TulongLegal clicked", Toast.LENGTH_SHORT).show()
        }
    }

    // Handle back button to close the drawer if open
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
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
