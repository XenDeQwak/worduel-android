package com.xen.worduel_android.ui.composable.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.xen.worduel_android.ui.screen.ColorBorder
import com.xen.worduel_android.ui.screen.ColorEmpty
import com.xen.worduel_android.ui.viewmodel.RoomViewModel

@Composable
fun EmptyRow() {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        repeat(RoomViewModel.WORD_LENGTH) {
            LetterTile(letter = "", background = ColorEmpty, borderColor = ColorBorder)
        }
    }
}