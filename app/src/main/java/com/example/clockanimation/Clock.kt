package com.example.clockanimation

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime


@Composable
fun ClockWithMusicInfo() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        // Display date
        val currentDate = LocalDate.now()
        BasicText(
            text = "${currentDate.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }} ${currentDate.dayOfMonth}",
            style = TextStyle(fontSize = 24.sp, color = Color(0xFFD38C91)),
            modifier = Modifier.padding(top = 70.dp)
        )

        Spacer(modifier = Modifier.height(170.dp))

        // Display the clock
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(275.dp)
                .background(Color.Black)
        ) {
            AnimatedMusicClock()
        }

        Spacer(modifier = Modifier.height(64.dp))


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {


            Row(

                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp)
            ) {
                Column {
                    BasicText(
                        text = "That Cool Song",
                        style = TextStyle(fontSize = 20.sp, color = Color(0xFFD38C91)),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Spacer(modifier = Modifier.padding(3.dp))
                    BasicText(
                        text = "It's Artist",
                        style = TextStyle(fontSize = 16.sp, color = Color(0xFFD38C91)),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }


                Spacer(modifier = Modifier.padding(3.dp))


                IconButton(onClick = {   }) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "Previous",
                        tint = Color(0xFFD38C91),
                        modifier = Modifier.size(100.dp)
                    )
                }
                IconButton(onClick = {  }) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play/Pause",
                        tint = Color(0xFFD38C91),
                        modifier = Modifier.size(100.dp)
                    )
                }
                IconButton(onClick = {  }) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Next",
                        tint = Color(0xFFD38C91),
                        modifier = Modifier.size(100.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun AnimatedMusicClock() {
    val infiniteTransition = rememberInfiniteTransition()
    val animatedValue by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2500, easing = LinearEasing)
        )
    )

    val currentTime = remember { mutableStateOf(LocalTime.now()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime.value = LocalTime.now()
            delay(1000L)
        }
    }

    val minuteAngle = (currentTime.value.minute * 6).toFloat() + (currentTime.value.second * 6 / 60)
    val hourAngle = (currentTime.value.hour % 12 * 30).toFloat() + (minuteAngle / 12)

    Canvas(modifier = Modifier.size(300.dp)) {
        // Draw the animated circular border
        for (i in 0..359 step 5) {
            val startAngle = i.toFloat()
            val sweepAngle = 5f
            val factor = animatedValue + i / 360f
            val hue = (factor * 360) % 360
            val color = Color.hsl(hue, 1f, 0.5f)

            drawArc(
                color = color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round),
                size = size
            )
        }

        // Draw hour hand
        rotate(degrees = hourAngle, pivot = center) {
            drawLine(
                color = Color.Red,
                start = center,
                end = Offset(center.x, center.y - size.minDimension / 4),
                strokeWidth = 6f
            )
        }

        // Draw minute hand
        rotate(degrees = minuteAngle, pivot = center) {
            drawLine(
                color = Color.Cyan,
                start = center,
                end = Offset(center.x, center.y - size.minDimension / 3),
                strokeWidth = 6f
            )
        }

        // Draw cross design between hour and minute hand
        drawLine(
            color = Color.Red,
            start = Offset(center.x - 10, center.y),
            end = Offset(center.x + 10, center.y),
            strokeWidth = 2f
        )
        drawLine(
            color = Color.Red,
            start = Offset(center.x, center.y - 10),
            end = Offset(center.x, center.y + 10),
            strokeWidth = 2f
        )
    }
}




//change 1