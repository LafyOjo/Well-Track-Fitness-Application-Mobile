package com.example.welltrackapplicationassignment2.ApplicationPageActivities

import android.app.Activity
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.example.welltrackapplicationassignment2.Models.SettingsModel
import com.example.welltrackapplicationassignment2.Presenter.SettingsPresenter
import com.example.welltrackapplicationassignment2.R
import com.example.welltrackapplicationassignment2.Utils.UserProfile
import com.example.welltrackapplicationassignment2.Utils.NotificationService
import com.example.welltrackapplicationassignment2.Utils.ReminderBroadcastReceiver
import com.example.welltrackapplicationassignment2.Views.SettingsView
import com.example.welltrackapplicationassignment2.databinding.ActivitySettingsBinding
import com.example.welltrackapplicationassignment2.Utils.datavaseInfo
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.Calendar

class SettingsActivity : AppCompatActivity(), SettingsView {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var presenter: SettingsPresenter
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = datavaseInfo(this)
        presenter = SettingsPresenter(this, SettingsModel(database))

        val sharedPrefs = getSharedPreferences("user_settings", MODE_PRIVATE)
        val darkModeEnabled = sharedPrefs.getBoolean("dark_mode_enabled", false)
        applyDarkMode(darkModeEnabled)
        binding.darkModeSwitch.isChecked = darkModeEnabled

        setupListeners()
        setupBottomNavigation()
        loadProfileImage()
        presenter.onResume()

