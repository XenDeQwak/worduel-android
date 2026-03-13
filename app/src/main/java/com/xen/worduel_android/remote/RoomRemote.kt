package com.xen.worduel_android.remote

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

data class RoomModel (
    val roomId: String,
    val roomType: String
)

data class GuessRequest (
    val roomId: String,
    val playerId: String,
    val player: PlayerModel,
    val guess: String
)

const val roomTypeEndpoint = "api/room"

interface RoomApi {
    @POST(roomTypeEndpoint)
    suspend fun createRoom(@Query("type") roomType: String): RoomModel

    @POST("$roomTypeEndpoint/join")
    suspend fun joinRoom(@Query("roomId") roomId: String): RoomModel

    @POST("$roomTypeEndpoint/leave")
    suspend fun leaveRoom(@Query("roomId") roomId: String): RoomModel

    @POST("$roomTypeEndpoint/start")
    suspend fun startGame(@Query("roomId") roomId: String): RoomModel

    @POST("$roomTypeEndpoint/guess")
    suspend fun submitGuess(@Body guessRequest: GuessRequest): RoomModel

}
