package com.example.welltrackapplicationassignment2.Presenter

import com.example.welltrackapplicationassignment2.Models.MainModel
import com.example.welltrackapplicationassignment2.Views.MainView

class MainPresenter(private val view: MainView, val model: MainModel) {

    var isPremiumUser: Boolean = false

    fun onResume() {
        val userProfile = model.getUserProfile()
        isPremiumUser = userProfile.premiumStatus == 1
        view.displayUserGreeting(userProfile.name)
        fetchAndDisplayWorkouts()
    }

    private fun fetchAndDisplayWorkouts() {
        if (isPremiumUser) model.unlockAllPremiumWorkouts()

        // Enrolled Courses
        val enrolledCourses = model.getEnrolledPrograms().take(10)
        view.displayEnrolledCourses(enrolledCourses)

        // Unenrolled Courses - Limit to 4
        val unenrolledCourses = model.getUnenrolledWorkouts().take(4)
        view.displayUnenrolledCourses(unenrolledCourses)

        // Workout Bundles
        val workoutBundles = model.getAllWorkoutBundles() ?: emptyList()
        view.displayWorkoutBundles(workoutBundles)
    }

    fun onWorkoutSelected(workoutId: Int) {
        val workout = model.getStretchProgramById(workoutId)
        if (workout != null) {
            if (workout.isPremium && !isPremiumUser) {
                view.showToast("This workout is for premium users only.")
            } else {
                view.navigateToWorkoutDetails(workoutId)
            }
        } else {
            view.showToast("Workout not found.")
        }
    }

    fun onBundleSelected(bundleId: Int) {
        view.navigateToBundleDetails(bundleId)
    }


}