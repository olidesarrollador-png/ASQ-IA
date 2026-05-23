package com.example.view

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun GoogleOliverIntro(
    onIntroFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    var phase by remember { mutableStateOf(0) } // 0: Google Gemini dots, 1: Oliver Signature & FLX Badge, 2: Official "Ask AI" logo animation
    val scale = remember { Animatable(0.5f) }
    val alpha = remember { Animatable(0f) }

    // Double rotation for Google RGB dots merging
    val infiniteTransition = rememberInfiniteTransition(label = "DotsRotation")
    val dotsAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "angle"
    )

    LaunchedEffect(phase) {
        scale.snapTo(0.6f)
        alpha.snapTo(0f)
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(dampingRatio = 0.82f, stiffness = Spring.StiffnessLow)
        )
        alpha.animateTo(1f, animationSpec = tween(700))

        when (phase) {
            0 -> {
                delay(2200)
                phase = 1
            }
            1 -> {
                delay(2600)
                phase = 2
            }
            2 -> {
                delay(2800)
                onIntroFinished()
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        TwinklingStarsBackground(Modifier.fillMaxSize())

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(24.dp)
                .alpha(alpha.value)
                .scale(scale.value)
        ) {
            when (phase) {
                0 -> {
                    // --- PHASE 0: REAL GOOGLE GEMINI ENTRANCE ---
                    Text(
                        text = "CONEXIÓN",
                        color = MutedGrey,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 3.sp,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    // Real Google orbital particles merging
                    Box(
                        modifier = Modifier.size(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        for (i in 0 until 4) {
                            val orbitAngle = dotsAngle + (i * 90f)
                            val rad = Math.toRadians(orbitAngle.toDouble())
                            val distance = 25f + (10f * Math.sin(Math.toRadians((dotsAngle * 3).toDouble()))).toFloat()
                            val offsetX = (distance * Math.cos(rad)).dp
                            val offsetY = (distance * Math.sin(rad)).dp
                            val dotColor = when (i) {
                                0 -> GoogleBlue
                                1 -> GoogleRed
                                2 -> GoogleYellow
                                else -> GoogleGreen
                            }
                            Box(
                                modifier = Modifier
                                    .offset(x = offsetX, y = offsetY)
                                    .size(18.dp)
                                    .background(dotColor, CircleShape)
                                    .border(BorderStroke(1.5.dp, Color.White.copy(alpha = 0.3f)), CircleShape)
                            )
                        }

                        // Center glowing engine
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    Brush.radialGradient(
                                        listOf(Color.White, Color.Transparent)
                                    ), CircleShape
                                )
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "Google AI Link",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Light,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Sincronizando con Gemini Engine...",
                        color = ElectricBlue,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                1 -> {
                    // --- PHASE 1: OLIVER DEVELOPER ACCOUNT CONSOLE ---
                    Text(
                        text = "imaginado por",
                        color = MutedGrey,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 1.sp,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Oliver",
                        color = StarWhite,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Light,
                        letterSpacing = 1.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    OliverBadgeEmblem(modifier = Modifier.size(230.dp))
                }

                2 -> {
                    // --- PHASE 2: ASK AI LOGO REVEAL (MATCHING IMAGE_0.PNG DEFINITION) ---
                    Text(
                        text = "ACCEDIENDO A",
                        color = MutedGrey,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 3.sp,
                        modifier = Modifier.padding(bottom = 30.dp)
                    )

                    // Replica of image_0.png! Beautiful rounded card containing glow, border, and white central sparkle.
                    Box(
                        modifier = Modifier
                            .size(160.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Ambient radial purple aura behind it
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            Color(0xFF8E24AA).copy(alpha = 0.4f),
                                            Color.Transparent
                                        )
                                    )
                                )
                        )

                        // The rounded card representing image_0.png
                        Box(
                            modifier = Modifier
                                .size(126.dp)
                                .border(
                                    BorderStroke(
                                        1.8.dp,
                                        Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFFE040FB), // Glowing Magenta/Purple left
                                                Color(0xFF00E5FF)  // Glowing Cyan/Blue right
                                            )
                                        )
                                    ),
                                    RoundedCornerShape(38.dp)
                                )
                                .background(Color.White.copy(alpha = 0.03f), RoundedCornerShape(38.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            // Sparkling 4-pointed star inside card
                            SparkleFourPoint(modifier = Modifier.size(72.dp), color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Text(
                        text = "Ask AI",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )

                    Text(
                        text = "Tu centro inteligente de aplicaciones.",
                        color = Color(0xFFA78BFA),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SparkleFourPoint(
    modifier: Modifier = Modifier,
    color: Color = Color.White
) {
    // Elegant four pointed sparkle star vector using overlapping shapes
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Horizontal bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, color, Color.Transparent)
                    ),
                    RoundedCornerShape(2.dp)
                )
        )
        // Vertical bar
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(4.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, color, Color.Transparent)
                    ),
                    RoundedCornerShape(2.dp)
                )
        )
        // Central glow star burst
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(color, Color.Transparent)
                    ), CircleShape
                )
        )
    }
}

@Composable
fun OliverBadgeEmblem(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Outer glow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(NeonCyan.copy(alpha = 0.15f), Color.Transparent),
                    ),
                    shape = CircleShape
                )
        )

        // Outer Metallic Rim
        Box(
            modifier = Modifier
                .fillMaxSize(0.92f)
                .border(BorderStroke(2.dp, Color(0xFFC0C0C0)), CircleShape)
                .background(Color(0xFF0F0F11), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Inner Frame
            Box(
                modifier = Modifier
                    .fillMaxSize(0.94f)
                    .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // Content of Badge
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "DEVELOPER ACCOUNT ENABLED",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "FLX",
                        color = NeonCyan,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                        textAlign = TextAlign.Center
                    )
                    
                    Text(
                        text = "APP STUDIO",
                        color = NeonCyan.copy(alpha = 0.85f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "GOOGLE PLAY CONSOLE LICENSED",
                        color = Color.White.copy(alpha = 0.45f),
                        fontSize = 7.sp,
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 0.5.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Bottom-right Blue Verified Verification Checkmark Badge
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 12.dp)
                .size(44.dp)
                .background(Color.White, CircleShape)
                .padding(1.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Licenciado",
                tint = Color(0xFF1D9BF0), // Beautiful crisp verification blue
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
