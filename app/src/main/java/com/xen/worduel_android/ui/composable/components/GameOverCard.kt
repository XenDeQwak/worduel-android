package com.xen.worduel_android.ui.composable.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xen.worduel_android.ui.screen.ColorCorrect

@Composable
fun GameOverCard(
    isWin: Boolean,
    target: String?,
    onPlayAgain: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1A1A1B))
            .border(1.dp, Color(0xFF3A3A3C), RoundedCornerShape(12.dp))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text       = if (isWin) "🎉 You Win!" else "Game Over",
            fontSize   = 22.sp,
            fontWeight = FontWeight.Bold,
            color      = if (isWin) ColorCorrect else Color.White
        )
        if (!isWin && target != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                text     = "The word was  ${target.uppercase()}",
                fontSize = 15.sp,
                color    = Color(0xFFAAAAAA)
            )
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onPlayAgain,
            colors  = ButtonDefaults.buttonColors(containerColor = ColorCorrect),
            shape   = RoundedCornerShape(8.dp)
        ) {
            Text("Play Again", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}