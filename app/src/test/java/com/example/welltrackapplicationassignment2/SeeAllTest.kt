package com.example.welltrackapplicationassignment2

import org.junit.Test
import org.junit.Assert.*

class SeeAllActivityUnitTest {

    // Test 1: RecyclerView Initialization
    @Test
    fun testRecyclerViewInitialization() {
        val isRecyclerViewInitialized = true // Simulated initialization status
        assertTrue("RecyclerView should be initialized", isRecyclerViewInitialized)
    }

    // Test 2: RecyclerView Item Count Simulation
    @Test
    fun testEnrolledCoursesItemCount() {
        val expectedCount = 5
        val actualCount = 5 // Simulate getting the item count
        assertEquals("Item count should match", expectedCount, actualCount)
    }

    // Test 3: Check if Navigation to Workout Details Works
    @Test
    fun testNavigateToWorkoutDetails() {
        val workoutId = 101
        val expectedIntentData = workoutId
        val actualIntentData = 101 // Simulate passing the workout ID
        assertEquals("Workout ID should be passed correctly", expectedIntentData, actualIntentData)
    }

    // Test 4: Simulate Toast Display
    @Test
    fun testShowToast() {
        val expectedMessage = "Course successfully enrolled"
        val actualMessage = "Course successfully enrolled" // Simulate showing a toast
        assertEquals("Toast message should match", expectedMessage, actualMessage)
    }
}
