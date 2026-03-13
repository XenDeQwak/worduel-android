package com.xen.worduel_android.remote.repository

import com.xen.worduel_android.remote.NicknameRequest
import com.xen.worduel_android.remote.PlayerApi
import com.xen.worduel_android.remote.PlayerModel

class PlayerRepository (private val api: PlayerApi) {
    suspend fun setNickname(nicknameRequest: NicknameRequest): PlayerModel {
        return api.setNickname(nicknameRequest)
    }
}