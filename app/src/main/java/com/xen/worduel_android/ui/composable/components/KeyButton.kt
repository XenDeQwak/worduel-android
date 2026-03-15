package com.xen.worduel_android.ui.composable.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun KeyButton(
    label: String,
    width: Dp,
    color: Color,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick        = onClick,
        enabled        = enabled,
        shape          = RoundedCornerShape(4.dp),
        colors         = ButtonDefaults.buttonColors(containerColor = color),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 14.dp),
        modifier       = Modifier.width(width).height(58.dp)
    ) {
        Text(
            text       = label,
            fontSize   = if (label == "ENTER") 10.sp else 14.sp,
            fontWeight = FontWeight.Bold,
            color      = Color.White,
            maxLines   = 1
        )
    }
}