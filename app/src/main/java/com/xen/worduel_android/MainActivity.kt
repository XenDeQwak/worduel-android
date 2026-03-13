package com.xen.worduel_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.xen.worduel_android.remote.PlayerApi
import com.xen.worduel_android.remote.RoomApi
import com.xen.worduel_android.remote.repository.PlayerRepository
import com.xen.worduel_android.remote.repository.RoomRepository
import com.xen.worduel_android.ui.composable.LoginScreen
import com.xen.worduel_android.ui.screen.MenuScreen
import com.xen.worduel_android.ui.theme.WorduelandroidTheme
import com.xen.worduel_android.ui.viewmodel.PlayerViewModel
import com.xen.worduel_android.ui.viewmodel.RoomViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    private var currentScreen = mutableStateOf("login")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val playerApi = retrofit.create(PlayerApi::class.java)
        val playerRepository = PlayerRepository(playerApi)
        val playerFactory = PlayerViewModel.Factory(playerRepository)
        val playerViewModel = ViewModelProvider(this, playerFactory)[PlayerViewModel::class.java]

        val roomApi = retrofit.create(RoomApi::class.java)
        val roomRepository = RoomRepository(roomApi)
        val roomFactory = RoomViewModel.Factory(roomRepository)
        val roomViewModel = ViewModelProvider(this, roomFactory)[RoomViewModel::class.java]


        setContent {
            WorduelandroidTheme {
                when (currentScreen.value) {
                    "login" -> LoginScreen(
                        playerViewModel,
                        onNicknameSet = {
                            currentScreen.value = "menu"
                        }
                    )

                    "menu" -> MenuScreen(
                        roomViewModel,
                        onRoomCreated = {
                            currentScreen.value = "game"
                        }
                    )
                }
            }
        }
    }
}