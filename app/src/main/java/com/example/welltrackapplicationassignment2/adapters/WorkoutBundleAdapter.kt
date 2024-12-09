package com.example.welltrackapplicationassignment2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.welltrackapplicationassignment2.R
import com.example.welltrackapplicationassignment2.WorkoutBundle
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.welltrackapplicationassignment2.SupplementalActivities.WorkoutPackDetailsActivity
import com.example.welltrackapplicationassignment2.SupplementalActivities.WorkoutDetailsActivity
import com.example.welltrackapplicationassignment2.databinding.ActivityWorkoutPackDetailsBinding
import com.example.welltrackapplicationassignment2.StretchProgram
import com.example.welltrackapplicationassignment2.datavaseInfo

class WorkoutBundleAdapter(
    private val bundles: List<WorkoutBundle>,
    private val onBundleClick: (WorkoutBundle) -> Unit
) : RecyclerView.Adapter<WorkoutBundleAdapter.BundleViewHolder>() {

    class BundleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageBundle: ImageView = itemView.findViewById(R.id.imageBundle)
        val textBundleTitle: TextView = itemView.findViewById(R.id.textBundleTitle)
        val textBundleDescription: TextView = itemView.findViewById(R.id.textBundleDescription)
        val premiumBadge: ImageView = itemView.findViewById(R.id.premiumBadge) // Optional premium indicator
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BundleViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout_bundle, parent, false)
        return BundleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BundleViewHolder, position: Int) {
        val bundle = bundles[position] // Get the current bundle
        val context = holder.itemView.context

        // Set bundle title and description
        holder.textBundleTitle.text = bundle.title
        holder.textBundleDescription.text = bundle.description

        // Load cover image
        val resourceId = context.resources.getIdentifier(bundle.image, "drawable", context.packageName)
        holder.imageBundle.setImageResource(resourceId)

        // Show premium badge if applicable
        if (bundle.isPremium) {
            holder.premiumBadge.visibility = View.VISIBLE
        } else {
            holder.premiumBadge.visibility = View.GONE
        }

        // Handle click to show workouts in the bundle
        holder.itemView.setOnClickListener {
            val intent = Intent(context, WorkoutPackDetailsActivity::class.java)
            intent.putExtra("PACK_ID", bundle.id) // Pass the bundle ID
            context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int = bundles.size
}

// In WorkoutPackDetailsActivity, after displaying the list of workouts, implement click handling:
// When a workout is clicked, navigate back to WorkoutDetailsActivity to show workout details.

class WorkoutPackDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutPackDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutPackDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get pack ID from intent
        val packId = intent.getIntExtra("PACK_ID", -1)

        if (packId != -1) {
            // Fetch pack details and associated workouts
            val database = datavaseInfo(this)
            val pack = database.getAllWorkoutBundles()?.find { it.id == packId }
            val workouts = database.getWorkoutsForBundle(packId)

            // Bind pack details
            pack?.let {
                binding.textPackTitle.text = it.title
                binding.textPackDescription.text = it.description
            }

            // Set up RecyclerView for workouts in the pack
            setupRecyclerView(workouts)
        }
    }

    private fun setupRecyclerView(workouts: List<StretchProgram>) {
        val adapter = CourseAdapter(workouts) { workout ->
            // Handle click to navigate to course details
            val intent = Intent(this, WorkoutDetailsActivity::class.java)
            intent.putExtra("COURSE_ID", workout.id)
            startActivity(intent)
        }
        binding.recyclerPackWorkouts.layoutManager = LinearLayoutManager(this)
        binding.recyclerPackWorkouts.adapter = adapter
    }
}
