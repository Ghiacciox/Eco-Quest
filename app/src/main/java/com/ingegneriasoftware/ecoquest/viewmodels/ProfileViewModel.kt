package com.ingegneriasoftware.ecoquest.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.ingegneriasoftware.ecoquest.data.models.Profile
import com.ingegneriasoftware.ecoquest.data.repositories.AuthRepository
import com.ingegneriasoftware.ecoquest.data.repositories.ProfileRepository
import com.ingegneriasoftware.ecoquest.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository // Inject AuthRepository for logout functionality
) : ViewModel() {

    // StateFlow for the user's profile
    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> get() = _profile

    // Load the user's profile
    fun loadProfile() {
        viewModelScope.launch {
            try {
                _profile.value = profileRepository.getProfile()
            } catch (_: Exception) {
                // Handle error (e.g., show a message to the user)
                _profile.value = null
            }
        }
    }

    fun logout(navController: NavController) {
        viewModelScope.launch {
            authRepository.logout()
            // Navigate to Login screen and clear the back stack
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    fun uploadProfileImage(imageBytes: ByteArray) {
        viewModelScope.launch {
            val result = profileRepository.uploadProfileImage(imageBytes)
            result.onSuccess {
                _profile.value = profileRepository.getProfile()
            }.onFailure { error ->
                Log.e("ProfileViewModel", "Errore durante l'upload dell'immagine: $error")
            }
        }
    }
}