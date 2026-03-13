package com.xen.worduel_android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.xen.worduel_android.remote.NicknameRequest
import com.xen.worduel_android.remote.repository.PlayerRepository
import kotlinx.coroutines.launch
import kotlin.jvm.java

class PlayerViewModel(private val repository: PlayerRepository): ViewModel() {
    fun setNickname(nicknameRequest: NicknameRequest) {
        viewModelScope.launch {
            repository.setNickname(nicknameRequest)
        }
    }

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