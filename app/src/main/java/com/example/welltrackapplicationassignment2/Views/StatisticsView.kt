package com.example.welltrackapplicationassignment2.Views

interface StatisticsView {
    fun displayStatistics(
        favoriteWorkout: String?,
        enrolledCoursesCount: Int,
        averageWorkoutsPerDay: Double,
        totalWorkoutsCompleted: Int,
        totalTimeSpent: Int,
        totalCaloriesBurned: Int
    )
    fun showToast(message: String)
}