package com.sgalera.gaztelubira.data.provider

import com.sgalera.gaztelubira.domain.model.Team.*
import com.sgalera.gaztelubira.domain.model.matches.Match
import com.sgalera.gaztelubira.domain.model.matches.Starters
import javax.inject.Inject

class MatchesProvider @Inject constructor() {
    fun getMatches(): List<Match>{
        return listOf(
            Match(
                local = GazteluBira,
                visitor = Aterbea,
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
                local = GazteluBira,
                visitor = EsicGazteak,
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