package com.example.view

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.ElectricBlue
import com.example.model.AppSettings
import kotlinx.coroutines.isActive
import kotlin.math.sin
import kotlin.random.Random

data class EnhancedPoint(
    var xPercent: Float,
    var yPercent: Float,
    val isMedium: Boolean, // True for medium-sized glowing dots, false for little dots
    val size: Float,
    var alpha: Float,
    var alphaDir: Float,
    val speed: Float,
    val maxAlpha: Float,
    val minAlpha: Float
)

@Composable
fun TwinklingStarsBackground(modifier: Modifier = Modifier) {
    // Generate a beautiful combination of little dots and medium-sized dots
    val pointCount = 75
    val points = remember {
        val list = mutableListOf<EnhancedPoint>()
        for (i in 0 until pointCount) {
            val isMedium = i % 5 == 0 // 20% are medium glowing dots
            val size = if (isMedium) {
                Random.nextFloat() * 2.5f + 4f // 4.0 to 6.5 width
            } else {
                Random.nextFloat() * 1.5f + 1.2f // 1.2 to 2.7 width
            }
            list.add(
                EnhancedPoint(
                    xPercent = Random.nextFloat(),
                    yPercent = Random.nextFloat(),
                    isMedium = isMedium,
                    size = size,
                    alpha = Random.nextFloat(),
                    alphaDir = if (Random.nextBoolean()) 1f else -1f,
                    speed = if (isMedium) {
                        Random.nextFloat() * 0.00018f + 0.00008f // slower drift for medium
                    } else {
                        Random.nextFloat() * 0.00035f + 0.00015f // faster small ones
                    },
                    maxAlpha = if (isMedium) 1.0f else 0.75f,
                    minAlpha = if (isMedium) 0.15f else 0.05f
                )
            )
        }
        list
    }

    // A time variable to compute wave flow
    var wavePhase by remember { mutableStateOf(0f) }
    var tick by remember { mutableStateOf(0L) }

    LaunchedEffect(Unit) {
        while (isActive) {
            withFrameMillis { frameTime ->
                // Update stars drift and twinkling
                for (pt in points) {
                    pt.alpha += pt.alphaDir * (if (pt.isMedium) 0.008f else 0.016f)
                    if (pt.alpha >= pt.maxAlpha) {
                        pt.alpha = pt.maxAlpha
                        pt.alphaDir = -1f
                    } else if (pt.alpha <= pt.minAlpha) {
                        pt.alpha = pt.minAlpha
                        pt.alphaDir = 1f
                    }

                    pt.yPercent += pt.speed
                    if (pt.yPercent > 1.0f) {
                        pt.yPercent = 0.0f
                        pt.xPercent = Random.nextFloat()
                    }
                }
                
                // Advance wave animation phase
                wavePhase += 0.015f
                if (wavePhase > (2 * Math.PI)) {
                    wavePhase = 0f
                }
                tick = frameTime
            }
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        val _tick = tick // Read to force recomposition on tick

        // 1. Draw elegant neon ambient waves in the background
        val wavePath1 = Path()
        val wavePath2 = Path()

        val yBase1 = height * 0.82f
        val yBase2 = height * 0.85f

        wavePath1.moveTo(0f, yBase1)
        wavePath2.moveTo(0f, yBase2)

        for (x in 0..width.toInt() step 15) {
            val fx = x.toFloat()
            // Sine waves with phase shift
            val fy1 = yBase1 + sin(fx * 0.004f + wavePhase) * 28f
            val fy2 = yBase2 + sin(fx * 0.005f - wavePhase + 1.2f) * 20f

            wavePath1.lineTo(fx, fy1)
            wavePath2.lineTo(fx, fy2)
        }

        // Draw waves with gorgeous gradient strokes
        drawPath(
            path = wavePath1,
            brush = Brush.horizontalGradient(
                colors = listOf(NeonCyan.copy(alpha = 0.22f), ElectricBlue.copy(alpha = 0.04f))
            ),
            style = Stroke(width = 3f)
        )

        drawPath(
            path = wavePath2,
            brush = Brush.horizontalGradient(
                colors = listOf(ElectricBlue.copy(alpha = 0.03f), NeonCyan.copy(alpha = 0.18f))
            ),
            style = Stroke(width = 2.5f)
        )

        // 2. Draw stars (STARS mode), Fortnite dot-pairs (FORTNITE mode), or Glow Fog (GLOW mode)
        if (AppSettings.selectedBackground == "FORTNITE") {
            // Animated pairs of tiny white dots floating together (representing the requested Fortnite style)
            for (pt in points) {
                val px = pt.xPercent * width
                val py = pt.yPercent * height

                // First dot representation
                drawCircle(
                    color = Color.White.copy(alpha = pt.alpha * 0.95f),
                    radius = 2.4f,
                    center = Offset(px, py)
                )

                // Second dot of the pair placed extremely close together (conforming to 'dots together')
                drawCircle(
                    color = Color.White.copy(alpha = pt.alpha * 0.95f),
                    radius = 2.4f,
                    center = Offset(px + 4.2f, py + 1.5f)
                )
            }

            // Subtle dark neon aura overlay
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF673AB7).copy(alpha = 0.08f), Color.Transparent),
                    center = Offset(width * 0.5f, height * 0.5f),
                    radius = width * 0.8f
                )
            )
        } else if (AppSettings.selectedBackground == "GLOW") {
            // Glistening space gas clouds and soft colorful visual gradients (Electric blue and violet purple highlights)
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF8E24AA).copy(alpha = 0.18f), Color.Transparent), // Deep Purple
                    center = Offset(0f, 0f),
                    radius = width * 0.8f
                )
            )
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF00B0FF).copy(alpha = 0.14f), Color.Transparent), // Electric Cyan
                    center = Offset(width, height * 0.3f),
                    radius = width * 0.7f
                )
            )
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF1A237E).copy(alpha = 0.15f), Color.Transparent), // Indigo Base
                    center = Offset(width * 0.4f, height * 0.8f),
                    radius = width * 0.9f
                )
            )

            // Scattered soft twinkling stars
            for (pt in points) {
                if (!pt.isMedium) {
                    val px = pt.xPercent * width
                    val py = pt.yPercent * height
                    drawCircle(
                        color = Color.White.copy(alpha = pt.alpha * 0.7f),
                        radius = 1.6f,
                        center = Offset(px, py)
                    )
                }
            }
        } else {
            // Traditional cosmic stars (STARS mode)
            for (pt in points) {
                val px = pt.xPercent * width
                val py = pt.yPercent * height

                if (pt.isMedium) {
                    // Glow aura for medium dots
                    drawCircle(
                        color = NeonCyan.copy(alpha = pt.alpha * 0.25f),
                        radius = pt.size * 2.2f,
                        center = Offset(px, py)
                    )
                    drawCircle(
                        color = Color.White.copy(alpha = pt.alpha),
                        radius = pt.size,
                        center = Offset(px, py)
                    )
                } else {
                    // Plain sharp little dot
                    drawCircle(
                        color = Color.White.copy(alpha = pt.alpha * 0.8f),
                        radius = pt.size,
                        center = Offset(px, py)
                    )
                }
            }
        }
    }
}
