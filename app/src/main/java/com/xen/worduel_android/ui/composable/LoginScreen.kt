package com.xen.worduel_android.ui.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xen.worduel_android.remote.PlayerModel
import com.xen.worduel_android.ui.viewmodel.PlayerViewModel
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    playerViewModel: PlayerViewModel,
    onNicknameSet: (PlayerModel) -> Unit
) {
    val isLoading    by playerViewModel.isLoading.collectAsState()
    val errorMessage by playerViewModel.errorMessage.collectAsState()

    var nickname by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121213)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text          = "WORDUEL",
                color         = Color.White,
                fontSize      = 32.sp,
                fontWeight    = FontWeight.Bold,
                letterSpacing = 6.sp
            )

            Text(
                text     = "Enter a nickname to start",
                color    = Color(0xFFAAAAAA),
                fontSize = 14.sp
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value         = nickname,
                onValueChange = { nickname = it },
                placeholder   = { Text("Nickname", color = Color(0xFF666666)) },
                singleLine    = true,
                colors        = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor   = Color.White,
                    unfocusedBorderColor = Color(0xFF3A3A3C),
                    focusedTextColor     = Color.White,
                    unfocusedTextColor   = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Error toast
            AnimatedVisibility(visible = errorMessage != null, enter = fadeIn(), exit = fadeOut()) {
                ErrorToast(message = errorMessage ?: "", onDismiss = playerViewModel::dismissError)
            }

            Button(
                onClick = {
                    if (nickname.isNotBlank()) {
                        playerViewModel.setNickname(nickname.trim()) { player ->
                            onNicknameSet(player)   // ← forwards PlayerModel to MainActivity
                        }
                    }
                },
                enabled  = nickname.isNotBlank() && !isLoading,
                shape    = RoundedCornerShape(8.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = Color(0xFF538D4E)),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                } else {
                    Text("Play", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
private fun ErrorToast(message: String, onDismiss: () -> Unit) {
    LaunchedEffect(message) { delay(2500); onDismiss() }
    Text(text = message, color = Color(0xFFE57373), fontSize = 13.sp)
}