package com.example.welltrackapplicationassignment2.SupplementalActivities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.welltrackapplicationassignment2.databinding.ActivityWorkoutPromoBinding
import com.example.welltrackapplicationassignment2.Utils.datavaseInfo

class WorkoutPromoDescriptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutPromoBinding
    private lateinit var databaseInfo: datavaseInfo
    private var workoutId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutPromoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseInfo = datavaseInfo(this)

        workoutId = intent.getIntExtra("WORKOUT_ID", 0)
        val workout = databaseInfo.getStretchProgramById(workoutId)

        workout?.let {
            binding.txtTitle.text = it.title
            binding.txtDescription.text = it.description1
            binding.btnEnroll.setOnClickListener { _ ->
                if (it.isPremium && !isPremiumUser()) {
                    Toast.makeText(this, "Upgrade to Premium to enroll!", Toast.LENGTH_SHORT).show()
                } else {
                    databaseInfo.enrollWorkout(it.id)
                    Toast.makeText(this, "${it.title} enrolled successfully!", Toast.LENGTH_SHORT).show()
                    finish() // Close promo page
                }
            }
        }
    }

    private fun isPremiumUser(): Boolean {
        val userProfile = databaseInfo.getUserProfile()
        return userProfile.premiumStatus == 1
    }
}
