package com.example.mypets

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mypets.databinding.ActivityMainBinding
import com.example.mypets.workers.NotificationsWorker

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavView: BottomNavigationView

    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_food, R.id.navigation_info, R.id.navigation_home, R.id.navigation_care, R.id.navigation_health
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener {_, destination, _ ->
            when (destination.id) {
                R.id.startScreenFragment -> hideBottomNavigation()
                R.id.choosePetFragment -> hideBottomNavigation()
                R.id.generalScheduleFragment -> hideBottomNavigation()
                else -> showBottomNavigation()
            }
        }

        if (intent != null) {
            val petId = intent.getIntExtra(NotificationsWorker.PET_ID_KEY, -1)
            if (petId != -1) {
                sharedViewModel.setCurrentPetId(petId)
                navController.navigate(R.id.navigation_home)
            }
        }
    }

    private fun showBottomNavigation() {
        bottomNavView.visibility = View.VISIBLE
    }

    private fun hideBottomNavigation() {
        bottomNavView.visibility = View.GONE
    }

}