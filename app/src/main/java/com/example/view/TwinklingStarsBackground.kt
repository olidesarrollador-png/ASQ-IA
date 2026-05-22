package com.example.view

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.isActive
import kotlinx.coroutines.delay
import kotlin.random.Random

// Class representing a single twinkling, drifting point (star)
data class TwinklePoint(
    var xPercent: Float,
    var yPercent: Float,
    val size: Float,
    var alpha: Float,
    var alphaDir: Float, // 1f for brightening, -1f for dimming
    val speed: Float,    // movement speed (drift)
    val maxAlpha: Float,
    val minAlpha: Float
)

@Composable
fun TwinklingStarsBackground(modifier: Modifier = Modifier) {
    // Generate a fixed number of stars
    val pointCount = 60
    val points = remember {
        val list = mutableListOf<TwinklePoint>()
        for (i in 0 until pointCount) {
            list.add(
                TwinklePoint(
                    xPercent = Random.nextFloat(),
                    yPercent = Random.nextFloat(),
                    size = Random.nextFloat() * 2.5f + 1f, // Sizes between 1dp and 3.5dp
                    alpha = Random.nextFloat(),
                    alphaDir = if (Random.nextBoolean()) 1f else -1f,
                    speed = Random.nextFloat() * 0.0003f + 0.0001f, // Drift speed
                    maxAlpha = Random.nextFloat() * 0.7f + 0.3f,
                    minAlpha = Random.nextFloat() * 0.15f
                )
            )
        }
        list
    }

    // A simple animation frame tick state to trigger Canvas redraws
    var tick by remember { mutableStateOf(0L) }

    LaunchedEffect(Unit) {
        while (isActive) {
            withFrameMillis { frameTime ->
                // Update stars state
                for (pt in points) {
                    // Update transparency (Twinkle effect)
                    pt.alpha += pt.alphaDir * 0.012f
                    if (pt.alpha >= pt.maxAlpha) {
                        pt.alpha = pt.maxAlpha
                        pt.alphaDir = -1f
                    } else if (pt.alpha <= pt.minAlpha) {
                        pt.alpha = pt.minAlpha
                        pt.alphaDir = 1f
                    }

                    // Gentle drift downwards (Drifting effect)
                    pt.yPercent += pt.speed
                    if (pt.yPercent > 1.0f) {
                        pt.yPercent = 0.0f
                        pt.xPercent = Random.nextFloat()
                    }
                }
                tick = frameTime
            }
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        
        // Trigger redrawing based on tick
        val _tick = tick 

        for (pt in points) {
            val px = pt.xPercent * width
            val py = pt.yPercent * height
            
            drawCircle(
                color = Color.White.copy(alpha = pt.alpha),
                radius = pt.size,
                center = Offset(px, py)
            )
        }
    }
}
