package com.xen.worduel_android.remote

import retrofit2.http.POST
import retrofit2.http.Query

data class RoomModel (
    val roomId: String,
    val roomType: String
)
const val roomTypeEndpoint = "api/room"

interface RoomApi {
    @POST("$roomTypeEndpoint/room")
    suspend fun createRoom(@Query("type") roomType: String): RoomModel

}