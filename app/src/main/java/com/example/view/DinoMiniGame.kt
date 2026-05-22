package com.example.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.PowerBiYellow
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.random.Random

@Composable
fun DinoMiniGame(modifier: Modifier = Modifier) {
    var score by remember { mutableStateOf(0) }
    var highScore by remember { mutableStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }
    var isGameOver by remember { mutableStateOf(false) }

    // Dimensions
    val groundY = 250f
    val dinoWidth = 24f
    val dinoHeight = 32f

    // Game states
    var dinoY by remember { mutableStateOf(0f) } // y displacement above ground
    var dinoVelocity by remember { mutableStateOf(0f) }
    val gravity = -1.2f
    val jumpForce = 15f

    // Cacti obstacles list: pair of (xPosition, width)
    val cactiList = remember { mutableStateListOf<Pair<Float, Float>>() }

    // Main Game Engine Loop
    LaunchedEffect(isPlaying) {
        if (!isPlaying) return@LaunchedEffect
        
        cactiList.clear()
        cactiList.add(600f to 15f)
        score = 0
        dinoY = 0f
        dinoVelocity = 0f
        isGameOver = false

        var ticks = 0
        while (isActive && isPlaying) {
            delay(16) // ~60 FPS
            ticks++
            
            // Physics: Dino jumping
            if (dinoY > 0 || dinoVelocity > 0) {
                dinoY += dinoVelocity
                dinoVelocity += gravity
                if (dinoY <= 0) {
                    dinoY = 0f
                    dinoVelocity = 0f
                }
            }

            // Move Cacti left
            val speed = 6.5f + (score / 150f) // accelerate speed over score
            for (i in cactiList.indices) {
                val pair = cactiList[i]
                cactiList[i] = (pair.first - speed) to pair.second
            }

            // Recycle and generate cacti
            if (cactiList.isNotEmpty() && cactiList[0].first < -50f) {
                cactiList.removeAt(0)
                score += 10
                if (score > highScore) {
                    highScore = score
                }
            }

            if (cactiList.size < 2 && (cactiList.isEmpty() || cactiList.last().first < (300f + Random.nextInt(150, 300)))) {
                val nextWidth = Random.nextFloat() * 12f + 12f
                cactiList.add(650f to nextWidth)
            }

            // Collision detection
            val dinoRect = Rect(
                left = 50f,
                top = groundY - dinoY - dinoHeight,
                right = 50f + dinoWidth,
                bottom = groundY - dinoY
            )

            for (cactus in cactiList) {
                val cactusRect = Rect(
                    left = cactus.first,
                    top = groundY - 30f,
                    right = cactus.first + cactus.second,
                    bottom = groundY
                )

                if (dinoRect.overlaps(cactusRect)) {
                    isGameOver = true
                    isPlaying = false
                    break
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
            .clickable {
                if (isGameOver) {
                    isGameOver = false
                    isPlaying = true
                } else if (!isPlaying) {
                    isPlaying = true
                } else {
                    // JUMP if on the ground
                    if (dinoY == 0f) {
                        dinoVelocity = jumpForce
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            val canvasWidth = size.width
            val scaleX = canvasWidth / 600f // Scaling factor to handle screen resizing fluidly
            
            // Draw Ground level
            drawLine(
                color = Color.White.copy(alpha = 0.35f),
                start = Offset(0f, groundY),
                end = Offset(size.width, groundY),
                strokeWidth = 2f
            )

            // Draw Dinosaur (Cute Retro Style)
            val dLeft = 50f * scaleX
            val dTop = groundY - dinoY - dinoHeight
            
            // T-Rex Body
            drawRect(
                color = NeonCyan,
                topLeft = Offset(dLeft, dTop),
                size = Size(dinoWidth, dinoHeight)
            )
            // Little eye
            drawRect(
                color = Color.Black,
                topLeft = Offset(dLeft + dinoWidth - 6f, dTop + 4f),
                size = Size(3f, 3f)
            )
            // Little arm
            drawRect(
                color = NeonCyan,
                topLeft = Offset(dLeft + dinoWidth, dTop + 14f),
                size = Size(6f, 3f)
            )

            // Draw Cacti obstacles
            for (cactus in cactiList) {
                val cLeft = cactus.first * scaleX
                val cWidth = cactus.second
                val cHeight = 30f
                val cTop = groundY - cHeight

                // Draw central cactus trunk
                drawRect(
                    color = PowerBiYellow,
                    topLeft = Offset(cLeft, cTop),
                    size = Size(cWidth, cHeight)
                )

                // Left branch
                if (cWidth > 15f) {
                    drawRect(
                        color = PowerBiYellow,
                        topLeft = Offset(cLeft - 5f, cTop + 10f),
                        size = Size(5f, 4f)
                    )
                    drawRect(
                        color = PowerBiYellow,
                        topLeft = Offset(cLeft - 5f, cTop + 4f),
                        size = Size(3f, 8f)
                    )
                }

                // Right branch
                drawRect(
                    color = PowerBiYellow,
                    topLeft = Offset(cLeft + cWidth, cTop + 14f),
                    size = Size(4f, 4f)
                )
                drawRect(
                    color = PowerBiYellow,
                    topLeft = Offset(cLeft + cWidth + 2f, cTop + 8f),
                    size = Size(3f, 8f)
                )
            }
        }

        // Overlay Game Over or Start prompts
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isGameOver) {
                Text(
                    text = "GAME OVER",
                    color = Color.Red,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Toca para reintentar",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            } else if (!isPlaying) {
                Text(
                    text = "Dino Google Jump",
                    color = NeonCyan,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Toca la ventana para jugar",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }
        }

        // Display Score HUD
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
        ) {
            Text(
                text = "HI $highScore   ",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$score",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
