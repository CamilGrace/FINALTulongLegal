package com.example.tulonglegal

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tulonglegal.adapters.InboxAdapter
import com.example.tulonglegal.databinding.ActivityInboxBinding
import com.example.tulonglegal.databinding.LawyerEditProfileBinding
import com.example.tulonglegal.models.InboxThread
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LawyerInboxActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var inboxRecyclerView: RecyclerView
    private lateinit var inboxAdapter: InboxAdapter
    private lateinit var currentUserId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox)
        var binding = ActivityInboxBinding.inflate(layoutInflater)
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
            val intent = Intent(this, LawyerMessagingActivity::class.java)
            intent.putExtra("clientId", thread.clientId) // Pass client ID
            startActivity(intent)
        }
        inboxRecyclerView.adapter = inboxAdapter

        // Load inbox threads
        loadLawyerInboxThreads()
    }

    private fun loadLawyerInboxThreads() {
        firestore.collection("threads")
            .whereEqualTo("lawyerId", currentUserId) // Fetch threads for the current lawyer
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
