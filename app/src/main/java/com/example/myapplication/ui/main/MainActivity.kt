package com.example.myapplication.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.myapplication.R

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment? ?: return

        val navController = host.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//        navigateToTrackingFragment(intent)
//    }
//
//    private fun navigateToTrackingFragment(intent: Intent?) {
//        if (intent?.action == RESUME_SERVICE) {
//            findNavController(R.id.fragmentContainerView).navigate(R.id.action_move_to_homeFragment)
//        }
//    }
}