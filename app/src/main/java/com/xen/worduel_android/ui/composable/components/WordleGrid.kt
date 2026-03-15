package com.xen.worduel_android.ui.composable.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xen.worduel_android.remote.dto.GuessResponse
import com.xen.worduel_android.ui.viewmodel.RoomViewModel

@Composable
fun WordleGrid(
    guessHistory: List<GuessResponse>,
    currentInput: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier            = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        repeat(RoomViewModel.MAX_ATTEMPTS) { row ->
            when {
                row < guessHistory.size  -> SubmittedRow(guessHistory[row])
                row == guessHistory.size -> ActiveRow(currentInput)
                else                     -> EmptyRow()
            }
        }
    }
}