package com.ingegneriasoftware.ecoquest.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingegneriasoftware.ecoquest.data.models.AchievedTrophy
import com.ingegneriasoftware.ecoquest.data.models.Trophy
import com.ingegneriasoftware.ecoquest.data.repositories.TrophyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel // Enable Hilt injection
class TrophyViewModel @Inject constructor(
    private val trophyRepository: TrophyRepository // Inject TrophyRepository
) : ViewModel() {

    // StateFlow for achieved trophies
    private val _achievedTrophies = MutableStateFlow<List<AchievedTrophy>>(emptyList())
    val achievedTrophies: StateFlow<List<AchievedTrophy>> get() = _achievedTrophies

    // StateFlow for non-achieved trophies
    private val _trophies = MutableStateFlow<List<Trophy>>(emptyList())
    val trophies: StateFlow<List<Trophy>> get() = _trophies

    // Load trophies when the ViewModel is created
    init {
        loadTrophies()
    }

    // Function to load trophies
    fun loadTrophies() {
        viewModelScope.launch {
            _achievedTrophies.value = trophyRepository.getAchievedTrophies()
            _trophies.value = trophyRepository.getTrophies()
        }
    }
}