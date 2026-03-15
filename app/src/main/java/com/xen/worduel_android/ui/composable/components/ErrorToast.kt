package com.xen.worduel_android.ui.composable.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun ErrorToast(message: String, onDismiss: () -> Unit) {
    LaunchedEffect(message) {
        delay(2000)
        onDismiss()
    }
    Box(
        modifier = Modifier
            .padding(horizontal = 48.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = message, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
    Spacer(Modifier.height(8.dp))
}