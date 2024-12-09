package com.example.welltrackapplicationassignment2.SupplementalActivities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.welltrackapplicationassignment2.StretchProgram
import com.example.welltrackapplicationassignment2.adapters.CourseAdapter
import com.example.welltrackapplicationassignment2.databinding.ActivityBundleDetailsBinding
import com.example.welltrackapplicationassignment2.datavaseInfo

class BundleDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBundleDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBundleDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the bundle ID from intent
        val bundleId = intent.getIntExtra("BUNDLE_ID", -1)

        if (bundleId != -1) {
            // Load bundle details and associated workouts
            val database = datavaseInfo(this)
            val bundle = (database.getAllWorkoutBundles() ?: emptyList()).find { it.id == bundleId }
            val workouts = database.getWorkoutsForBundle(bundleId)

            // Display bundle details
            bundle?.let {
                binding.textBundleTitle.text = it.title
                binding.textBundleDescription.text = it.description
            }

            // Setup RecyclerView for workouts
            setupWorkoutsRecyclerView(workouts)
        }
    }

    private fun setupWorkoutsRecyclerView(workouts: List<StretchProgram>) {
        val adapter = CourseAdapter(workouts) { selectedWorkout ->
            // Handle workout click
            val intent = Intent(this, CourseDetailsActivity::class.java)
            intent.putExtra("COURSE_ID", selectedWorkout.id)
            startActivity(intent)
        }
        binding.recyclerWorkouts.layoutManager = LinearLayoutManager(this)
        binding.recyclerWorkouts.adapter = adapter
    }
}
