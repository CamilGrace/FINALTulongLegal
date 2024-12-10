package com.example.tulonglegal

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tulonglegal.adapters.InboxAdapter
import com.example.tulonglegal.databinding.ActivityInboxBinding
import com.example.tulonglegal.databinding.ClientDashboardBinding
import com.example.tulonglegal.models.InboxThread
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ClientInboxActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInboxBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var inboxRecyclerView: RecyclerView
    private lateinit var inboxAdapter: InboxAdapter
    private lateinit var currentUserId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox)

        // Initialize View Binding
        binding = ActivityInboxBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle Back Button click
        binding.imageBack.setOnClickListener {
            finish() // Close the activity and go back to the previous screen
        }

        // Initialize Firebase and Firestore
        firestore = FirebaseFirestore.getInstance()
        currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        // Initialize RecyclerView
        inboxRecyclerView = findViewById(R.id.inboxRecycler)
        inboxRecyclerView.layoutManager = LinearLayoutManager(this)
        inboxAdapter = InboxAdapter(mutableListOf()) { thread ->
            // Open the messaging activity for the selected thread
            val intent = Intent(this, ClientMessagingActivity::class.java)
            intent.putExtra("lawyerId", thread.lawyerId) // Pass lawyer ID
            startActivity(intent)
        }
        inboxRecyclerView.adapter = inboxAdapter

        // Load inbox threads
        loadClientInboxThreads()

        // Handle Bottom Navigation selection
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.person -> {
                    // Navigate to Profile Activity
                    true
                }
                R.id.home -> {
                    startActivity(Intent(this, ClientDashboardActivity::class.java))
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

    private fun loadClientInboxThreads() {
        firestore.collection("threads")
            .whereEqualTo("clientId", currentUserId) // Fetch threads for the current client
            .get()
            .addOnSuccessListener { querySnapshot ->
                val threads = querySnapshot.documents.mapNotNull { it.toObject(InboxThread::class.java) }
                inboxAdapter.updateThreads(threads)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load inbox", Toast.LENGTH_SHORT).show()
            }
    }
}
