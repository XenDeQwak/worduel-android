package com.xen.worduel_android.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.xen.worduel_android.remote.GuessRequest
import com.xen.worduel_android.remote.PlayerModel
import com.xen.worduel_android.remote.RoomModel
import com.xen.worduel_android.remote.dto.GuessResponse
import com.xen.worduel_android.remote.repository.RoomRepository
import com.xen.worduel_android.remote.LetterType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RoomViewModel(
    private val repository: RoomRepository,
    private val playerId: String,
    private val playerModel: PlayerModel
) : ViewModel() {

    companion object {
        const val WORD_LENGTH  = 5
        const val MAX_ATTEMPTS = 6
    }

    private val _currentRoom = MutableStateFlow<RoomModel?>(null)
    val currentRoom = _currentRoom.asStateFlow()

    private val _guessHistory = MutableStateFlow<List<GuessResponse>>(emptyList())
    val guessHistory = _guessHistory.asStateFlow()

    private val _currentInput = MutableStateFlow("")
    val currentInput = _currentInput.asStateFlow()

    private val _isGameOver = MutableStateFlow(false)
    val isGameOver = _isGameOver.asStateFlow()

    private val _isWin = MutableStateFlow(false)
    val isWin = _isWin.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun createSoloGame(onReady: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            runCatching {
                val created = repository.createRoom("solo")
                val joined  = repository.joinRoom(created.roomId)
                repository.startGame(joined.roomId)
            }.onSuccess { started ->
                _currentRoom.value = started
                onReady()
            }.onFailure {
                _errorMessage.value = it.message ?: "Failed to start game"
            }
            _isLoading.value = false
        }
    }

    fun createRoom(roomType: String) {
        viewModelScope.launch {
            _isLoading.value = true
            runCatching { repository.createRoom(roomType) }
                .onSuccess { _currentRoom.value = it }
                .onFailure { _errorMessage.value = it.message }
            _isLoading.value = false
        }
    }

    fun joinRoom(roomId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            runCatching { repository.joinRoom(roomId) }
                .onSuccess { _currentRoom.value = it }
                .onFailure { _errorMessage.value = it.message }
            _isLoading.value = false
        }
    }

    fun leaveRoom(roomId: String) {
        viewModelScope.launch {
            _currentRoom.value = repository.leaveRoom(roomId)
        }
    }

    fun startGame(roomId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            runCatching { repository.startGame(roomId) }
                .onSuccess { _currentRoom.value = it }
                .onFailure { _errorMessage.value = it.message }
            _isLoading.value = false
        }
    }

    fun onKey(letter: Char) {
        if (_isGameOver.value) return
        if (_currentInput.value.length < WORD_LENGTH) {
            _currentInput.value += letter.uppercaseChar()
        }
    }

    fun onBackspace() {
        if (_currentInput.value.isNotEmpty()) {
            _currentInput.value = _currentInput.value.dropLast(1)
        }
    }

    fun onEnter() {
        if (_currentInput.value.length < WORD_LENGTH) {
            _errorMessage.value = "Not enough letters"
            return
        }
        submitGuess()
    }

    private fun submitGuess() {
        val room  = _currentRoom.value ?: return
        val guess = _currentInput.value

        viewModelScope.launch {
            _isLoading.value = true
            runCatching {
                repository.submitGuess(
                    GuessRequest(
                        roomId   = room.roomId,
                        playerId = playerId,
                        player   = playerModel,
                        guess    = guess
                    )
                )
            }.onSuccess { response ->
                _currentInput.value = ""
                _guessHistory.value += response

                val won           = response.position.all { it == LetterType.CORRECT }
                val outOfAttempts = _guessHistory.value.size >= MAX_ATTEMPTS
                when {
                    won           -> { _isWin.value = true; _isGameOver.value = true }
                    outOfAttempts -> { _isGameOver.value = true }
                }
                Log.d("SUBMIT_GUESS", "IsWon?: $won");
                Log.d("SUBMIT_GUESS", "IsOutOfAttempts?: $outOfAttempts");
            }.onFailure { error ->
                _errorMessage.value = error.message ?: "Something went wrong"
            }
            _isLoading.value = false
        }

        Log.d("SUBMIT_GUESS", "Guess Submitted: $guess");
    }

    fun dismissError() { _errorMessage.value = null }

    fun resetGame() {
        _guessHistory.value = emptyList()
        _currentInput.value = ""
        _isGameOver.value   = false
        _isWin.value        = false
        _currentRoom.value  = null
        _errorMessage.value = null
    }

    class Factory(
        private val repository: RoomRepository,
        private val playerId: String,
        private val playerModel: PlayerModel
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RoomViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RoomViewModel(repository, playerId, playerModel) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}