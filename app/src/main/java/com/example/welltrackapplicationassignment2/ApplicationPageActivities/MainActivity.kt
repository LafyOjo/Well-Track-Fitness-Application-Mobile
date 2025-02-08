package com.example.welltrackapplicationassignment2.ApplicationPageActivities

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.welltrackapplicationassignment2.Models.MainModel
import com.example.welltrackapplicationassignment2.Presenter.MainPresenter
import com.example.welltrackapplicationassignment2.R
import com.example.welltrackapplicationassignment2.Utils.StretchProgram
import com.example.welltrackapplicationassignment2.SupplementalActivities.BundleDetailsActivity
import com.example.welltrackapplicationassignment2.SupplementalActivities.WorkoutDetailsActivity
import com.example.welltrackapplicationassignment2.Views.MainView
import com.example.welltrackapplicationassignment2.Utils.WorkoutBundle
import com.example.welltrackapplicationassignment2.adapters.CourseAdapter
import com.example.welltrackapplicationassignment2.adapters.WorkoutBundleAdapter
import com.example.welltrackapplicationassignment2.databinding.ActivityMainBinding
import com.example.welltrackapplicationassignment2.Utils.datavaseInfo
import java.io.File

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

    private val REQUEST_CODE_READ_EXTERNAL_STORAGE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        databaseInfo = datavaseInfo(this)
        presenter = MainPresenter(this, MainModel(databaseInfo))

        setupBottomNavigation()
        setupRecyclerViews()
        setupListeners()

        sharedPreferences = getSharedPreferences("user_settings", MODE_PRIVATE)

        checkAndRequestPermissions()
        //loadProfileImage()
    }

    private fun checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                    data = Uri.parse("package:$packageName")
                }
                startActivity(intent)
            } else {
                loadProfileImage()
            }
        } else if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_READ_EXTERNAL_STORAGE)
        } else {
            loadProfileImage()
        }
    }

    // Method to open the store page
    fun openStorePage(view: View) {
        val intent = Intent(this, StoreActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadProfileImage()
        } else {
            Toast.makeText(this, "Permission denied to read external storage", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveToMediaStore(uri: Uri) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "profile_image.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        imageUri?.let {
            val outputStream = contentResolver.openOutputStream(it)
            val inputStream = contentResolver.openInputStream(uri)
            inputStream?.copyTo(outputStream!!)
            binding.profileImageView.setImageURI(it)
        }
    }



    private fun loadProfileImage() {
        val sharedPreferences = getSharedPreferences("user_settings", MODE_PRIVATE)
        val filePath = sharedPreferences.getString("profile_image_uri", null)

        if (!filePath.isNullOrEmpty()) {
            try {
                val file = File(filePath)
                if (file.exists()) {
                    binding.profileImageView.setImageURI(Uri.fromFile(file))
                } else {
                    binding.profileImageView.setImageResource(R.drawable.ic_profile) // Default image
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Failed to load profile image", Toast.LENGTH_SHORT).show()
            }
        } else {
            binding.profileImageView.setImageResource(R.drawable.ic_profile) // Default image
        }
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
        loadProfileImage()
    }

    private fun setupRecyclerViews() {
        binding.recyclerEnrolledCourses.layoutManager = LinearLayoutManager(this)
        binding.recyclerNotEnrolledCourses.layoutManager = LinearLayoutManager(this)
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
                startActivityForResult(intent, SETTINGS_REQUEST_CODE)
                overridePendingTransition(0, 0)
            }
    }

    private fun setupListeners() {
        binding.btnSeeAll.setOnClickListener {
            startActivity(Intent(this, SeeAllActivity::class.java))
        }
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

    override fun displayUnenrolledCourses(courses: List<StretchProgram>) {
        val adapter = CourseAdapter(courses) { selectedCourse ->
            // Navigate to workout details screen instead of showing a Toast
            navigateToWorkoutDetails(selectedCourse.id)
        }
        binding.recyclerNotEnrolledCourses.adapter = adapter
    }
}