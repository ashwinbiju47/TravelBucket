package com.example.travelbucket.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelbucket.data.local.CountryInfoEntity
import com.example.travelbucket.data.repository.CountryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CountryViewModel(
    private val repo: CountryRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _countryInfo = MutableStateFlow<CountryInfoEntity?>(null)
    val countryInfo = _countryInfo.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    val searchHistory = repo.getHistory().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptyList()
    )

    fun updateQuery(q: String) {
        _query.value = q
    }

    fun search(name: String) {
        viewModelScope.launch {

            if (name.isBlank()) {
                _errorMessage.value = "Enter Country name to search"
                Log.e("CountrySearch", "Blank query submitted")
                return@launch
            }

            try {
                val result = repo.searchCountry(name.trim())

                _countryInfo.value = result

            } catch (e: Exception) {
                // repo throws: IllegalStateException("Country not found for: $name")
                Log.e("CountrySearch", "Search failed: ${e.message}", e)

                _countryInfo.value = null

                // show placeholder guidance
                _errorMessage.value = "Country not found."
            }
        }
    }
}
