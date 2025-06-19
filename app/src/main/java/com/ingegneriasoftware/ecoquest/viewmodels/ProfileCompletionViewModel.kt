package com.ingegneriasoftware.ecoquest.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingegneriasoftware.ecoquest.data.repositories.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class ProfileCompletionViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _isUsernameAvailable = MutableStateFlow<Boolean?>(null)
    val isUsernameAvailable: StateFlow<Boolean?> = _isUsernameAvailable

    private val _firstName = MutableStateFlow("")
    val firstName: StateFlow<String> = _firstName

    private val _lastName = MutableStateFlow("")
    val lastName: StateFlow<String> = _lastName

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        viewModelScope.launch {
            _username
                .debounce(500) // Wait 500ms after last input to check
                .collect { username ->
                    if (username.isNotEmpty()) {
                        checkUsernameAvailability(username)
                    } else {
                        _isUsernameAvailable.value = null
                    }
                }
        }
    }

    fun setUsername(value: String) {
        _username.value = value
    }

    fun setFirstName(value: String) {
        _firstName.value = value
    }

    fun setLastName(value: String) {
        _lastName.value = value
    }

    private suspend fun checkUsernameAvailability(username: String) {
        profileRepository.isUsernameAvailable(username).fold(
            onSuccess = { isAvailable ->
                _isUsernameAvailable.value = isAvailable
            },
            onFailure = { error ->
                _errorMessage.value = "Failed to check username: ${error.message}"
                _isUsernameAvailable.value = null
            }
        )
    }

    fun saveProfile(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            if (_isUsernameAvailable.value != true) {
                onError("Username is not available")
                return@launch
            }

            val result = profileRepository.saveProfileCompletion(
                username = _username.value,
                firstName = _firstName.value,
                lastName = _lastName.value
            )

            if (result.isSuccess) {
                onSuccess()
            } else {
                onError(result.exceptionOrNull()?.localizedMessage ?: "Failed to save profile")
            }
        }
    }

    fun updateProfile(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val result = profileRepository.updateProfile(
                username = _username.value,
                firstName = _firstName.value,
                lastName = _lastName.value
            )

            if (result.isSuccess) {
                onSuccess()
            } else {
                onError(result.exceptionOrNull()?.localizedMessage ?: "Failed to update profile")
            }
        }
    }
    fun setErrorMessage(message: String?) {
        _errorMessage.value = message
    }

}