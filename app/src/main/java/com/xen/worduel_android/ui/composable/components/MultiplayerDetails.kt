package com.xen.worduel_android.ui.composable.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// GPT >>>> Claude
@Composable
fun MultiplayerDetails(
    roomId: String,
    player1Name: String,
    player2Name: String,
    player1Attempts: Int,
    player2Attempts: Int,
    modifier: Modifier = Modifier
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1A1A1B))
            .border(1.dp, Color(0xFF3A3A3C), RoundedCornerShape(12.dp))
            .padding(24.dp)
    ) {

        Text(
            text = "RoomID: $roomId",
            color = Color.White,
            fontSize = 16.sp
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = player1Name,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                    Text(
                        text = "(You)",
                        fontWeight = FontWeight.Light,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }

                Text(
                    text = "$player1Attempts/6",
                    color = Color(0xFF818384)
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = player2Name,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 20.sp
                )

                Text(
                    text = "$player2Attempts/6",
                    color = Color(0xFF818384)
                )
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF121213)
@Composable
fun MultiplayerDetailsPreview() {

    MultiplayerDetails(
        roomId = "1234567890",
        player1Name = "Gabriel",
        player2Name = "Ulrich",
        player1Attempts = 3,
        player2Attempts = 5
    )
}