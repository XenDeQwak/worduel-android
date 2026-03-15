package com.xen.worduel_android.remote.dto

import com.xen.worduel_android.remote.LetterType

data class GuessResponse (
    val playerId: String?,
    val guess: String,
    val target: String?,
    val position: List<LetterType>
)

