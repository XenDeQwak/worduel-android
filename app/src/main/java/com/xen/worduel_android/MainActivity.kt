package com.xen.worduel_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import com.xen.worduel_android.remote.PlayerApi
import com.xen.worduel_android.remote.PlayerModel
import com.xen.worduel_android.remote.RoomApi
import com.xen.worduel_android.remote.repository.PlayerRepository
import com.xen.worduel_android.remote.repository.RoomRepository
import com.xen.worduel_android.ui.composable.LoginScreen
import com.xen.worduel_android.ui.screen.GameScreen
import com.xen.worduel_android.ui.composable.MenuScreen
import com.xen.worduel_android.ui.theme.WorduelandroidTheme
import com.xen.worduel_android.ui.viewmodel.PlayerViewModel
import com.xen.worduel_android.ui.viewmodel.RoomViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    private var currentScreen by mutableStateOf("login")
    private var loggedInPlayer by mutableStateOf<PlayerModel?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val playerApi        = retrofit.create(PlayerApi::class.java)
        val playerRepository = PlayerRepository(playerApi)
        val playerFactory    = PlayerViewModel.Factory(playerRepository)
        val playerViewModel  = ViewModelProvider(this, playerFactory)[PlayerViewModel::class.java]

        val roomApi        = retrofit.create(RoomApi::class.java)
        val roomRepository = RoomRepository(roomApi)

        setContent {
            WorduelandroidTheme {
                when (currentScreen) {
                    "login" -> LoginScreen(
                        playerViewModel = playerViewModel,
                        onNicknameSet   = { player ->
                            loggedInPlayer  = player
                            currentScreen   = "menu"
                        }
                    )

                    "menu" -> {
                        val player = loggedInPlayer!!

                        val roomFactory = RoomViewModel.Factory(
                            repository  = roomRepository,
                            playerId    = player.playerId,
                            playerModel = player
                        )
                        val roomViewModel = ViewModelProvider(this, roomFactory)[RoomViewModel::class.java]

                        MenuScreen(
                            roomViewModel = roomViewModel,
                            onRoomCreated = { currentScreen = "game" }
                        )
                    }

                    "game" -> {
                        val player = loggedInPlayer!!

                        val roomFactory = RoomViewModel.Factory(
                            repository  = roomRepository,
                            playerId    = player.playerId,
                            playerModel = player
                        )
                        val roomViewModel = ViewModelProvider(this, roomFactory)[RoomViewModel::class.java]

                        GameScreen(roomViewModel = roomViewModel)
                    }
                }
            }
        }
    }
}