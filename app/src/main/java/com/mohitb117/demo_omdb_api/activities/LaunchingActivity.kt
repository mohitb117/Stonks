package com.mohitb117.demo_omdb_api.activities

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.mohitb117.demo_omdb_api.IMDB_ID_KEY
import com.mohitb117.demo_omdb_api.R
import com.mohitb117.demo_omdb_api.databinding.ActivityLaunchingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LaunchingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLaunchingBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLaunchingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView: BottomNavigationView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_launching)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_search, R.id.navigation_favourites)
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        bottomNavigationView.setupWithNavController(navController)
    }

    fun gotoDetails(imdbId: String) {
        navController.navigate(
            R.id.navigation_details,
            Bundle().apply { putString(IMDB_ID_KEY, imdbId) }
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }
}