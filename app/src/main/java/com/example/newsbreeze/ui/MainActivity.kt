package com.example.newsbreeze.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.newsbreeze.R
import com.example.newsbreeze.databinding.ActivityMainBinding
import com.example.newsbreeze.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    var _binding: ActivityMainBinding? = null
    val binding: ActivityMainBinding get() = _binding!!

    val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { controller, destination, arguments ->  }
        //call the API

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    companion object{
        var isPrimaryAPICallDone = false;
    }
}