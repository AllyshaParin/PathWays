package com.example.a216487_cikguizwan_lab01 // Ensure this matches your package

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

// Task 2: Data Class
data class UserProfile(
    val name: String = "",
    val phone: String = "",
    val email: String = ""
)

// Task 2: ViewModel Integration
class ProfileViewModel : ViewModel() {
    // This holds the current state of the profile
    private val _uiState = mutableStateOf(UserProfile())
    val uiState: State<UserProfile> = _uiState

    // Function to update the data from the Form screen
    fun updateProfile(newName: String, newPhone: String, newEmail: String) {
        _uiState.value = UserProfile(newName, newPhone, newEmail)
    }
}