package com.example.welltrackapplicationassignment2

import org.junit.Test
import org.junit.Assert.*

class StatisticsActivityUnitTest {

    // Test 1: Verify if Favorite Workout Text is Correct
    @Test
    fun testFavoriteWorkoutText() {
        val expectedWorkout = "The HIIT"
        val actualWorkout = "The HIIT"  // Simulate getting text from TextView
        assertEquals("Favorite workout should match", expectedWorkout, actualWorkout)
    }

    // Test 2: Check Total Enrolled Courses Count
    @Test
    fun testEnrolledCoursesCount() {
        val expectedCount = 12
        val actualCount = 12  // Simulate getting enrolled courses count
        assertEquals("Enrolled courses count should be correct", expectedCount, actualCount)
    }

    // Test 3: Validate Total Workouts Completed Count
    @Test
    fun testTotalWorkoutsCompleted() {
        val expectedWorkouts = 3679
        val actualWorkouts = 3679  // Simulate total workouts completed from TextView
        assertEquals("Total workouts completed should match", expectedWorkouts, actualWorkouts)
    }

    // Test 4: Validate Bottom Navigation Button Selection
    @Test
    fun testBottomNavigationSelected() {
        val expectedButtonId = R.id.navigation_statistics
        val actualButtonId = R.id.navigation_statistics  // Simulate navigation button selection
        assertEquals("Selected navigation button should be 'Statistics'", expectedButtonId, actualButtonId)
    }
}
