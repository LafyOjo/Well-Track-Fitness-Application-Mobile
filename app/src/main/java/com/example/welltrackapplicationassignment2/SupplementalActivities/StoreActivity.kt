//package com.example.welltrackapplicationassignment2.SupplementalActivities
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.welltrackapplicationassignment2.R
//import com.example.welltrackapplicationassignment2.StretchProgram
//import com.example.welltrackapplicationassignment2.activityClasses.MainActivity
//import com.example.welltrackapplicationassignment2.activityClasses.MainActivity.Companion.SETTINGS_REQUEST_CODE
//import com.example.welltrackapplicationassignment2.adapters.CourseAdapter
//import com.example.welltrackapplicationassignment2.databinding.ActivityStoreBinding
//import com.example.welltrackapplicationassignment2.datavaseInfo
//
//class StoreActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityStoreBinding
//    private lateinit var databaseInfo: datavaseInfo
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityStoreBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        databaseInfo = datavaseInfo(this)
//
//        // Fetch unenrolled workouts
//        val unenrolledWorkouts = databaseInfo.getUnenrolledWorkouts()
//
//        // Set up RecyclerView
//        val adapter = CourseAdapter(unenrolledWorkouts) { selectedCourse ->
//            navigateToWorkoutPromo(selectedCourse)
//        }
//        binding.recyclerViewStore.layoutManager = LinearLayoutManager(this)
//        binding.recyclerViewStore.adapter = adapter
//
//        setupBottomNavigation()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        fetchAndUpdateWorkouts()
//
//        val bottomNavigationView = binding.bottomNavigationView
//        bottomNavigationView.selectedItemId = R.id.navigation_store
//        }
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
//                R.id.navigation_store -> true
//
//                R.id.navigation_statistics -> {
//                    if (bottomNavigationView.selectedItemId != R.id.navigation_statistics) {
//                        val intent = Intent(this, StatisticsActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
//                        startActivity(intent)
//                        overridePendingTransition(0, 0)
//                    }
//                    true
//                }
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
//
//
//    private fun fetchAndUpdateWorkouts() {
//        // Fetch updated list of unenrolled workouts from the database
//        val unenrolledWorkouts = databaseInfo.getUnenrolledWorkouts()
//
//        // Check if there are any updates or the list is empty
//        if (unenrolledWorkouts.isEmpty()) {
//            binding.recyclerViewStore.adapter = null
//            binding.txtEmptyState.text = getString(R.string.no_unenrolled_workouts) // Add a placeholder text
//            binding.txtEmptyState.visibility = View.VISIBLE
//        } else {
//            binding.txtEmptyState.visibility = View.GONE // Hide empty state if there are workouts
//
//            // Set up the RecyclerView with the updated data
//            val adapter = CourseAdapter(unenrolledWorkouts) { selectedCourse ->
//                navigateToWorkoutPromo(selectedCourse)
//            }
//            binding.recyclerViewStore.adapter = adapter
//        }
//    }
//
//
//    private fun navigateToWorkoutPromo(course: StretchProgram) {
//        val intent = Intent(this, WorkoutPromoDescriptionActivity::class.java).apply {
//            putExtra("WORKOUT_ID", course.id)
//        }
//        startActivity(intent)
//    }
//}
