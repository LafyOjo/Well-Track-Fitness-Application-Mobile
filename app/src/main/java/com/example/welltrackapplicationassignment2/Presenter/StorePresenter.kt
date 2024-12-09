package com.example.welltrackapplicationassignment2.Presenter

import com.example.welltrackapplicationassignment2.Models.StoreModel
import com.example.welltrackapplicationassignment2.Views.StoreView

class StorePresenter(private val view: StoreView, private val model: StoreModel) {

    fun onResume() {
        fetchAndDisplayWorkouts()
    }

    private fun fetchAndDisplayWorkouts() {
        val workouts = model.getUnenrolledWorkouts()
        if (workouts.isEmpty()) {
            view.showEmptyState()
        } else {
            view.hideEmptyState()
            view.displayUnenrolledWorkouts(workouts)
        }
    }
}