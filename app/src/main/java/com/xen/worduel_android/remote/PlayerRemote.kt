package com.xen.worduel_android.remote

import retrofit2.http.Body
import retrofit2.http.POST

data class NicknameRequest (
    val nickname: String
)


const val playerEndpoint = "/players"
interface PlayerApi {
    @POST("/nickname")
    suspend fun setNickname(@Body nicknameRequest: NicknameRequest): NicknameRequest
}