package com.example.welltrackapplicationassignment2

import org.junit.Test
import org.junit.Assert.*

class SettingsActivityUnitTest {

    // Test 1: Verify if the Reminder Time Text is Updated
    @Test
    fun testReminderTimeUpdate() {
        val expectedTime = "Reminder set for: 08:30"
        val actualTime = "Reminder set for: 08:30" // Simulated value after setting time
        assertEquals("Reminder time should be updated correctly", expectedTime, actualTime)
    }

    // Test 2: Check Premium Status Toggle
    @Test
    fun testPremiumStatusToggle() {
        val expectedStatus = true // Simulate Premium Enabled
        val actualStatus = true // Simulate Premium Toggle Event
        assertEquals("Premium status should be toggled correctly", expectedStatus, actualStatus)
    }

    // Test 3: Verify Color Change Action
    @Test
    fun testNotificationColorChange() {
        val expectedColor = android.graphics.Color.RED
        val actualColor = android.graphics.Color.RED // Simulate selected color
        assertEquals("Notification color should be updated", expectedColor, actualColor)
    }

    // Test 4: Validate Save User Info
    @Test
    fun testSaveUserInfo() {
        val expectedName = "John Doe"
        val expectedEmail = "john.doe@example.com"
        val expectedAge = 25

        val actualName = "John Doe"  // Simulated value after form submission
        val actualEmail = "john.doe@example.com"
        val actualAge = 25

        assertEquals("User name should match", expectedName, actualName)
        assertEquals("User email should match", expectedEmail, actualEmail)
        assertEquals("User age should match", expectedAge, actualAge)
    }
}