        val serviceIntent = Intent(this, NotificationService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()

        binding.setReminderButton.setOnClickListener {
            showTimePickerDialog() // Correct function to show the time picker
        }

        val shareButton = findViewById<Button>(R.id.shareButton)
        shareButton.setOnClickListener {
            shareApp()
        }
    }

    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain" // Specifies sharing text content
            putExtra(Intent.EXTRA_TEXT, "Check out this amazing app: https://yourapp.link")
        }

        // Show the share menu
        val chooserIntent = Intent.createChooser(shareIntent, "Share via")
        startActivity(chooserIntent)
    }

    private fun setupBottomNavigation() {
        binding.custombottomnavigation.navigationHome.findViewById<ImageButton>(R.id.navigation_home)
            .setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish() // Finish SettingsActivity to prevent it from staying in the back stack
            }

        binding.custombottomnavigation.navigationStore.findViewById<ImageButton>(R.id.navigation_store)
            .setOnClickListener {
                val intent = Intent(this, StoreActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivity(intent)
            }

        binding.custombottomnavigation.navigationStatistics.findViewById<ImageButton>(R.id.navigation_statistics)
            .setOnClickListener {
                val intent = Intent(this, StatisticsActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivity(intent)
            }
    }

    private fun navigateTo(destination: Class<*>) {
        val intent = Intent(this, destination)
        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    private fun shareAppContent(content: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, content)
        }

        // Show chooser dialog
        val chooser = Intent.createChooser(intent, "Share using...")
        startActivity(chooser)
    }

    private fun setupListeners() {

        binding.setReminderButton.setOnClickListener {
            showReminderDialog()
        }

        binding.selectDaysButton.setOnClickListener {
            showReminderDaysDialog()
        }


        binding.saveUserInfoButton.setOnClickListener {
            val updatedName = binding.editUsername.text.toString()
            val updatedEmail = binding.editEmail.text.toString()
            val updatedAge = binding.editAge.text.toString().toIntOrNull()

            if (updatedName.isBlank() || updatedEmail.isBlank() || updatedAge == null) {
                Toast.makeText(this, "Please fill in all fields correctly!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedProfile = UserProfile(
                name = updatedName,
                email = updatedEmail,
                age = updatedAge,
                profileImage = (binding.profileImageView.tag as? String) ?: "",
                premiumStatus = if (binding.premiumStatusSwitch.isChecked) 1 else 0
            )
            presenter.saveUserProfile(updatedProfile)
        }

        binding.uploadImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        binding.changeColorButton.setOnClickListener {
            showColorPickerDialog()
        }

        binding.darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            applyDarkMode(isChecked)
            val prefs = getSharedPreferences("user_settings", MODE_PRIVATE)
            prefs.edit().putBoolean("dark_mode_enabled", isChecked).apply()
        }

        binding.premiumStatusSwitch.setOnCheckedChangeListener { _, isChecked ->
            presenter.updatePremiumStatus(isChecked)
        }

        binding.saveSettingsButton.setOnClickListener {
            saveChanges()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                try {
                    saveImageLocally(uri)
                    Toast.makeText(this, "Profile image updated successfully!", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Image selection failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveImageLocally(uri: Uri) {
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val file = File(filesDir, "profile_image.jpg")
            val outputStream = FileOutputStream(file)

            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()

            // Update UI and save file path in SharedPreferences
            binding.profileImageView.setImageURI(Uri.fromFile(file))
            binding.profileImageView.tag = file.absolutePath
            saveProfileImageUri(file.absolutePath)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    }


    private fun showReminderDialog() {
        val reminderOptions = arrayOf("Morning", "Afternoon", "Evening")
        AlertDialog.Builder(this)
            .setTitle("Set Reminder")
            .setSingleChoiceItems(reminderOptions, -1) { dialog, which ->
                val selectedTime = reminderOptions[which]
                binding.reminderTimeText.text = "Reminder Time: $selectedTime"
                saveReminderTime(selectedTime)
                Toast.makeText(this, "Reminder set for $selectedTime", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showReminderDaysDialog() {
        val days = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
        val selectedDays = mutableListOf<String>()

        AlertDialog.Builder(this)
            .setTitle("Select Reminder Days")
            .setMultiChoiceItems(days, null) { _, which, isChecked ->
                if (isChecked) {
                    selectedDays.add(days[which])
                } else {
                    selectedDays.remove(days[which])
                }
            }
            .setPositiveButton("Save") { dialog, _ ->
                binding.selectedDaysText.text = "Selected Days: ${selectedDays.joinToString(", ")}"
                saveReminderDays(selectedDays)
                Toast.makeText(this, "Days Saved!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("premiumStatus", binding.premiumStatusSwitch.isChecked)
        outState.putString("reminderTime", binding.reminderTimeText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.premiumStatusSwitch.isChecked = savedInstanceState.getBoolean("premiumStatus")
        binding.reminderTimeText.text = savedInstanceState.getString("reminderTime")
    }

    private fun saveReminderTime(time: String) {
        val sharedPreferences = getSharedPreferences("user_settings", MODE_PRIVATE)
        sharedPreferences.edit().putString("reminder_time", time).apply()
    }

    private fun saveReminderDays(days: List<String>) {
        val sharedPreferences = getSharedPreferences("user_settings", MODE_PRIVATE)
        sharedPreferences.edit().putStringSet("reminder_days", days.toSet()).apply()
    }


    private fun saveProfileImageUri(filePath: String) {
        val sharedPreferences = getSharedPreferences("user_settings", MODE_PRIVATE)
        sharedPreferences.edit().putString("profile_image_uri", filePath).apply()
    }


    private fun loadProfileImage() {
        val sharedPreferences = getSharedPreferences("user_settings", MODE_PRIVATE)
        val uriString = sharedPreferences.getString("profile_image_uri", null)
        uriString?.let { path ->
            val file = File(path)
            if (file.exists()) {
                binding.profileImageView.setImageURI(Uri.fromFile(file))
                binding.profileImageView.tag = file.absolutePath
            }
        }
    }


    private fun copyImageLocally(uri: Uri) {
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val file = File(getExternalFilesDir(null), "profile_image.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            binding.profileImageView.setImageURI(Uri.fromFile(file))
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to copy image locally.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun showColorPickerDialog() {
        val colors = arrayOf("Red", "Green", "Blue", "Yellow", "Cyan", "Magenta")
        val colorValues = arrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA)

        AlertDialog.Builder(this)
            .setTitle("Pick a Color")
            .setItems(colors) { _, which ->
                val selectedColor = colorValues[which]
                binding.changeColorButton.setBackgroundColor(selectedColor)
                saveNotificationColor(selectedColor)
            }
            .show()
    }

    private fun saveNotificationColor(color: Int) {
        val sharedPreferences = getSharedPreferences("user_settings", MODE_PRIVATE)
        sharedPreferences.edit().putInt("notification_color", color).apply()
        Toast.makeText(this, "Notification color saved!", Toast.LENGTH_SHORT).show()
    }

    private fun applyDarkMode(enabled: Boolean) {
        val mode = if (enabled) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    override fun displayUserProfile(profile: UserProfile) {
        binding.apply {
            editUsername.setText(profile.name)
            editEmail.setText(profile.email)
            editAge.setText(profile.age.toString())
            premiumStatusSwitch.isChecked = profile.premiumStatus == 1
            updatePremiumStatusText(profile.premiumStatus == 1)

            if (profile.profileImage.isNotEmpty()) {
                profileImageView.setImageURI(Uri.parse(profile.profileImage))
                profileImageView.tag = profile.profileImage
            }
        }
    }

    override fun showUpdateSuccess() {
        Toast.makeText(this, "User information updated successfully!", Toast.LENGTH_SHORT).show()
    }

    override fun showUpdateFailure() {
        Toast.makeText(this, "Failed to update user information. Please try again.", Toast.LENGTH_SHORT).show()
    }

    override fun updatePremiumStatusText(isPremium: Boolean) {
        binding.txtPremiumStatus.text = if (isPremium) {
            "Premium Status: Premium User"
        } else {
            "Premium Status: Free User"
        }
    }

    private fun saveChanges() {
        val updatedProfile = UserProfile(
            name = binding.editUsername.text.toString(),
            email = binding.editEmail.text.toString(),
            age = binding.editAge.text.toString().toIntOrNull() ?: 0,
            profileImage = (binding.profileImageView.tag as? String) ?: "",
            premiumStatus = if (binding.premiumStatusSwitch.isChecked) 1 else 0
        )
        presenter.saveUserProfile(updatedProfile)
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()

        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                binding.reminderTimeText.text = String.format("Reminder set for: %02d:%02d", hourOfDay, minute)
                scheduleReminder(calendar.timeInMillis)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    private fun scheduleReminder(triggerTime: Long) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, ReminderBroadcastReceiver::class.java).apply {
            putExtra("notification_title", "Workout Reminder")
            putExtra("notification_message", "Time to complete your workout!")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            } else {
                requestExactAlarmPermission()
            }
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
        }

        Toast.makeText(this, "Reminder set successfully!", Toast.LENGTH_SHORT).show()
    }


    private fun requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = Uri.fromParts("package", packageName, null)
            }
            startActivity(intent)
        }
    }
}

