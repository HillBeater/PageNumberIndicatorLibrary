package com.hillbeater.indicator_compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hillbeater.indicator_compose.theme.Poppins

@Composable
fun AnimatedIndicator(
    currentPage: Int,
    totalPages: Int,
    modifier: Modifier = Modifier,
    maxVisibleDots: Int = 3
) {

    if (totalPages <= 0) return

    val start = maxOf(0, currentPage - maxVisibleDots)
    val end = minOf(totalPages, currentPage + maxVisibleDots + 1)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // LEFT DOTS
        repeat(currentPage - start) { index ->

            val color =
                if (index == currentPage - start - 1) Color.DarkGray
                else Color.Gray

            Dot(color)
        }

        // COUNTER
        Counter(currentPage + 1, totalPages)

        // RIGHT DOTS
        repeat(end - currentPage - 1) { index ->

            val color =
                if (index == 0) Color.DarkGray
                else Color.Gray

            Dot(color)
        }
    }
}

@Composable
fun Dot(color: Color) {

    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(),
        label = ""
    )

    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .size(6.dp)
            .scale(scale)
            .background(color, CircleShape)
    )
}

@Composable
fun Counter(
    current: Int,
    total: Int
) {

    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(),
        label = ""
    )

    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .scale(scale)
            .background(
                color = Color.DarkGray,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 8.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "$current/$total",
            color = Color.White,
            fontSize = 12.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun IndicatorPreview() {

    AnimatedIndicator(
        currentPage = 1,
        totalPages = 5
    )
}