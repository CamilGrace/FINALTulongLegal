package com.example.tulonglegal

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LawyerMessagingActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var messageInput: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var messagesRecyclerView: RecyclerView
    private lateinit var messagesAdapter: MessagesAdapter
    private lateinit var currentUserId: String
    private var clientId: String? = null // The client the lawyer is messaging

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lawyer_messaging)

        // Initialize Firebase and Firestore
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

        // Get Client ID from Intent
        clientId = intent.getStringExtra("clientId") // Passed from wherever this activity is launched

        // Send message on button click
        sendButton.setOnClickListener { sendMessage() }

        // Load existing messages
        loadMessages()
    }

    private fun sendMessage() {
        val messageText = messageInput.text.toString()
        if (messageText.isNotBlank()) {
            val newMessage = Message(
                text = messageText,
                timestamp = System.currentTimeMillis(),
                senderId = currentUserId,
                receiverId = clientId ?: ""
            )

            // Update the UI instantly
            messagesAdapter.addMessage(newMessage, messagesRecyclerView)
            messageInput.text.clear()

            // Save the message to Firestore
            firestore.collection("messages")
                .add(newMessage)
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun loadMessages() {
        firestore.collection("messages")
            .whereEqualTo("senderId", currentUserId)
            .whereEqualTo("receiverId", clientId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val sentMessages = querySnapshot.documents.mapNotNull { it.toObject(Message::class.java) }

                firestore.collection("messages")
                    .whereEqualTo("senderId", clientId)
                    .whereEqualTo("receiverId", currentUserId)
                    .get()
                    .addOnSuccessListener { receivedSnapshot ->
                        val receivedMessages = receivedSnapshot.documents.mapNotNull { it.toObject(Message::class.java) }

                        // Combine and sort messages
                        val allMessages = (sentMessages + receivedMessages).sortedBy { it.timestamp }
                        messagesAdapter = MessagesAdapter(allMessages.toMutableList())
                        messagesRecyclerView.adapter = messagesAdapter
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load messages", Toast.LENGTH_SHORT).show()
            }
    }
}
