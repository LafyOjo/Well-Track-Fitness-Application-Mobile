package com.example.welltrackapplicationassignment2.ApplicationPageActivities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.welltrackapplicationassignment2.Models.StoreModel
import com.example.welltrackapplicationassignment2.Presenter.StorePresenter
import com.example.welltrackapplicationassignment2.R
import com.example.welltrackapplicationassignment2.Utils.StretchProgram
import com.example.welltrackapplicationassignment2.SupplementalActivities.WorkoutPromoDescriptionActivity
import com.example.welltrackapplicationassignment2.Views.StoreView
import com.example.welltrackapplicationassignment2.adapters.CourseAdapter
import com.example.welltrackapplicationassignment2.databinding.ActivityStoreBinding
import com.example.welltrackapplicationassignment2.Utils.datavaseInfo

class StoreActivity : AppCompatActivity(), StoreView {

    private lateinit var binding: ActivityStoreBinding
    private lateinit var presenter: StorePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = datavaseInfo(this)
        presenter = StorePresenter(this, StoreModel(database))

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


    override fun displayUnenrolledWorkouts(workouts: List<StretchProgram>) {
        val adapter = CourseAdapter(workouts) { selectedCourse ->
            navigateToWorkoutPromo(selectedCourse)
        }
        binding.recyclerViewStore.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewStore.adapter = adapter
    }

    override fun showEmptyState() {
        binding.recyclerViewStore.adapter = null
        binding.txtEmptyState.text = getString(R.string.no_unenrolled_workouts)
        binding.txtEmptyState.visibility = View.VISIBLE
    }

    override fun hideEmptyState() {
        binding.txtEmptyState.visibility = View.GONE
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
            // Already on this page, no action needed
        }

        navigationStatistics.setOnClickListener {
            val intent = Intent(this, StatisticsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        navigationSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivityForResult(intent, MainActivity.SETTINGS_REQUEST_CODE)
            overridePendingTransition(0, 0)
        }
    }

    private fun navigateToWorkoutPromo(course: StretchProgram) {
        val intent = Intent(this, WorkoutPromoDescriptionActivity::class.java).apply {
            putExtra("WORKOUT_ID", course.id)
        }
        startActivity(intent)
    }
}