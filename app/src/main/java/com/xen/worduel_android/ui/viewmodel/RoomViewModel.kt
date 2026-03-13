package com.xen.worduel_android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.xen.worduel_android.remote.RoomModel
import com.xen.worduel_android.remote.repository.RoomRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RoomViewModel(private val repository: RoomRepository): ViewModel() {

    private val _currentRoom = MutableStateFlow<RoomModel?>(null)
    private val currentRoom = _currentRoom.asStateFlow()

    suspend fun createRoom(roomType: String) {
        viewModelScope.launch {
            val room = repository.createRoom(roomType)
            _currentRoom.value = room
        }
    }

    class Factory(private val repository: RoomRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RoomViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RoomViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}