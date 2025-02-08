package com.example.welltrackapplicationassignment2.ApplicationPageActivities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.welltrackapplicationassignment2.Models.MainModel
import com.example.welltrackapplicationassignment2.Presenter.SeeAllPresenter
import com.example.welltrackapplicationassignment2.Utils.StretchProgram
import com.example.welltrackapplicationassignment2.SupplementalActivities.WorkoutDetailsActivity
import com.example.welltrackapplicationassignment2.Views.SeeAllView
import com.example.welltrackapplicationassignment2.adapters.CourseAdapter
import com.example.welltrackapplicationassignment2.databinding.ActivitySeeAllBinding
import com.example.welltrackapplicationassignment2.Utils.datavaseInfo

class SeeAllActivity : AppCompatActivity(), SeeAllView {

    private lateinit var binding: ActivitySeeAllBinding
    private lateinit var presenter: SeeAllPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeeAllBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val databaseInfo = datavaseInfo(this)
        presenter = SeeAllPresenter(this, MainModel(databaseInfo))

        setupRecyclerView()
        presenter.fetchEnrolledCourses()
    }

    private fun setupRecyclerView() {
        binding.recyclerEnrolledCourses.layoutManager = LinearLayoutManager(this)
    }

    override fun displayEnrolledCourses(courses: List<StretchProgram>) {
        val adapter = CourseAdapter(courses) { selectedCourse ->
            navigateToWorkoutDetails(selectedCourse.id)
        }
        binding.recyclerEnrolledCourses.adapter = adapter
    }

    fun navigateToWorkoutDetails(workoutId: Int) {
        val intent = Intent(this, WorkoutDetailsActivity::class.java).apply {
            putExtra(MainActivity.EXTRA_WORKOUT_ID, workoutId)
        }
        startActivity(intent)
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
