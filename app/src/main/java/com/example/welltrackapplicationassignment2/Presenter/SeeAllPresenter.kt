package com.example.welltrackapplicationassignment2.Presenter

import com.example.welltrackapplicationassignment2.Models.MainModel
import com.example.welltrackapplicationassignment2.Views.SeeAllView

class SeeAllPresenter(
    private val view: SeeAllView,
    private val model: MainModel
) {

    fun fetchEnrolledCourses() {
        val enrolledCourses = model.getEnrolledPrograms()
        if (enrolledCourses.isNotEmpty()) {
            view.displayEnrolledCourses(enrolledCourses)
        } else {
            view.showToast("No enrolled courses available")
        }
    }
}
