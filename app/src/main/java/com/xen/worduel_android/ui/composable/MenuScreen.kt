package com.xen.worduel_android.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.xen.worduel_android.ui.viewmodel.RoomViewModel

@Composable
fun MenuScreen(
    roomViewModel: RoomViewModel,
    onRoomCreated: () -> Unit
) {
    val isLoading by roomViewModel.isLoading.collectAsState()
    var joinRoomId by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                modifier = Modifier.weight(1f),
                enabled  = !isLoading,
                onClick  = {
                    roomViewModel.createSoloGame(onReady = onRoomCreated)
                }
            ) {
                if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp))
                else Text("Create Solo Game")
            }

            Button(
                modifier = Modifier.weight(1f),
                enabled  = !isLoading,
                onClick  = { roomViewModel.createDuelGame(onReady = onRoomCreated) }
            ) {
                Text("Create Duel Game")
            }
        }

        OutlinedTextField(
            value         = joinRoomId,
            onValueChange = { joinRoomId = it },
            label         = { Text("Room ID") },
            modifier      = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
        )

        Button(
            onClick  = { roomViewModel.joinNewRoom(onRoomCreated, joinRoomId) },
            enabled  = joinRoomId.isNotBlank() && !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Join Room")
        }
    }
}