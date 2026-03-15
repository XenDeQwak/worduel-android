package com.xen.worduel_android.ui.composable.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LetterTile(
    letter: String,
    background: Color,
    borderColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(62.dp)
            .border(2.dp, borderColor, RoundedCornerShape(4.dp))
            .background(background, RoundedCornerShape(4.dp))
    ) {
        Text(
            text       = letter,
            color      = Color.White,
            fontSize   = 26.sp,
            fontWeight = FontWeight.Bold,
            textAlign  = TextAlign.Center
        )
    }
}