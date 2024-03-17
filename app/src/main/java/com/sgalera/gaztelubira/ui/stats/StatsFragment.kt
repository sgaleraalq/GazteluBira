package com.sgalera.gaztelubira.ui.stats

import com.sgalera.gaztelubira.ui.manager.PasswordManager
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sgalera.gaztelubira.BuildConfig
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.FragmentStatsBinding
import com.sgalera.gaztelubira.domain.model.players.PlayerStats
import com.sgalera.gaztelubira.ui.manager.SharedPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StatsFragment : Fragment() {
    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!
    private val statsViewModel by viewModels<StatsViewModel>()
    private lateinit var playerStats: List<PlayerStats>
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        initToken()
        initComponents()
        initListeners()
    }

    private fun initToken() {
        sharedPreferences = SharedPreferences(requireContext())
        val token = sharedPreferences.getAdminToken()
        if (token != null) {
            binding.tvLoggedAsAdmin.visibility = View.VISIBLE
            binding.cvAdmin.visibility = View.INVISIBLE
        }
    }

    private fun initListeners() {
        binding.cvAdmin.setOnClickListener {
            showAdminPopUp()
        }
    }

    private fun startLateListeners() {
        binding.percentageIcon.setOnClickListener {
            clearAllBackgrounds()
            binding.percentageIcon.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.grey_80_opacity
                )
            )
            successState(playerStats.sortedByDescending { it.percentage })
        }
        binding.goalsIcon.setOnClickListener {
            clearAllBackgrounds()
            binding.goalsIcon.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.grey_80_opacity
                )
            )
            successState(playerStats.sortedByDescending { it.goals })
        }
        binding.assistsIcon.setOnClickListener {
            clearAllBackgrounds()
            binding.assistsIcon.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.grey_80_opacity
                )
            )
            successState(playerStats.sortedByDescending { it.assists })
        }
        binding.bigMistakesIcon.setOnClickListener {
            clearAllBackgrounds()
            binding.bigMistakesIcon.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.grey_80_opacity
                )
            )
            successState(playerStats.sortedByDescending { it.bigMistakes })
        }
        binding.cleanSheetIcon.setOnClickListener {
            clearAllBackgrounds()
            binding.cleanSheetIcon.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.grey_80_opacity
                )
            )
            successState(playerStats.sortedByDescending { it.cleanSheet })
        }
        binding.gamesIcon.setOnClickListener {
            clearAllBackgrounds()
            binding.gamesIcon.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.grey_80_opacity
                )
            )
            successState(playerStats.sortedByDescending { it.gamesPlayed })
        }
    }

    private fun clearAllBackgrounds() {
        binding.percentageIcon.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.primary
            )
        )
        binding.goalsIcon.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.primary
            )
        )
        binding.assistsIcon.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.primary
            )
        )
        binding.bigMistakesIcon.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.primary
            )
        )
        binding.cleanSheetIcon.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.primary
            )
        )
        binding.gamesIcon.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.primary
            )
        )
    }


    private fun initComponents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                statsViewModel.state.collect { it ->
                    when (it) {
                        StatsState.Loading -> loadingState()
                        is StatsState.Success -> {
                            playerStats = it.data.sortedByDescending { it.percentage }
                            startLateListeners()
                            successState(playerStats)
                            showImage( playerStats[0] )
                        }
                        is StatsState.Error -> errorState(it.message)
                    }
                }
            }
        }
    }

    private fun showImage(player: PlayerStats) {
        binding.ivIconGoals.visibility = View.VISIBLE
        binding.ivIconAssists.visibility = View.VISIBLE
        binding.pbLoadingChampion.visibility = View.INVISIBLE
        binding.tvNameChampion.text = player.name.name
        binding.tvChampionGoals.text = player.goals.toString()
        binding.tvChampionAssists.text = player.assists.toString()
        binding.ivChampion.setImageResource(player.name.img)
    }

    private fun loadingState() {
        binding.pbLoadingChampion.visibility = View.VISIBLE
        binding.pbLoading.visibility = View.VISIBLE
    }

    private fun errorState(error: String) {
        binding.pbLoadingChampion.visibility = View.GONE
        binding.pbLoading.visibility = View.GONE
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    private fun successState(stats: List<PlayerStats>) {
        binding.tlClassification.removeAllViews()
        var index = 0
        for (player in stats) {
            val view = setRowPlayerView(player, index)
            binding.tlClassification.addView(view)
            index += 1
        }
    }

    private fun showAdminPopUp() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.item_admin_popup, null)
        builder.setView(view)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        val logInBtn = view.findViewById<AppCompatButton>(R.id.btnLogInAdmin)
        val password = view.findViewById<EditText>(R.id.etAdminPassword)
        val eyeIcon = view.findViewById<ImageView>(R.id.ivEye)
        initEyeIcon(eyeIcon, password)
        val passwordManager = PasswordManager()
        logInBtn.setOnClickListener {
            val enteredPassword = password.text.toString()
            if (passwordManager.checkPassword(enteredPassword)) {
                sharedPreferences = SharedPreferences(requireContext())
                sharedPreferences.saveAdminToken(BuildConfig.AUTH_TOKEN)
                Toast.makeText(context, "Contraseña correcta", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(context, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
            }
        }
        initToken()
    }

    private fun initEyeIcon(eye: ImageView, password: EditText) {
        eye.setOnClickListener {
            if (password.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                password.inputType = InputType.TYPE_CLASS_TEXT
                eye.setImageResource(R.drawable.ic_eye_open)
            } else {
                password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                eye.setImageResource(R.drawable.ic_eye_closed)
            }
            // Mueve el cursor al final del texto
            password.setSelection(password.text.length)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun setRowPlayerView(player: PlayerStats, index: Int): View {
        val view = layoutInflater.inflate(R.layout.item_table_row, null)
        val arrow = getArrow(player)
        view.findViewById<ImageView>(R.id.ivArrow).setImageResource(arrow)
        view.findViewById<TextView>(R.id.tvRanking).text = (index + 1).toString()
        view.findViewById<TextView>(R.id.tvPlayerName).text = player.name.toString()
        view.findViewById<TextView>(R.id.tvPlayerProportion).text = player.percentage
        view.findViewById<TextView>(R.id.tvPlayerGoals).text = player.goals.toString()
        view.findViewById<TextView>(R.id.tvPlayerAssists).text = player.assists.toString()
        view.findViewById<TextView>(R.id.tvPlayerBigMistakes).text = player.bigMistakes.toString()
        view.findViewById<TextView>(R.id.tvPlayerCleanSheet).text = player.cleanSheet.toString()
        view.findViewById<TextView>(R.id.tvPlayerGames).text = player.gamesPlayed.toString()

        return view
    }

    private fun getArrow(player: PlayerStats): Int {
        return if (player.lastRanking < player.ranking) {
            R.drawable.ic_arrow_down
        } else if (player.lastRanking > player.ranking) {
            R.drawable.ic_arrow_up
        } else {
            R.drawable.ic_arrow_equal
        }
    }
}