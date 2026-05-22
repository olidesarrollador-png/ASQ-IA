package com.example.view

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
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
    var step by remember { mutableStateOf(0) }
    val transitionScale = remember { Animatable(0.6f) }
    val transitionAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Step 0: Google logo and "Power BI Google" fades in
        transitionScale.animateTo(
            targetValue = 1f,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        )
        transitionAlpha.animateTo(1f, animationSpec = tween(1000))
        
        delay(1500)
        
        // Step 1: Transition to "imaginado por Oliver" and showing the FLX App Studio badge
        step = 1
        transitionScale.snapTo(0.85f)
        transitionAlpha.snapTo(0.2f)
        
        transitionScale.animateTo(
            targetValue = 1f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
        )
        transitionAlpha.animateTo(1f, animationSpec = tween(800))
        
        delay(2500)
        
        // Auto-advance
        onIntroFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        // Our glowing stars background twinkling underneath
        TwinklingStarsBackground(Modifier.fillMaxSize())

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(24.dp)
                .alpha(transitionAlpha.value)
                .scale(transitionScale.value)
        ) {
            if (step == 0) {
                // Símbolo de Google y Power BI Google
                GoogleColorLogo()
                
                Spacer(modifier = Modifier.height(28.dp))
                
                Text(
                    text = "POWER BI GOOGLE",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center,
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(2.dp)
                            .background(PowerBiYellow)
                    )
                    Text(
                        text = "  SPACE INTELLIGENCE  ",
                        color = MutedGrey,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 3.sp
                    )
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(2.dp)
                            .background(GoogleBlue)
                    )
                }
            } else {
                // Siguiente paso: Imaginado por Oliver + Oliver's Custom Badge Logo!
                Text(
                    text = "imaginado por",
                    color = MutedGrey,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 1.sp,
                    textAlign = TextAlign.Center
                )
                
                Text(
                    text = "Oliver",
                    color = StarWhite,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Light,
                    letterSpacing = 1.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Real virtual replica of Oliver's Badge from the prompt image!
                OliverBadgeEmblem(modifier = Modifier.size(240.dp))

                Spacer(modifier = Modifier.height(48.dp))
                
                Button(
                    onClick = onIntroFinished,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.15f),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f)),
                    modifier = Modifier.height(45.dp)
                ) {
                    Text(text = "Entrar al Sistema", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun GoogleColorLogo(modifier: Modifier = Modifier) {
    // Custom beautiful vector rendition of Google G icon using shapes
    Box(
        modifier = modifier
            .size(72.dp)
            .background(Color.White.copy(alpha = 0.05f), CircleShape)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row {
                Box(modifier = Modifier.size(16.dp).background(GoogleRed, CircleShape))
                Spacer(modifier = Modifier.width(4.dp))
                Box(modifier = Modifier.size(16.dp).background(GoogleYellow, CircleShape))
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Box(modifier = Modifier.size(16.dp).background(GoogleBlue, CircleShape))
                Spacer(modifier = Modifier.width(4.dp))
                Box(modifier = Modifier.size(16.dp).background(GoogleGreen, CircleShape))
            }
        }
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

        // Bottom-right Blue Verified Verification Checkmark Badge (just like in prompt image!)
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
