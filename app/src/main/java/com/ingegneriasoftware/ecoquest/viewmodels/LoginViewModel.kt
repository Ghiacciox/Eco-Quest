package com.ingegneriasoftware.ecoquest.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingegneriasoftware.ecoquest.data.repositories.AuthRepository
import com.ingegneriasoftware.ecoquest.data.repositories.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            if (result.isSuccess) {
                // Check profile completion after successful login
                checkProfileCompletion(
                    onComplete = {
                        onSuccess() // Navigate to Home if profile is complete
                    },
                    onIncomplete = {
                        onSuccess() // Navigate to ProfileCompletionScreen if profile is incomplete
                    }
                )
            } else {
                onError(result.exceptionOrNull()?.localizedMessage ?: "Login failed")
            }
        }
    }

    private suspend fun checkProfileCompletion(
        onComplete: () -> Unit,
        onIncomplete: () -> Unit
    ) {
        try {
            val profileExists = profileRepository.checkProfile()
            if (profileExists) {
                onComplete() // Profile exists; proceed to Home
            } else {
                onIncomplete() // Profile does not exist; redirect to ProfileCompletionScreen
            }
        } catch (_: Exception) {
            onIncomplete() // Fallback to ProfileCompletionScreen on error
        }
    }
}