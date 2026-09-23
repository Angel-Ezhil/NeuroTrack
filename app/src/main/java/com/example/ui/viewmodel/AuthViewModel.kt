package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.User
import com.example.data.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun login(email: String, pass: String, onSuccess: () -> Unit) {
        if (email.isBlank() || pass.isBlank()) {
            _errorMessage.value = "Please fill in all credentials"
            return
        }
        _currentUser.value = User(
            id = "user_auth_1",
            fullName = email.substringBefore("@").replace(".", " ").capitalize(),
            email = email,
            role = UserRole.SPORTS_TRAINER,
            isAuthenticated = true
        )
        _isAuthenticated.value = true
        _errorMessage.value = null
        onSuccess()
    }

    fun demoLogin(onSuccess: () -> Unit) {
        _currentUser.value = User(
            id = "user_trainer_demo",
            fullName = "Dr. Marcus Vance",
            email = "m.vance@neurotrack.ai",
            role = UserRole.SPORTS_TRAINER,
            organization = "Apex Athletic Institute",
            isAuthenticated = true
        )
        _isAuthenticated.value = true
        _errorMessage.value = null
        onSuccess()
    }

    fun register(
        fullName: String,
        email: String,
        pass: String,
        confirmPass: String,
        role: UserRole,
        onSuccess: () -> Unit
    ) {
        if (fullName.isBlank() || email.isBlank() || pass.isBlank()) {
            _errorMessage.value = "All registration fields are required"
            return
        }
        if (pass != confirmPass) {
            _errorMessage.value = "Passwords do not match"
            return
        }
        if (pass.length < 6) {
            _errorMessage.value = "Password must be at least 6 characters"
            return
        }
        _currentUser.value = User(
            id = "user_reg_${System.currentTimeMillis()}",
            fullName = fullName,
            email = email,
            role = role,
            isAuthenticated = true
        )
        _isAuthenticated.value = true
        _errorMessage.value = null
        onSuccess()
    }

    fun logout(onLoggedOut: () -> Unit) {
        _currentUser.value = null
        _isAuthenticated.value = false
        _errorMessage.value = null
        onLoggedOut()
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
