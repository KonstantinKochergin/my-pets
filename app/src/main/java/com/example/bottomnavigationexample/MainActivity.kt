package com.example.bottomnavigationexample

import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.example.bottomnavigationexample.data.layer.database.AppDatabase
import com.example.bottomnavigationexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavView: BottomNavigationView

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

    }

    private fun showBottomNavigation() {
        bottomNavView.visibility = View.VISIBLE
    }

    private fun hideBottomNavigation() {
        bottomNavView.visibility = View.GONE
    }

}