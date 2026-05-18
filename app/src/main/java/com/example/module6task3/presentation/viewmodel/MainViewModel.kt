package com.example.module6task3.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.module6task3.domain.model.User
import com.example.module6task3.domain.repository.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow(

    )
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    val token = repository.getToken() // Это Flow из DataStore
    fun loadUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            repository.getUsers()
                .onSuccess { _users.value = it }
                .onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }
    fun login(u: String, p: String) = viewModelScope.launch {
        _authState.value = AuthState.Loading
        repository.login(u, p)
            .onSuccess { _authState.value = AuthState.Success }
            .onFailure { _authState.value = AuthState.Error("Ошибка входа: ${it.message}") }
    }
    fun logout() = viewModelScope.launch {
        repository.logout()
        _authState.value = AuthState.Idle
    }

    private val _selectedUser = MutableStateFlow<User?>(null)
    val selectedUser = _selectedUser.asStateFlow()

    fun loadUserById(id: Int) = viewModelScope.launch {
        _isLoading.value = true
        repository.getUsers().onSuccess { list ->
            _selectedUser.value = list.find { it.id == id }
        }
        _isLoading.value = false
    }
}