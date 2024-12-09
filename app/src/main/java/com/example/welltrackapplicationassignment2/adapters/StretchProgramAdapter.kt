package com.example.welltrackapplicationassignment2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.welltrackapplicationassignment2.R
import com.example.welltrackapplicationassignment2.StretchProgram

class StretchProgramAdapter(
    private val courses: List<StretchProgram>,
    private val onCourseClick: (StretchProgram) -> Unit
) : RecyclerView.Adapter<StretchProgramAdapter.CourseViewHolder>() {

    // ViewHolder Class
    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageCourse: ImageView = itemView.findViewById(R.id.imageCourse)
        val textCourseTitle: TextView = itemView.findViewById(R.id.textCourseTitle)
        val textCourseDescription: TextView = itemView.findViewById(R.id.textCourseDescription)
        val textCourseDetails: TextView = itemView.findViewById(R.id.textCourseDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        // Inflate the layout for each item
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_course_card, parent, false)
        return CourseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courses[position]

        // Bind course title
        holder.textCourseTitle.text = course.title

        // Bind course short description (description1)
        holder.textCourseDescription.text = course.description1

        // Bind course details (calories burned and duration)
        holder.textCourseDetails.text = "${course.caloriesBurned} cal • ${course.timeInSeconds / 60} min"

        // Load course image
        val context = holder.itemView.context
        val resourceId = context.resources.getIdentifier(course.image, "drawable", context.packageName)
        if (resourceId != 0) {
            holder.imageCourse.setImageResource(resourceId)
        } else {
            holder.imageCourse.setImageResource(R.drawable.ic_default_image) // Placeholder image
        }

        // Set content description for accessibility
        holder.imageCourse.contentDescription = "${course.title} course image"

        // Handle item click
        holder.itemView.setOnClickListener { onCourseClick(course) }
    }

    override fun getItemCount(): Int = courses.size
}
