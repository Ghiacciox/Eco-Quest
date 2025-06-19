package com.ingegneriasoftware.ecoquest.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingegneriasoftware.ecoquest.data.models.CompletedMission
import com.ingegneriasoftware.ecoquest.data.repositories.MissionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

@HiltViewModel
class QuestHistoryViewModel @Inject constructor(
    private val missionsRepository: MissionsRepository
) : ViewModel() {

    private val _missionHistory = MutableStateFlow<List<CompletedMission>>(emptyList())

    // Grouped missions as StateFlow
    private val _groupedMissions = MutableStateFlow<Map<String, List<CompletedMission>>>(emptyMap())
    val groupedMissions: StateFlow<Map<String, List<CompletedMission>>> get() = _groupedMissions

    init {
        loadMissionHistory()
    }

    private fun loadMissionHistory() {
        viewModelScope.launch {
            _missionHistory.value = missionsRepository.getMissionHistory()
            _groupedMissions.value = groupMissionsByDate(_missionHistory.value)
        }
    }

    private fun groupMissionsByDate(missions: List<CompletedMission>): Map<String, List<CompletedMission>> {
        return missions
            .groupBy { mission ->
                mission.completedAt.toLocalDateTime(TimeZone.currentSystemDefault())
                    .date.toString() // YYYY-MM-DD format
            }
            .toSortedMap(compareByDescending { it }) // Newest first
    }
}