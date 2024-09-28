package com.sgalera.gaztelubira.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.domain.model.Credentials
import com.sgalera.gaztelubira.domain.model.PlayerModel
import com.sgalera.gaztelubira.domain.usecases.players.GetPlayersUseCase
import com.sgalera.gaztelubira.ui.manager.SharedPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
): ViewModel() {

    private val _state = MutableStateFlow<MainState>(MainState.Loading)
    val state = _state

    fun getCredentials(){
        viewModelScope.launch {
            _state.value = MainState.Loading
            val result = withContext(Dispatchers.IO){
                sharedPreferences.getCredentials()
            }
            if (result) {
                _state.value = MainState.Success
            } else {
                _state.value = MainState.Error
            }
        }
    }
}

sealed class MainState{
    data object Loading: MainState()
    data object Error: MainState()
    data object Success: MainState()
}