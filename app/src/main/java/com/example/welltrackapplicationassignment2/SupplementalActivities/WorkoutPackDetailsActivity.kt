package com.example.welltrackapplicationassignment2.SupplementalActivities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.welltrackapplicationassignment2.StretchProgram
import com.example.welltrackapplicationassignment2.databinding.ActivityWorkoutPackDetailsBinding
import com.example.welltrackapplicationassignment2.adapters.CourseAdapter
import com.example.welltrackapplicationassignment2.datavaseInfo

class WorkoutPackDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutPackDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutPackDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get pack ID from intent
        val packId = intent.getIntExtra("PACK_ID", -1)

        if (packId != -1) {
            // Fetch pack details and associated workouts
            val database = datavaseInfo(this)
            val pack = database.getAllWorkoutBundles()?.find { it.id == packId }
            val workouts = database.getWorkoutsForBundle(packId)

            // Bind pack details
            pack?.let {
                binding.textPackTitle.text = it.title
                binding.textPackDescription.text = it.description
            }

            // Set up RecyclerView for workouts in the pack
            setupRecyclerView(workouts)
        } else {
            Log.e("WorkoutPackDetailsActivity", "Invalid PACK_ID received: $packId")
        }
    }

    private fun setupRecyclerView(workouts: List<StretchProgram>) {
        val adapter = CourseAdapter(workouts) { workout ->
            // Log the workout ID
            Log.d("WorkoutPackDetailsActivity", "Selected workout ID: ${workout.id}")

            // Handle click to navigate to course details
            val intent = Intent(this, WorkoutDetailsActivity::class.java)
            intent.putExtra("WORKOUT_ID", workout.id)
            startActivity(intent)
        }
        binding.recyclerPackWorkouts.layoutManager = LinearLayoutManager(this)
        binding.recyclerPackWorkouts.adapter = adapter
    }
}
