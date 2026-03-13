package com.xen.worduel_android.remote

import retrofit2.http.Body
import retrofit2.http.POST

data class NicknameRequest (
    val nickname: String
)

const val playerEndpoint = "api/players"
interface PlayerApi {
    @POST("$playerEndpoint/nickname")
    suspend fun setNickname(@Body nicknameRequest: NicknameRequest): NicknameRequest
}