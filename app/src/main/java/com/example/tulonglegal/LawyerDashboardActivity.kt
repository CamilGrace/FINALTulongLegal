package com.example.tulonglegal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.tulonglegal.databinding.ClientDashboardBinding
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.example.tulonglegal.databinding.LawyerDashboardBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class LawyerDashboardActivity : AppCompatActivity() {
    private lateinit var binding: LawyerDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lawyer_dashboard)

        // Initialize View Binding
        binding = LawyerDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Find the MaterialCalendarView in the layout
        val calendarView: MaterialCalendarView = findViewById(R.id.calendarView)

        // Example: Highlight today's date
        calendarView.selectedDate = CalendarDay.today()

        // Set a listener for date selection
        calendarView.setOnDateChangedListener(OnDateSelectedListener { widget, date, selected ->
            // Handle date selection
            val selectedDate = "${date.year}-${date.month + 1}-${date.day}"
            println("Selected date: $selectedDate")
        })

        // Additional customization if needed
        calendarView.topbarVisible = true // Show/hide top bar
        calendarView.state().edit()
            .setMinimumDate(CalendarDay.from(2023, 1, 1)) // Set minimum date
            .setMaximumDate(CalendarDay.from(2025, 12, 31)) // Set maximum date
            .commit()

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
                    startActivity(Intent(this, LawyerProfileActivity::class.java))
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
                    startActivity(Intent(this, LawyerProfileActivity::class.java))
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
    }
}
