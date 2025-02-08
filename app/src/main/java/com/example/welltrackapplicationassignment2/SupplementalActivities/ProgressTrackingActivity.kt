package com.example.welltrackapplicationassignment2.SupplementalActivities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.welltrackapplicationassignment2.databinding.ActivityProgressTrackingBinding
import com.example.welltrackapplicationassignment2.Utils.datavaseInfo

class ProgressTrackingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProgressTrackingBinding
    private lateinit var database: datavaseInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProgressTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = datavaseInfo(this)

        try {
            // Fetch the user's favorite workout (highest number of completions)
            val favoriteWorkout = database.getAllStretchPrograms().maxByOrNull { it.workoutsCompleted }
            favoriteWorkout?.let {
                binding.textFavoriteWorkout.text = it.title
            }

            // Fetch total number of enrolled courses
            val totalEnrolledCourses = database.getAllCourses().size
            binding.textTotalEnrolledCourses.text = totalEnrolledCourses.toString()

            // Fetch overall statistics
            val averageWorkoutsPerDay = database.getAverageWorkoutsPerDay()
            val totalWorkoutsCompleted = database.getTotalWorkoutsCompleted()
            val totalTimeSpent = database.getTotalTimeSpentOnWorkouts()
            val totalCaloriesBurned = database.getTotalCaloriesBurned()

            // Set data to UI
            binding.textAverageWorkoutsPerDay.text = averageWorkoutsPerDay.toString()
            binding.textTotalWorkoutsCompleted.text = totalWorkoutsCompleted.toString()
            binding.textTotalTimeSpentOnWorkouts.text = totalTimeSpent.toString()
            binding.textTotalCaloriesBurned.text = totalCaloriesBurned.toString()

        } catch (e: Exception) {
            Log.e("ProgressTrackingActivity", "Error fetching progress tracking data", e)
        }
    }
}
