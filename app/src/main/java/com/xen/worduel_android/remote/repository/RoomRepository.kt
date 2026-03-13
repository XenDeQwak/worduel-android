package com.xen.worduel_android.remote.repository

import com.xen.worduel_android.remote.RoomApi

class RoomRepository(private val api: RoomApi) {
    suspend fun createRoom(roomType: String) = api.createRoom(roomType)
}