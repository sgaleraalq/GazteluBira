package com.sgalera.gaztelubira.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        initVariables()
    }

    private fun initVariables() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.state.collect { state ->
                    when (state) {
                        MainState.Loading -> { onLoading() }
                        is MainState.Error -> { onStateError() }
                        is MainState.Success -> { onStateSuccess() }
                    }
                }
            }
        }
    }

    private fun onLoading() {
        mainViewModel.getCredentials()
    }

    private fun onStateSuccess() {
        initNavigation()
    }

    private fun onStateError() {
        Toast.makeText(this, getString(R.string.error_fetching_credentials), Toast.LENGTH_SHORT).show()
    }

    private fun initNavigation() {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHost.navController
        binding.bottomNavView.setupWithNavController(navController)
    }
}