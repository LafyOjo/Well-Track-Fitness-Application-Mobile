package com.example.welltrackapplicationassignment2.Views

import com.example.welltrackapplicationassignment2.Utils.UserProfile

interface SettingsView {
    fun displayUserProfile(profile: UserProfile)
    fun showUpdateSuccess()
    fun showUpdateFailure()
    fun updatePremiumStatusText(isPremium: Boolean)
//    fun scheduleReminder(triggerTime: Long)
}