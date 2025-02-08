package com.example.welltrackapplicationassignment2.Utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import android.os.Parcel
import android.util.Log

// Database Helper Class
class datavaseInfo(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "stretchPrograms.db"
        private const val DATABASE_VERSION = 23

        // Table names

        // Columns for Stretch Programs
        const val TABLE_STRETCH_PROGRAMS = "stretch_programs"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION1 = "description1"
        const val COLUMN_DESCRIPTION2 = "description2"
        const val COLUMN_IMAGE = "image" // String for image data
        const val COLUMN_TIME_IN_SECONDS = "time_in_seconds"
        const val COLUMN_CALORIES_BURNED = "calories_burned"
        const val COLUMN_WORKOUTS_COMPLETED = "workouts_completed"
        const val COLUMN_SECONDS_SPENT = "seconds_spent"
        const val COLUMN_AVERAGE_WORKOUTS_PER_DAY = "average_workouts_per_day"
        const val COLUMN_TOTAL_CALORIES_BURNED = "total_calories_burned"
        const val COLUMN_ENROLLED_STATUS = "enrolled_status" // 0 = Not Enrolled, 1 = Enrolled
        const val COLUMN_PROGRAM_PREMIUM_STATUS = "premium_status" // 0 = Free, 1 = Premium

        // Columns for Workout Bundles
        const val TABLE_WORKOUT_BUNDLES = "workout_bundles"
        const val COLUMN_BUNDLE_IMAGE = "bundle_image"
        const val COLUMN_BUNDLE_ID = "bundle_id"
        const val COLUMN_BUNDLE_TITLE = "bundle_title"
        const val COLUMN_BUNDLE_DESCRIPTION = "bundle_description"
        const val COLUMN_BUNDLE_PREMIUM_STATUS = "bundle_premium_status" // 0 = Free, 1 = Premium

        // Columns for User Profile
        const val TABLE_USER_PROFILE = "user_profile"
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_USER_NAME = "user_name"
        const val COLUMN_USER_EMAIL = "user_email"
        const val COLUMN_USER_AGE = "user_age"
        const val COLUMN_PROFILE_IMAGE = "profile_image" // File path or URL
        const val COLUMN_USER_PREMIUM_STATUS = "premium_status" // 0 = Free, 1 = Premium

        // Workout Bundle Mapping
        const val TABLE_WORKOUT_BUNDLE_MAPPING = "workout_bundle_mapping"
        const val COLUMN_MAPPING_BUNDLE_ID = "bundle_id"
        const val COLUMN_MAPPING_WORKOUT_ID = "workout_id"

    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create table query
        val createTableQuery = """
        CREATE TABLE IF NOT EXISTS $TABLE_STRETCH_PROGRAMS (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_TITLE TEXT,
            $COLUMN_DESCRIPTION1 TEXT,
            $COLUMN_DESCRIPTION2 TEXT,
            $COLUMN_IMAGE TEXT,
            $COLUMN_TIME_IN_SECONDS INTEGER,
            $COLUMN_CALORIES_BURNED INTEGER,
            $COLUMN_WORKOUTS_COMPLETED INTEGER DEFAULT 0,
            $COLUMN_SECONDS_SPENT INTEGER DEFAULT 0,
            $COLUMN_AVERAGE_WORKOUTS_PER_DAY REAL DEFAULT 0.0,
            $COLUMN_TOTAL_CALORIES_BURNED INTEGER DEFAULT 0,
            $COLUMN_ENROLLED_STATUS INTEGER DEFAULT 0,
            $COLUMN_PROGRAM_PREMIUM_STATUS INTEGER DEFAULT 0
        )
    """.trimIndent()
        db.execSQL(createTableQuery)

        // Create Workout Bundles Table
        val createWorkoutBundlesTable = """
        CREATE TABLE IF NOT EXISTS $TABLE_WORKOUT_BUNDLES (
            $COLUMN_BUNDLE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_BUNDLE_TITLE TEXT,
            $COLUMN_BUNDLE_DESCRIPTION TEXT,
            $COLUMN_BUNDLE_IMAGE TEXT,
            $COLUMN_BUNDLE_PREMIUM_STATUS INTEGER DEFAULT 0
        )
    """.trimIndent()
        db.execSQL(createWorkoutBundlesTable)

        // Create User Profile Table
        val createUserProfileTable = """
        CREATE TABLE IF NOT EXISTS $TABLE_USER_PROFILE (
            $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_USER_NAME TEXT,
            $COLUMN_USER_EMAIL TEXT,
            $COLUMN_USER_AGE INTEGER,
            $COLUMN_PROFILE_IMAGE TEXT,
            $COLUMN_USER_PREMIUM_STATUS INTEGER DEFAULT 0,
            $COLUMN_WORKOUTS_COMPLETED INTEGER DEFAULT 0

        )
    """.trimIndent()
        db.execSQL(createUserProfileTable)

        // Create Bundles Workout Table
        val createBundleWorkoutsTable = """
        CREATE TABLE IF NOT EXISTS bundle_workouts (
            bundle_id INTEGER,
            workout_id INTEGER,
            FOREIGN KEY (bundle_id) REFERENCES $TABLE_WORKOUT_BUNDLES($COLUMN_BUNDLE_ID),
            FOREIGN KEY (workout_id) REFERENCES $TABLE_STRETCH_PROGRAMS($COLUMN_ID)
        )
    """.trimIndent()
        db.execSQL(createBundleWorkoutsTable)

        val createWorkoutBundleMappingTable = """
        CREATE TABLE IF NOT EXISTS $TABLE_WORKOUT_BUNDLE_MAPPING (
            $COLUMN_MAPPING_BUNDLE_ID INTEGER,
            $COLUMN_MAPPING_WORKOUT_ID INTEGER,
            FOREIGN KEY ($COLUMN_MAPPING_BUNDLE_ID) REFERENCES $TABLE_WORKOUT_BUNDLES($COLUMN_BUNDLE_ID),
            FOREIGN KEY ($COLUMN_MAPPING_WORKOUT_ID) REFERENCES $TABLE_STRETCH_PROGRAMS($COLUMN_ID)
        )
    """.trimIndent()
        db.execSQL(createWorkoutBundleMappingTable)

        // Insert default Profile
        insertDefaultUserProfile(db)

        // Inserting default workout programs
        insertDefaultStretchProgram(db)

        // Inserting default bundles
        insertDefaultWorkoutBundles(db)

        // Inserting default bundleMappings
        insertDefaultWorkoutBundleMappings(db)
    }

    private fun insertDefaultUserProfile(db: SQLiteDatabase) {
        val defaultProfile = ContentValues().apply {
            put(COLUMN_USER_NAME, "Default User")
            put(COLUMN_USER_EMAIL, "user@example.com")
            put(COLUMN_USER_AGE, 25)
            put(COLUMN_PROFILE_IMAGE, "")
            put(COLUMN_USER_PREMIUM_STATUS, 0)
        }
        db.insert(TABLE_USER_PROFILE, null, defaultProfile)
    }

    private fun insertDefaultWorkoutBundles(db: SQLiteDatabase) {
        val bundles = listOf(
            ContentValues().apply {
                put(COLUMN_BUNDLE_TITLE, "Beginner Yoga Pack")
                put(COLUMN_BUNDLE_DESCRIPTION, "A pack of yoga workouts for beginners.")
                put(COLUMN_BUNDLE_IMAGE, "bundle_yoga") // Drawable resource name
                put(COLUMN_BUNDLE_PREMIUM_STATUS, 0)
            },
            ContentValues().apply {
                put(COLUMN_BUNDLE_TITLE, "Strength Training Essentials")
                put(COLUMN_BUNDLE_DESCRIPTION, "Build strength with these essential workouts.")
                put(COLUMN_BUNDLE_IMAGE, "bundle_strength")
                put(COLUMN_BUNDLE_PREMIUM_STATUS, 1)
            },
            ContentValues().apply {
                put(COLUMN_BUNDLE_TITLE, "Cardio Burn Pack")
                put(COLUMN_BUNDLE_DESCRIPTION, "Burn calories with high-intensity cardio sessions.")
                put(COLUMN_BUNDLE_IMAGE, "bundle_cardio")
                put(COLUMN_BUNDLE_PREMIUM_STATUS, 0)
            },
            ContentValues().apply {
                put(COLUMN_BUNDLE_TITLE, "Morning Energizers")
                put(COLUMN_BUNDLE_DESCRIPTION, "Start your day with stretches and light cardio for energy.")
                put(COLUMN_BUNDLE_IMAGE, "bundle_morning_energizers")
                put(COLUMN_BUNDLE_PREMIUM_STATUS, 0) // Free
            },
            ContentValues().apply {
                put(COLUMN_BUNDLE_TITLE, "Strength Masters")
                put(COLUMN_BUNDLE_DESCRIPTION, "Advanced strength training sessions for muscle building.")
                put(COLUMN_BUNDLE_IMAGE, "bundle_strength_masters")
                put(COLUMN_BUNDLE_PREMIUM_STATUS, 1) // Premium
            },
            ContentValues().apply {
                put(COLUMN_BUNDLE_TITLE, "Relax & Restore")
                put(COLUMN_BUNDLE_DESCRIPTION, "Yoga and stretches to unwind and relax your body and mind.")
                put(COLUMN_BUNDLE_IMAGE, "bundle_relax_restore")
                put(COLUMN_BUNDLE_PREMIUM_STATUS, 0) // Free
            },
            ContentValues().apply {
                put(COLUMN_BUNDLE_TITLE, "Cardio Mania")
                put(COLUMN_BUNDLE_DESCRIPTION, "High-intensity cardio workouts for fat burning.")
                put(COLUMN_BUNDLE_IMAGE, "bundle_cardio_mania")
                put(COLUMN_BUNDLE_PREMIUM_STATUS, 1) // Premium
            },
            ContentValues().apply {
                put(COLUMN_BUNDLE_TITLE, "Flexibility Boost")
                put(COLUMN_BUNDLE_DESCRIPTION, "Stretch routines to improve flexibility and mobility.")
                put(COLUMN_BUNDLE_IMAGE, "bundle_flexibility_boost")
                put(COLUMN_BUNDLE_PREMIUM_STATUS, 0) // Free
            },
            ContentValues().apply {
                put(COLUMN_BUNDLE_TITLE, "Core Power")
                put(COLUMN_BUNDLE_DESCRIPTION, "Workouts designed to build a stronger and stable core.")
                put(COLUMN_BUNDLE_IMAGE, "bundle_core_power")
                put(COLUMN_BUNDLE_PREMIUM_STATUS, 1) // Premium
            },
            ContentValues().apply {
                put(COLUMN_BUNDLE_TITLE, "Dance Fitness Party")
                put(COLUMN_BUNDLE_DESCRIPTION, "Dance-based workouts to burn calories and have fun.")
                put(COLUMN_BUNDLE_IMAGE, "bundle_dance_party")
                put(COLUMN_BUNDLE_PREMIUM_STATUS, 0) // Free
            }
        )

        for (bundle in bundles) {
            db.insert(TABLE_WORKOUT_BUNDLES, null, bundle)
        }
    }

    private fun insertDefaultWorkoutBundleMappings(db: SQLiteDatabase) {
        val mappings = listOf(
            // Beginner Yoga Pack
            ContentValues().apply { put(COLUMN_MAPPING_BUNDLE_ID, 1); put(COLUMN_MAPPING_WORKOUT_ID, 1) },
            ContentValues().apply { put(COLUMN_MAPPING_BUNDLE_ID, 1); put(COLUMN_MAPPING_WORKOUT_ID, 2) },
            ContentValues().apply { put(COLUMN_MAPPING_BUNDLE_ID, 1); put(COLUMN_MAPPING_WORKOUT_ID, 3) },

            // Strength Training Essentials
            ContentValues().apply { put(COLUMN_MAPPING_BUNDLE_ID, 2); put(COLUMN_MAPPING_WORKOUT_ID, 4) },
            ContentValues().apply { put(COLUMN_MAPPING_BUNDLE_ID, 2); put(COLUMN_MAPPING_WORKOUT_ID, 5) },
            ContentValues().apply { put(COLUMN_MAPPING_BUNDLE_ID, 2); put(COLUMN_MAPPING_WORKOUT_ID, 6) },

            // Cardio Burn Pack
            ContentValues().apply { put(COLUMN_MAPPING_BUNDLE_ID, 3); put(COLUMN_MAPPING_WORKOUT_ID, 7) },
            ContentValues().apply { put(COLUMN_MAPPING_BUNDLE_ID, 3); put(COLUMN_MAPPING_WORKOUT_ID, 8) },
            ContentValues().apply { put(COLUMN_MAPPING_BUNDLE_ID, 3); put(COLUMN_MAPPING_WORKOUT_ID, 9) },

            // Morning Energizers
            ContentValues().apply { put(COLUMN_MAPPING_BUNDLE_ID, 4); put(COLUMN_MAPPING_WORKOUT_ID, 10) },
            ContentValues().apply { put(COLUMN_MAPPING_BUNDLE_ID, 4); put(COLUMN_MAPPING_WORKOUT_ID, 11) },
            ContentValues().apply { put(COLUMN_MAPPING_BUNDLE_ID, 4); put(COLUMN_MAPPING_WORKOUT_ID, 12) },

            // Strength Masters
            ContentValues().apply { put(COLUMN_MAPPING_BUNDLE_ID, 5); put(COLUMN_MAPPING_WORKOUT_ID, 13) },
            ContentValues().apply { put(COLUMN_MAPPING_BUNDLE_ID, 5); put(COLUMN_MAPPING_WORKOUT_ID, 14) },
            ContentValues().apply { put(COLUMN_MAPPING_BUNDLE_ID, 5); put(COLUMN_MAPPING_WORKOUT_ID, 15) },

            // Relax & Restore
            ContentValues().apply { put(COLUMN_MAPPING_BUNDLE_ID, 6); put(COLUMN_MAPPING_WORKOUT_ID, 16) },
            ContentValues().apply { put(COLUMN_MAPPING_BUNDLE_ID, 6); put(COLUMN_MAPPING_WORKOUT_ID, 17) },
            ContentValues().apply { put(COLUMN_MAPPING_BUNDLE_ID, 6); put(COLUMN_MAPPING_WORKOUT_ID, 18) },

            // Cardio Mania
            ContentValues().apply { put(COLUMN_MAPPING_BUNDLE_ID, 7); put(COLUMN_MAPPING_WORKOUT_ID, 19) },
            ContentValues().apply { put(COLUMN_MAPPING_BUNDLE_ID, 7); put(COLUMN_MAPPING_WORKOUT_ID, 20) },
            ContentValues().apply { put(COLUMN_MAPPING_BUNDLE_ID, 7); put(COLUMN_MAPPING_WORKOUT_ID, 21) },

            // Flexibility Boost
            ContentValues().apply { put(COLUMN_MAPPING_BUNDLE_ID, 8); put(COLUMN_MAPPING_WORKOUT_ID, 22) },
            ContentValues().apply { put(COLUMN_MAPPING_BUNDLE_ID, 8); put(COLUMN_MAPPING_WORKOUT_ID, 23) },
            ContentValues().apply { put(COLUMN_MAPPING_BUNDLE_ID, 8); put(COLUMN_MAPPING_WORKOUT_ID, 24) },

            // Core Power
            ContentValues().apply { put(COLUMN_MAPPING_BUNDLE_ID, 9); put(COLUMN_MAPPING_WORKOUT_ID, 25) },
            ContentValues().apply { put(COLUMN_MAPPING_BUNDLE_ID, 9); put(COLUMN_MAPPING_WORKOUT_ID, 26) },
            ContentValues().apply { put(COLUMN_MAPPING_BUNDLE_ID, 9); put(COLUMN_MAPPING_WORKOUT_ID, 27) },

            // Dance Fitness Party
            ContentValues().apply { put(COLUMN_MAPPING_BUNDLE_ID, 10); put(COLUMN_MAPPING_WORKOUT_ID, 28) },
            ContentValues().apply { put(COLUMN_MAPPING_BUNDLE_ID, 10); put(COLUMN_MAPPING_WORKOUT_ID, 29) },
            ContentValues().apply { put(COLUMN_MAPPING_BUNDLE_ID, 10); put(COLUMN_MAPPING_WORKOUT_ID, 30) }
        )

        for (mapping in mappings) {
            db.insert(TABLE_WORKOUT_BUNDLE_MAPPING, null, mapping)
        }
    }

    private fun insertDefaultStretchProgram(db: SQLiteDatabase) {
        // Example default stretch programs
        val defaultWorkouts = listOf(
            ContentValues().apply {
                put(COLUMN_TITLE, "Morning Yoga")
                put(COLUMN_DESCRIPTION1, "Start your day with energizing yoga stretches.")
                put(
                    COLUMN_DESCRIPTION2, """
            Morning Yoga is the perfect way to start your day with energy and focus. This session is designed to awaken your body and mind through gentle yet effective stretches and movements. It encourages a sense of calm and balance, setting a positive tone for the rest of your day.

            The session includes full-body stretches aimed at improving flexibility and promoting blood circulation. Key poses like Downward Dog, Cat-Cow, and Sun Salutations are included to stretch major muscle groups while easing any stiffness from sleep. The gentle flow helps you connect with your breath, creating a sense of mindfulness.

            Whether you're new to yoga or an experienced practitioner, this routine is suitable for all levels. It can be adjusted to meet your flexibility and comfort. By incorporating Morning Yoga into your daily routine, you'll experience enhanced mobility, reduced stress, and a refreshing start to your day.
        """.trimIndent()
                )
                put(COLUMN_IMAGE, "blueblackwoman1") // Drawable resource name
                put(COLUMN_TIME_IN_SECONDS, 10)
                put(COLUMN_CALORIES_BURNED, 150)
                put(COLUMN_WORKOUTS_COMPLETED, 0)
                put(COLUMN_SECONDS_SPENT, 0)
                put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, 0.0)
                put(COLUMN_TOTAL_CALORIES_BURNED, 0)
                put(COLUMN_ENROLLED_STATUS, 1) //
                put(COLUMN_PROGRAM_PREMIUM_STATUS, 0) // Free Course
            },
            ContentValues().apply {
                put(COLUMN_TITLE, "HIIT Workout")
                put(COLUMN_DESCRIPTION1, "High-Intensity Interval Training for quick results.")
                put(
                    COLUMN_DESCRIPTION2, """
        High-Intensity Interval Training (HIIT) is a form of workout that alternates between short, intense bursts of exercise and periods of rest or low-intensity activity. Designed to maximize calorie burn in a shorter amount of time, HIIT is highly effective for improving cardiovascular health, increasing endurance, and boosting metabolism.

        This workout combines cardio and strength-training exercises to provide a full-body experience. Expect dynamic moves like jumping jacks, burpees, and squat jumps alongside strength-building exercises such as push-ups and lunges. The variation in intensity keeps your heart rate elevated, making it a time-efficient way to burn calories and build muscle.

        Perfect for individuals of all fitness levels, this workout can be customized to match your pace and preferences. Whether you're looking to lose weight, enhance muscle tone, or simply challenge yourself, this HIIT session offers a versatile and impactful way to achieve your fitness goals.
    """.trimIndent()
                )
                put(COLUMN_IMAGE, "blueblackwoman2")
                put(COLUMN_TIME_IN_SECONDS, 15)
                put(COLUMN_CALORIES_BURNED, 300)
                put(COLUMN_WORKOUTS_COMPLETED, 0)
                put(COLUMN_SECONDS_SPENT, 0)
                put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, 0.0)
                put(COLUMN_TOTAL_CALORIES_BURNED, 0)
                put(COLUMN_ENROLLED_STATUS, 1) // Not enrolled
                put(COLUMN_PROGRAM_PREMIUM_STATUS, 0) // Free Course
            },
            ContentValues().apply {
                put(COLUMN_TITLE, "Power Pilates")
                put(COLUMN_DESCRIPTION1, "Strengthen your core with Pilates.")
                put(
                    COLUMN_DESCRIPTION2, """
            Power Pilates focuses on building core strength, improving posture, and enhancing overall flexibility. This session includes a mix of mat exercises and dynamic movements that engage your core muscles while promoting balance and stability.

            The workout features controlled movements such as leg raises, planks, and roll-ups to target the abdominal muscles and lower back. With proper breathing techniques, you'll also strengthen your pelvic floor and improve mind-body coordination.

            Perfect for those seeking a low-impact workout with high-impact results, Power Pilates is suitable for all fitness levels. It's an excellent option for building strength, reducing stress, and achieving a lean, toned physique.
        """.trimIndent()
                )
                put(COLUMN_IMAGE, "blueblackwoman3")
                put(COLUMN_TIME_IN_SECONDS, 20)
                put(COLUMN_CALORIES_BURNED, 200)
                put(COLUMN_WORKOUTS_COMPLETED, 0)
                put(COLUMN_SECONDS_SPENT, 0)
                put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, 0.0)
                put(COLUMN_TOTAL_CALORIES_BURNED, 0)
                put(COLUMN_ENROLLED_STATUS, 0) // Not enrolled
                put(COLUMN_PROGRAM_PREMIUM_STATUS, 1) // Free Course
            },
            // 3. Cardio Burn
            ContentValues().apply {
                put(COLUMN_TITLE, "Cardio Burn")
                put(COLUMN_DESCRIPTION1, "Boost your heart health and burn calories.")
                put(
                    COLUMN_DESCRIPTION2, """
            Cardio Burn is a high-energy session designed to improve your cardiovascular health and torch calories. This workout features dynamic movements like running in place, high knees, and lateral shuffles to keep your heart rate elevated.

            The session alternates between moderate and high-intensity exercises, ensuring you burn calories efficiently while building stamina. The variety of movements keeps the workout engaging and challenging for all fitness levels.

            Whether you're looking to lose weight, improve endurance, or simply get your heart pumping, Cardio Burn is a versatile and effective choice. Incorporate it into your routine for a healthier and more active lifestyle.
        """.trimIndent()
                )
                put(COLUMN_IMAGE, "blueblackwoman4")
                put(COLUMN_TIME_IN_SECONDS, 30)
                put(COLUMN_CALORIES_BURNED, 400)
                put(COLUMN_WORKOUTS_COMPLETED, 0)
                put(COLUMN_SECONDS_SPENT, 0)
                put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, 0.0)
                put(COLUMN_TOTAL_CALORIES_BURNED, 0)
                put(COLUMN_ENROLLED_STATUS, 0) // Not enrolled
                put(COLUMN_PROGRAM_PREMIUM_STATUS, 0) // Free Course
            },
            // 4. Strength Training
            ContentValues().apply {
                put(COLUMN_TITLE, "Strength Training")
                put(COLUMN_DESCRIPTION1, "Build muscle and enhance strength.")
                put(
                    COLUMN_DESCRIPTION2, """
            Strength Training focuses on building lean muscle and enhancing overall strength. This session combines bodyweight exercises and resistance training techniques to challenge your muscles and improve endurance.

            The workout includes push-ups, squats, and lunges, targeting key muscle groups like the chest, legs, and core. With consistent effort, you'll increase muscle tone, improve joint health, and boost your metabolism.

            Suitable for beginners and experienced athletes alike, Strength Training helps you achieve a strong, sculpted physique. Add this to your fitness routine for long-lasting strength and improved physical performance.
        """.trimIndent()
                )
                put(COLUMN_IMAGE, "blueblackwoman5")
                put(COLUMN_TIME_IN_SECONDS, 25)
                put(COLUMN_CALORIES_BURNED, 250)
                put(COLUMN_WORKOUTS_COMPLETED, 0)
                put(COLUMN_SECONDS_SPENT, 0)
                put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, 0.0)
                put(COLUMN_TOTAL_CALORIES_BURNED, 0)
                put(COLUMN_ENROLLED_STATUS, 0) // Not enrolled
                put(COLUMN_PROGRAM_PREMIUM_STATUS, 0) // Free Course
            },
            // 5. Evening Relaxation
            ContentValues().apply {
                put(COLUMN_TITLE, "Evening Relaxation")
                put(COLUMN_DESCRIPTION1, "Unwind with calming stretches and poses.")
                put(
                    COLUMN_DESCRIPTION2, """
            Evening Relaxation is designed to help you unwind and de-stress after a busy day. This session includes gentle stretches and calming yoga poses that promote relaxation and prepare your body for restful sleep.

            You'll perform soothing movements like seated forward folds, reclined twists, and Child's Pose, focusing on deep breathing to release tension from the body and mind. The session helps alleviate stress and improves flexibility.

            Ideal for all fitness levels, Evening Relaxation is the perfect way to end your day feeling refreshed and at peace. Incorporate it into your evening routine for better sleep and reduced stress.
        """.trimIndent()
                )
                put(COLUMN_IMAGE, "blueblackwoman6")
                put(COLUMN_TIME_IN_SECONDS, 15)
                put(COLUMN_CALORIES_BURNED, 100)
                put(COLUMN_WORKOUTS_COMPLETED, 0)
                put(COLUMN_SECONDS_SPENT, 0)
                put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, 0.0)
                put(COLUMN_TOTAL_CALORIES_BURNED, 0)
                put(COLUMN_ENROLLED_STATUS, 0) // Not enrolled
                put(COLUMN_PROGRAM_PREMIUM_STATUS, 1) // Free Course
            },
            // 6. Core Blaster
            ContentValues().apply {
                put(COLUMN_TITLE, "Core Blaster")
                put(COLUMN_DESCRIPTION1, "Sculpt your abs and strengthen your core.")
                put(
                    COLUMN_DESCRIPTION2, """
            Core Blaster is a high-intensity session that targets your abdominal muscles for a stronger, more sculpted core. The workout includes exercises like crunches, mountain climbers, and Russian twists.

            This session is designed to challenge your core stability while improving posture and balance. The variety of movements ensures you engage all parts of your core, including the obliques and lower abs.

            Suitable for all levels, Core Blaster is an excellent choice for building core strength and enhancing athletic performance. Add this workout to your routine for a defined and powerful midsection.
        """.trimIndent()
                )
                put(COLUMN_IMAGE, "blueblackwoman7")
                put(COLUMN_TIME_IN_SECONDS, 20)
                put(COLUMN_CALORIES_BURNED, 180)
                put(COLUMN_WORKOUTS_COMPLETED, 0)
                put(COLUMN_SECONDS_SPENT, 0)
                put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, 0.0)
                put(COLUMN_TOTAL_CALORIES_BURNED, 0)
                put(COLUMN_ENROLLED_STATUS, 0) // Not enrolled
                put(COLUMN_PROGRAM_PREMIUM_STATUS, 0) // Free Course
            },
            // 7. Full-Body Blast
            ContentValues().apply {
                put(COLUMN_TITLE, "Full-Body Blast")
                put(COLUMN_DESCRIPTION1, "Engage every muscle group with this intense session.")
                put(
                    COLUMN_DESCRIPTION2, """
            Full-Body Blast is a dynamic workout that targets all major muscle groups for a comprehensive fitness experience. It combines cardio, strength, and endurance exercises to maximize calorie burn.

            The session includes moves like burpees, push-ups, and jump squats, providing a balanced mix of strength and agility. The high-intensity intervals ensure your heart rate stays elevated for an effective fat-burning session.

            Ideal for those seeking a challenging yet rewarding workout, Full-Body Blast improves overall strength, endurance, and coordination. Incorporate it into your routine for a fitter, more resilient body.
        """.trimIndent()
                )
                put(COLUMN_IMAGE, "blueblackwoman8")
                put(COLUMN_TIME_IN_SECONDS, 30)
                put(COLUMN_CALORIES_BURNED, 400)
                put(COLUMN_WORKOUTS_COMPLETED, 0)
                put(COLUMN_SECONDS_SPENT, 0)
                put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, 0.0)
                put(COLUMN_TOTAL_CALORIES_BURNED, 0)
                put(COLUMN_ENROLLED_STATUS, 0) // Not enrolled
                put(COLUMN_PROGRAM_PREMIUM_STATUS, 1) // Free Course
            },
            // 8. Flexibility Focus
            ContentValues().apply {
                put(COLUMN_TITLE, "Flexibility Focus")
                put(COLUMN_DESCRIPTION1, "Improve your range of motion and muscle flexibility.")
                put(
                    COLUMN_DESCRIPTION2, """
            Flexibility Focus is a gentle session designed to increase your range of motion and muscle elasticity. It incorporates dynamic and static stretches to target tight muscles.

            This session includes movements like hamstring stretches, hip openers, and spinal twists. These stretches are perfect for loosening up your body and preventing stiffness, especially if you spend long hours sitting.

            Great for beginners and experienced athletes, Flexibility Focus helps improve athletic performance and reduces the risk of injury. Add this session to your routine for better mobility and relaxation.
        """.trimIndent()
                )
                put(COLUMN_IMAGE, "blueblackwoman9")
                put(COLUMN_TIME_IN_SECONDS, 15)
                put(COLUMN_CALORIES_BURNED, 120)
                put(COLUMN_WORKOUTS_COMPLETED, 0)
                put(COLUMN_SECONDS_SPENT, 0)
                put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, 0.0)
                put(COLUMN_TOTAL_CALORIES_BURNED, 0)
                put(COLUMN_ENROLLED_STATUS, 0) // Not enrolled
                put(COLUMN_PROGRAM_PREMIUM_STATUS, 1) // Free Course
            },
            ContentValues().apply {
                put(COLUMN_TITLE, "Dynamic Dance")
                put(COLUMN_DESCRIPTION1, "Burn calories with a fun, dance-inspired workout.")
                put(
                    COLUMN_DESCRIPTION2, """
        Dynamic Dance is a high-energy workout that combines rhythmic dance moves with fitness exercises for a fun and effective session. This workout is designed to improve coordination, boost cardiovascular health, and burn calories.

        The session includes upbeat routines inspired by popular dance styles like salsa, hip-hop, and Zumba. Each movement is paired with music to keep you motivated and make exercising feel like a party. The workout also incorporates core-strengthening and toning elements to enhance your overall fitness.

        Perfect for all skill levels, Dynamic Dance is an enjoyable way to stay active while learning new moves. Whether you’re looking to have fun, reduce stress, or reach your fitness goals, this workout offers a lively and engaging experience.
    """.trimIndent()
                )
                put(COLUMN_IMAGE, "blueblackwoman10")
                put(COLUMN_TIME_IN_SECONDS, 20)
                put(COLUMN_CALORIES_BURNED, 250)
                put(COLUMN_WORKOUTS_COMPLETED, 0)
                put(COLUMN_SECONDS_SPENT, 0)
                put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, 0.0)
                put(COLUMN_TOTAL_CALORIES_BURNED, 0)
                put(COLUMN_ENROLLED_STATUS, 0) // Not enrolled
                put(COLUMN_PROGRAM_PREMIUM_STATUS, 0) // Free Course
            },
            ContentValues().apply {
                put(COLUMN_TITLE, "Dynamic Dance")
                put(COLUMN_DESCRIPTION1, "Burn calories with a fun, dance-inspired workout.")
                put(
                    COLUMN_DESCRIPTION2, """
            Dynamic Dance is a high-energy workout that combines rhythmic dance moves with fitness exercises for a fun and effective session. Perfect for all skill levels, it's an enjoyable way to stay active.
        """.trimIndent()
                )
                put(COLUMN_IMAGE, "greenblackwoman1")
                put(COLUMN_TIME_IN_SECONDS, 15)
                put(COLUMN_CALORIES_BURNED, 120)
                put(COLUMN_WORKOUTS_COMPLETED, 0)
                put(COLUMN_SECONDS_SPENT, 0)
                put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, 0.0)
                put(COLUMN_TOTAL_CALORIES_BURNED, 0)
                put(COLUMN_ENROLLED_STATUS, 0)
                put(COLUMN_PROGRAM_PREMIUM_STATUS, 1)
            },
            ContentValues().apply {
                put(COLUMN_TITLE, "Strength Stretch")
                put(
                    COLUMN_DESCRIPTION1,
                    "Boost flexibility and muscle strength with targeted stretches."
                )
                put(
                    COLUMN_DESCRIPTION2, """
            This workout emphasizes improving joint flexibility and muscle strength through slow, controlled stretches. A perfect cooldown or standalone session for all fitness levels.
        """.trimIndent()
                )
                put(COLUMN_IMAGE, "greenblackwoman2")
                put(COLUMN_TIME_IN_SECONDS, 20)
                put(COLUMN_CALORIES_BURNED, 80)
                put(COLUMN_WORKOUTS_COMPLETED, 0)
                put(COLUMN_SECONDS_SPENT, 0)
                put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, 0.0)
                put(COLUMN_TOTAL_CALORIES_BURNED, 0)
                put(COLUMN_ENROLLED_STATUS, 0)
                put(COLUMN_PROGRAM_PREMIUM_STATUS, 0)
            },
            ContentValues().apply {
                put(COLUMN_TITLE, "Cardio Burst")
                put(
                    COLUMN_DESCRIPTION1,
                    "Elevate your heart rate with high-intensity cardio routines."
                )
                put(
                    COLUMN_DESCRIPTION2, """
            Cardio Burst features fast-paced exercises to burn calories and improve endurance. Great for individuals seeking a short, intense workout session.
        """.trimIndent()
                )
                put(COLUMN_IMAGE, "greenblackwoman3")
                put(COLUMN_TIME_IN_SECONDS, 30)
                put(COLUMN_CALORIES_BURNED, 150)
                put(COLUMN_WORKOUTS_COMPLETED, 0)
                put(COLUMN_SECONDS_SPENT, 0)
                put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, 0.0)
                put(COLUMN_TOTAL_CALORIES_BURNED, 0)
                put(COLUMN_ENROLLED_STATUS, 0)
                put(COLUMN_PROGRAM_PREMIUM_STATUS, 1)
            },
            ContentValues().apply {
                put(COLUMN_TITLE, "Core Crusher")
                put(COLUMN_DESCRIPTION1, "Strengthen your core with targeted ab exercises.")
                put(
                    COLUMN_DESCRIPTION2, """
            Core Crusher offers a series of abdominal exercises aimed at building strength and stability. Tailored for both beginners and advanced users.
        """.trimIndent()
                )
                put(COLUMN_IMAGE, "greenblackwoman4")
                put(COLUMN_TIME_IN_SECONDS, 25)
                put(COLUMN_CALORIES_BURNED, 100)
                put(COLUMN_WORKOUTS_COMPLETED, 0)
                put(COLUMN_SECONDS_SPENT, 0)
                put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, 0.0)
                put(COLUMN_TOTAL_CALORIES_BURNED, 0)
                put(COLUMN_ENROLLED_STATUS, 0)
                put(COLUMN_PROGRAM_PREMIUM_STATUS, 1)
            },
            ContentValues().apply {
                put(COLUMN_TITLE, "Morning Mobility")
                put(COLUMN_DESCRIPTION1, "Start your day right with this gentle mobility workout.")
                put(
                    COLUMN_DESCRIPTION2, """
            A perfect morning session combining light stretches and easy movements to energize your body and prepare you for the day ahead.
        """.trimIndent()
                )
                put(COLUMN_IMAGE, "greenblackwoman5")
                put(COLUMN_TIME_IN_SECONDS, 20)
                put(COLUMN_CALORIES_BURNED, 70)
                put(COLUMN_WORKOUTS_COMPLETED, 0)
                put(COLUMN_SECONDS_SPENT, 0)
                put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, 0.0)
                put(COLUMN_TOTAL_CALORIES_BURNED, 0)
                put(COLUMN_ENROLLED_STATUS, 0)
                put(COLUMN_PROGRAM_PREMIUM_STATUS, 0)
            },
            ContentValues().apply {
                put(COLUMN_TITLE, "Power Yoga")
                put(COLUMN_DESCRIPTION1, "Combine strength and flexibility with this yoga session.")
                put(
                    COLUMN_DESCRIPTION2, """
            Power Yoga blends traditional yoga poses with strength-building exercises, offering a holistic workout that improves both body and mind.
        """.trimIndent()
                )
                put(COLUMN_IMAGE, "greenblackwoman6")
                put(COLUMN_TIME_IN_SECONDS, 40)
                put(COLUMN_CALORIES_BURNED, 200)
                put(COLUMN_WORKOUTS_COMPLETED, 0)
                put(COLUMN_SECONDS_SPENT, 0)
                put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, 0.0)
                put(COLUMN_TOTAL_CALORIES_BURNED, 0)
                put(COLUMN_ENROLLED_STATUS, 0)
                put(COLUMN_PROGRAM_PREMIUM_STATUS, 1)
            },
            ContentValues().apply {
                put(COLUMN_TITLE, "HIIT Express")
                put(COLUMN_DESCRIPTION1, "High-Intensity Interval Training for quick results.")
                put(
                    COLUMN_DESCRIPTION2, """
            A fast and effective workout featuring intense exercise bursts with short recovery periods. Designed for maximum calorie burn in minimal time.
        """.trimIndent()
                )
                put(COLUMN_IMAGE, "greenblackwoman7")
                put(COLUMN_TIME_IN_SECONDS, 30)
                put(COLUMN_CALORIES_BURNED, 180)
                put(COLUMN_WORKOUTS_COMPLETED, 0)
                put(COLUMN_SECONDS_SPENT, 0)
                put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, 0.0)
                put(COLUMN_TOTAL_CALORIES_BURNED, 0)
                put(COLUMN_ENROLLED_STATUS, 0)
                put(COLUMN_PROGRAM_PREMIUM_STATUS, 1)
            },
            ContentValues().apply {
                put(COLUMN_TITLE, "Evening Stretch")
                put(COLUMN_DESCRIPTION1, "Relax and wind down with soothing stretches.")
                put(
                    COLUMN_DESCRIPTION2, """
            Designed to help you relax and relieve tension, Evening Stretch is a perfect way to end your day and prepare for restful sleep.
        """.trimIndent()
                )
                put(COLUMN_IMAGE, "greenblackwoman8")
                put(COLUMN_TIME_IN_SECONDS, 15)
                put(COLUMN_CALORIES_BURNED, 50)
                put(COLUMN_WORKOUTS_COMPLETED, 0)
                put(COLUMN_SECONDS_SPENT, 0)
                put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, 0.0)
                put(COLUMN_TOTAL_CALORIES_BURNED, 0)
                put(COLUMN_ENROLLED_STATUS, 0)
                put(COLUMN_PROGRAM_PREMIUM_STATUS, 0)
            },
            ContentValues().apply {
                put(COLUMN_TITLE, "Mountain Pose")
                put(COLUMN_IMAGE, "blackwomanwhiteclothing1")
                put(COLUMN_DESCRIPTION1, "Improve posture and balance with this foundational pose.")
                put(
                    COLUMN_DESCRIPTION2, """
        Mountain Pose, also known as Tadasana, is a foundational yoga pose that promotes good posture and enhances balance. This pose helps ground your body and improves overall alignment.
        
        Stand tall with feet together, arms relaxed by your sides, and shoulders rolled back. Focus on evenly distributing your weight across both feet and engage your core.
        
        Ideal for all fitness levels, Mountain Pose can serve as the starting point for many routines.
        """.trimIndent()
                )
                put(COLUMN_TIME_IN_SECONDS, 15)
                put(COLUMN_CALORIES_BURNED, 5)
                put(COLUMN_WORKOUTS_COMPLETED, 0)
                put(COLUMN_SECONDS_SPENT, 0)
                put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, 0.0)
                put(COLUMN_TOTAL_CALORIES_BURNED, 0)
                put(COLUMN_ENROLLED_STATUS, 1)
                put(COLUMN_PROGRAM_PREMIUM_STATUS, 0)
            },
            ContentValues().apply {
                put(COLUMN_TITLE, "Chair Pose")
                put(COLUMN_IMAGE, "blackwomanwhiteclothing2")
                put(COLUMN_DESCRIPTION1, "Strengthen your legs and core with this empowering pose.")
                put(
                    COLUMN_DESCRIPTION2, """
        Chair Pose, or Utkatasana, is a yoga pose that targets the lower body and strengthens the core. It builds stamina and improves balance while engaging the thighs and glutes.
        
        From a standing position, bend your knees as if sitting in an invisible chair, keeping your back straight and arms extended overhead. Hold the position and feel the burn.
        
        Great for intermediate levels but suitable for beginners with minor modifications.
        """.trimIndent()
                )
                put(COLUMN_TIME_IN_SECONDS, 20)
                put(COLUMN_CALORIES_BURNED, 15)
                put(COLUMN_WORKOUTS_COMPLETED, 0)
                put(COLUMN_SECONDS_SPENT, 0)
                put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, 0.0)
                put(COLUMN_TOTAL_CALORIES_BURNED, 0)
                put(COLUMN_ENROLLED_STATUS, 1)
                put(COLUMN_PROGRAM_PREMIUM_STATUS, 0)
            },
            ContentValues().apply {
                put(COLUMN_TITLE, "Warrior I Pose")
                put(COLUMN_IMAGE, "blackwomanwhiteclothing3")
                put(COLUMN_DESCRIPTION1, "Boost focus and strength with this empowering stance.")
                put(
                    COLUMN_DESCRIPTION2, """
        Warrior I Pose, or Virabhadrasana I, strengthens the legs, opens the hips, and improves focus. It’s a great way to boost stamina and develop balance.
        
        Begin in a lunge position with your front knee bent and back leg straight. Raise your arms above your head and focus your gaze forward. Feel the stretch in your hips and thighs.
        
        Perfect for all fitness levels, this pose builds both physical and mental resilience.
        """.trimIndent()
                )
                put(COLUMN_TIME_IN_SECONDS, 25)
                put(COLUMN_CALORIES_BURNED, 20)
                put(COLUMN_WORKOUTS_COMPLETED, 0)
                put(COLUMN_SECONDS_SPENT, 0)
                put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, 0.0)
                put(COLUMN_TOTAL_CALORIES_BURNED, 0)
                put(COLUMN_ENROLLED_STATUS, 1)
                put(COLUMN_PROGRAM_PREMIUM_STATUS, 0)
            },
            ContentValues().apply {
                put(COLUMN_TITLE, "Downward Dog")
                put(COLUMN_IMAGE, "blackwomanwhiteclothing4")
                put(
                    COLUMN_DESCRIPTION1,
                    "Stretch your entire body and relieve tension with this iconic pose."
                )
                put(
                    COLUMN_DESCRIPTION2, """
        Downward Dog, or Adho Mukha Svanasana, is a full-body stretch that lengthens the spine, strengthens the arms and legs, and improves blood flow.
        
        Begin on all fours, then lift your hips upward, forming an inverted "V" shape with your body. Press your hands and heels into the ground and keep your back straight.
        
        This pose is beginner-friendly and provides a rejuvenating stretch.
        """.trimIndent()
                )
                put(COLUMN_TIME_IN_SECONDS, 30)
                put(COLUMN_CALORIES_BURNED, 25)
                put(COLUMN_WORKOUTS_COMPLETED, 0)
                put(COLUMN_SECONDS_SPENT, 0)
                put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, 0.0)
                put(COLUMN_TOTAL_CALORIES_BURNED, 0)
                put(COLUMN_ENROLLED_STATUS, 1)
                put(COLUMN_PROGRAM_PREMIUM_STATUS, 0)
            },
            ContentValues().apply {
                put(COLUMN_TITLE, "Triangle Pose")
                put(COLUMN_IMAGE, "blackwomanwhiteclothing5")
                put(
                    COLUMN_DESCRIPTION1,
                    "Enhance balance and flexibility with this lateral stretch."
                )
                put(
                    COLUMN_DESCRIPTION2, """
        Triangle Pose, or Trikonasana, is a yoga pose that strengthens the legs, stretches the sides of the body, and improves balance and stability.
        
        From a standing position, step your feet apart and extend your arms sideways. Bend towards one leg while keeping the other arm raised and your back straight.
        
        This pose is ideal for improving flexibility and posture for all levels.
        """.trimIndent()
                )
                put(COLUMN_TIME_IN_SECONDS, 30)
                put(COLUMN_CALORIES_BURNED, 25)
                put(COLUMN_WORKOUTS_COMPLETED, 0)
                put(COLUMN_SECONDS_SPENT, 0)
                put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, 0.0)
                put(COLUMN_TOTAL_CALORIES_BURNED, 0)
                put(COLUMN_ENROLLED_STATUS, 1)
                put(COLUMN_PROGRAM_PREMIUM_STATUS, 0)
            },
            ContentValues().apply {
                put(COLUMN_TITLE, "Child's Pose")
                put(COLUMN_IMAGE, "blackwomanwhiteclothing6")
                put(COLUMN_DESCRIPTION1, "Relax and restore with this calming stretch.")
                put(
                    COLUMN_DESCRIPTION2, """
        Child's Pose, or Balasana, is a restorative yoga pose that helps relax the mind and gently stretch the back and hips. It’s a great way to release tension and promote relaxation.
        
        Kneel on the floor and lower your hips to your heels, then extend your arms forward and rest your forehead on the ground. Breathe deeply and hold the pose.
        
        Perfect for unwinding after a long day or during yoga practice.
        """.trimIndent()
                )
                put(COLUMN_TIME_IN_SECONDS, 20)
                put(COLUMN_CALORIES_BURNED, 10)
                put(COLUMN_WORKOUTS_COMPLETED, 0)
                put(COLUMN_SECONDS_SPENT, 0)
                put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, 0.0)
                put(COLUMN_TOTAL_CALORIES_BURNED, 0)
                put(COLUMN_ENROLLED_STATUS, 1)
                put(COLUMN_PROGRAM_PREMIUM_STATUS, 0)
            },
            ContentValues().apply {
                put(COLUMN_TITLE, "Bridge Pose")
                put(COLUMN_IMAGE, "blackwomanwhiteclothing7")
                put(
                    COLUMN_DESCRIPTION1,
                    "Strengthen your glutes and back with this invigorating pose."
                )
                put(
                    COLUMN_DESCRIPTION2, """
        Bridge Pose, or Setu Bandhasana, strengthens the glutes, lower back, and hamstrings while opening up the chest and improving posture.
        
        Lie on your back with knees bent and feet flat on the floor. Lift your hips upward, engaging your glutes and back, and hold the pose while keeping your shoulders relaxed.
        
        Suitable for all fitness levels, this pose energizes the body.
        """.trimIndent()
                )
                put(COLUMN_TIME_IN_SECONDS, 25)
                put(COLUMN_CALORIES_BURNED, 20)
                put(COLUMN_WORKOUTS_COMPLETED, 0)
                put(COLUMN_SECONDS_SPENT, 0)
                put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, 0.0)
                put(COLUMN_TOTAL_CALORIES_BURNED, 0)
                put(COLUMN_ENROLLED_STATUS, 1)
                put(COLUMN_PROGRAM_PREMIUM_STATUS, 0)
            },
            ContentValues().apply {
                put(COLUMN_TITLE, "Plank Pose")
                put(COLUMN_IMAGE, "blackwomanwhiteclothing8")
                put(
                    COLUMN_DESCRIPTION1,
                    "Build core strength and stability with this essential exercise."
                )
                put(
                    COLUMN_DESCRIPTION2, """
        Plank Pose, or Kumbhakasana, is a full-body exercise that primarily strengthens the core, arms, and shoulders while improving balance and endurance.
        
        Position your body parallel to the ground, supported by your hands and toes. Keep your body straight and core engaged throughout the pose.
        
        This pose is a fitness staple for all levels and enhances core stability.
        """.trimIndent()
                )
                put(COLUMN_TIME_IN_SECONDS, 30)
                put(COLUMN_CALORIES_BURNED, 30)
                put(COLUMN_WORKOUTS_COMPLETED, 0)
                put(COLUMN_SECONDS_SPENT, 0)
                put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, 0.0)
                put(COLUMN_TOTAL_CALORIES_BURNED, 0)
                put(COLUMN_ENROLLED_STATUS, 1)
                put(COLUMN_PROGRAM_PREMIUM_STATUS, 0)
            },
            ContentValues().apply {
                put(COLUMN_TITLE, "Seated Forward Bend")
                put(COLUMN_IMAGE, "blackwomanwhiteclothing9")
                put(
                    COLUMN_DESCRIPTION1,
                    "Stretch your hamstrings and lower back with this calming pose."
                )
                put(
                    COLUMN_DESCRIPTION2, """
        Seated Forward Bend, or Paschimottanasana, is a yoga pose that stretches the hamstrings, spine, and lower back while promoting relaxation and flexibility.
        
        Sit with legs extended, then reach forward towards your toes, keeping your back straight. Hold the pose and breathe deeply.
        
        Ideal for increasing flexibility and calming the mind for all skill levels.
        """.trimIndent()
                )
                put(COLUMN_TIME_IN_SECONDS, 25)
                put(COLUMN_CALORIES_BURNED, 15)
                put(COLUMN_WORKOUTS_COMPLETED, 0)
                put(COLUMN_SECONDS_SPENT, 0)
                put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, 0.0)
                put(COLUMN_TOTAL_CALORIES_BURNED, 0)
                put(COLUMN_ENROLLED_STATUS, 1)
                put(COLUMN_PROGRAM_PREMIUM_STATUS, 0)
            },
            ContentValues().apply {
                put(COLUMN_TITLE, "Cobra Pose")
                put(COLUMN_IMAGE, "blackwomanwhiteclothing10")
                put(
                    COLUMN_DESCRIPTION1,
                    "Open your chest and strengthen your spine with this rejuvenating pose."
                )
                put(
                    COLUMN_DESCRIPTION2, """
        Cobra Pose, or Bhujangasana, is a backbend pose that strengthens the spine, opens the chest, and improves posture. It’s great for relieving back stiffness.
        
        Lie on your stomach, place your hands under your shoulders, and lift your chest upward while keeping your elbows bent. Engage your lower back and hold the pose.
        
        Suitable for all levels, this pose energizes and strengthens.
        """.trimIndent()
                )
                put(COLUMN_TIME_IN_SECONDS, 20)
                put(COLUMN_CALORIES_BURNED, 10)
                put(COLUMN_WORKOUTS_COMPLETED, 0)
                put(COLUMN_SECONDS_SPENT, 0)
                put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, 0.0)
                put(COLUMN_TOTAL_CALORIES_BURNED, 0)
                put(COLUMN_ENROLLED_STATUS, 1)
                put(COLUMN_PROGRAM_PREMIUM_STATUS, 0)
            },
            ContentValues().apply {
                put(COLUMN_TITLE, "Leg Raises")
                put(COLUMN_IMAGE, "blackwomanwhiteclothing11")
                put(
                    COLUMN_DESCRIPTION1,
                    "Strengthen your core and lower abs with this effective exercise."
                )
                put(
                    COLUMN_DESCRIPTION2, """
        Leg Raises target the lower abs and hip flexors, helping to strengthen your core and improve overall stability.
        
        Lie on your back with legs straight, then lift them together to a 90-degree angle and lower slowly. Keep your back flat and core engaged throughout the exercise.
        
        Perfect for all fitness levels, this exercise builds core strength efficiently.
        """.trimIndent()
                )
                put(COLUMN_TIME_IN_SECONDS, 30)
                put(COLUMN_CALORIES_BURNED, 20)
                put(COLUMN_WORKOUTS_COMPLETED, 0)
                put(COLUMN_SECONDS_SPENT, 0)
                put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, 0.0)
                put(COLUMN_TOTAL_CALORIES_BURNED, 0)
                put(COLUMN_ENROLLED_STATUS, 1)
                put(COLUMN_PROGRAM_PREMIUM_STATUS, 0)
            }
        )
        val blackWomanMultiColoredWorkouts = listOf(
            ContentValues().apply {
                put(COLUMN_TITLE, "Squat Jumps")
                put(COLUMN_IMAGE, "blackwomanmulticoloured1")
                put(COLUMN_DESCRIPTION1, "A dynamic exercise to build leg strength and power.")
                put(
                    COLUMN_DESCRIPTION2, """
        Squat Jumps target the quadriceps, hamstrings, and glutes, improving lower body strength and explosive power.
        
        Start in a squat position with your feet shoulder-width apart, then jump explosively into the air before landing softly back into the squat.
        
        This high-intensity exercise is excellent for building strength and improving cardiovascular fitness.
    """.trimIndent())
            },

                    ContentValues().apply {
                put(COLUMN_TITLE, "Push-Ups")
                put(COLUMN_IMAGE, "blackwomanmulticoloured2")
                put(COLUMN_DESCRIPTION1, "A classic bodyweight exercise to strengthen your upper body.")
                put(
                    COLUMN_DESCRIPTION2, """
        Push-Ups primarily target the chest, shoulders, and triceps, building upper body strength and endurance.
        
        Keep your hands slightly wider than shoulder-width apart and lower your body until your chest nearly touches the floor, then push back up.
        
        Modify the exercise by performing knee push-ups if needed. Excellent for strength and stability.
    """.trimIndent())
            },

                    ContentValues().apply {
                put(COLUMN_TITLE, "Mountain Climbers")
                put(COLUMN_IMAGE, "blackwomanmulticoloured3")
                put(COLUMN_DESCRIPTION1, "A fast-paced exercise to boost cardiovascular fitness and core strength.")
                put(
                    COLUMN_DESCRIPTION2, """
        Mountain Climbers work the core, shoulders, and legs, promoting endurance and agility.
        
        Start in a plank position, then quickly alternate bringing your knees toward your chest as if you're climbing a mountain.
        
        This exercise increases heart rate and strengthens the entire body.
    """.trimIndent())
            },

                    ContentValues().apply {
                put(COLUMN_TITLE, "Plank")
                put(COLUMN_IMAGE, "blackwomanmulticoloured4")
                put(COLUMN_DESCRIPTION1, "An isometric exercise that strengthens the core and improves stability.")
                put(
                    COLUMN_DESCRIPTION2, """
        The Plank targets the abdominal muscles, back, and shoulders, building core stability and endurance.
        
        Hold a push-up position with your body in a straight line from head to heels, engaging your core and maintaining the position for as long as possible.
        
        A great exercise for overall core strength and posture improvement.
    """.trimIndent())
            },

                    ContentValues().apply {
                put(COLUMN_TITLE, "Lunges")
                put(COLUMN_IMAGE, "blackwomanmulticoloured5")
                put(COLUMN_DESCRIPTION1, "A powerful lower body exercise that targets the legs and glutes.")
                put(
                    COLUMN_DESCRIPTION2, """
        Lunges work the quads, hamstrings, and glutes, helping to tone and strengthen the lower body.
        
        Step forward with one leg, lowering your hips until both knees are bent at 90 degrees. Push off with your front leg to return to the starting position.
        
        A great addition to any lower-body workout routine.
    """.trimIndent())
            },

                    ContentValues().apply {
                put(COLUMN_TITLE, "Bicep Curls")
                put(COLUMN_IMAGE, "blackwomanmulticoloured6")
                put(COLUMN_DESCRIPTION1, "An effective exercise to build and tone the biceps.")
                put(
                    COLUMN_DESCRIPTION2, """
        Bicep Curls primarily target the biceps, improving arm strength and definition.
        
        Hold a dumbbell in each hand with your palms facing forward, then curl the weights up toward your shoulders, keeping your elbows close to your sides.
        
        A staple exercise for arm toning and strength.
    """.trimIndent())
            },

                    ContentValues().apply {
                put(COLUMN_TITLE, "Burpees")
                put(COLUMN_IMAGE, "blackwomanmulticoloured7")
                put(COLUMN_DESCRIPTION1, "A full-body workout that improves strength and cardiovascular fitness.")
                put(
                    COLUMN_DESCRIPTION2, """
        Burpees are a high-intensity exercise that work the whole body, including the chest, arms, legs, and core.
        
        Begin in a standing position, drop into a squat, kick your feet back into a plank, perform a push-up, then jump back up to standing and explode into the air.
        
        Burpees are excellent for building strength and burning calories.
    """.trimIndent())
            },

                    ContentValues().apply {
                put(COLUMN_TITLE, "Russian Twists")
                put(COLUMN_IMAGE, "blackwomanmulticoloured8")
                put(COLUMN_DESCRIPTION1, "A rotational movement that targets the obliques and strengthens the core.")
                put(
                    COLUMN_DESCRIPTION2, """
        Russian Twists engage the oblique muscles and improve overall core stability.
        
        Sit on the floor with your knees bent and feet flat, then lean back slightly while keeping your back straight. Rotate your torso to each side, tapping the floor with your hands.
        
        A great way to enhance rotational strength and balance.
    """.trimIndent())
            },

                    ContentValues().apply {
                put(COLUMN_TITLE, "Deadlifts")
                put(COLUMN_IMAGE, "blackwomanmulticoloured9")
                put(COLUMN_DESCRIPTION1, "A compound exercise that strengthens the posterior chain.")
                put(
                    COLUMN_DESCRIPTION2, """
        Deadlifts target the hamstrings, glutes, and lower back, making it a key exercise for building total-body strength.
        
        Stand with your feet shoulder-width apart, grip a barbell, and lift the weight while maintaining a flat back and engaging your core.
        
        Essential for developing strength and power.
    """.trimIndent())
            }
        )
        val whitenblue = listOf(
            ContentValues().apply {
                put(COLUMN_TITLE, "Sunrise Flow Yoga")
                put(COLUMN_IMAGE, "whitewomanblueclotihng1")
                put(COLUMN_DESCRIPTION1, "A gentle yoga flow to awaken your senses.")
                put(
                    COLUMN_DESCRIPTION2, """
        Sunrise Flow Yoga is designed to gently awaken your body with flowing movements and mindful breathing. This session includes poses like Sun Salutations, Warrior I, and Cobra Pose.

        The sequence helps improve flexibility, boosts circulation, and sets a positive tone for the day. It's suitable for all fitness levels and can be modified to your comfort.

        Start your day energized and focused with this refreshing yoga flow.
        """.trimIndent())
                put(COLUMN_TIME_IN_SECONDS, 15)
                put(COLUMN_CALORIES_BURNED, 50)
                put(COLUMN_WORKOUTS_COMPLETED, 0)
                put(COLUMN_SECONDS_SPENT, 0)
                put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, 0.0)
                put(COLUMN_TOTAL_CALORIES_BURNED, 0)
                put(COLUMN_ENROLLED_STATUS, 1)
                put(COLUMN_PROGRAM_PREMIUM_STATUS, 0)
            },
            ContentValues().apply {
                put(COLUMN_TITLE, "Cardio Sculpt")
                put(COLUMN_IMAGE, "whitewomanblueclotihng2")
                put(COLUMN_DESCRIPTION1, "Burn fat and tone muscles with this dynamic workout.")
                put(
                    COLUMN_DESCRIPTION2, """
        Cardio Sculpt combines high-energy cardio exercises with muscle-toning movements. This session features jumping jacks, squats, and mountain climbers to target both strength and endurance.

        The workout is designed to maximize calorie burn while building lean muscle. Modifications are provided for all fitness levels, making it versatile and effective.

        Incorporate this workout to sculpt and strengthen your body efficiently.
        """.trimIndent())
                put(COLUMN_TIME_IN_SECONDS, 20)
                put(COLUMN_CALORIES_BURNED, 100)
                put(COLUMN_WORKOUTS_COMPLETED, 0)
                put(COLUMN_SECONDS_SPENT, 0)
                put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, 0.0)
                put(COLUMN_TOTAL_CALORIES_BURNED, 0)
                put(COLUMN_ENROLLED_STATUS, 1)
                put(COLUMN_PROGRAM_PREMIUM_STATUS, 0)
            },
            ContentValues().apply {
                put(COLUMN_TITLE, "Evening Calm Stretch")
                put(COLUMN_IMAGE, "whitewomanblueclotihng3")
                put(COLUMN_DESCRIPTION1, "Relax your body and mind with gentle evening stretches.")
                put(
                    COLUMN_DESCRIPTION2, """
        Evening Calm Stretch is the perfect way to unwind after a busy day. This session includes stretches like Seated Forward Fold, Cat-Cow, and Reclined Twist to release tension and promote relaxation.

        The routine is designed to improve flexibility and help you prepare for a restful night of sleep. Suitable for all levels, it can be customized to match your comfort.

        Incorporate this session into your evening routine for a peaceful end to your day.
        """.trimIndent())
                put(COLUMN_TIME_IN_SECONDS, 15)
                put(COLUMN_CALORIES_BURNED, 40)
                put(COLUMN_WORKOUTS_COMPLETED, 0)
                put(COLUMN_SECONDS_SPENT, 0)
                put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, 0.0)
                put(COLUMN_TOTAL_CALORIES_BURNED, 0)
                put(COLUMN_ENROLLED_STATUS, 1)
                put(COLUMN_PROGRAM_PREMIUM_STATUS, 0)
            }
        )

        // Insert each default workout into the database
        for (workout in defaultWorkouts) {
            db.insert(TABLE_STRETCH_PROGRAMS, null, workout)
        }
        for (workout in blackWomanMultiColoredWorkouts) {
            db.insert(TABLE_STRETCH_PROGRAMS, null, workout)
        }
        for (workout in whitenblue) {
            db.insert(TABLE_STRETCH_PROGRAMS, null, workout)
        }
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 23) {
            // Drop existing tables
            db.execSQL("DROP TABLE IF EXISTS $TABLE_STRETCH_PROGRAMS")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_WORKOUT_BUNDLES")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_USER_PROFILE")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_WORKOUT_BUNDLE_MAPPING")
            db.execSQL("DROP TABLE IF EXISTS bundle_workouts")

            // Create new tables
            onCreate(db)
        }
    }

    // Save user profile
    fun saveUserProfile(userProfile: UserProfile) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_NAME, userProfile.name)
            put(COLUMN_USER_EMAIL, userProfile.email)
            put(COLUMN_USER_AGE, userProfile.age)
            put(COLUMN_PROFILE_IMAGE, userProfile.profileImage)
            put(COLUMN_USER_PREMIUM_STATUS, userProfile.premiumStatus)
        }
        db.update(TABLE_USER_PROFILE, values, "$COLUMN_USER_ID = ?", arrayOf("1"))
        db.close()
    }

    // Get user profile
    fun getUserProfile(): UserProfile {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USER_PROFILE, null, "$COLUMN_USER_ID = ?", arrayOf("1"),
            null, null, null
        )
        var userProfile = UserProfile("Default User", 25, "user@example.com", 0, "")

        if (cursor.moveToFirst()) {
            userProfile = UserProfile(
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_AGE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_PREMIUM_STATUS)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROFILE_IMAGE))
            )
        }
        cursor.close()
        db.close()
        return userProfile
    }

    // Insert a workout/stretch program
    fun insertStretchProgram(
        title: String,
        description1: String,
        description2: String,
        image: String,
        timeInSeconds: Int,
        caloriesBurned: Int,
        workoutsCompleted: Int = 0,
        secondsSpent: Int = 0,
        averageWorkoutsPerDay: Int = 0,
        totalCaloriesBurned: Int = 0,
        isPremium: Boolean = false
    ) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_DESCRIPTION1, description1)
            put(COLUMN_DESCRIPTION2, description2)
            put(COLUMN_IMAGE, image)
            put(COLUMN_TIME_IN_SECONDS, timeInSeconds)
            put(COLUMN_CALORIES_BURNED, caloriesBurned)
            put(COLUMN_WORKOUTS_COMPLETED, workoutsCompleted)
            put(COLUMN_SECONDS_SPENT, secondsSpent)
            put(COLUMN_AVERAGE_WORKOUTS_PER_DAY, averageWorkoutsPerDay)
            put(COLUMN_TOTAL_CALORIES_BURNED, totalCaloriesBurned)
            put(COLUMN_PROGRAM_PREMIUM_STATUS, if (isPremium) 1 else 0) // Add this field
        }
        try {
            db.insert(TABLE_STRETCH_PROGRAMS, null, values)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
    }

    // Retrieve all stretch programs
    fun getAllStretchPrograms(): List<StretchProgram> {
        val programs = mutableListOf<StretchProgram>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_STRETCH_PROGRAMS"
        val cursor = db.rawQuery(query, null)

        cursor.use { // Ensures the cursor is closed automatically
            while (it.moveToNext()) {
                val program = StretchProgram(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID)),
                    title = it.getString(it.getColumnIndexOrThrow(COLUMN_TITLE)),
                    description1 = it.getString(it.getColumnIndexOrThrow(COLUMN_DESCRIPTION1)),
                    description2 = it.getString(it.getColumnIndexOrThrow(COLUMN_DESCRIPTION2)),
                    image = it.getString(it.getColumnIndexOrThrow(COLUMN_IMAGE)),
                    timeInSeconds = it.getInt(it.getColumnIndexOrThrow(COLUMN_TIME_IN_SECONDS)),
                    caloriesBurned = it.getInt(it.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED)),
                    workoutsCompleted = it.getInt(it.getColumnIndexOrThrow(COLUMN_WORKOUTS_COMPLETED)),
                    secondsSpent = it.getInt(it.getColumnIndexOrThrow(COLUMN_SECONDS_SPENT)),
                    averageWorkoutsPerDay = it.getDouble(it.getColumnIndexOrThrow(
                        COLUMN_AVERAGE_WORKOUTS_PER_DAY
                    )),
                    totalCaloriesBurned = it.getInt(it.getColumnIndexOrThrow(
                        COLUMN_TOTAL_CALORIES_BURNED
                    )),
                    enrolledStatus = it.getInt(it.getColumnIndexOrThrow(COLUMN_ENROLLED_STATUS)),
                    isPremium = it.getInt(it.getColumnIndexOrThrow(COLUMN_PROGRAM_PREMIUM_STATUS)) == 1
                )
                programs.add(program)
            }
        }

        db.close()
        return programs
    }

    fun getEnrolledPrograms(): List<StretchProgram> {
        val db = readableDatabase
        val programs = mutableListOf<StretchProgram>()
        val cursor = db.rawQuery("SELECT * FROM $TABLE_STRETCH_PROGRAMS WHERE $COLUMN_ENROLLED_STATUS = 1", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                val description1 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION1))
                val description2 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION2))
                val image = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE))
                val timeInSeconds = cursor.getInt(cursor.getColumnIndexOrThrow(
                    COLUMN_TIME_IN_SECONDS
                ))
                val caloriesBurned = cursor.getInt(cursor.getColumnIndexOrThrow(
                    COLUMN_CALORIES_BURNED
                ))
                val workoutsCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(
                    COLUMN_WORKOUTS_COMPLETED
                ))
                val secondsSpent = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SECONDS_SPENT))
                val averageWorkoutsPerDay = cursor.getDouble(cursor.getColumnIndexOrThrow(
                    COLUMN_AVERAGE_WORKOUTS_PER_DAY
                ))
                val totalCaloriesBurned = cursor.getInt(cursor.getColumnIndexOrThrow(
                    COLUMN_TOTAL_CALORIES_BURNED
                ))
                val enrolledStatus = cursor.getInt(cursor.getColumnIndexOrThrow(
                    COLUMN_ENROLLED_STATUS
                ))
                val isPremium = cursor.getInt(cursor.getColumnIndexOrThrow(
                    COLUMN_PROGRAM_PREMIUM_STATUS
                )) == 1

                programs.add(
                    StretchProgram(
                        id, title, description1, description2, image, timeInSeconds, caloriesBurned,
                        workoutsCompleted, secondsSpent, averageWorkoutsPerDay, totalCaloriesBurned,
                        enrolledStatus, isPremium, null
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return programs
    }

    fun getWorkoutBundles(): List<Pair<WorkoutBundle, List<StretchProgram>>> {
        val db = readableDatabase
        val bundles = mutableListOf<Pair<WorkoutBundle, List<StretchProgram>>>()

        val cursor = db.rawQuery("SELECT * FROM $TABLE_WORKOUT_BUNDLES", null)
        if (cursor.moveToFirst()) {
            do {
                val bundleId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BUNDLE_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BUNDLE_TITLE))
                val description = cursor.getString(cursor.getColumnIndexOrThrow(
                    COLUMN_BUNDLE_DESCRIPTION
                ))
                val image = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BUNDLE_IMAGE))
                val isPremium = cursor.getInt(cursor.getColumnIndexOrThrow(
                    COLUMN_BUNDLE_PREMIUM_STATUS
                )) == 1

                val workoutsQuery = """
                SELECT sp.* FROM $TABLE_STRETCH_PROGRAMS sp
                INNER JOIN bundle_workouts bw
                ON sp.$COLUMN_ID = bw.workout_id
                WHERE bw.bundle_id = ?
            """.trimIndent()

                val workoutsCursor = db.rawQuery(workoutsQuery, arrayOf(bundleId.toString()))
                val workouts = mutableListOf<StretchProgram>()

                if (workoutsCursor.moveToFirst()) {
                    do {
                        val program = StretchProgram(
                            id = workoutsCursor.getInt(workoutsCursor.getColumnIndexOrThrow(
                                COLUMN_ID
                            )),
                            title = workoutsCursor.getString(workoutsCursor.getColumnIndexOrThrow(
                                COLUMN_TITLE
                            )),
                            description1 = workoutsCursor.getString(workoutsCursor.getColumnIndexOrThrow(
                                COLUMN_DESCRIPTION1
                            )),
                            description2 = workoutsCursor.getString(workoutsCursor.getColumnIndexOrThrow(
                                COLUMN_DESCRIPTION2
                            )),
                            image = workoutsCursor.getString(workoutsCursor.getColumnIndexOrThrow(
                                COLUMN_IMAGE
                            )),
                            timeInSeconds = workoutsCursor.getInt(workoutsCursor.getColumnIndexOrThrow(
                                COLUMN_TIME_IN_SECONDS
                            )),
                            caloriesBurned = workoutsCursor.getInt(workoutsCursor.getColumnIndexOrThrow(
                                COLUMN_CALORIES_BURNED
                            )),
                            isPremium = workoutsCursor.getInt(workoutsCursor.getColumnIndexOrThrow(
                                COLUMN_PROGRAM_PREMIUM_STATUS
                            )) == 1
                        )
                        workouts.add(program)
                    } while (workoutsCursor.moveToNext())
                }
                workoutsCursor.close()

                bundles.add(Pair(WorkoutBundle(bundleId, title, description, image, isPremium, workouts), workouts))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return bundles
    }

    fun getAllWorkoutBundles(): List<WorkoutBundle>? {
        val db = readableDatabase
        val bundles = mutableListOf<WorkoutBundle>()
        val cursor = db.rawQuery("SELECT * FROM $TABLE_WORKOUT_BUNDLES", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BUNDLE_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BUNDLE_TITLE))
                val description = cursor.getString(cursor.getColumnIndexOrThrow(
                    COLUMN_BUNDLE_DESCRIPTION
                ))
                val image = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BUNDLE_IMAGE))
                val isPremium = cursor.getInt(cursor.getColumnIndexOrThrow(
                    COLUMN_BUNDLE_PREMIUM_STATUS
                )) == 1

                // Fetch workout IDs for this bundle
                val workoutIds = mutableListOf<Int>()
                val workoutCursor = db.rawQuery("SELECT workout_id FROM bundle_workouts WHERE bundle_id = ?", arrayOf(id.toString()))
                while (workoutCursor.moveToNext()) {
                    workoutIds.add(workoutCursor.getInt(workoutCursor.getColumnIndexOrThrow("workout_id")))
                }
                workoutCursor.close()

                // Create a list of StretchProgram objects using the workout IDs
                val workouts = workoutIds.map { workoutId ->
                    getStretchProgramById(workoutId) // Assuming you have a method to fetch StretchProgram by ID
                }

                bundles.add(WorkoutBundle(id, title, description, image, isPremium, workouts))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return bundles
    }

    fun getStretchProgramById(workoutId: Int): StretchProgram {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_STRETCH_PROGRAMS, // Replace with your table name
            null,
            "$COLUMN_ID = ?", // Replace with your workout ID column name
            arrayOf(workoutId.toString()),
            null,
            null,
            null
        )

        cursor.moveToFirst()
        val program = StretchProgram(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
            title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)), // Add this line
            description1 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION1)), // Add this line
            description2 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION2)), // Add this line
            image = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE)), // Add this line
            timeInSeconds = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TIME_IN_SECONDS)), // Add this line
            caloriesBurned = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED)), // Add this line
            isPremium = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PROGRAM_PREMIUM_STATUS)) == 1,
        )
        cursor.close()
        db.close()

        return program
    }

    fun getMoreCourses(): List<StretchProgram> {
        val db = readableDatabase
        val programs = mutableListOf<StretchProgram>()

        val query = "SELECT * FROM $TABLE_STRETCH_PROGRAMS WHERE $COLUMN_ENROLLED_STATUS = 0"
        val cursor = db.rawQuery(query, null)

        cursor.use {
            while (it.moveToNext()) {
                val program = StretchProgram(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID)),
                    title = it.getString(it.getColumnIndexOrThrow(COLUMN_TITLE)),
                    description1 = it.getString(it.getColumnIndexOrThrow(COLUMN_DESCRIPTION1)),
                    description2 = it.getString(it.getColumnIndexOrThrow(COLUMN_DESCRIPTION2)),
                    image = it.getString(it.getColumnIndexOrThrow(COLUMN_IMAGE)),
                    timeInSeconds = it.getInt(it.getColumnIndexOrThrow(COLUMN_TIME_IN_SECONDS)),
                    caloriesBurned = it.getInt(it.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED)),
                    workoutsCompleted = it.getInt(it.getColumnIndexOrThrow(COLUMN_WORKOUTS_COMPLETED)),
                    secondsSpent = it.getInt(it.getColumnIndexOrThrow(COLUMN_SECONDS_SPENT)),
                    averageWorkoutsPerDay = it.getDouble(it.getColumnIndexOrThrow(
                        COLUMN_AVERAGE_WORKOUTS_PER_DAY
                    )),
                    totalCaloriesBurned = it.getInt(it.getColumnIndexOrThrow(
                        COLUMN_TOTAL_CALORIES_BURNED
                    )),
                    enrolledStatus = it.getInt(it.getColumnIndexOrThrow(COLUMN_ENROLLED_STATUS)),
                    isPremium = it.getInt(it.getColumnIndexOrThrow(COLUMN_PROGRAM_PREMIUM_STATUS)) == 1
                )
                programs.add(program)
            }
        }
        db.close()
        return programs
    }

    fun incrementWorkoutStats(workoutId: Int, durationInSeconds: Int, caloriesBurned: Int) {
        val db = writableDatabase

        // Update workout-specific data
        val workoutQuery = """
    UPDATE $TABLE_STRETCH_PROGRAMS
    SET 
        $COLUMN_WORKOUTS_COMPLETED = $COLUMN_WORKOUTS_COMPLETED + 1,
        $COLUMN_SECONDS_SPENT = $COLUMN_SECONDS_SPENT + ?,
        $COLUMN_TOTAL_CALORIES_BURNED = $COLUMN_TOTAL_CALORIES_BURNED + ?
    WHERE $COLUMN_ID = ?
    """
        db.execSQL(
            workoutQuery,
            arrayOf(durationInSeconds, caloriesBurned, workoutId)
        )

        // Update total workout stats (e.g., in user profile or global stats)
        val globalStatsQuery = """
    UPDATE $TABLE_USER_PROFILE
    SET 
        $COLUMN_WORKOUTS_COMPLETED = $COLUMN_WORKOUTS_COMPLETED + 1
    WHERE $COLUMN_USER_ID = 1
    """
        db.execSQL(
            globalStatsQuery,
            arrayOf()
        )
        db.close()
    }

    fun getAllCourses(): List<Course> {
        val courseList = mutableListOf<Course>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM courses", null)
        if (cursor.moveToFirst()) {
            do {
                val course = Course(
                    id = cursor.getString(cursor.getColumnIndexOrThrow("id")),
                    title = cursor.getString(cursor.getColumnIndexOrThrow("title")),
                    description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
                )
                courseList.add(course)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return courseList
    }

    // New method to get the average number of workouts per day
    fun getAverageWorkoutsPerDay(): Double {
        val db = readableDatabase
        var avgWorkouts = 0.0

        val query = "SELECT AVG($COLUMN_WORKOUTS_COMPLETED) AS avgWorkouts FROM $TABLE_STRETCH_PROGRAMS"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            avgWorkouts = cursor.getDouble(cursor.getColumnIndexOrThrow("avgWorkouts"))
        }

        cursor.close()
        db.close()

        return avgWorkouts
    }

    // New method to get the total number of workouts completed
    fun getTotalWorkoutsCompleted(): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT SUM($COLUMN_WORKOUTS_COMPLETED) AS totalWorkouts FROM $TABLE_STRETCH_PROGRAMS", null)
        var totalWorkouts = 0
        if (cursor.moveToFirst()) {
            totalWorkouts = cursor.getInt(cursor.getColumnIndexOrThrow("totalWorkouts"))
        }
        cursor.close()
        return totalWorkouts
    }

    // New method to get the total time spent on workouts
    fun getTotalTimeSpentOnWorkouts(): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT SUM($COLUMN_SECONDS_SPENT) AS totalTime FROM $TABLE_STRETCH_PROGRAMS", null)
        var totalTime = 0
        if (cursor.moveToFirst()) {
            totalTime = cursor.getInt(cursor.getColumnIndexOrThrow("totalTime"))
        }
        cursor.close()
        return totalTime
    }

    fun enrollAllPremiumCourses() {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("enrolledStatus", 1) // Set to enrolled
        }
        db.update("StretchPrograms", values, "isPremium = 1", null) // Only premium courses
        db.close()
    }

    // New method to get the total calories burned
    fun getTotalCaloriesBurned(): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT SUM($COLUMN_CALORIES_BURNED) AS totalCalories FROM $TABLE_STRETCH_PROGRAMS", null)
        var totalCalories = 0
        if (cursor.moveToFirst()) {
            totalCalories = cursor.getInt(cursor.getColumnIndexOrThrow("totalCalories"))
        }
        cursor.close()
        return totalCalories
    }

    fun getWorkoutById(workoutId: Int): StretchProgram? {
        val db = readableDatabase
        var workout: StretchProgram? = null

        val query = "SELECT * FROM $TABLE_STRETCH_PROGRAMS WHERE $COLUMN_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(workoutId.toString()))

        if (cursor.moveToFirst()) {
            workout = StretchProgram(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                description1 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION1)),
                description2 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION2)),
                image = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE)),
                timeInSeconds = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TIME_IN_SECONDS)),
                caloriesBurned = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED)),
                workoutsCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(
                    COLUMN_WORKOUTS_COMPLETED
                )),
                secondsSpent = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SECONDS_SPENT)),
                averageWorkoutsPerDay = cursor.getDouble(cursor.getColumnIndexOrThrow(
                    COLUMN_AVERAGE_WORKOUTS_PER_DAY
                )),
                totalCaloriesBurned = cursor.getInt(cursor.getColumnIndexOrThrow(
                    COLUMN_TOTAL_CALORIES_BURNED
                )),
                enrolledStatus = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ENROLLED_STATUS)),
                isPremium = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PROGRAM_PREMIUM_STATUS)) == 1
            )
        }

        cursor.close()
        db.close()
        return workout
    }


    fun getWorkoutsForBundle(bundleId: Int): List<StretchProgram> {
        val db = readableDatabase
        val workouts = mutableListOf<StretchProgram>()

        val query = """
        SELECT sp.* FROM $TABLE_STRETCH_PROGRAMS sp
        INNER JOIN $TABLE_WORKOUT_BUNDLE_MAPPING wbm
        ON sp.$COLUMN_ID = wbm.$COLUMN_MAPPING_WORKOUT_ID
        WHERE wbm.$COLUMN_MAPPING_BUNDLE_ID = ?
    """
        val cursor = db.rawQuery(query, arrayOf(bundleId.toString()))

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                val description1 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION1))
                val description2 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION2))
                val image = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE))
                val timeInSeconds = cursor.getInt(cursor.getColumnIndexOrThrow(
                    COLUMN_TIME_IN_SECONDS
                ))
                val caloriesBurned = cursor.getInt(cursor.getColumnIndexOrThrow(
                    COLUMN_CALORIES_BURNED
                ))
                val workoutsCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(
                    COLUMN_WORKOUTS_COMPLETED
                ))
                val secondsSpent = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SECONDS_SPENT))
                val averageWorkoutsPerDay = cursor.getDouble(cursor.getColumnIndexOrThrow(
                    COLUMN_AVERAGE_WORKOUTS_PER_DAY
                ))
                val totalCaloriesBurned = cursor.getInt(cursor.getColumnIndexOrThrow(
                    COLUMN_TOTAL_CALORIES_BURNED
                ))
                val enrolledStatus = cursor.getInt(cursor.getColumnIndexOrThrow(
                    COLUMN_ENROLLED_STATUS
                ))
                val isPremium = cursor.getInt(cursor.getColumnIndexOrThrow(
                    COLUMN_PROGRAM_PREMIUM_STATUS
                )) == 1

                workouts.add(
                    StretchProgram(
                        id, title, description1, description2, image, timeInSeconds, caloriesBurned,
                        workoutsCompleted, secondsSpent, averageWorkoutsPerDay, totalCaloriesBurned, enrolledStatus, isPremium
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return workouts
    }

    // Database Helper Class (datavaseInfo)
    fun getTotalEnrolledCourses(): Int {
        val db = readableDatabase
        val query = "SELECT COUNT(*) FROM $TABLE_STRETCH_PROGRAMS WHERE $COLUMN_ENROLLED_STATUS = 1"
        val cursor = db.rawQuery(query, null)
        var count = 0

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }

        cursor.close()
        db.close()
        return count
    }

    fun unlockAllPremiumWorkouts() {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ENROLLED_STATUS, 1) // Mark as enrolled
        }
        // Unlock only premium workouts
        val rowsUpdated = db.update(TABLE_STRETCH_PROGRAMS, values, "$COLUMN_PROGRAM_PREMIUM_STATUS = 1", null)
        db.close()
        Log.d("datavaseInfo", "Unlocked $rowsUpdated premium workouts")
    }

    fun lockAllPremiumWorkouts() {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ENROLLED_STATUS, 0) // Mark as not enrolled
        }
        // Lock only premium workouts
        val rowsUpdated = db.update(TABLE_STRETCH_PROGRAMS, values, "$COLUMN_PROGRAM_PREMIUM_STATUS = 1", null)
        db.close()
        Log.d("datavaseInfo", "Locked $rowsUpdated premium workouts")
    }

    fun getUnenrolledWorkouts(): List<StretchProgram> {
        val unenrolledWorkouts = mutableListOf<StretchProgram>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_STRETCH_PROGRAMS WHERE $COLUMN_ENROLLED_STATUS = 0"
        val cursor = db.rawQuery(query, null)

        cursor.use {
            while (it.moveToNext()) {
                val program = StretchProgram(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID)),
                    title = it.getString(it.getColumnIndexOrThrow(COLUMN_TITLE)),
                    description1 = it.getString(it.getColumnIndexOrThrow(COLUMN_DESCRIPTION1)),
                    description2 = it.getString(it.getColumnIndexOrThrow(COLUMN_DESCRIPTION2)),
                    image = it.getString(it.getColumnIndexOrThrow(COLUMN_IMAGE)),
                    timeInSeconds = it.getInt(it.getColumnIndexOrThrow(COLUMN_TIME_IN_SECONDS)),
                    caloriesBurned = it.getInt(it.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED)),
                    isPremium = it.getInt(it.getColumnIndexOrThrow(COLUMN_PROGRAM_PREMIUM_STATUS)) == 1,
                    enrolledStatus = 0 // Default as not enrolled
                )
                unenrolledWorkouts.add(program)
            }
        }
        db.close()
        return unenrolledWorkouts
    }

    fun enrollWorkout(workoutId: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ENROLLED_STATUS, 1) // Mark as enrolled
        }
        db.update(TABLE_STRETCH_PROGRAMS, values, "$COLUMN_ID = ?", arrayOf(workoutId.toString()))
        db.close()
    }

    fun getFavoriteWorkout(): StretchProgram? {
        val db = readableDatabase
        val query = """
        SELECT * FROM $TABLE_STRETCH_PROGRAMS
        ORDER BY $COLUMN_WORKOUTS_COMPLETED DESC
        LIMIT 1
    """
        val cursor = db.rawQuery(query, null)
        var favoriteWorkout: StretchProgram? = null

        if (cursor.moveToFirst()) {
            favoriteWorkout = StretchProgram(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                description1 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION1)),
                description2 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION2)),
                image = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE)),
                timeInSeconds = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TIME_IN_SECONDS)),
                caloriesBurned = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CALORIES_BURNED)),
                workoutsCompleted = cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                        COLUMN_WORKOUTS_COMPLETED
                    )
                ),
                secondsSpent = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SECONDS_SPENT)),
                averageWorkoutsPerDay = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(
                        COLUMN_AVERAGE_WORKOUTS_PER_DAY
                    )
                ),
                totalCaloriesBurned = cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                        COLUMN_TOTAL_CALORIES_BURNED
                    )
                ),
                enrolledStatus = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ENROLLED_STATUS)),
                isPremium = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PROGRAM_PREMIUM_STATUS)) == 1
            )
        }

        cursor.close()
        db.close()
        return favoriteWorkout
    }
}

