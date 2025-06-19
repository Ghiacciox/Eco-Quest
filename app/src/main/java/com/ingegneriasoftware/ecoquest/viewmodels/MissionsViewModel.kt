package com.ingegneriasoftware.ecoquest.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingegneriasoftware.ecoquest.data.models.CompletedMission
import com.ingegneriasoftware.ecoquest.data.models.Missions
import com.ingegneriasoftware.ecoquest.data.repositories.MissionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MissionsViewModel @Inject constructor(
    private val missionsRepository: MissionsRepository
) : ViewModel() {

    private val _uncompletedMissions = MutableStateFlow<List<Missions>>(emptyList())
    val uncompletedMissions: StateFlow<List<Missions>> = _uncompletedMissions.asStateFlow()

    private val _completedMissions = MutableStateFlow<List<CompletedMission>>(emptyList())
    val completedMissions: StateFlow<List<CompletedMission>> = _completedMissions.asStateFlow()

    // Trigger per forzare l'aggiornamento
    private val _refreshTrigger = MutableStateFlow(0)
    val refreshTrigger: StateFlow<Int> = _refreshTrigger.asStateFlow()

    init {
        loadMissions()
    }

    fun loadMissions() {
        viewModelScope.launch {
            _uncompletedMissions.value = missionsRepository.getUncompletedMissions()
            _completedMissions.value = missionsRepository.getCompletedMissions()
        }
    }

    fun completeMission(missionId: Int, points: Int) {
        viewModelScope.launch {
            missionsRepository.completeMission(missionId, points)
            // Aggiorna entrambe le liste
            _uncompletedMissions.value = missionsRepository.getUncompletedMissions()
            _completedMissions.value = missionsRepository.getCompletedMissions()
            // Notifica il cambiamento
            _refreshTrigger.value++
        }
    }
}