package com.xen.worduel_android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import com.xen.worduel_android.remote.PlayerApi
import com.xen.worduel_android.remote.PlayerIdInterceptor
import com.xen.worduel_android.remote.PlayerModel
import com.xen.worduel_android.remote.RoomApi
import com.xen.worduel_android.remote.repository.PlayerRepository
import com.xen.worduel_android.remote.repository.RoomRepository
import com.xen.worduel_android.ui.composable.LoginScreen
import com.xen.worduel_android.ui.composable.GameScreen
import com.xen.worduel_android.ui.composable.MenuScreen
import com.xen.worduel_android.ui.theme.WorduelandroidTheme
import com.xen.worduel_android.ui.viewmodel.PlayerViewModel
import com.xen.worduel_android.ui.viewmodel.RoomViewModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.logging.HttpLoggingInterceptor
import okio.ByteString
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    private var currentScreen by mutableStateOf("login")
    private var loggedInPlayer by mutableStateOf<PlayerModel?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logger)
            .addInterceptor(PlayerIdInterceptor())
            .build()

        val wsRequest = Request.Builder()
            .url("ws://10.0.2.2:8080/ws")
            .build()

        val wsListener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                println("Connected to server")
                webSocket.send("Hello from Kotlin client")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                println("Received message: $text")
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                println("Received bytes: ${bytes.hex()}")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                println("Closing: $code / $reason")
                webSocket.close(1000, null)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                t.printStackTrace()
            }
        }

        val webSocket = okHttpClient.newWebSocket(wsRequest, wsListener)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .client(okHttpClient)
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
                            player = player
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
                            player = player
                        )
                        val roomViewModel = ViewModelProvider(this, roomFactory)[RoomViewModel::class.java]

                        val shouldExitGame by roomViewModel.shouldExitGame.collectAsState()
                        LaunchedEffect(shouldExitGame) {
                            if (shouldExitGame) {
                                Log.d("GAME_EXIT", "Exited a Completed Game")
                                roomViewModel.prepareRoom()
                                currentScreen = "menu"
                            }
                        }

                        GameScreen(roomViewModel = roomViewModel)
                    }

                }
            }
        }

    }
}