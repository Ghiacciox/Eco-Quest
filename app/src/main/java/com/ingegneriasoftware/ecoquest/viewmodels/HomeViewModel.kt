package com.ingegneriasoftware.ecoquest.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingegneriasoftware.ecoquest.data.models.Missions
import com.ingegneriasoftware.ecoquest.data.models.AchievedTrophy
import com.ingegneriasoftware.ecoquest.data.models.LeaderboardEntry
import com.ingegneriasoftware.ecoquest.data.repositories.LeaderboardRepository
import com.ingegneriasoftware.ecoquest.data.repositories.MissionsRepository
import com.ingegneriasoftware.ecoquest.data.repositories.ProfileRepository
import com.ingegneriasoftware.ecoquest.data.repositories.TrophyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val profileRepository: ProfileRepository, // Inject ProfileRepository
    private val missionsRepository: MissionsRepository, // Inject MissionsRepository
    private val trophyRepository: TrophyRepository, // Inject TrophyRepository
    private val leaderboardRepository: LeaderboardRepository // Inject LeaderboardRepository
) : ViewModel() {

    // StateFlow for ECO-Points
    private val _ecoPoints = MutableStateFlow<Long?>(null)
    val ecoPoints: StateFlow<Long?> get() = _ecoPoints

    // StateFlow for active missions
    private val _activeMissions = MutableStateFlow<List<Missions>>(emptyList())
    val activeMissions: StateFlow<List<Missions>> get() = _activeMissions

    // StateFlow for recent trophies (use AchievedTrophy)
    private val _recentTrophies = MutableStateFlow<List<AchievedTrophy>>(emptyList())
    val recentTrophies: StateFlow<List<AchievedTrophy>> get() = _recentTrophies

    // StateFlow for leaderboard summary
    private val _leaderboardSummaryD = MutableStateFlow<LeaderboardEntry?>(null)
    val leaderboardSummaryDaily: StateFlow<LeaderboardEntry?> get() = _leaderboardSummaryD

    private val _leaderboardSummaryW = MutableStateFlow<LeaderboardEntry?>(null)
    val leaderboardSummaryWeekly: StateFlow<LeaderboardEntry?> get() = _leaderboardSummaryW

    private val _leaderboardSummaryM = MutableStateFlow<LeaderboardEntry?>(null)
    val leaderboardSummaryMonthly: StateFlow<LeaderboardEntry?> get() = _leaderboardSummaryM

    private val _profilePic = MutableStateFlow<String?>(null)
    val profilePic: StateFlow<String?> get() = _profilePic

    private val _username = MutableStateFlow<String?>(null)
    val username: StateFlow<String?> get() = _username

    // Load all data for the HomeScreen
    fun loadHomeData() {
        // Load profile data
        viewModelScope.launch {
            val profile = profileRepository.getProfile()
            _ecoPoints.value = profile.points
            _profilePic.value = profile.profilePic
            _username.value = profile.username
        }

        // Load active missions
        viewModelScope.launch {
            val missions = missionsRepository.getUncompletedMissions()
            _activeMissions.value = missions
        }

        // Load recent trophies (only achieved trophies)
        viewModelScope.launch {
            val trophies = trophyRepository.getAchievedTrophies()
            _recentTrophies.value = trophies.take(3) // Show only the 3 most recent trophies
        }

        // Load leaderboard summary
        viewModelScope.launch {
            val leaderboard = leaderboardRepository.getSurroundingUsers("daily")  // Default to daily leaderboard
            val profile = profileRepository.getProfile()
            _leaderboardSummaryD.value = leaderboard.find { it.username == profile.username }
        }

        // Load leaderboard summary
        viewModelScope.launch {
            val leaderboard = leaderboardRepository.getSurroundingUsers("weekly") // Default to daily leaderboard
            val profile = profileRepository.getProfile()
            _leaderboardSummaryW.value = leaderboard.find { it.username == profile.username }
        }

        // Load leaderboard summary
        viewModelScope.launch {
            val leaderboard = leaderboardRepository.getSurroundingUsers("monthly")  // Default to daily leaderboard
            val profile = profileRepository.getProfile()
            _leaderboardSummaryM.value = leaderboard.find { it.username == profile.username }
        }
    }
}