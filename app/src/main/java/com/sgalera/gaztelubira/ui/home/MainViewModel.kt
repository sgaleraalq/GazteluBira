package com.sgalera.gaztelubira.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.domain.manager.SharedPreferences
import com.sgalera.gaztelubira.domain.model.UIState
import com.sgalera.gaztelubira.domain.model.UIState.Error
import com.sgalera.gaztelubira.domain.model.UIState.Loading
import com.sgalera.gaztelubira.domain.model.UIState.Success
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

    private val _state = MutableStateFlow<UIState>(Loading)
    val state = _state

    fun getCredentials(){
        viewModelScope.launch {
            _state.value = Loading
            val result = withContext(Dispatchers.IO){
                sharedPreferences.getCredentials()
            }
            if (result) {
                _state.value = Success
            } else {
                _state.value = Error
            }
        }
    }
}