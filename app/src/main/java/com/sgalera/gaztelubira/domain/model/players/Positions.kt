package com.sgalera.gaztelubira.domain.model.players

enum class Position {
    GoalKeeper,
    LeftBack,
    RightBack,
    LeftCentreBack,
    RightCentreBack,
    DefensiveMidfielder,
    LeftMidfielder,
    RightMidfielder,
    LeftStriker,
    RightStriker,
    Striker
}

data class Positions(
    val position: Position,
    val percentage: Int
)
