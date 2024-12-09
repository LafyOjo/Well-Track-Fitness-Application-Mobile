package com.example.welltrackapplicationassignment2.Views

import com.example.welltrackapplicationassignment2.StretchProgram

interface StoreView {
    fun displayUnenrolledWorkouts(workouts: List<StretchProgram>)
    fun showEmptyState()
    fun hideEmptyState()
}