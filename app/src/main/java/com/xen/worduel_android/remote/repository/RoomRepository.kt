package com.xen.worduel_android.remote.repository

import com.xen.worduel_android.remote.GuessRequest
import com.xen.worduel_android.remote.RoomApi
import com.xen.worduel_android.remote.RoomModel
import com.xen.worduel_android.remote.dto.GuessResponse

class RoomRepository(private val api: RoomApi) {

    suspend fun getRoom(roomId: String): RoomModel = api.getRoom(roomId)
    suspend fun createRoom(roomType: String): RoomModel = api.createRoom(roomType)
    suspend fun joinRoom(roomId: String): RoomModel = api.joinRoom(roomId)
    suspend fun leaveRoom(roomId: String): RoomModel = api.leaveRoom(roomId)
    suspend fun startGame(roomId: String): RoomModel = api.startGame(roomId)
    suspend fun submitGuess(guessRequest: GuessRequest): GuessResponse = api.submitGuess(guessRequest)

}