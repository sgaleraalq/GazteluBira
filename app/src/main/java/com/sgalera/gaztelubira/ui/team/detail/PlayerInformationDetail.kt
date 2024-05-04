package com.sgalera.gaztelubira.ui.team.detail

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.navArgs
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.ActivityPlayerInformationDetailBinding

class PlayerInformationDetail : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerInformationDetailBinding
    private val playerInformationViewModel: PlayerInformationViewModel by viewModels()
    private val args: PlayerInformationDetailArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerInformationDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}