package com.sgalera.gaztelubira.ui.matches.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navArgs
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.ActivityDetailMatchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailMatchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMatchBinding
    private val matchViewModel: DetailMatchViewModel by viewModels()
    private val args: DetailMatchActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_match)
        binding = ActivityDetailMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        initUIState()
        initListeners()
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                matchViewModel.state.collect{
                    when (it) {
                        DetailMatchState.Loading -> loadingState()
                        is DetailMatchState.Error -> errorState(it.error)
                        is DetailMatchState.Success -> successState(it)
                    }
                }
            }
        }
    }

    private fun loadingState() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun errorState(error: String) {
        binding.progressBar.visibility = View.INVISIBLE
        Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
    }

    private fun successState(state: DetailMatchState.Success){
        binding.progressBar.visibility = View.INVISIBLE
        println(state.match)
    }

    private fun initListeners() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }
}