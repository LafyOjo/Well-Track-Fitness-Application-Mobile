package com.example.welltrackapplicationassignment2.activityClasses

import android.app.Activity
import android.app.AlertDialog
import android.content.SharedPreferences
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.welltrackapplicationassignment2.Models.MainModel
import com.example.welltrackapplicationassignment2.Presenter.MainPresenter
import com.example.welltrackapplicationassignment2.R
import com.example.welltrackapplicationassignment2.StretchProgram
import com.example.welltrackapplicationassignment2.SupplementalActivities.BundleDetailsActivity
import com.example.welltrackapplicationassignment2.SupplementalActivities.SettingsActivity
import com.example.welltrackapplicationassignment2.SupplementalActivities.StatisticsActivity
import com.example.welltrackapplicationassignment2.SupplementalActivities.StoreActivity
import com.example.welltrackapplicationassignment2.SupplementalActivities.WorkoutDetailsActivity
import com.example.welltrackapplicationassignment2.Views.MainView
import com.example.welltrackapplicationassignment2.WorkoutBundle
import com.example.welltrackapplicationassignment2.adapters.CourseAdapter
import com.example.welltrackapplicationassignment2.adapters.WorkoutBundleAdapter
import com.example.welltrackapplicationassignment2.databinding.ActivityMainBinding
import com.example.welltrackapplicationassignment2.datavaseInfo

class MainActivity : AppCompatActivity(), MainView {

    private lateinit var binding: ActivityMainBinding
    private lateinit var databaseInfo: datavaseInfo
    private lateinit var presenter: MainPresenter
    private lateinit var sharedPreferences: SharedPreferences


    companion object {
        const val EXTRA_WORKOUT_ID = "WORKOUT_ID"
        const val EXTRA_BUNDLE_ID = "BUNDLE_ID"
        const val SETTINGS_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseInfo = datavaseInfo(this)
        presenter = MainPresenter(this, MainModel(databaseInfo))

        setupBottomNavigation()

        binding.storeSection.setOnClickListener { openStorePage() }
        binding.btnSeeAll.setOnClickListener { openSeeAllPage() }

        setupRecyclerView()
        loadProfileImage()

        setupRecyclerViews()
        setupNavigation()
        setupListeners()

        sharedPreferences = getSharedPreferences("user_settings", MODE_PRIVATE)

        presenter.onResume()
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("Exit App")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { _, _ ->
                saveUserSettings()
                super.onBackPressed()
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        saveUserSettings()
    }

    private fun saveUserSettings() {
        val editor = sharedPreferences.edit()
        with(editor) {
            apply()  // This saves unsaved changes
        }
        showToast("User settings saved!")
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    private fun setupRecyclerView() {
        binding.recyclerNotEnrolledCourses.layoutManager = LinearLayoutManager(this)
        binding.recyclerEnrolledCourses.layoutManager = LinearLayoutManager(this)
    }

    private fun setupBottomNavigation() {
        binding.custombottombavigation.navigationHome.findViewById<ImageButton>(R.id.navigation_home)
            .setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivity(intent)
                overridePendingTransition(0, 0) // No animation
            }

        binding.custombottombavigation.navigationStore.findViewById<ImageButton>(R.id.navigation_store)
            .setOnClickListener {
                val intent = Intent(this, StoreActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivity(intent)
                overridePendingTransition(0, 0) // No animation
            }

        binding.custombottombavigation.navigationStatistics.findViewById<ImageButton>(R.id.navigation_statistics)
            .setOnClickListener {
                val intent = Intent(this, StatisticsActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivity(intent)
                overridePendingTransition(0, 0)
            }

        binding.custombottombavigation.navigationSettings.findViewById<ImageButton>(R.id.navigation_settings)
            .setOnClickListener {
                val intent = Intent(this, SettingsActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivityForResult(intent, MainActivity.SETTINGS_REQUEST_CODE)
                overridePendingTransition(0, 0)
            }
    }

    private fun loadProfileImage() {
        val sharedPreferences = getSharedPreferences("user_settings", MODE_PRIVATE)
        val uriString = sharedPreferences.getString("profile_image_uri", null)
        uriString?.let { binding.profileImageView.setImageURI(Uri.parse(it)) }
    }

    private fun openStorePage() {
        startActivity(Intent(this, StoreActivity::class.java))
    }

    private fun openSeeAllPage() {
        startActivity(Intent(this, SeeAllActivity::class.java))
    }

    override fun displayUserGreeting(userName: String) {
        binding.textUserGreeting.text = "Hello $userName"
    }

    override fun displayEnrolledCourses(courses: List<StretchProgram>) {
        val adapter = CourseAdapter(courses) { selectedCourse ->
            navigateToWorkoutDetails(selectedCourse.id)
        }
        binding.recyclerEnrolledCourses.adapter = adapter
    }

    override fun displayWorkoutBundles(bundles: List<WorkoutBundle>) {
        val adapter = WorkoutBundleAdapter(bundles) { selectedBundle ->
            if (!presenter.isPremiumUser && selectedBundle.isPremium) {
                showToast("This bundle is for premium users only.")
            } else {
                navigateToBundleDetails(selectedBundle.id)
            }
        }
        binding.recyclerWorkoutBundles.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerWorkoutBundles.adapter = adapter
    }

    override fun navigateToWorkoutDetails(workoutId: Int) {
        val intent = Intent(this, WorkoutDetailsActivity::class.java).apply {
            putExtra(EXTRA_WORKOUT_ID, workoutId)
        }
        startActivity(intent)
    }

    override fun navigateToBundleDetails(bundleId: Int) {
        val intent = Intent(this, BundleDetailsActivity::class.java).apply {
            putExtra(EXTRA_BUNDLE_ID, bundleId)
        }
        startActivity(intent)
    }

    fun displayMoreCourses(courses: List<StretchProgram>) {
        val adapter = CourseAdapter(courses) { selectedCourse ->
            showToast("Selected: ${selectedCourse.title}")
        }
        binding.recyclerNotEnrolledCourses.adapter = adapter
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupRecyclerViews() {
        binding.recyclerEnrolledCourses.layoutManager = LinearLayoutManager(this)
        binding.recyclerNotEnrolledCourses.layoutManager = LinearLayoutManager(this)
    }

    override fun displayUnenrolledCourses(courses: List<StretchProgram>) {
        val adapter = CourseAdapter(courses) { selectedCourse ->
            // Navigate to workout details screen instead of showing a Toast
            navigateToWorkoutDetails(selectedCourse.id)
        }
        binding.recyclerNotEnrolledCourses.adapter = adapter
    }

//    private fun setupRecyclerViews() {
//        binding.recyclerEnrolledCourses.layoutManager = LinearLayoutManager(this)
//        binding.recyclerNotEnrolledCourses.layoutManager = LinearLayoutManager(this)
//        binding.recyclerWorkoutBundles.layoutManager =
//            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//    }

    private fun setupNavigation() {
        binding.custombottombavigation.apply {
            navigationHome.setOnClickListener { }
            navigationStore.setOnClickListener {
                startActivity(Intent(this@MainActivity, StoreActivity::class.java))
            }
            navigationStatistics.setOnClickListener {
                startActivity(Intent(this@MainActivity, StatisticsActivity::class.java))
            }
            navigationSettings.setOnClickListener {
                startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
            }
        }
    }

    private fun setupListeners() {
        binding.btnSeeAll.setOnClickListener {
            startActivity(Intent(this, SeeAllActivity::class.java))
        }
    }
}