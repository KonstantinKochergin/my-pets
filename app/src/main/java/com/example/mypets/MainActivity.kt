package com.example.mypets

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mypets.data.layer.SynchronizationConstants
import com.example.mypets.data.layer.firebase.database.FirebaseDatabaseHelper
import com.example.mypets.databinding.ActivityMainBinding
import com.example.mypets.workers.NotificationsWorker
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavView: BottomNavigationView

    private val sharedViewModel: SharedViewModel by viewModels()

    private lateinit var firebaseDBHelper: FirebaseDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

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
                R.id.synchronizationFragment -> hideBottomNavigation()
                else -> showBottomNavigation()
            }
        }

        val prefs = getSharedPreferences(SynchronizationConstants.PREFS_FILE_NAME, Context.MODE_PRIVATE)
        val userId = prefs.getString(SynchronizationConstants.USER_ID_PREFS_KEY, null)

        if (userId != null) {
            val petsListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("debug-tag", snapshot.toString())
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            }
            FirebaseDatabaseHelper.database.child(userId).child(SynchronizationConstants.PETS_PATH).addValueEventListener(petsListener)
        }

        // Переход на страницу питомца по клику на уведомление
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