package com.xen.worduel_android.remote

import com.xen.worduel_android.remote.dto.GuessResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

data class RoomModel (
    val roomId: String,
    val roomType: String,
    val game: GameInstance,
    val players: List<PlayerModel>
)

data class GameInstance (
    val gameId: String,
    val targetWord: String,
    val playerGameStats: Map<UUID, PlayerGameStats>
)

data class GuessRequest (
    val roomId: String,
    val playerId: String,
    val player: PlayerModel,
    val guess: String
)

const val roomTypeEndpoint = "api/games/room"

interface RoomApi {

    @GET("$roomTypeEndpoint/{id}")
    suspend fun getRoom(@Path(value = "id") id: String): RoomModel

    @POST(roomTypeEndpoint)
    suspend fun createRoom(@Query("type") roomType: String): RoomModel

    @POST("$roomTypeEndpoint/join")
    suspend fun joinRoom(@Query("roomId") roomId: String): RoomModel

    @POST("$roomTypeEndpoint/leave")
    suspend fun leaveRoom(@Query("roomId") roomId: String): RoomModel

    @POST("$roomTypeEndpoint/start")
    suspend fun startGame(@Query("roomId") roomId: String): RoomModel

    @POST("$roomTypeEndpoint/guess")
    suspend fun submitGuess(@Body guessRequest: GuessRequest): GuessResponse

}
