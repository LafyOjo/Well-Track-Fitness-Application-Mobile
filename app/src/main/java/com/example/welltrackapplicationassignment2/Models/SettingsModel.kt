package com.example.welltrackapplicationassignment2.Models

import com.example.welltrackapplicationassignment2.Utils.datavaseInfo
import com.example.welltrackapplicationassignment2.Utils.UserProfile

class SettingsModel(private val database: datavaseInfo) {

    fun getUserProfile(): UserProfile {
        return database.getUserProfile()
    }

    fun saveUserProfile(profile: UserProfile) {
        database.saveUserProfile(profile)
    }

    fun unlockAllPremiumWorkouts() {
        database.unlockAllPremiumWorkouts()
    }

    fun lockAllPremiumWorkouts() {
        database.lockAllPremiumWorkouts()
    }
}