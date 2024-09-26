package com.sgalera.gaztelubira.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.domain.model.Credentials
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

            if (!credentials.isAdmin){
                _state.value = MainState.Error("Error fetching credentials")
            } else {
                // TODO player
                _state.value = MainState.Success(credentials)
            }
        }
    }
}

sealed class MainState{
    data object Loading: MainState()
    data class Success(val credentials: Credentials): MainState()
    data class Error(val error: String): MainState()
}