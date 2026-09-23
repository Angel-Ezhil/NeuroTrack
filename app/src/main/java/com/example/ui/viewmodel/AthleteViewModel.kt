package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AssessmentStatus
import com.example.data.model.Athlete
import com.example.data.repository.NeuroTrackRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AthleteViewModel(
    private val repository: NeuroTrackRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _sportFilter = MutableStateFlow<String?>(null)
    val sportFilter: StateFlow<String?> = _sportFilter.asStateFlow()

    private val _selectedAthlete = MutableStateFlow<Athlete?>(null)
    val selectedAthlete: StateFlow<Athlete?> = _selectedAthlete.asStateFlow()

    val athletes: StateFlow<List<Athlete>> = combine(
        repository.athletes,
        _searchQuery,
        _sportFilter
    ) { allAthletes, query, sport ->
        allAthletes.filter { athlete ->
            val matchesQuery = query.isBlank() ||
                    athlete.name.contains(query, ignoreCase = true) ||
                    athlete.sport.contains(query, ignoreCase = true)
            val matchesSport = sport == null || athlete.sport.equals(sport, ignoreCase = true)
            matchesQuery && matchesSport
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val totalAthletesCount = repository.athleteCount.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSportFilter(sport: String?) {
        _sportFilter.value = sport
    }

    fun selectAthlete(athlete: Athlete?) {
        _selectedAthlete.value = athlete
    }

    fun loadAthleteById(id: Long) {
        viewModelScope.launch {
            _selectedAthlete.value = repository.getAthleteByIdSync(id)
        }
    }

    fun addAthlete(
        name: String,
        sport: String,
        age: Int,
        position: String,
        jerseyNumber: String,
        onCreated: (Long) -> Unit
    ) {
        viewModelScope.launch {
            val newAthlete = Athlete(
                name = name.trim(),
                sport = sport.trim(),
                age = age,
                position = position.trim(),
                jerseyNumber = jerseyNumber.trim(),
                avatarIndex = (0..5).random(),
                hasBaseline = false,
                latestStatus = AssessmentStatus.WITHIN_BASELINE
            )
            val id = repository.insertAthlete(newAthlete)
            onCreated(id)
        }
    }

    fun deleteAthlete(id: Long) {
        viewModelScope.launch {
            repository.deleteAthlete(id)
            if (_selectedAthlete.value?.id == id) {
                _selectedAthlete.value = null
            }
        }
    }
}
