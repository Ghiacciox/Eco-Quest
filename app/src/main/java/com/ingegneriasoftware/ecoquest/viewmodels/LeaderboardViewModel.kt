package com.ingegneriasoftware.ecoquest.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingegneriasoftware.ecoquest.data.models.LeaderboardEntry
import com.ingegneriasoftware.ecoquest.data.repositories.LeaderboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val leaderboardRepository: LeaderboardRepository // Inject LeaderboardRepository
) : ViewModel() {

    // StateFlow for top users
    private val _topUsers = MutableStateFlow<List<LeaderboardEntry>>(emptyList())
    val topUsers: StateFlow<List<LeaderboardEntry>> get() = _topUsers

    // StateFlow for surrounding users
    private val _surroundingUsers = MutableStateFlow<List<LeaderboardEntry>>(emptyList())
    val surroundingUsers: StateFlow<List<LeaderboardEntry>> get() = _surroundingUsers

    // Load leaderboard data for a specific period
    fun loadLeaderboard(period: String) {
        viewModelScope.launch {
            _topUsers.value = leaderboardRepository.getTopUsers(period)
            _surroundingUsers.value = leaderboardRepository.getSurroundingUsers(period)
        }
    }
}