package com.example.registrocriminal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ListaCrimenesViewModel : ViewModel() {

    private val repository = CrimenRepository.get()

    private val _crimenes: MutableStateFlow<List<Crimen>> = MutableStateFlow(emptyList())
    val crimenes: StateFlow<List<Crimen>>
        get() = _crimenes.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getCrimenes().collect { listCrimenes ->
                _crimenes.value = listCrimenes
            }
        }
    }

    suspend fun ingresarCrimen(crimen: Crimen) {
        repository.ingresarCrimen(crimen)
    }
}