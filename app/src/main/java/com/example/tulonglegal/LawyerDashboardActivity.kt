package com.example.tulonglegal

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import java.text.SimpleDateFormat
import java.util.*

class LawyerDashboardActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var calendarView: CompactCalendarView
    private lateinit var calendarTitle: TextView
    private lateinit var collapseButton: Button
    private val scheduleList = mutableListOf<ScheduleItem>()
    private var calendarCollapsed = true // Initially collapsed

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lawyer_dashboard)

        // Initialize views
        drawerLayout = findViewById(R.id.drawer_layout)
        recyclerView = findViewById(R.id.recyclerView_schedule)
        calendarView = findViewById(R.id.compactCalendarView)
        calendarTitle = findViewById(R.id.calendarTitle)
        collapseButton = findViewById(R.id.collapseButton)

        // Set up RecyclerView
        setupRecyclerView()

        // Load sample schedules
        loadSampleSchedules()

        // Set up CompactCalendarView
        setupCalendar()

        // Handle menu button clicks
        findViewById<View>(R.id.img_menu).setOnClickListener {
            drawerLayout.open()
        }

        // Handle collapse/expand button clicks
        collapseButton.setOnClickListener {
            toggleCalendar()
        }

        // Initially collapse the calendar
        toggleCalendar() // Call toggleCalendar to collapse the calendar on startup
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ScheduleAdapter(scheduleList)
    }

    private fun loadSampleSchedules() {
        scheduleList.add(ScheduleItem("Consultation with Ms. Sandra Reyes", "8:30 AM - 9:30 AM"))
        scheduleList.add(ScheduleItem("Document prep for Mr. Victor Solis", "10:00 AM - 11:00 AM"))
        scheduleList.add(ScheduleItem("Review immigration papers for Mr. Ali", "1:00 PM - 2:00 PM"))
        scheduleList.add(ScheduleItem("Conference call with HR of TechStart Corp", "2:30 PM - 4:00 PM"))
        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun setupCalendar() {
        val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())

        calendarView.setListener(object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date) {
                // Handle day clicks if needed
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date) {
                calendarTitle.text = dateFormat.format(firstDayOfNewMonth)
            }
        })

        calendarView.addEvent(Event(R.color.colorAccent, Date().time, "Sample Event"))
        calendarTitle.text = dateFormat.format(Date())
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun toggleCalendar() {
        calendarCollapsed = !calendarCollapsed
        val params = calendarView.layoutParams as ViewGroup.MarginLayoutParams
        val newHeight = if (calendarCollapsed) {
            0 // Set height to 0 for collapsed state
        } else {
            ViewGroup.LayoutParams.WRAP_CONTENT // Reset to original height for expanded state
        }

        // Set visibility based on the collapsed state
        calendarView.visibility = if (calendarCollapsed) View.GONE else View.VISIBLE

        // Optionally animate the height change
        val animator = ObjectAnimator.ofInt(calendarView, "layoutParams.height", calendarView.height, newHeight)
        animator.addUpdateListener { valueAnimator ->
            val layoutParams = calendarView.layoutParams
            layoutParams.height = valueAnimator.animatedValue as Int
            calendarView.layoutParams = layoutParams
            calendarView.requestLayout()
        }
        animator.duration = 300 // Duration of the animation
        animator.start()
    }

    // Data class and adapter
    data class ScheduleItem(val title: String, val time: String)

    inner class ScheduleAdapter(private val scheduleList: List<ScheduleItem>) :
        RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

        inner class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val titleTextView: TextView = itemView.findViewById(R.id.tv_schedule_title) // Ensure this ID matches
            private val timeTextView: TextView = itemView.findViewById(R.id.tv_schedule_time) // Ensure this ID matches

            fun bind(scheduleItem: ScheduleItem) {
                titleTextView.text = scheduleItem.title
                timeTextView.text = scheduleItem.time
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_schedule, parent, false)
            return ScheduleViewHolder(view)
        }

        override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
            holder.bind(scheduleList[position])
        }

        override fun getItemCount(): Int {
            return scheduleList.size
        }
    }
}