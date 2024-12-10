package com.example.tulonglegal

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.tulonglegal.databinding.ActivityDocumentDetailBinding
import com.example.tulonglegal.databinding.LegalDocumentLibraryBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DocumentLibraryActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
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
        var binding = LegalDocumentLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        firestore = FirebaseFirestore.getInstance()

        // Fetch and update the user's name in the navigation drawer header
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let { loadUserData(it) }

        // Open navigation drawer when menu icon is clicked
        imgMenu.setOnClickListener {
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
                    startActivity(Intent(this, ClientProfileActivity::class.java))
                    true
                }
                R.id.home -> {
                    // Stay on Home, no action needed
                    true
                }
                R.id.settings -> {
                    startActivity(Intent(this, ClientSettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // Create a common OnClickListener for all document cards
        val cardClickListener = View.OnClickListener { view ->
            val intent = Intent(this, DocumentDetailActivity::class.java)

            // Pass the document type as an extra
            when (view.id) {
                R.id.employmentContractCard -> {
                    intent.putExtra("DOCUMENT_TYPE", "Employment Contract")
                }
                R.id.leaseAgreement -> {
                    intent.putExtra("DOCUMENT_TYPE", "Lease Agreement")
                }
                R.id.terminationCard -> {
                    intent.putExtra("DOCUMENT_TYPE", "Notice of Termination")
                }
                R.id.ceaseanddesistCard -> {
                    intent.putExtra("DOCUMENT_TYPE", "Cease and Desist Letter")
                }
            }

            startActivity(intent)
        }

        // Set the common click listener for all cards
        employmentContractCard.setOnClickListener(cardClickListener)
        leaseAgreementCard.setOnClickListener(cardClickListener)
        terminationCard.setOnClickListener(cardClickListener)
        ceaseAndDesistCard.setOnClickListener(cardClickListener)

        // Handle search bar input
        searchBar.setOnEditorActionListener { _, _, _ ->
            val query = searchBar.text.toString()
            Toast.makeText(this, "Searching for: $query", Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun loadUserData(userId: String) {
        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(UserProfile::class.java)
                    user?.let {
                        // Update the navigation header with the user's full name
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
