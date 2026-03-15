package com.xen.worduel_android.ui.composable.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xen.worduel_android.remote.dto.GuessResponse

@Composable
fun SoloGameContent(
    guessHistory: List<GuessResponse>,
    currentInput: String,
    isGameOver: Boolean,
    isWin: Boolean,
    errorMessage: String?,
    isLoading: Boolean,
    onKey: (Char) -> Unit,
    onBackspace: () -> Unit,
    onEnter: () -> Unit,
    onDismissError: () -> Unit,
    onPlayAgain: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121213))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text          = "WORDUEL",
                color         = Color.White,
                fontSize      = 28.sp,
                fontWeight    = FontWeight.Bold,
                letterSpacing = 6.sp,
                modifier      = Modifier.padding(top = 20.dp, bottom = 8.dp)
            )
            HorizontalDivider(color = Color(0xFF3A3A3C), thickness = 1.dp)

            Spacer(Modifier.height(12.dp))
            AnimatedVisibility(
                visible = errorMessage != null,
                enter   = slideInVertically() + fadeIn(),
                exit    = slideOutVertically() + fadeOut()
            ) {
                ErrorToast(message = errorMessage ?: "", onDismiss = onDismissError)
            }

            WordleGrid(
                guessHistory = guessHistory,
                currentInput = currentInput,
                modifier     = Modifier.padding(vertical = 8.dp)
            )

            Spacer(Modifier.weight(1f))

            AnimatedVisibility(
                visible = isGameOver,
                enter   = scaleIn() + fadeIn()
            ) {
                GameOverCard(
                    isWin = isWin,
                    target = guessHistory.lastOrNull()?.target,
                    onPlayAgain = onPlayAgain,
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
                )
            }

            Spacer(Modifier.weight(1f))

            if (!isGameOver) {
                WordleKeyboard(
                    guessHistory = guessHistory,
                    onKey        = onKey,
                    onBackspace  = onBackspace,
                    onEnter      = onEnter,
                    enabled      = !isLoading,
                    modifier     = Modifier.padding(bottom = 16.dp, start = 6.dp, end = 6.dp)
                )
            }
        }

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color    = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SoloGameContentPreview() {

    SoloGameContent(
        guessHistory = emptyList(),
        currentInput = "",
        isGameOver = false,
        isWin = false,
        errorMessage = null,
        isLoading = false,
        onKey = {},
        onBackspace = {},
        onEnter = {},
        onDismissError = {},
        onPlayAgain = {}
    )
}