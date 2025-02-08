package com.example.welltrackapplicationassignment2.Models

import com.example.welltrackapplicationassignment2.Utils.StretchProgram
import com.example.welltrackapplicationassignment2.Utils.datavaseInfo

class MainModel(private val databaseInfo: datavaseInfo) {

    fun getUserProfile() = databaseInfo.getUserProfile()

    fun getAllStretchPrograms() = databaseInfo.getAllStretchPrograms()

    fun getAllWorkoutBundles() = databaseInfo.getAllWorkoutBundles()

    fun unlockAllPremiumWorkouts() = databaseInfo.unlockAllPremiumWorkouts()

    fun getMoreCourses() = databaseInfo.getMoreCourses()

    fun getEnrolledPrograms() = databaseInfo.getEnrolledPrograms()

    fun getUnenrolledWorkouts() = databaseInfo.getUnenrolledWorkouts()

    fun getWorkoutBundles() = databaseInfo.getWorkoutBundles()
    fun getStretchProgramById(workoutId: Int) = databaseInfo.getStretchProgramById(workoutId)
    fun getWorkoutById(workoutId: Int) = databaseInfo.getWorkoutById(workoutId)
    fun getWorkoutsForBundle(bundleId: Int) = databaseInfo.getWorkoutsForBundle(bundleId)
    fun getAllCourses() = databaseInfo.getAllCourses()


    // Should be statistics model but oh well
    fun getFavoriteWorkout(): StretchProgram? {
        return databaseInfo.getFavoriteWorkout()
    }

    fun getTotalEnrolledCourses(): Int {
        return databaseInfo.getTotalEnrolledCourses()
    }

    fun getAverageWorkoutsPerDay(): Double {
        return databaseInfo.getAverageWorkoutsPerDay()
    }

    fun getTotalWorkoutsCompleted(): Int {
        return databaseInfo.getTotalWorkoutsCompleted()
    }

    fun getTotalTimeSpentOnWorkouts(): Int {
        return databaseInfo.getTotalTimeSpentOnWorkouts()
    }

    fun getTotalCaloriesBurned(): Int {
        return databaseInfo.getTotalCaloriesBurned()
    }
}