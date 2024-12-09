package com.example.welltrackapplicationassignment2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.welltrackapplicationassignment2.StretchProgram
import com.example.welltrackapplicationassignment2.databinding.ItemCourseCardBinding

class CourseAdapter(
    private val courses: List<StretchProgram>, // Pass the list of courses here
    private val onCourseClick: (StretchProgram) -> Unit // Handle clicks on courses
) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    // ViewHolder class with View Binding
    class CourseViewHolder(private val binding: ItemCourseCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(course: StretchProgram, onCourseClick: (StretchProgram) -> Unit) {
            // Load course image from drawable
            val context = binding.root.context
            val resourceId = context.resources.getIdentifier(course.image, "drawable", context.packageName)
            binding.imageCourse.setImageResource(resourceId)

            // Bind course title and details
            binding.textCourseTitle.text = course.title
            binding.textCourseDescription.text = course.description1
            binding.textCourseDetails.text = "${course.caloriesBurned} cal • ${course.timeInSeconds / 60} min"

            // Set click listener to handle course selection
            binding.root.setOnClickListener { onCourseClick(course) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        // Inflate the layout using View Binding
        val binding = ItemCourseCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        // Bind the current course to the ViewHolder
        holder.bind(courses[position], onCourseClick)
    }

    override fun getItemCount(): Int = courses.size
}
