package com.sgalera.gaztelubira.ui.insert_game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.sgalera.gaztelubira.databinding.ActivityInsertGameBinding
import com.sgalera.gaztelubira.domain.model.TeamInformation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InsertGameActivity : AppCompatActivity() {

    companion object {
        private const val ID = "id"
        private const val LEAGUE_JOURNEY = "leagueJourney"
        fun create(context: Context, id: Int, leagueJourney: Int) = Intent(context, InsertGameActivity::class.java).apply {
            putExtra(ID, id)
            putExtra(LEAGUE_JOURNEY, leagueJourney)
        }
    }

    private lateinit var binding: ActivityInsertGameBinding
    private var id: Int = 0
    private var journey: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Get the passed arguments
        id = intent.getIntExtra(ID, 0)
        journey = intent.getIntExtra(LEAGUE_JOURNEY, 0)
        Log.i("InsertGameActivity", "id: $id, journey: $journey")
//        initUI()
        initListeners()
    }

    private fun initListeners(){
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initUI() {
//        fetchTeamsInfo()
    }

}


