package com.xen.worduel_android.ui.composable.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xen.worduel_android.remote.LetterType
import com.xen.worduel_android.remote.dto.GuessResponse
import com.xen.worduel_android.ui.screen.ColorKey
import com.xen.worduel_android.ui.screen.KeyboardRows
import com.xen.worduel_android.ui.screen.bestOf
import com.xen.worduel_android.ui.screen.toColor
import kotlin.collections.forEach

@Composable
fun WordleKeyboard(
    guessHistory: List<GuessResponse>,
    onKey: (Char) -> Unit,
    onBackspace: () -> Unit,
    onEnter: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    val letterStates: Map<Char, LetterType> = remember(guessHistory) {
        buildMap {
            guessHistory.forEach { response ->
                response.guess.forEachIndexed { i, ch ->
                    val type = response.position.getOrNull(i) ?: return@forEachIndexed
                    val key  = ch.uppercaseChar()
                    put(key, bestOf(get(key), type))
                }
            }
        }
    }

    Column(
        modifier            = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        KeyboardRows.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment     = Alignment.CenterVertically
            ) {
                row.forEach { key ->
                    when (key) {
                        "ENTER" -> KeyButton(key, 54.dp, ColorKey, enabled, onEnter)
                        "⌫"     -> KeyButton(key, 46.dp, ColorKey, enabled, onBackspace)
                        else    -> {
                            val ch    = key.first()
                            val color = letterStates[ch].toColor(default = ColorKey)
                            KeyButton(key, 34.dp, color, enabled) { onKey(ch) }
                        }
                    }
                }
            }
        }
    }
}