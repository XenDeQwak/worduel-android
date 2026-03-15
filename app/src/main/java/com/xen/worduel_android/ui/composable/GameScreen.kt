package com.xen.worduel_android.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.xen.worduel_android.remote.LetterType
import com.xen.worduel_android.ui.composable.components.SoloGameContent
import com.xen.worduel_android.ui.viewmodel.RoomViewModel

//holy power of claude
val ColorCorrect = Color(0xFF538D4E)
private val ColorPresent = Color(0xFFB59F3B)
private val ColorAbsent  = Color(0xFF3A3A3C)
val ColorEmpty   = Color(0xFF121213)
val ColorBorder  = Color(0xFF3A3A3C)
val ColorFilled  = Color(0xFF565758)
val ColorKey     = Color(0xFF818384)

fun LetterType?.toColor(default: Color = ColorEmpty): Color = when (this) {
    LetterType.CORRECT -> ColorCorrect
    LetterType.PRESENT -> ColorPresent
    LetterType.ABSENT  -> ColorAbsent
    null               -> default
}
fun bestOf(a: LetterType?, b: LetterType): LetterType = when {
    a == LetterType.CORRECT || b == LetterType.CORRECT -> LetterType.CORRECT
    a == LetterType.PRESENT || b == LetterType.PRESENT -> LetterType.PRESENT
    else                                                -> LetterType.ABSENT
}

val KeyboardRows = listOf(
    listOf("Q","W","E","R","T","Y","U","I","O","P"),
    listOf("A","S","D","F","G","H","J","K","L"),
    listOf("ENTER","Z","X","C","V","B","N","M","⌫")
)

@Composable
fun GameScreen(roomViewModel: RoomViewModel) {
    val guessHistory by roomViewModel.guessHistory.collectAsState()
    val currentInput by roomViewModel.currentInput.collectAsState()
    val isGameOver   by roomViewModel.isGameOver.collectAsState()
    val isWin        by roomViewModel.isWin.collectAsState()
    val errorMessage by roomViewModel.errorMessage.collectAsState()
    val isLoading    by roomViewModel.isLoading.collectAsState()

    SoloGameContent(
        guessHistory   = guessHistory,
        currentInput   = currentInput,
        isGameOver     = isGameOver,
        isWin          = isWin,
        errorMessage   = errorMessage,
        isLoading      = isLoading,
        onKey          = roomViewModel::onKey,
        onBackspace    = roomViewModel::onBackspace,
        onEnter        = roomViewModel::onEnter,
        onDismissError = roomViewModel::dismissError,
        onPlayAgain    = roomViewModel::resetGame
    )
}