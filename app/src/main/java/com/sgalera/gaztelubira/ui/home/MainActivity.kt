package com.sgalera.gaztelubira.ui.home

import android.os.Build
import android.os.Bundle
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
import com.sgalera.gaztelubira.domain.model.UIState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val notificationsCode = 100

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == notificationsCode){
            if (grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, getString(R.string.notifications_granted), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.notifications_denied), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initUI() {
        initVariables()
        checkNotifications()
    }

    private fun checkNotifications() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), notificationsCode)
        }
    }

    private fun initVariables() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.state.collect { state ->
                    when (state) {
                        UIState.Loading -> { onLoading() }
                        is UIState.Error -> { onStateError() }
                        is UIState.Success -> { onStateSuccess() }
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