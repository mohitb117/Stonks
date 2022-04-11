package com.mohitb117.stonks.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.mohitb117.stonks.R
import com.mohitb117.stonks.databinding.ActivityLaunchingBinding
import com.mohitb117.stonks.STOCK_KEY
import com.mohitb117.stonks.datamodels.Stock
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LaunchingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLaunchingBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLaunchingBinding.inflate(layoutInflater)

        setContentView(binding.root)

        navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_launching) as NavHostFragment)
            .navController
    }

    fun gotoDetails(stock: Stock) {
        navController.navigate(
            R.id.navigation_details,
            Bundle().apply { putParcelable(STOCK_KEY, stock) }
        )
    }
}