package com.example.tulonglegal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth

class MatchesFoundActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var lawyerList: List<Lawyer> // To hold the fetched lawyers data
    private lateinit var selectedLegalCategory: String // Category passed from LawyerMatchingActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.matches_found)

        // Get the selected legal category from the Intent
        selectedLegalCategory = intent.getStringExtra("selectedLegalCategory") ?: ""

        firestore = FirebaseFirestore.getInstance()

        // Initialize RecyclerView for the carousel
        val recyclerView = findViewById<RecyclerView>(R.id.carouselRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Attach PagerSnapHelper for centering the cards (both start and end)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        // Fetch the lawyers data from Firestore
        fetchLawyersData(recyclerView)
    }

    // Fetch Lawyers data from Firestore
    private fun fetchLawyersData(recyclerView: RecyclerView) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            firestore.collection("lawyers")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    lawyerList = querySnapshot.documents.mapNotNull { document ->
                        document.toObject(Lawyer::class.java)
                    }

                    // Filter the lawyers based on the selected legal category
                    val filteredLawyers = lawyerList.filter { lawyer ->
                        lawyer.legalspecialization.contains(selectedLegalCategory, ignoreCase = true)
                    }

                    // If no matches found, display "No Matches Found"
                    val noMatchesTextView = findViewById<TextView>(R.id.textNoMatchesFound)
                    if (filteredLawyers.isEmpty()) {
                        noMatchesTextView.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE // Hide RecyclerView
                    } else {
                        noMatchesTextView.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        // Set up the carousel adapter with the filtered data
                        recyclerView.adapter = CarouselAdapter(filteredLawyers)
                    }

                    // Programmatically scroll to the first item to make sure it's centered initially
                    recyclerView.post {
                        recyclerView.scrollToPosition(0)
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error fetching data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // Data class for Lawyer (matches Firestore fields)
    data class Lawyer(
        val fullName: String = "",
        val gender: String = "",
        val legalspecialization: String = "",
        val yearsofexp: Int = 0,
        val consultationfee: Double = 0.0 // In pesos
    )

    // Carousel Adapter Implementation
    class CarouselAdapter(private val items: List<Lawyer>) : RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

        // ViewHolder class for the adapter
        class CarouselViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val nameTextView: TextView = itemView.findViewById(R.id.textFullname)
            val genderTextView: TextView = itemView.findViewById(R.id.textGender)
            val lawCategoryTextView: TextView = itemView.findViewById(R.id.userLegal)
            val experienceTextView: TextView = itemView.findViewById(R.id.userYearsofExp)
            val feeTextView: TextView = itemView.findViewById(R.id.userConsultationFee)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.carousel_item, parent, false)
            return CarouselViewHolder(view)
        }

        override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
            val lawyer = items[position]
            holder.nameTextView.text = "Atty. ${lawyer.fullName}"
            holder.genderTextView.text = lawyer.gender
            holder.lawCategoryTextView.text = lawyer.legalspecialization
            holder.experienceTextView.text = "${lawyer.yearsofexp} years of experience"
            holder.feeTextView.text = "${lawyer.consultationfee} pesos"
        }

        override fun getItemCount(): Int = items.size
    }
}
