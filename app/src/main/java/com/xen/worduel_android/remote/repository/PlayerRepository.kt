package com.xen.worduel_android.remote.repository

import com.xen.worduel_android.remote.NicknameRequest
import com.xen.worduel_android.remote.PlayerApi

class PlayerRepository (private val api: PlayerApi) {
    suspend fun setNickname(nickname: NicknameRequest) = api.setNickname(nickname);
}