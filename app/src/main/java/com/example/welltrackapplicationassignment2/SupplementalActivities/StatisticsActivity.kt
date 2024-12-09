//package com.example.welltrackapplicationassignment2.SupplementalActivities
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.welltrackapplicationassignment2.R
//import com.example.welltrackapplicationassignment2.activityClasses.MainActivity
//import com.example.welltrackapplicationassignment2.activityClasses.MainActivity.Companion.SETTINGS_REQUEST_CODE
//import com.example.welltrackapplicationassignment2.databinding.ActivityStatisticsBinding
//import com.example.welltrackapplicationassignment2.datavaseInfo
//
//class StatisticsActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityStatisticsBinding
//    private lateinit var database: datavaseInfo
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityStatisticsBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        database = datavaseInfo(this)
//
//        setupBottomNavigation()
//
//        try {
//            Log.d("StatisticsActivity", "Fetching favorite workout...")
//            val favoriteWorkout = database.getFavoriteWorkout()
//            Log.d("StatisticsActivity", "Favorite Workout: $favoriteWorkout")
//
//            Log.d("StatisticsActivity", "Fetching total enrolled courses count...")
//            val enrolledCoursesCount = database.getTotalEnrolledCourses()
//            Log.d("StatisticsActivity", "Total Enrolled Courses: $enrolledCoursesCount")
//
//            Log.d("StatisticsActivity", "Fetching average workouts per day...")
//            val averageWorkoutsPerDay = database.getAverageWorkoutsPerDay()
//            Log.d("StatisticsActivity", "Average Workouts Per Day: $averageWorkoutsPerDay")
//
//            Log.d("StatisticsActivity", "Fetching total workouts completed...")
//            val totalWorkoutsCompleted = database.getTotalWorkoutsCompleted()
//            Log.d("StatisticsActivity", "Total Workouts Completed: $totalWorkoutsCompleted")
//
//            Log.d("StatisticsActivity", "Fetching total time spent on workouts...")
//            val totalTimeSpent = database.getTotalTimeSpentOnWorkouts()
//            Log.d("StatisticsActivity", "Total Time Spent on Workouts: $totalTimeSpent")
//
//            Log.d("StatisticsActivity", "Fetching total calories burned...")
//            val totalCaloriesBurned = database.getTotalCaloriesBurned()
//            Log.d("StatisticsActivity", "Total Calories Burned: $totalCaloriesBurned")
//
//            // Check if the values are 0 or null and update UI accordingly
//            binding.textFavoriteWorkoutName.text = favoriteWorkout?.title ?: "No favorite workout"
//            binding.textTotalEnrolledCoursesCount.text = enrolledCoursesCount.toString()
//            binding.textAverageWorkoutsPerDay.text = averageWorkoutsPerDay.toString()
//            binding.textTotalWorkoutsCompleted.text = totalWorkoutsCompleted.toString()
//            binding.textTotalTimeSpentOnWorkouts.text = totalTimeSpent.toString()
//            binding.textTotalCaloriesBurned.text = totalCaloriesBurned.toString()
//
//        } catch (e: Exception) {
//            Log.e("StatisticsActivity", "Error fetching data from database", e)
//            Toast.makeText(this, "Error loading statistics. Please try again.", Toast.LENGTH_LONG).show()
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        val bottomNavigationView = binding.bottomNavigationView
//        bottomNavigationView.selectedItemId = R.id.navigation_statistics
//    }
//
//    private fun setupBottomNavigation() {
//        val bottomNavigationView = binding.bottomNavigationView
//
//        // Set the selected item to Home
//        bottomNavigationView.selectedItemId = R.id.navigation_home
//
//        // Handle navigation item selection
//        bottomNavigationView.setOnItemSelectedListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.navigation_home -> {
//                    if (bottomNavigationView.selectedItemId == R.id.navigation_home) {
//                        val intent = Intent(this, MainActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
//                        startActivity(intent)
//                        overridePendingTransition(0, 0) // No animation
//                    }
//                    true
//                }
//                R.id.navigation_store -> {
//                    if (bottomNavigationView.selectedItemId != R.id.navigation_store) {
//                        val intent = Intent(this, StoreActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
//                        startActivity(intent)
//                        overridePendingTransition(0, 0) // No animation
//                    }
//                    true
//                }
//                R.id.navigation_statistics -> true
//
//                R.id.navigation_settings -> {
//                    if (bottomNavigationView.selectedItemId != R.id.navigation_settings) {
//                        val intent = Intent(this, SettingsActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
//                        startActivityForResult(intent, SETTINGS_REQUEST_CODE)
//                        overridePendingTransition(0, 0)
//                    }
//                    true
//                }
//                else -> false
//            }
//        }
//    }
//}
