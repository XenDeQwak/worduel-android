package com.xen.worduel_android.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.xen.worduel_android.ui.viewmodel.RoomViewModel

@Composable
fun GameScreen(
    roomViewModel: RoomViewModel
) {
    val currentRoom by roomViewModel.currentRoom.collectAsState()

    if (currentRoom?.roomType.equals("solo")) {

    }

}