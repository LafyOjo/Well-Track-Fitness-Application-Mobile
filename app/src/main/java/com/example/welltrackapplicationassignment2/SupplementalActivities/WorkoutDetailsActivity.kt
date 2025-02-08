package com.example.welltrackapplicationassignment2.SupplementalActivities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.welltrackapplicationassignment2.R
import com.example.welltrackapplicationassignment2.Utils.StretchProgram
import com.example.welltrackapplicationassignment2.databinding.ActivityWorkoutDetailsBinding
import com.example.welltrackapplicationassignment2.Utils.datavaseInfo

class WorkoutDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutDetailsBinding
    private lateinit var databaseInfo: datavaseInfo
    private var isPremiumUser: Boolean = false
    private var workout: StretchProgram? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseInfo = datavaseInfo(this)

        // Fetch user profile to check premium status
        val userProfile = databaseInfo.getUserProfile()
        isPremiumUser = userProfile.premiumStatus == 1

        // Get the workout ID from intent
        val workoutId = intent.getIntExtra("WORKOUT_ID", -1)
        if (workoutId == -1) {
            Log.e("WorkoutDetailsActivity", "Invalid WORKOUT_ID received: $workoutId")
            Toast.makeText(this, "Invalid Workout ID", Toast.LENGTH_SHORT).show()
            finish() // End the activity gracefully
            return
        }

        // Fetch workout details
        workout = databaseInfo.getWorkoutById(workoutId)
        if (workout == null) {
            Toast.makeText(this, "Workout not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Configure the start workout button
        configureStartWorkoutButton()

        // Populate workout details on UI
        populateWorkoutDetails(workout!!)
    }

    private fun configureStartWorkoutButton() {
        if (workout!!.enrolledStatus == 0) {
            // Workout is not enrolled (locked), disable the button
            binding.startWorkoutButton.isEnabled = false
            binding.startWorkoutButton.alpha = 0.5f
            binding.startWorkoutButton.text = getString(R.string.premium_lock)
            binding.lockIcon.visibility = android.view.View.VISIBLE

            binding.startWorkoutButton.setOnClickListener {
                Toast.makeText(this, "This workout is locked. Enroll to access.", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Workout is enrolled, enable the button
            binding.startWorkoutButton.isEnabled = true
            binding.startWorkoutButton.alpha = 1.0f
            binding.startWorkoutButton.text = getString(R.string.start_workout)
            binding.lockIcon.visibility = android.view.View.GONE

            // Set click listener to start the workout
            binding.startWorkoutButton.setOnClickListener {
                val intent = Intent(this, WorkoutActivity::class.java)
                intent.putExtra("WORKOUT_ID", workout!!.id) // Pass the workout ID
                startActivity(intent)
            }
        }
    }



    private fun populateWorkoutDetails(workout: StretchProgram) {
        binding.apply {
            txtWorkoutTitle.text = workout.title

            // Set workout image
            val imageResId = resources.getIdentifier(workout.image, "drawable", packageName)
            if (imageResId != 0) {
                imgWorkout.setImageResource(imageResId)
            } else {
                Log.e("WorkoutDetailsActivity", "Image resource not found: ${workout.image}")
            }

            // Set other workout details
            txtWorkoutDuration.text = getString(R.string.time_format, workout.timeInSeconds)
            txtCalories.text = getString(R.string.calories_format, workout.caloriesBurned)
            txtCourseStatus.text = if (workout.isPremium) "Premium" else "Free"
            txtWorkoutDescription1.text = workout.description1
            txtWorkoutDescription2.text = workout.description2
            txtCompletedWorkouts.text = getString(R.string.completed_workouts_format, workout.workoutsCompleted)
        }
    }
}