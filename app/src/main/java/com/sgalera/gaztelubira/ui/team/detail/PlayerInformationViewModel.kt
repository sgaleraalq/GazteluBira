package com.sgalera.gaztelubira.ui.team.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentReference
import com.sgalera.gaztelubira.data.provider.PlayersProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PlayerInformationViewModel @Inject constructor(private val playersProvider: PlayersProvider) : ViewModel() {
    private var _statePlayerInformation = MutableStateFlow<PlayerInformationDetailState>(PlayerInformationDetailState.Loading)
    val statePlayerInformation: StateFlow<PlayerInformationDetailState> = _statePlayerInformation

    fun getReference(reference: String): DocumentReference? {
        return playersProvider.getReferenceFromString(reference)
    }

    suspend fun getPlayerStatsByReference(reference: DocumentReference) {
        viewModelScope.launch {
            _statePlayerInformation.value = PlayerInformationDetailState.Loading
            withContext(Dispatchers.IO) {
                val playerStats = playersProvider.getPlayerStatsByReference(reference)
                if (playerStats != null) {
                    _statePlayerInformation.value = PlayerInformationDetailState.Success(playerStats)
                } else {
                    _statePlayerInformation.value = PlayerInformationDetailState.Error("Ha ocurrido un error al cargar los datos del jugador.")
                }
            }
        }
    }

}