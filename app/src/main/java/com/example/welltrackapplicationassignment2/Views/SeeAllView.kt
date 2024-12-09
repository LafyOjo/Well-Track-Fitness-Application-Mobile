package com.example.welltrackapplicationassignment2.Views

import com.example.welltrackapplicationassignment2.StretchProgram

interface SeeAllView {
    fun displayEnrolledCourses(courses: List<StretchProgram>)
    fun showToast(message: String)
}
