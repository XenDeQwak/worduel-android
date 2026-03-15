package com.xen.worduel_android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.xen.worduel_android.remote.NicknameRequest
import com.xen.worduel_android.remote.PlayerModel
import com.xen.worduel_android.remote.repository.PlayerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayerViewModel(private val repository: PlayerRepository) : ViewModel() {

    private val _player = MutableStateFlow<PlayerModel?>(null)
    val player = _player.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun setNickname(nickname: String, onSuccess: (PlayerModel) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            runCatching {
                repository.setNickname(NicknameRequest(nickname))
            }.onSuccess { playerModel ->
                _player.value = playerModel
                onSuccess(playerModel)
            }.onFailure {
                _errorMessage.value = it.message ?: "Failed to set nickname"
            }
            _isLoading.value = false
        }
    }

    fun dismissError() { _errorMessage.value = null }

    class Factory(private val repository: PlayerRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PlayerViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}