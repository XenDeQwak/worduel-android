package com.xen.worduel_android.ui.composable.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.xen.worduel_android.remote.dto.GuessResponse
import com.xen.worduel_android.ui.viewmodel.RoomViewModel

@Composable
fun SubmittedRow(response: GuessResponse) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        repeat(RoomViewModel.WORD_LENGTH) { i ->
            FlipTile(
                letter = response.guess.getOrNull(i)?.toString() ?: "",
                letterType = response.position.getOrNull(i),
                flipDelay = i * 300
            )
        }
    }
}