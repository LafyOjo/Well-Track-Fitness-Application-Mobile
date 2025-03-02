package com.example.welltrackapplicationassignment2.Presenter

import com.example.welltrackapplicationassignment2.Models.SettingsModel
import com.example.welltrackapplicationassignment2.Views.SettingsView
import com.example.welltrackapplicationassignment2.Utils.UserProfile

class SettingsPresenter(private val view: SettingsView, private val model: SettingsModel) {

    fun onResume() {
        fetchAndDisplayUserProfile()
    }

    private fun fetchAndDisplayUserProfile() {
        try {
            val profile = model.getUserProfile()
            view.displayUserProfile(profile)
        } catch (e: Exception) {
            view.showUpdateFailure()
        }
    }

    fun saveUserProfile(profile: UserProfile) {
        try {
            model.saveUserProfile(profile)
            view.showUpdateSuccess()
        } catch (e: Exception) {
            view.showUpdateFailure()
        }
    }

    fun updatePremiumStatus(isPremium: Boolean) {
        val profile = model.getUserProfile().copy(premiumStatus = if (isPremium) 1 else 0)
        model.saveUserProfile(profile)
        if (isPremium) {
            model.unlockAllPremiumWorkouts()
        } else {
            model.lockAllPremiumWorkouts()
        }
        view.updatePremiumStatusText(isPremium)
    }
}