// Data class to represent a Stretch Program
@Parcelize
class StretchProgram(
    val id: Int,
    val title: String,
    val description1: String,
    val description2: String,
    val image: String, // New Field for storing the drawable resource name
    val timeInSeconds: Int,
    val caloriesBurned: Int,
    val workoutsCompleted: Int = 0,
    val secondsSpent: Int = 0,
    val averageWorkoutsPerDay: Double = 0.0,
    val totalCaloriesBurned: Int = 0,
    val enrolledStatus: Int = 0,
    val isPremium: Boolean,
    val bundleId: Int? = null, // Add this property
    val workouts: List<StretchProgram> = emptyList()
) : Parcelable {
    override fun describeContents(): Int = 0

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(description1)
        parcel.writeString(description2)
        parcel.writeString(image)
        parcel.writeInt(timeInSeconds)
        parcel.writeInt(caloriesBurned)
        parcel.writeInt(workoutsCompleted)
        parcel.writeInt(secondsSpent)
        parcel.writeDouble(averageWorkoutsPerDay)
        parcel.writeInt(totalCaloriesBurned)
        parcel.writeInt(enrolledStatus)
        parcel.writeByte(if (isPremium) 1 else 0)
        parcel.writeValue(bundleId)
        parcel.writeTypedList(workouts)
    }

    companion object CREATOR : Parcelable.Creator<StretchProgram> {
        override fun createFromParcel(parcel: Parcel): StretchProgram {
            return StretchProgram(
                id = parcel.readInt(),
                title = parcel.readString() ?: "",
                description1 = parcel.readString() ?: "",
                description2 = parcel.readString() ?: "",
                image = parcel.readString() ?: "",
                timeInSeconds = parcel.readInt(),
                caloriesBurned = parcel.readInt(),
                workoutsCompleted = parcel.readInt(),
                secondsSpent = parcel.readInt(),
                averageWorkoutsPerDay = parcel.readDouble(),
                totalCaloriesBurned = parcel.readInt(),
                enrolledStatus = parcel.readInt(),
                isPremium = parcel.readByte() != 0.toByte(),
                bundleId = parcel.readValue(Int::class.java.classLoader) as? Int,
                workouts = parcel.createTypedArrayList(CREATOR) ?: emptyList()

            )
        }

        override fun newArray(size: Int): Array<StretchProgram?> = arrayOfNulls(size)
    }
}

// Data class for User Profile
data class UserProfile(
    val name: String,
    val age: Int,
    val email: String,
    val premiumStatus: Int, // 0 = Free, 1 = Premium
    val profileImage: String
)

data class WorkoutBundle(
    val id: Int,
    val title: String,
    val description: String,
    val image: String, // New field for the bundle cover image
    val isPremium: Boolean,
    val workouts: List<StretchProgram>
)

// Data class for Course
data class Course(
    val id: String,
    val title: String,
    val description: String
)
