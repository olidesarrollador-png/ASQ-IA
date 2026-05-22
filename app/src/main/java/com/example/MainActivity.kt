package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.view.AiAssistantWorkspace
import com.example.view.GoogleOliverIntro
import com.example.view.SlideWindowsDeck
import com.example.view.TwinklingStarsBackground

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                var isIntroFinished by rememberSaveable { mutableStateOf(false) }

                Crossfade(
                    targetState = isIntroFinished,
                    animationSpec = fadeDecaySpec(),
                    label = "IntroMainTransition"
                ) { finished ->
                    if (!finished) {
                        GoogleOliverIntro(onIntroFinished = {
                            isIntroFinished = true
                        })
                    } else {
                        MainWorkspaceScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun fadeDecaySpec() = tween<Float>(durationMillis = 600)

@Composable
fun MainWorkspaceScreen() {
    var isGoogleConnected by rememberSaveable { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black) // Deep pure black background
    ) {
        // Twinkling floating background
        TwinklingStarsBackground(modifier = Modifier.fillMaxSize())

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent, // Transparent to let deep black + stars shine through
            contentWindowInsets = WindowInsets.safeDrawing // Ensure edge-to-edge support with safe insets
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // --- 1. Top Navigation Bar & Brand Signature ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "POWER BI GOOGLE",
                                color = StarWhite,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.2.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Sello Certificado",
                                tint = Color(0xFF1D9BF0), // Blue check verified icon for Oliver's studio
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = "Por Oliver • Free & No Ads",
                            color = MutedGrey,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Small Circular Replica Info button
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.05f))
                            .clickable { showInfoDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Información",
                            tint = NeonCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // --- 2. Draggable/Swipeable Sliding Windows Deck ---
                // Slide Pager between: Display clock/Hour -> Weather Details -> Retro Dino Game!
                SlideWindowsDeck(
                    modifier = Modifier.wrapContentHeight()
                )

                // --- 3. Gemini Assistant Workspace Section ---
                // Contains Connection dashboard + Ask AI dialogue thread
                AiAssistantWorkspace(
                    modifier = Modifier.weight(1f),
                    isGoogleConnected = isGoogleConnected,
                    onGoogleConnectionChanged = { isGoogleConnected = it }
                )
            }
        }
    }

    // --- Info dialog regarding app credentials and gratis ---
    if (showInfoDialog) {
        AlertDialog(
            onDismissRequest = { showInfoDialog = false },
            title = {
                Text(
                    text = "Licencia FLX App Studio",
                    fontWeight = FontWeight.Bold,
                    color = StarWhite,
                    fontSize = 18.sp
                )
            },
            text = {
                Column {
                    Text(
                        text = "Esta aplicación ha sido imaginada y diseñada exclusivamente por Oliver.",
                        fontSize = 13.sp,
                        color = StarWhite.copy(alpha = 0.8f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "• 100% Gratis, sin anuncios molestos ni compras ocultas.\n• Conexión directa a Gemini 3.5 AI.\n• Integración simulada/real con Google Drive y Gmail para análisis inteligente de reportes Power BI.",
                        fontSize = 12.sp,
                        color = MutedGrey
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showInfoDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = Color.Black)
                ) {
                    Text(text = "Entendido", fontWeight = FontWeight.Bold)
                }
            },
            containerColor = Color(0xFF0F0F12),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.border(BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)), RoundedCornerShape(16.dp))
        )
    }
}
