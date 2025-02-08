package com.example.welltrackapplicationassignment2.Views

import com.example.welltrackapplicationassignment2.Utils.StretchProgram

interface SeeAllView {
    fun displayEnrolledCourses(courses: List<StretchProgram>)
    fun showToast(message: String)
}
