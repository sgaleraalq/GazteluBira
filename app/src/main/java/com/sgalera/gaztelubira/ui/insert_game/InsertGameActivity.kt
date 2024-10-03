package com.sgalera.gaztelubira.ui.insert_game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.ActivityInsertGameBinding
import com.sgalera.gaztelubira.ui.insert_game.InsertGameExpandable.MATCH_TYPE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InsertGameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInsertGameBinding
    private val insertGameViewModel by viewModels<InsertGameViewModel>()
    private var id: Int = 0
    private var journey: Int = 0

    companion object {
        private const val ID = "id"
        private const val LEAGUE_JOURNEY = "leagueJourney"
        fun create(context: Context, id: Int, leagueJourney: Int) = Intent(context, InsertGameActivity::class.java).apply {
            putExtra(ID, id)
            putExtra(LEAGUE_JOURNEY, leagueJourney)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Get the passed arguments
        id = intent.getIntExtra(ID, 0)
        journey = intent.getIntExtra(LEAGUE_JOURNEY, 0)
        Log.i("InsertGameActivity", "id: $id, journey: $journey")
        initUI()
    }

    private fun initUI() {
        initExpandable()
        initListeners()

    }

    private fun initExpandable() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                insertGameViewModel.expandable.collect { expandable ->
                    when (expandable) {
                        MATCH_TYPE -> {
                            showItem(binding.ivArrowMatchType, binding.llMatchType, binding.secondDivider)
                        }
                        InsertGameExpandable.MATCH_LOCAL -> {
                            showItem(binding.ivArrowMatchLocal, binding.llMatchLocal, binding.fourthDivider)
                        }
                        InsertGameExpandable.MATCH_CONTENT -> {

                        }
                        null -> { deselectAll() }
                    }
                }
            }
        }
    }

    private fun initListeners(){
        binding.ivBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.cvMatchType.setOnClickListener{ insertGameViewModel.onExpandableChanged(MATCH_TYPE) }
        binding.cvMatchLocal.setOnClickListener{ insertGameViewModel.onExpandableChanged(InsertGameExpandable.MATCH_LOCAL) }
    }

    private fun showItem(arrow: ImageView, childView: LinearLayout, divider: View) {
        deselectAll()
        childView.setBackgroundColor(resources.getColor(R.color.grey_selected, null))
        divider.visibility = LinearLayout.VISIBLE
        arrow.rotation = if (arrow.rotation == 0f) 90f else 0f
        childView.visibility = if (childView.visibility == LinearLayout.VISIBLE) LinearLayout.GONE else LinearLayout.VISIBLE
    }

    private fun deselectAll() {
        binding.cvMatchType.setCardBackgroundColor(resources.getColor(R.color.primary, null))
        binding.cvMatchLocal.setCardBackgroundColor(resources.getColor(R.color.primary, null))
        binding.tvMatchType.setTextColor(resources.getColor(R.color.white, null))
        binding.tvMatchLocal.setTextColor(resources.getColor(R.color.white, null))
        binding.secondDivider.visibility = LinearLayout.GONE
        binding.fourthDivider.visibility = LinearLayout.GONE
        binding.ivArrowMatchType.rotation = 0f
        binding.ivArrowMatchLocal.rotation = 0f
        binding.llMatchType.visibility = LinearLayout.GONE
        binding.llMatchLocal.visibility = LinearLayout.GONE
    }
}


