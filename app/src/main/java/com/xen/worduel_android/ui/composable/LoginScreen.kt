package com.xen.worduel_android.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xen.worduel_android.ui.viewmodel.PlayerViewModel

@Composable
fun LoginScreen(
    playerViewModel: PlayerViewModel,
    onNicknameSet: () -> Unit
) {
    var nickname by remember { mutableStateOf("") }
    val currentPlayer by playerViewModel.currentPlayer.collectAsState()

    LaunchedEffect(currentPlayer) {
        if (currentPlayer != null)
            onNicknameSet()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Enter Your Name")

        OutlinedTextField(
            value = nickname,
            onValueChange = { nickname = it },
            singleLine = true,
            label = { Text("Player Name") }
        )

        Button(
            onClick = {
                if (nickname.isNotBlank()) {
                    playerViewModel.setNickname(nickname)
                }
            },
            enabled = nickname.isNotBlank(),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Login")
        }
    }
}