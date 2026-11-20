package com.example.registrocriminal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class CrimenViewModel(crimenId: UUID) : ViewModel() {
    private val repositorio = CrimenRepository.get()
    private val _crimen: MutableStateFlow<Crimen?> = MutableStateFlow(null)
    val crimen: StateFlow<Crimen?> = _crimen.asStateFlow()

    init {
        viewModelScope.launch {
            _crimen.value = repositorio.getCrimen(crimenId)
        }
    }

    fun actualizarCrimen(onUpdate: (Crimen) -> Crimen) {
        _crimen.update { anterior ->
            anterior?.let { onUpdate(it) }
        }
    }

    // GUARDANDO EN LA BASE DE DATOS AL DESTRUIR EL VIEWMODEL
    override fun onCleared() {
        super.onCleared()
        _crimen.value?.let { crimenActual ->
            viewModelScope.launch {
                repositorio.actualizarCrimen(crimenActual)
            }
        }
    }
}

class CrimenViewModelFactory(
    private val crimenId: UUID
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CrimenViewModel(crimenId) as T
    }
}