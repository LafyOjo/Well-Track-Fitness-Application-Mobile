package com.example.welltrackapplicationassignment2.Models

import com.example.welltrackapplicationassignment2.datavaseInfo
import com.example.welltrackapplicationassignment2.StretchProgram

class StoreModel(private val database: datavaseInfo) {

    fun getUnenrolledWorkouts(): List<StretchProgram> {
        return database.getUnenrolledWorkouts()
    }
}