package com.example.tulonglegal

import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ClientMessagingActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var messageInput: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var messagesRecyclerView: RecyclerView
    private lateinit var messagesAdapter: MessagesAdapter
    private lateinit var currentUserId: String
    private var lawyerId: String? = null // Replace with the lawyer's ID you want to chat with

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_messaging)

        // Initialize Firestore and FirebaseAuth
        firestore = FirebaseFirestore.getInstance()
        currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        // Initialize Views
        messageInput = findViewById(R.id.messageEditText)
        sendButton = findViewById(R.id.sendButton)
        messagesRecyclerView = findViewById(R.id.messagesRecyclerView)

        // Initialize RecyclerView
        messagesAdapter = MessagesAdapter(mutableListOf())
        messagesRecyclerView.layoutManager = LinearLayoutManager(this)
        messagesRecyclerView.adapter = messagesAdapter

        // Get Lawyer ID from Intent
        lawyerId = intent.getStringExtra("lawyerId") // Passed from MatchesFoundActivity

        // Send message when the button is clicked
        sendButton.setOnClickListener { sendMessage() }

        // Load chat messages
        loadMessages()
    }

    private fun sendMessage() {
        val messageText = messageInput.text.toString()
        if (messageText.isNotBlank()) {
            val newMessage = Message(
                text = messageText,
                timestamp = System.currentTimeMillis(),
                senderId = currentUserId,
                receiverId = lawyerId ?: ""
            )

            // Update the UI instantly
            messagesAdapter.addMessage(newMessage, messagesRecyclerView)
            messageInput.text.clear()

            // Save message to Firestore
            firestore.collection("messages")
                .add(newMessage)
                .addOnFailureListener {
                    // Handle failure if needed (e.g., show a retry option)
                }
        }
    }

    private fun loadMessages() {
        firestore.collection("messages")
            .whereEqualTo("senderId", currentUserId)
            .whereEqualTo("receiverId", lawyerId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val messages = querySnapshot.documents.mapNotNull { it.toObject(Message::class.java) }
                messagesAdapter = MessagesAdapter(messages.toMutableList())
                messagesRecyclerView.adapter = messagesAdapter
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load messages", Toast.LENGTH_SHORT).show()
            }
    }
}
