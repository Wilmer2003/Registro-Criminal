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

class CrimenDetalleViewModel(crimenId: UUID) : ViewModel() {

    private val repository = CrimenRepository.get()

    private val _crimen: MutableStateFlow<Crimen?> = MutableStateFlow(null)
    val crimen: StateFlow<Crimen?> = _crimen.asStateFlow()

    init {
        viewModelScope.launch {
            _crimen.value = repository.getCrimen(crimenId)
        }
    }

    fun actualizarCrimen(onUpdate: (Crimen) -> Crimen) {
        _crimen.update { crimenActual ->
            crimenActual?.let(onUpdate)
        }
    }

    override fun onCleared() {
        super.onCleared()
        _crimen.value?.let { crimen ->
            viewModelScope.launch {
                repository.actualizarCrimen(crimen)
            }
        }
    }
}

class CrimenDetalleViewModelFactory(
    private val crimenId: UUID
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CrimenDetalleViewModel(crimenId) as T
    }
}