package com.example.welltrackapplicationassignment2.Views

import com.example.welltrackapplicationassignment2.Utils.StretchProgram
import com.example.welltrackapplicationassignment2.Utils.WorkoutBundle

interface MainView {
    fun displayUserGreeting(userName: String)
    fun displayEnrolledCourses(courses: List<StretchProgram>)
    fun displayUnenrolledCourses(courses: List<StretchProgram>)
    fun displayWorkoutBundles(bundles: List<WorkoutBundle>)
    fun navigateToWorkoutDetails(workoutId: Int)
    fun navigateToBundleDetails(bundleId: Int)
    fun showToast(message: String)
}
