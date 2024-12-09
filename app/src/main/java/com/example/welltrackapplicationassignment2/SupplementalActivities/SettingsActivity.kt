//package com.example.welltrackapplicationassignment2.SupplementalActivities
//
//import android.app.Activity
//import android.app.AlarmManager
//import android.app.AlertDialog
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.app.TimePickerDialog
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.graphics.Color
//import android.net.Uri
//import android.os.Bundle
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.NotificationCompat
//import com.example.welltrackapplicationassignment2.R
//import com.example.welltrackapplicationassignment2.activityClasses.MainActivity
//import com.example.welltrackapplicationassignment2.databinding.ActivitySettingsBinding
//import com.example.welltrackapplicationassignment2.datavaseInfo
//import java.util.Calendar
//
//class SettingsActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivitySettingsBinding
//    private lateinit var databaseInfo: datavaseInfo
//    private var isPremiumUser: Boolean = false
//    private val PICK_IMAGE_REQUEST = 1
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivitySettingsBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        databaseInfo = datavaseInfo(this)
//
//        // Load saved settings
//        loadSettings()
//        loadReminderTime() // Load the reminder time here
//
//        // Set up event listeners for the buttons and switches
//        setupListeners()
//
//        binding.setReminderButton.setOnClickListener {
//            showTimePickerDialog()
//        }
//        binding.selectDaysButton.setOnClickListener {
//            showDaysPickerDialog()
//        }
//
//        setupBottomNavigation()
//        loadProfileImage()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        val bottomNavigationView = binding.bottomNavigationView
//        bottomNavigationView.selectedItemId = R.id.navigation_settings
//        loadSettings()
//    }
//
//    private fun setupBottomNavigation() {
//        val bottomNavigationView = binding.bottomNavigationView
//
//        // Set the selected item to Home
//        bottomNavigationView.selectedItemId = R.id.navigation_home
//
//        // Handle navigation item selection
//        bottomNavigationView.setOnItemSelectedListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.navigation_home -> {
//                    if (bottomNavigationView.selectedItemId == R.id.navigation_home) {
//                        val intent = Intent(this, MainActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
//                        startActivity(intent)
//                        overridePendingTransition(0, 0) // No animation
//                    }
//                    true
//                }
//                R.id.navigation_store -> {
//                    if (bottomNavigationView.selectedItemId != R.id.navigation_store) {
//                        val intent = Intent(this, StoreActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
//                        startActivity(intent)
//                        overridePendingTransition(0, 0) // No animation
//                    }
//                    true
//                }
//                R.id.navigation_statistics -> {
//                    if (bottomNavigationView.selectedItemId != R.id.navigation_statistics) {
//                        val intent = Intent(this, StatisticsActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
//                        startActivity(intent)
//                        overridePendingTransition(0, 0)
//                    }
//                    true
//                }
//                R.id.navigation_settings ->  true
//                else -> false
//            }
//        }
//    }
//
//    private fun setupListeners() {
//
//        // Save User Info Button
//        binding.saveUserInfoButton.setOnClickListener {
//            val updatedName = binding.editUsername.text.toString()
//            val updatedEmail = binding.editEmail.text.toString()
//            val updatedAge = binding.editAge.text.toString().toIntOrNull()
//
//            if (updatedName.isBlank() || updatedEmail.isBlank() || updatedAge == null) {
//                Toast.makeText(this, "Please fill in all fields correctly!", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            // Save updated user profile
//            val updatedProfile = databaseInfo.getUserProfile().copy(
//                name = updatedName,
//                email = updatedEmail,
//                age = updatedAge
//            )
//            databaseInfo.saveUserProfile(updatedProfile)
//
//            Toast.makeText(this, "User information updated successfully!", Toast.LENGTH_SHORT).show()
//        }
//
//        // Image Upload Button
//        binding.uploadImageButton.setOnClickListener {
//            val intent = Intent(Intent.ACTION_PICK)
//            intent.type = "image/*"
//            startActivityForResult(intent, PICK_IMAGE_REQUEST)
//        }
//
//        // Change Notification Color Button
//        binding.changeColorButton.setOnClickListener {
//            showColorPickerDialog()
//        }
//
//        // Premium Status Toggle
//        binding.premiumStatusSwitch.setOnCheckedChangeListener { _, isChecked ->
//            val userProfile = databaseInfo.getUserProfile().copy(premiumStatus = if (isChecked) 1 else 0)
//            databaseInfo.saveUserProfile(userProfile)
//
//            // Unlock or lock workouts based on the new status
//            if (isChecked) {
//                databaseInfo.unlockAllPremiumWorkouts()
//                Toast.makeText(this, "Premium workouts unlocked!", Toast.LENGTH_SHORT).show()
//            } else {
//                databaseInfo.lockAllPremiumWorkouts()
//                Toast.makeText(this, "Premium workouts locked!", Toast.LENGTH_SHORT).show()
//            }
//
//            // Update the premium status text
//            updatePremiumStatusText(isChecked)
//
//            // Notify the MainActivity to refresh the premium status
//            setResult(Activity.RESULT_OK)
//        }
//
//        // Save Settings Button
//        binding.saveSettingsButton.setOnClickListener {
//            saveChanges()
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
//            data?.data?.let { uri ->
//                binding.profileImageView.setImageURI(uri)
//                val sharedPreferences = getSharedPreferences("user_settings", MODE_PRIVATE)
//                sharedPreferences.edit().putString("profile_image_uri", uri.toString()).apply()
//            }
//        }
//    }
//
//    private fun saveProfileImageUri(uri: String) {
//        val sharedPreferences = getSharedPreferences("user_settings", MODE_PRIVATE)
//        sharedPreferences.edit().putString("profile_image_uri", uri).apply()
//    }
//
//    private fun loadProfileImage() {
//        val sharedPreferences = getSharedPreferences("user_settings", MODE_PRIVATE)
//        val uriString = sharedPreferences.getString("profile_image_uri", null)
//        uriString?.let { uri ->
//            binding.profileImageView.setImageURI(Uri.parse(uri))
//        }
//    }
//
//    private fun showColorPickerDialog() {
//        val colors = arrayOf("Red", "Green", "Blue", "Yellow", "Cyan", "Magenta")
//        val colorValues = arrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA)
//
//        AlertDialog.Builder(this)
//            .setTitle("Pick a Color")
//            .setItems(colors) { _, which ->
//                val selectedColor = colorValues[which]
//                binding.changeColorButton.setBackgroundColor(selectedColor)
//                saveNotificationColor(selectedColor)
//            }
//            .show()
//    }
//
//    private fun saveNotificationColor(color: Int) {
//        val sharedPreferences = getSharedPreferences("user_settings", MODE_PRIVATE)
//        sharedPreferences.edit().putInt("notification_color", color).apply()
//        Toast.makeText(this, "Notification color saved!", Toast.LENGTH_SHORT).show()
//    }
//
//    private fun loadSettings() {
//        val userProfile = databaseInfo.getUserProfile()
//
//        // Set profile image
//        if (userProfile.profileImage.isNotEmpty()) {
//            binding.profileImageView.setImageURI(Uri.parse(userProfile.profileImage))
//            binding.profileImageView.tag = userProfile.profileImage
//        }
//
//        // Set user profile informatins
//        binding.editUsername.setText(userProfile.name)
//        binding.editEmail.setText(userProfile.email)
//        binding.editAge.setText(userProfile.age.toString())
//
//
//
//        // Set premium status
//        isPremiumUser = userProfile.premiumStatus == 1
//        binding.premiumStatusSwitch.isChecked = isPremiumUser
//
//        // Set premium status text
//        updatePremiumStatusText(isPremiumUser)
//
//        // Set notification color
//        val color = getNotificationColor()
//        binding.changeColorButton.setBackgroundColor(color)
//
//        // Load saved reminder days
//        val sharedPreferences = getSharedPreferences("user_settings", Context.MODE_PRIVATE)
//        val savedDays = sharedPreferences.getStringSet("reminder_days", emptySet()) ?: emptySet()
//        val daysText = if (savedDays.isEmpty()) "None" else savedDays.joinToString(", ")
//        binding.selectedDaysText.text = "Selected Days: $daysText"
//    }
//
//    private fun saveChanges() {
//        val userProfile = databaseInfo.getUserProfile().copy(
//            profileImage = (binding.profileImageView.tag as? String) ?: "",
//            premiumStatus = if (binding.premiumStatusSwitch.isChecked) 1 else 0
//
//        )
//        databaseInfo.saveUserProfile(userProfile)
//
//        val updatedName = binding.editUsername.text.toString().trim()
//        if (updatedName.isNotEmpty()) {
//            val sharedPreferences = getSharedPreferences("user_settings", MODE_PRIVATE)
//            sharedPreferences.edit()
//                .putString("user_name", updatedName)
//                .apply()
//            Toast.makeText(this, "Changes saved successfully!", Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(this, "Name cannot be empty!", Toast.LENGTH_SHORT).show()
//        }
//
//        // Notify MainActivity to refresh
//        setResult(Activity.RESULT_OK)
//        finish() // Close the settings activity
//    }
//
//    private fun getNotificationColor(): Int {
//        val sharedPreferences = getSharedPreferences("user_settings", MODE_PRIVATE)
//        return sharedPreferences.getInt("notification_color", Color.RED) // Default to RED
//    }
//
//    private fun updatePremiumStatusText(isPremium: Boolean) {
//        binding.txtPremiumStatus.text = if (isPremium) {
//            "Premium Status: Premium User"
//        } else {
//            "Premium Status: Free User"
//        }
//    }
//
//    private fun showDaysPickerDialog() {
//        val daysArray = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
//        val selectedDays = BooleanArray(daysArray.size)
//
//        val sharedPreferences = getSharedPreferences("user_settings", Context.MODE_PRIVATE)
//        val savedDays = sharedPreferences.getStringSet("reminder_days", emptySet()) ?: emptySet()
//
//        // Pre-check the previously saved days
//        for (i in daysArray.indices) {
//            selectedDays[i] = savedDays.contains(daysArray[i])
//        }
//
//        AlertDialog.Builder(this)
//            .setTitle("Select Reminder Days")
//            .setMultiChoiceItems(daysArray, selectedDays) { _, which, isChecked ->
//                selectedDays[which] = isChecked
//            }
//            .setPositiveButton("Save") { _, _ ->
//                val chosenDays = mutableSetOf<String>()
//                for (i in daysArray.indices) {
//                    if (selectedDays[i]) {
//                        chosenDays.add(daysArray[i])
//                    }
//                }
//
//                // Save the selected days to Shared Preferences
//                sharedPreferences.edit().putStringSet("reminder_days", chosenDays).apply()
//
//                // Update the UI to reflect the chosen days
//                val daysText = if (chosenDays.isEmpty()) "None" else chosenDays.joinToString(", ")
//                binding.selectedDaysText.text = "Selected Days: $daysText"
//
//                Toast.makeText(this, "Reminder days saved!", Toast.LENGTH_SHORT).show()
//            }
//            .setNegativeButton("Cancel", null)
//            .show()
//    }
//
//    private fun showTimePickerDialog() {
//        val calendar = Calendar.getInstance()
//        val hour = calendar.get(Calendar.HOUR_OF_DAY)
//        val minute = calendar.get(Calendar.MINUTE)
//
//        val timePicker = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
//            // Save the selected time
//            val reminderTime = Calendar.getInstance().apply {
//                set(Calendar.HOUR_OF_DAY, selectedHour)
//                set(Calendar.MINUTE, selectedMinute)
//                set(Calendar.SECOND, 0)
//            }
//
//            scheduleWorkoutReminder(reminderTime)
//
//            // Update the UI
//            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
//            binding.reminderTimeText.text = "Reminder Time: $formattedTime"
//        }, hour, minute, true)
//
//        timePicker.show()
//    }
//
//    private fun scheduleWorkoutReminder(reminderTime: Calendar) {
//        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val intent = Intent(this, ReminderBroadcastReceiver::class.java).apply {
//            putExtra("notification_title", "Workout Reminder")
//            putExtra("notification_message", "It's time to start your workout!")
//        }
//
//        val pendingIntent = PendingIntent.getBroadcast(
//            this,
//            0,
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        alarmManager.setExact(
//            AlarmManager.RTC_WAKEUP,
//            reminderTime.timeInMillis,
//            pendingIntent
//        )
//
//        // Save the reminder time in Shared Preferences
//        val sharedPreferences = getSharedPreferences("user_settings", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.putInt("reminder_hour", reminderTime.get(Calendar.HOUR_OF_DAY))
//        editor.putInt("reminder_minute", reminderTime.get(Calendar.MINUTE))
//        editor.apply()
//
//        Toast.makeText(this, "Workout reminder set!", Toast.LENGTH_SHORT).show()
//    }
//
//    private fun loadReminderTime() {
//        val sharedPreferences = getSharedPreferences("user_settings", Context.MODE_PRIVATE)
//        val savedHour = sharedPreferences.getInt("reminder_hour", -1) // Default to -1 if not set
//        val savedMinute = sharedPreferences.getInt("reminder_minute", -1)
//
//        if (savedHour != -1 && savedMinute != -1) {
//            val formattedTime = String.format("%02d:%02d", savedHour, savedMinute)
//            binding.reminderTimeText.text = "Reminder Time: $formattedTime"
//        } else {
//            binding.reminderTimeText.text = "Reminder Time: Not Set"
//        }
//    }
//}
//
//class ReminderBroadcastReceiver : BroadcastReceiver() {
//
//    override fun onReceive(context: Context, intent: Intent) {
//        val notificationTitle = intent.getStringExtra("notification_title") ?: "Reminder"
//        val notificationMessage = intent.getStringExtra("notification_message") ?: "It's time!"
//
//        val notificationManager =
//            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        val channelId = "workout_reminder_channel"
//        val channelName = "Workout Reminder Notifications"
//
//        // Create a NotificationChannel (for Android O and above)
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                channelId,
//                channelName,
//                NotificationManager.IMPORTANCE_HIGH
//            )
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        // Create an intent to open the MainActivity (or a specific activity)
//        val openAppIntent = Intent(context, MainActivity::class.java)
//        openAppIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//
//        val pendingIntent = PendingIntent.getActivity(
//            context,
//            0,
//            openAppIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        // Build the notification
//        val notification = NotificationCompat.Builder(context, channelId)
//            .setSmallIcon(R.drawable.ic_notification) // Replace with your app's icon
//            .setContentTitle(notificationTitle)
//            .setContentText(notificationMessage)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setContentIntent(pendingIntent) // Set the intent to open the app
//            .setAutoCancel(true) // Dismiss the notification when tapped
//            .build()
//
//        notificationManager.notify(1, notification)
//    }
//}
