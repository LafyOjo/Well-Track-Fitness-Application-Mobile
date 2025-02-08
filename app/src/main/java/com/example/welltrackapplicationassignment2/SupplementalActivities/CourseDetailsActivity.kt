package com.example.welltrackapplicationassignment2.SupplementalActivities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.welltrackapplicationassignment2.Utils.Course
import com.example.welltrackapplicationassignment2.R

class CourseDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_details)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerCourseList)

        // Simulate a list of courses
        val courses = intent.getStringExtra("COURSE_LIST")?.split(", ") ?: listOf("No courses")

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        val courseList = courses.map { Course(it, "Title for $it", "Description for $it") }
        recyclerView.adapter = CourseAdapter(courseList) { course ->
            // Handle item click
            Toast.makeText(this, "Clicked on: ${course.title}", Toast.LENGTH_SHORT).show()
        }
    }

    class CourseAdapter(
        private val courses: List<Course>, // Change to use a `Course` data class
        private val onItemClick: (Course) -> Unit // Click listener for better interaction
    ) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

        // ViewHolder class with more views if required
        class CourseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val courseTitle: TextView = view.findViewById(R.id.tvCourseTitle)
            val courseDescription: TextView = view.findViewById(R.id.tvCourseDescription)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_course, parent, false) // Use a custom layout
            return CourseViewHolder(view)
        }

        override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
            val course = courses[position]
            holder.courseTitle.text = course.title
            holder.courseDescription.text = course.description // Assuming `description` exists

            // Add click listener for the item
            holder.itemView.setOnClickListener {
                onItemClick(course)
            }
        }

        override fun getItemCount(): Int = courses.size
    }
}