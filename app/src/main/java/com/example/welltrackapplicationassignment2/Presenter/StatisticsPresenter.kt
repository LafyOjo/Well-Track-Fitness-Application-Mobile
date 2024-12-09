package com.example.welltrackapplicationassignment2.Presenter

import com.example.welltrackapplicationassignment2.Models.MainModel
import com.example.welltrackapplicationassignment2.Views.StatisticsView

class StatisticsPresenter(private val view: StatisticsView, private val model: MainModel) {

    fun onResume() {
        fetchAndDisplayStatistics()
    }

    private fun fetchAndDisplayStatistics() {
        try {
            val favoriteWorkout = model.getFavoriteWorkout()?.title
            val enrolledCoursesCount = model.getTotalEnrolledCourses()
            val averageWorkoutsPerDay = model.getAverageWorkoutsPerDay()
            val totalWorkoutsCompleted = model.getTotalWorkoutsCompleted()
            val totalTimeSpent = model.getTotalTimeSpentOnWorkouts()
            val totalCaloriesBurned = model.getTotalCaloriesBurned()

            view.displayStatistics(
                favoriteWorkout,
                enrolledCoursesCount,
                averageWorkoutsPerDay,
                totalWorkoutsCompleted,
                totalTimeSpent,
                totalCaloriesBurned
            )
        } catch (e: Exception) {
            view.showToast("Error loading statistics. Please try again.")
        }
    }
}