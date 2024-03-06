package com.sgalera.gaztelubira.ui.matches

import androidx.lifecycle.ViewModel
import com.sgalera.gaztelubira.domain.model.matches.Match
import com.sgalera.gaztelubira.domain.model.matches.Starters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MatchesViewModel @Inject constructor() : ViewModel() {
    private var _matches = MutableStateFlow<List<Match>>(emptyList())
    val matches: StateFlow<List<Match>> = _matches

    init {
        _matches.value = listOf(
            Match(
                local = "Gaztelu Bira",
                visitor = "Aterbea",
                localGoals = 3,
                visitorGoals = 1,
                starters = Starters(
                    goalKeeper = "Iker Casillas",
                    rightBack = "Dani Alves",
                    leftBack = "Marcelo",
                    rightCentreBack = "Sergio Ramos",
                    leftCentreBack = "Gerard Piqué",
                    defensiveMidFielder = "Xabi Alonso",
                    rightMidFielder = "Lionel Messi",
                    leftMidFielder = "Andrés Iniesta",
                    rightStriker = "Cristiano Ronaldo",
                    leftStriker = "Karim Benzema",
                    striker = "Karim Benzema"
                ),
                scorers = listOf("Cristiano Ronaldo", "Karim Benzema", "Lionel Messi"),
                assitants = listOf("Lionel Messi", "Andrés Iniesta", "Karim Benzema"),
                bench = listOf("Gareth Bale", "Luka Modric", "Sergio Busquets")
            ),
            Match(
                local = "Gaztelu Bira",
                visitor = "Aterbea",
                localGoals = 3,
                visitorGoals = 1,
                starters = Starters(
                    goalKeeper = "Iker Casillas",
                    rightBack = "Dani Alves",
                    leftBack = "Marcelo",
                    rightCentreBack = "Sergio Ramos",
                    leftCentreBack = "Gerard Piqué",
                    defensiveMidFielder = "Xabi Alonso",
                    rightMidFielder = "Lionel Messi",
                    leftMidFielder = "Andrés Iniesta",
                    rightStriker = "Cristiano Ronaldo",
                    leftStriker = "Karim Benzema",
                    striker = "Karim Benzema"
                ),
                scorers = listOf("Cristiano Ronaldo", "Karim Benzema", "Lionel Messi"),
                assitants = listOf("Lionel Messi", "Andrés Iniesta", "Karim Benzema"),
                bench = listOf("Gareth Bale", "Luka Modric", "Sergio Busquets")
            ),
        )
    }
}