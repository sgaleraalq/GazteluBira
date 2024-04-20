package com.sgalera.gaztelubira.ui.insert_game

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sgalera.gaztelubira.databinding.ActivityInsertGameBinding
import com.sgalera.gaztelubira.domain.model.Team

class InsertGame : AppCompatActivity() {
    private var homeTeam: Team? = null
    private var awayTeam: Team? = null
    private var homeGoals: Int = 0
    private var awayGoals: Int = 0

    private lateinit var binding: ActivityInsertGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
        initListeners()
    }

    private fun initUI() {
        insertTeams()
    }

    private fun insertTeams() {
        binding.tvHomeTeam.text = this.getString(homeTeam!!.name)
        binding.ivHomeTeam.setImageResource(homeTeam!!.img)

        binding.tvAwayTeam.text = this.getString(awayTeam!!.name)
        binding.ivAwayTeam.setImageResource(awayTeam!!.img)

        binding.ivHomeTeam.visibility = View.VISIBLE
        binding.ivAwayTeam.visibility = View.VISIBLE

        binding.psHomeTeam.clearSelectedItem()
        binding.psAwayTeam.clearSelectedItem()
    }
    private fun initListeners() {
        binding.btnContinue.isEnabled = checkFields()
        binding.btnContinue.setOnClickListener {
            insertGame()
            val intent = Intent(this, InsertGameDetailActivity::class.java)
            intent.apply {
                putExtra("homeTeam", homeTeam!!.name)
                putExtra("awayTeam", awayTeam!!.name)
                putExtra("homeGoals", homeGoals)
                putExtra("awayGoals", awayGoals)
            }
        }
    }

    private fun insertGame() {
        TODO("Not yet implemented")
    }

    private fun checkFields(): Boolean {
        return if (homeTeam != null && awayTeam != null && binding.etHomeGoals.text.toString()
                .isNotEmpty() && binding.etAwayGoals.text.toString().isNotEmpty()
        ) {
            true
        } else {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            false
        }
    }
}