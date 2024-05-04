package com.sgalera.gaztelubira.ui.team.detail

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.ActivityPlayerInformationDetailBinding

class PlayerInformationDetail : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerInformationDetailBinding
    private val playerInformationViewModel: PlayerInformationViewModel by viewModels()
    private val args: PlayerInformationDetailArgs by navArgs()

    private lateinit var playerName: String
    private lateinit var playerImage: String
    private var playerReference: String? = null
    private var playerDorsal = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerInformationDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initComponents()
        initUI()
        initListeners()
    }

    private fun initComponents() {
        playerName = args.name
        playerImage = args.image
        playerReference = args.reference
        playerDorsal = args.dorsal
    }

    private fun initUI() {
        if (playerReference != null){
            println("heree")
        } else{
            initManagerComponents()
        }
    }

    private fun initListeners() {
        binding.ivBack.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initManagerComponents() {
        binding.pbLoadingPlayerInformation.visibility = View.GONE
        binding.clPlayerStats.visibility = View.GONE
        binding.parentView.visibility = View.VISIBLE
        binding.tvPlayerName.text = playerName
        binding.tvPlayerPosition.text = getString(R.string.manager)
        Glide.with(this)
            .load(playerImage)
            .centerCrop()
            .into(binding.ivPlayerImage)
    }
}