package com.example.welltrackapplicationassignment2.SupplementalActivities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.welltrackapplicationassignment2.ApplicationPageActivities.MainActivity
import com.example.welltrackapplicationassignment2.R
import com.example.welltrackapplicationassignment2.SupplementalActivities.CircularProgressBar
import com.example.welltrackapplicationassignment2.Utils.datavaseInfo
import com.example.welltrackapplicationassignment2.databinding.ActivityWorkoutBinding

class WorkoutActivity : AppCompatActivity() {

    private lateinit var backButton: ImageView
    private lateinit var workoutTitle: TextView
    private lateinit var workoutImage: ImageView
    private lateinit var timerText: TextView
    private lateinit var restartButton: Button
    private lateinit var pauseButton: Button
    private lateinit var skipButton: TextView
    private lateinit var circularProgressBar: CircularProgressBar
    private lateinit var database: datavaseInfo
    private lateinit var binding: ActivityWorkoutBinding

    private var isPaused = false
    private var workoutId: Int = -1
    private var timeRemaining: Long = 0L
    private var totalWorkoutTime: Long = 0L
    private var caloriesBurned: Int = 0
    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)

        // Initialize views
        backButton = findViewById(R.id.backButton)
        workoutTitle = findViewById(R.id.workoutTitle)
        workoutImage = findViewById(R.id.workoutImage)
        timerText = findViewById(R.id.timer)
        restartButton = findViewById(R.id.restartButton)
        pauseButton = findViewById(R.id.pauseButton)
        skipButton = findViewById(R.id.skipButton)
        circularProgressBar = findViewById(R.id.circularProgressBar)

        // Initialize the database
        database = datavaseInfo(this)

        // Get workout data from Intent
        workoutId = intent.getIntExtra("WORKOUT_ID", -1)
        val workout = database.getWorkoutById(workoutId)

        // Validate workout data
        if (workout != null) {
            totalWorkoutTime = workout.timeInSeconds * 1000L
            caloriesBurned = workout.caloriesBurned

            workoutTitle.text = workout.title
            val imageResId = resources.getIdentifier(workout.image, "drawable", packageName)
            if (imageResId != 0) {
                workoutImage.setImageResource(imageResId)
            }
            timerText.text = "Starts in: 5"
            circularProgressBar.setProgress(100f)

            // Start countdown before workout
            startCountdown(5000)
        } else {
            Toast.makeText(this, "Workout not found!", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Back button click listener
        backButton.setOnClickListener { finish() }

        // Restart workout
        restartButton.setOnClickListener {
            timer.cancel()
            startCountdown(5000)
            isPaused = false
            pauseButton.text = "Pause"
        }

        // Pause/Resume workout
        pauseButton.setOnClickListener {
            if (isPaused) {
                startWorkoutTimer(timeRemaining)
                pauseButton.text = "Pause"
            } else {
                timer.cancel()
                pauseButton.text = "Resume"
            }
            isPaused = !isPaused
        }

        // Skip workout
        skipButton.setOnClickListener { completeWorkout() }
    }

    private fun startCountdown(countdownTime: Long) {
        timer = object : CountDownTimer(countdownTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                timerText.text = "Starts in: $seconds"
                circularProgressBar.setProgress((millisUntilFinished / countdownTime.toFloat()) * 100)
            }

            override fun onFinish() {
                startWorkoutTimer(totalWorkoutTime)
            }
        }.start()
    }

    private fun startWorkoutTimer(workoutTime: Long) {
        timer = object : CountDownTimer(workoutTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeRemaining = millisUntilFinished
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = millisUntilFinished / 1000 % 60
                timerText.text = String.format("%02d:%02d", minutes, seconds)
                circularProgressBar.setProgress((millisUntilFinished / workoutTime.toFloat()) * 100)
            }

            override fun onFinish() {
                completeWorkout()
            }
        }.start()
    }

    private fun completeWorkout() {
        if (workoutId != -1) {
            database.incrementWorkoutStats(
                workoutId = workoutId,
                durationInSeconds = (totalWorkoutTime / 1000).toInt(),
                caloriesBurned = caloriesBurned
            )
            Toast.makeText(this, "Workout completed!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Error updating workout stats!", Toast.LENGTH_SHORT).show()
        }

        // Navigate to the main menu
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isPaused", isPaused)
        outState.putLong("timeRemaining", timeRemaining)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        isPaused = savedInstanceState.getBoolean("isPaused")
        timeRemaining = savedInstanceState.getLong("timeRemaining")

        if (!isPaused) {
            startWorkoutTimer(timeRemaining)
            pauseButton.text = "Pause"
        } else {
            pauseButton.text = "Resume"
        }
    }
}
