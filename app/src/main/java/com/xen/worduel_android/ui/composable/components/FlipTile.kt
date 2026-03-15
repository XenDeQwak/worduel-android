package com.xen.worduel_android.ui.composable.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xen.worduel_android.remote.LetterType
import com.xen.worduel_android.ui.screen.ColorEmpty
import com.xen.worduel_android.ui.screen.ColorFilled
import com.xen.worduel_android.ui.screen.toColor
import kotlinx.coroutines.delay

@Composable
fun FlipTile(letter: String, letterType: LetterType?, flipDelay: Int) {
    var revealed by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(flipDelay.toLong())
        revealed = true
    }

    // Squish scaleY: 1 → 0 → 1. Colour swaps at the midpoint.
    val phase1 by animateFloatAsState(
        targetValue   = if (revealed) 0f else 1f,
        animationSpec = tween(150, easing = LinearEasing),
        label         = "flip_shrink"
    )
    val phase2 by animateFloatAsState(
        targetValue   = if (revealed) 1f else 0f,
        animationSpec = tween(150, delayMillis = 150, easing = LinearEasing),
        label         = "flip_grow"
    )

    val scaleY     = if (!revealed) phase1 else phase2
    val background = if (revealed) letterType.toColor() else ColorEmpty
    val border     = if (revealed) background else ColorFilled

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(62.dp)
            .border(2.dp, border, RoundedCornerShape(4.dp))
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