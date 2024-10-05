package com.sgalera.gaztelubira.domain.model

sealed class UIState{
    data object Loading: UIState()
    data object Error: UIState()
    data object Success: UIState()
}