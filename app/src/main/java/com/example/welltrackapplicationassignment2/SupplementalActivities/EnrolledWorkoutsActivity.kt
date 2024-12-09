package com.example.welltrackapplicationassignment2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.welltrackapplicationassignment2.SupplementalActivities.WorkoutDetailsActivity
import com.example.welltrackapplicationassignment2.adapters.CourseAdapter
import com.example.welltrackapplicationassignment2.databinding.ActivityEnrolledWorkoutsBinding

class EnrolledWorkoutsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEnrolledWorkoutsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnrolledWorkoutsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val enrolledWorkouts: ArrayList<StretchProgram> = try {
            intent.getSerializableExtra("ENROLLED_WORKOUTS") as? ArrayList<StretchProgram> ?: arrayListOf()
        } catch (e: Exception) {
            arrayListOf()
        }

        if (enrolledWorkouts.isEmpty()) {
            binding.tvEmptyState.visibility = View.VISIBLE
            return
        } else {
            binding.tvEmptyState.visibility = View.GONE
        }

        binding.recyclerEnrolledWorkouts.layoutManager = LinearLayoutManager(this)
        val adapter = CourseAdapter(enrolledWorkouts) { selectedWorkout ->
            navigateToWorkoutDetails(selectedWorkout.id)
        }
        binding.recyclerEnrolledWorkouts.adapter = adapter
    }

    private fun navigateToWorkoutDetails(workoutId: Int) {
        if (workoutId <= 0) {
            Toast.makeText(this, "Invalid workout selected.", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this, WorkoutDetailsActivity::class.java)
        intent.putExtra("WORKOUT_ID", workoutId)
        startActivity(intent)
    }
}
