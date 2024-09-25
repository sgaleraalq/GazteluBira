package com.sgalera.gaztelubira.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            val credentials = withContext(Dispatchers.IO){
                sharedPreferences.getCredentials()
            }

            if (credentials.token.isNullOrEmpty() || credentials.year == 0){
                _state.value = MainState.Error("Error fetching credentials")
            } else {
                // TODO player
                _state.value = MainState.Success(credentials.token, credentials.player ?: "", credentials.year)
            }
        }
    }

}

data class Credentials(
    val token: String?,
    val player: String?,
    val year: Int
)

sealed class MainState{
    data object Loading: MainState()
    data class Success(val token: String, val player: String, val year: Int): MainState()
    data class Error(val error: String): MainState()
}