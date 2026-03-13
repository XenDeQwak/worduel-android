package com.xen.worduel_android.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xen.worduel_android.ui.viewmodel.RoomViewModel

@Composable
fun MenuScreen(
    roomViewModel: RoomViewModel,
    onRoomCreated: () -> Unit
) {

    var roomId by remember { mutableStateOf("") }

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
                onClick = {
                    roomViewModel.createRoom("solo")
                    onRoomCreated()
                }
            ) {
                Text("Solo")
            }

            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    roomViewModel.createRoom("duel")
                    onRoomCreated()
                }
            ) {
                Text("Duel")
            }
        }

        OutlinedTextField(
            value = roomId,
            onValueChange = { roomId = it },
            label = { Text("Room ID") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
        )

        Button(
            onClick = {
                roomViewModel.joinRoom(roomId)
                onRoomCreated()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Join Room")
        }
    }
}