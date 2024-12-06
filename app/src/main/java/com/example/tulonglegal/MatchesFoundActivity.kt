package com.example.tulonglegal

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

class MatchesFoundActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.matches_found)

        // Sample data for the carousel
        val lawyers = listOf(
            Lawyer("Lawyer 1", "Criminal Law", 10, 2000.0),
            Lawyer("Lawyer 2", "Family Law", 8, 1500.0),
            Lawyer("Lawyer 3", "Corporate Law", 15, 3000.0),
            Lawyer("Lawyer 4", "Civil Law", 12, 1800.0),
            Lawyer("Lawyer 5", "Intellectual Property", 6, 2500.0)
        )

        // Initialize RecyclerView for the carousel
        val recyclerView = findViewById<RecyclerView>(R.id.carouselRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Attach PagerSnapHelper for centering the cards (both start and end)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        // Set up the carousel adapter with the lawyer list
        recyclerView.adapter = CarouselAdapter(lawyers)

        // Programmatically scroll to the first item to make sure it's centered initially
        recyclerView.post {
            recyclerView.scrollToPosition(0)
        }

        // Button to go to the next activity
        val btnFindMyLawyer = findViewById<AppCompatButton>(R.id.btnMessage)
        btnFindMyLawyer.setOnClickListener {
            // Handle the logic when the button is clicked
            Toast.makeText(applicationContext, "Finding a lawyer...", Toast.LENGTH_SHORT).show()
            // Go to the next activity (example: Lawyer Matching)
            startActivity(Intent(this, LawyerMatchingActivity::class.java))
        }
    }

    // Data class for Lawyer
    data class Lawyer(
        val name: String,
        val lawCategory: String,
        val yearsOfExperience: Int,
        val consultationFee: Double // In pesos
    )

    // Carousel Adapter Implementation
    class CarouselAdapter(private val items: List<Lawyer>) : RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

        // ViewHolder class for the adapter
        class CarouselViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val nameTextView: TextView = itemView.findViewById(R.id.textItemName)
            val lawCategoryTextView: TextView = itemView.findViewById(R.id.textItemCategory)
            val experienceTextView: TextView = itemView.findViewById(R.id.textItemExperience)
            val feeTextView: TextView = itemView.findViewById(R.id.textItemFee)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.carousel_item, parent, false)
            return CarouselViewHolder(view)
        }

        override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
            val lawyer = items[position]
            holder.nameTextView.text = lawyer.name
            holder.lawCategoryTextView.text = lawyer.lawCategory
            holder.experienceTextView.text = "${lawyer.yearsOfExperience} years of experience"
            holder.feeTextView.text = "Consultation Fee: ₱${lawyer.consultationFee}"
        }

        override fun getItemCount(): Int = items.size
    }
}
