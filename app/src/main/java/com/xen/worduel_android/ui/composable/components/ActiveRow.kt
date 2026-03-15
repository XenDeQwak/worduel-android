package com.xen.worduel_android.ui.composable.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xen.worduel_android.ui.composable.ColorBorder
import com.xen.worduel_android.ui.composable.ColorEmpty
import com.xen.worduel_android.ui.composable.ColorFilled
import com.xen.worduel_android.ui.viewmodel.RoomViewModel

@Composable
fun ActiveRow(input: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        repeat(RoomViewModel.WORD_LENGTH) { i ->
            val ch = input.getOrNull(i)?.toString() ?: ""
            LetterTile(
                letter = ch,
                background = ColorEmpty,
                borderColor = if (ch.isNotEmpty()) ColorFilled else ColorBorder
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActiveRowPreview() {
    ActiveRow("HELLO");
}