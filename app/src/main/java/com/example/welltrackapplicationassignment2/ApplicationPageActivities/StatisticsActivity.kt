package com.example.welltrackapplicationassignment2.SupplementalActivities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.welltrackapplicationassignment2.Models.MainModel
import com.example.welltrackapplicationassignment2.Presenter.StatisticsPresenter
import com.example.welltrackapplicationassignment2.R
import com.example.welltrackapplicationassignment2.Views.StatisticsView
import com.example.welltrackapplicationassignment2.activityClasses.MainActivity
import com.example.welltrackapplicationassignment2.databinding.ActivityStatisticsBinding
import com.example.welltrackapplicationassignment2.datavaseInfo

class StatisticsActivity : AppCompatActivity(), StatisticsView {

    private lateinit var binding: ActivityStatisticsBinding
    private lateinit var database: datavaseInfo
    private lateinit var presenter: StatisticsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = datavaseInfo(this)
        presenter = StatisticsPresenter(this, MainModel(database))

        setupBottomNavigation()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()

        // Update the selected state for navigation buttons manually
        updateSelectedNavigationButton(R.id.navigation_statistics)
    }

    private fun updateSelectedNavigationButton(selectedId: Int) {
        val navigationButtons = listOf(
            binding.custombottomnavigation.navigationHome,
            binding.custombottomnavigation.navigationStore,
            binding.custombottomnavigation.navigationStatistics,
            binding.custombottomnavigation.navigationSettings
        )

        // Update the state of each button
        navigationButtons.forEach { button ->
            val isSelected = button.id == selectedId
            button.isSelected = isSelected // You can use `isSelected` to toggle state-based styles
            button.alpha = if (isSelected) 1.0f else 0.5f // Example: adjust alpha for visual feedback
        }
    }


    override fun displayStatistics(
        favoriteWorkout: String?,
        enrolledCoursesCount: Int,
        averageWorkoutsPerDay: Double,
        totalWorkoutsCompleted: Int,
        totalTimeSpent: Int,
        totalCaloriesBurned: Int
    ) {
        binding.textFavoriteWorkoutName.text = favoriteWorkout ?: "No favorite workout"
        binding.textTotalEnrolledCoursesCount.text = enrolledCoursesCount.toString()
        binding.textAverageWorkoutsPerDay.text = String.format("%.2f", averageWorkoutsPerDay)
        binding.textTotalWorkoutsCompleted.text = totalWorkoutsCompleted.toString()
        binding.textTotalTimeSpentOnWorkouts.text = totalTimeSpent.toString()
        binding.textTotalCaloriesBurned.text = totalCaloriesBurned.toString()
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun setupBottomNavigation() {
        val navigationHome = binding.custombottomnavigation.navigationHome.findViewById<ImageButton>(R.id.navigation_home)
        val navigationStore = binding.custombottomnavigation.navigationStore.findViewById<ImageButton>(R.id.navigation_store)
        val navigationStatistics = binding.custombottomnavigation.navigationStatistics.findViewById<ImageButton>(R.id.navigation_statistics)
        val navigationSettings = binding.custombottomnavigation.navigationSettings.findViewById<ImageButton>(R.id.navigation_settings)

        navigationHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        navigationStore.setOnClickListener {
            val intent = Intent(this, StoreActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        navigationStatistics.setOnClickListener {
            // Already on this page, no action needed
        }

        navigationSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivityForResult(intent, MainActivity.SETTINGS_REQUEST_CODE)
            overridePendingTransition(0, 0)
        }
    }
}