package com.xen.worduel_android.remote

import retrofit2.http.Body
import retrofit2.http.POST

data class PlayerModel (
    val playerId: String,
    val nickname: String,
    val currentRoomId: String
)

data class PlayerGameStats (
    val currentGuessAttempt: Int,
    val guessList: List<String>,
    val hasWon: Boolean,
    val hasFinished: Boolean
)

data class NicknameRequest (
    val nickname: String
)

const val playerEndpoint = "api/players"
interface PlayerApi {
    @POST("$playerEndpoint/nickname")
    suspend fun setNickname(@Body nicknameRequest: NicknameRequest): PlayerModel
}