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
import java.util.UUID

class RoomViewModel(
    private val repository: RoomRepository,
    private val playerId: String,
    private val player: PlayerModel
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


    private val _isGameActive = MutableStateFlow(false)
    val isGameActive = _isGameActive.asStateFlow()


    private val _player1Name = MutableStateFlow("")
    val player1Name = _player1Name.asStateFlow()

    private val _player2Name = MutableStateFlow("")
    val player2Name = _player2Name.asStateFlow()

    private val _player1Attempts = MutableStateFlow(0)
    val player1Attempts = _player1Attempts.asStateFlow()

    private val _player2Attempts = MutableStateFlow(0)
    val player2Attempts = _player2Attempts.asStateFlow()



    private val _shouldExitGame = MutableStateFlow(false)
    val shouldExitGame = _shouldExitGame.asStateFlow()

    fun createSoloGame(onReady: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            runCatching {
                repository.createRoom("solo")
            }.onSuccess {
                joinRoom(it.roomId)
                _currentRoom.value = it
                onReady()
            }.onFailure {
                _errorMessage.value = it.message ?: "Failed to start game"
            }
            _isLoading.value = false
        }
    }

    fun createDuelGame(onReady: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            runCatching {
                repository.createRoom("duel")
            }.onSuccess {
                joinRoom(it.roomId)
                _currentRoom.value = it
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

    fun joinNewRoom(onReady: () -> Unit = {}, roomId: String) {
        viewModelScope.launch {
            joinRoom(roomId)
            onReady()
        }
    }

    fun joinRoom(roomId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            runCatching {
                repository.joinRoom(roomId)
            }.onSuccess {
                _currentRoom.value = it
                if (it.roomType == "SOLO") {
                    repository.startGame(it.roomId)
                } else if (it.roomType == "DUEL" && it.players.size == 2) {
                    repository.startGame(it.roomId)
                }
            }.onFailure {
                _errorMessage.value = it.message
            }
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
                .onSuccess {
                    _currentRoom.value = it
                    _player1Name.value = it.players.first().nickname
                    _player2Name.value = it.players.last().nickname
                    _player1Attempts.value = it.game.playerGameStats.get(UUID.fromString(it.players.first().playerId))!!.currentGuessAttempt
                    _player2Attempts.value = it.game.playerGameStats.get(UUID.fromString(it.players.last().playerId))!!.currentGuessAttempt
                }
                .onFailure { _errorMessage.value = it.message }
            _isLoading.value = false

            _isGameActive.value = true;
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
                        player   = player,
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
        _shouldExitGame.value = true
        _guessHistory.value = emptyList()
        _currentInput.value = ""
        _isGameOver.value   = false
        _isWin.value        = false
        _currentRoom.value  = null
        _errorMessage.value = null
        _player1Name.value = ""
        _player2Name.value = ""
        _player1Attempts.value = 0
        _player2Attempts.value = 0
    }

    fun prepareRoom() {
        _shouldExitGame.value = false
    }

    class Factory(
        private val repository: RoomRepository,
        private val playerId: String,
        private val player: PlayerModel
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RoomViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RoomViewModel(repository, playerId, player) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}