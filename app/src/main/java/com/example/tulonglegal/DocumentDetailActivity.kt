package com.example.tulonglegal

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.tulonglegal.databinding.ActivityDocumentDetailBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DocumentDetailActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var imageBack: ImageView
    private lateinit var documentTitle: TextView
    private lateinit var documentDesc: TextView
    private lateinit var containerDownload: FrameLayout
    private lateinit var textStartMatching: TextView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var imgMenu: ImageView
    private lateinit var binding: ActivityDocumentDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document_detail)
        binding = ActivityDocumentDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize views
        drawerLayout = findViewById(R.id.drawer_layout)
        imageBack = findViewById(R.id.imageBack)
        documentTitle = findViewById(R.id.documentTitle)
        documentDesc = findViewById(R.id.documentDesc)
        containerDownload = findViewById(R.id.container_download)
        textStartMatching = findViewById(R.id.text_start_matching)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        imgMenu = findViewById(R.id.img_menu)

        firestore = FirebaseFirestore.getInstance()

        // Get the document type from the Intent
        val documentType = intent.getStringExtra("DOCUMENT_TYPE")

        // Set the document title and description
        documentTitle.text = "$documentType Form"
        documentDesc.text = "This is the $documentType form. You can download it below."

        // Handle back button click
        imageBack.setOnClickListener {
            onBackPressed()
        }

        // Handle download button click
        containerDownload.setOnClickListener {
            Toast.makeText(this, "Downloading $documentType...", Toast.LENGTH_SHORT).show()
            // Implement actual download logic here
        }

        // Handle menu button click
        imgMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Fetch and update the user's name in the navigation drawer header
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let { loadUserData(it) }

        // Drawer and Navigation setup...
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navigationView: NavigationView = binding.navigationView
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigationView

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

        // Set up bottom navigation
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.person -> {
                    startActivity(Intent(this, ClientProfileActivity::class.java))
                    true
                }
                R.id.home -> {
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
