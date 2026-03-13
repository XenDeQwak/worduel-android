package com.xen.worduel_android.remote.repository

import com.xen.worduel_android.remote.GuessRequest
import com.xen.worduel_android.remote.RoomApi

class RoomRepository(private val api: RoomApi) {
    suspend fun createRoom(roomType: String) = api.createRoom(roomType)
    suspend fun joinRoom(roomId: String) = api.joinRoom(roomId)
    suspend fun leaveRoom(roomId: String) = api.leaveRoom(roomId)
    suspend fun startGame(roomId: String) = api.startGame(roomId)
    suspend fun submitGuess(guessRequest: GuessRequest) = api.submitGuess(guessRequest)



}