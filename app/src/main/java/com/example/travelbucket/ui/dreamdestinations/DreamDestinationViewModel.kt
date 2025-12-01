package com.example.travelbucket.ui.dreamdestinations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travelbucket.data.local.DreamDestination
import com.example.travelbucket.data.repository.DreamDestinationRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DreamDestinationViewModel(
    private val repository: DreamDestinationRepository
) : ViewModel() {

    val destinations: StateFlow<List<DreamDestination>> = repository.allDestinations
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addDestination(name: String, notes: String?, photoPath: String) {
        viewModelScope.launch {
            repository.addDestination(name, notes, photoPath)
        }
    }

    fun deleteDestination(id: Int) {
        viewModelScope.launch {
            repository.deleteDestination(id)
        }
    }
}

class DreamDestinationViewModelFactory(
    private val repository: DreamDestinationRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DreamDestinationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DreamDestinationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
