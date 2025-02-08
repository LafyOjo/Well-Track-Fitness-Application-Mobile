package com.example.welltrackapplicationassignment2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.welltrackapplicationassignment2.R

data class Workout(
    val title: String,
    val description: String,
    val imageResId: Int // Example resource ID for the workout image
)

class EnrolledWorkoutsAdapter(private val workouts: List<Workout>) :
    RecyclerView.Adapter<EnrolledWorkoutsAdapter.WorkoutViewHolder>() {

    class WorkoutViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvWorkoutTitle)
        val description: TextView = view.findViewById(R.id.tvWorkoutDescription)
        val image: ImageView = view.findViewById(R.id.ivWorkoutImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_enrolled_workout, parent, false)
        return WorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workout = workouts[position]
        holder.title.text = workout.title
        holder.description.text = workout.description
        holder.image.setImageResource(workout.imageResId)
    }

    override fun getItemCount(): Int = workouts.size
}
