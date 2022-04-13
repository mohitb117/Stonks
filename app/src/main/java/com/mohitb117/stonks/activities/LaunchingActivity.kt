package com.mohitb117.stonks.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.mohitb117.stonks.R
import com.mohitb117.stonks.databinding.ActivityLaunchingBinding
import com.mohitb117.stonks.STOCK_KEY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LaunchingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLaunchingBinding
    private lateinit var navController: NavController

    private val viewModel: LaunchActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLaunchingBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_launching) as NavHostFragment)
                .navController

        // Show stock portfolio details.
        viewModel
            .stockSelected
            .observe(this) {
                val bundle = Bundle().apply { putParcelable(STOCK_KEY, it) }
                navController.navigate(R.id.navigation_details, bundle)
            }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.portfolio_endpoint_selector, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.good -> {
                viewModel.onGoodPortfolioEndpointSelected()
                true
            }
            R.id.malformed -> {
                viewModel.onMalformedPortfolioEndpointSelected()
                true
            }
            R.id.empty -> {
                viewModel.onEmptyEndpointSelected()
                true
            }
            else -> {
                false
            }
        }
    }
}