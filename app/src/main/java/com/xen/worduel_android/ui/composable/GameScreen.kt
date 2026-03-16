package com.xen.worduel_android.ui.composable

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.xen.worduel_android.remote.LetterType
import com.xen.worduel_android.ui.composable.components.DuelGameContent
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
    val currentRoom by roomViewModel.currentRoom.collectAsState()
    val guessHistory by roomViewModel.guessHistory.collectAsState()
    val currentInput by roomViewModel.currentInput.collectAsState()
    val isGameOver by roomViewModel.isGameOver.collectAsState()
    val isWin by roomViewModel.isWin.collectAsState()
    val errorMessage by roomViewModel.errorMessage.collectAsState()
    val isLoading by roomViewModel.isLoading.collectAsState()

    currentRoom?.let { room ->

        Log.d("CURRENT_ROOM", currentRoom.toString())

        when (room.roomType) {
            "SOLO" -> {
                SoloGameContent(
                    guessHistory = guessHistory,
                    currentInput = currentInput,
                    isGameOver = isGameOver,
                    isWin = isWin,
                    errorMessage = errorMessage,
                    isLoading = isLoading,
                    onKey = roomViewModel::onKey,
                    onBackspace = roomViewModel::onBackspace,
                    onEnter = roomViewModel::onEnter,
                    onDismissError = roomViewModel::dismissError,
                    onPlayAgain = { roomViewModel.resetGame() }
                )
            }

            "DUEL" -> {
                val player1Name by roomViewModel.player1Name.collectAsState()
                val player2Name by roomViewModel.player2Name.collectAsState()
                val player1Attempts by roomViewModel.player1Attempts.collectAsState()
                val player2Attempts by roomViewModel.player2Attempts.collectAsState()

                val isGameActive by roomViewModel.isGameActive.collectAsState()

                DuelGameContent(
                    guessHistory = guessHistory,
                    currentInput = currentInput,
                    isGameOver = isGameOver,
                    isWin = isWin,
                    errorMessage = errorMessage,
                    isLoading = isLoading,
                    onKey = roomViewModel::onKey,
                    onBackspace = roomViewModel::onBackspace,
                    onEnter = roomViewModel::onEnter,
                    onDismissError = roomViewModel::dismissError,
                    onPlayAgain = { roomViewModel.resetGame() },

                    roomId = room.roomId,
                    player1Name = player1Name,
                    player2Name = player2Name,
                    player1Guess = player1Attempts,
                    player2Guess = player2Attempts,

                    OnJoin = {
                        roomViewModel.startGame(room.roomId)
                             },
                    isGameActive
                )
            }

            else -> {
                Text(
                    text = "Unknown room type",
                    color = Color.Red,
                    modifier = Modifier.fillMaxSize(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }



}