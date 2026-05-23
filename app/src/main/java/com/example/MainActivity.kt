package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppSettings
import com.example.ui.theme.*
import com.example.view.*
import kotlinx.coroutines.launch

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
fun fadeDecaySpec() = tween<Float>(durationMillis = 500)

@Composable
fun MainWorkspaceScreen() {
    var showInfoDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) } // 0: Inicio, 1: Apps, 2: Enlaces, 3: Herramientas (AI), 4: Ajustes

    val mainScrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val uriHandler = LocalUriHandler.current

    val notifyLambda: (String) -> Unit = { message ->
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black) // Premium Absolute Black Canvas
    ) {
        // Particles/Fogs background layers
        TwinklingStarsBackground(modifier = Modifier.fillMaxSize())

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets.safeDrawing,
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            bottomBar = {
                // --- CUSTOM PREMIUM iOS-STYLE FLOAT GLASSMOPHISM NAVIGATION BAR ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White.copy(alpha = 0.03f))
                        .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.08f)), RoundedCornerShape(24.dp))
                        .padding(vertical = 10.dp, horizontal = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        NavigationTabItem(
                            label = "Inicio",
                            emoji = "🏠",
                            isActive = selectedTab == 0,
                            onClick = { selectedTab = 0 }
                        )
                        NavigationTabItem(
                            label = "Apps",
                            emoji = "🎛️",
                            isActive = selectedTab == 1,
                            onClick = { selectedTab = 1 }
                        )
                        NavigationTabItem(
                            label = "Enlaces",
                            emoji = "🔗",
                            isActive = selectedTab == 2,
                            onClick = { selectedTab = 2 }
                        )
                        NavigationTabItem(
                            label = "Ask AI",
                            emoji = "♊",
                            isActive = selectedTab == 3,
                            onClick = { selectedTab = 3 }
                        )
                        NavigationTabItem(
                            label = "Ajustes",
                            emoji = "⚙️",
                            isActive = selectedTab == 4,
                            onClick = { selectedTab = 4 }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                // --- 1. Top Header Bar (With Discord community button) ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = AppSettings.translate("APP_TITLE"),
                                color = StarWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.5.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Sello Certificado",
                                tint = Color(0xFF1D9BF0), // Verification Blue
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = AppSettings.translate("SUBTITLE"),
                            color = MutedGrey,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Discord Community Button (Preserved exactly at top as requested)
                        Box(
                            modifier = Modifier
                                .height(36.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0xFF5865F2)) // Official Blurple
                                .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)), RoundedCornerShape(18.dp))
                                .clickable {
                                    uriHandler.openUri("https://discord.gg/GxMnw2h5")
                                }
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "👾",
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "Discord",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Info Button
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
                }

                Spacer(modifier = Modifier.height(10.dp))

                // --- 2. Dynamic Content Box based on Tab Selector ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    AnimatedContent(
                        targetState = selectedTab,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220))
                        },
                        label = "TabTransition"
                    ) { tab ->
                        when (tab) {
                            0 -> {
                                // Tab 0: Inicio Home Dashboard with side-by-side Clock & Weather + bento shortcuts
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .verticalScroll(mainScrollState),
                                    verticalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    // Side-by-side Clock and Weather widgets
                                    val showClock = AppSettings.showLargeTime
                                    val showWeather = AppSettings.showWeather

                                    if (showClock || showWeather) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                                        ) {
                                            if (showClock) {
                                                Box(modifier = Modifier.weight(1f)) {
                                                    LargeTimeSquareWidget(modifier = Modifier.fillMaxWidth())
                                                }
                                            }
                                            if (showWeather) {
                                                Box(modifier = Modifier.weight(1f)) {
                                                    WeatherWidget(modifier = Modifier.fillMaxWidth())
                                                }
                                            }
                                        }
                                    }

                                    // Quick access grid of 4 cards ("Accesos rápidos" matching Screen 2)
                                    Text(
                                        text = "Accesos Rápidos",
                                        color = StarWhite,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )

                                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                            QuickAccessCard(
                                                title = "Explorar Apps",
                                                subtitle = "Lista de servicios",
                                                emoji = "🎛️",
                                                onClick = { selectedTab = 1 },
                                                modifier = Modifier.weight(1f)
                                            )
                                            QuickAccessCard(
                                                title = "Mis Enlaces",
                                                subtitle = "Ecosistemas externos",
                                                emoji = "🔗",
                                                onClick = { selectedTab = 2 },
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                            QuickAccessCard(
                                                title = "IA Inteligente",
                                                subtitle = "Pregunta a Gemini",
                                                emoji = "♊",
                                                onClick = { selectedTab = 3 },
                                                modifier = Modifier.weight(1f)
                                            )
                                            QuickAccessCard(
                                                title = "Preferencias",
                                                subtitle = "Ajustar sistema",
                                                emoji = "⚙️",
                                                onClick = { selectedTab = 4 },
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                    }

                                    // "Próximos eventos" card
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.02f)),
                                        shape = RoundedCornerShape(16.dp),
                                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(14.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text("📅", fontSize = 18.sp)
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Column {
                                                Text("Próximos eventos", color = StarWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                                Text("Miércoles, 22 de mayo • No hay eventos hoy", color = MutedGrey, fontSize = 11.sp)
                                            }
                                        }
                                    }

                                    // YouTube Music Widget linked player
                                    YtMusicTracksWidget(
                                        onNavigateToApps = { selectedTab = 1 },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                            1 -> AppsScreen(onSendNotification = notifyLambda)
                            2 -> EnlacesScreen(onSendNotification = notifyLambda)
                            3 -> AiScreen(onSendNotification = notifyLambda)
                            4 -> AjustesScreen(onSendNotification = notifyLambda)
                        }
                    }
                }
            }
        }
    }

    // --- Info Dialog Verification Panel ---
    if (showInfoDialog) {
        AlertDialog(
            onDismissRequest = { showInfoDialog = false },
            title = {
                Text(
                    text = AppSettings.translate("LICENSE_TITLE"),
                    fontWeight = FontWeight.Bold,
                    color = StarWhite,
                    fontSize = 18.sp
                )
            },
            text = {
                Column {
                    Text(
                        text = AppSettings.translate("LICENSE_TEXT_1"),
                        fontSize = 13.sp,
                        color = StarWhite.copy(alpha = 0.8f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = AppSettings.translate("LICENSE_TEXT_2"),
                        fontSize = 12.sp,
                        color = MutedGrey
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showInfoDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6), contentColor = Color.White)
                ) {
                    Text(text = AppSettings.translate("UNDERSTOOD"), fontWeight = FontWeight.Bold)
                }
            },
            containerColor = Color(0xFF0F0F12),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.border(BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)), RoundedCornerShape(20.dp))
        )
    }
}

@Composable
fun NavigationTabItem(
    label: String,
    emoji: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = emoji,
            fontSize = 18.sp,
            modifier = Modifier.alpha(if (isActive) 1f else 0.5f)
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label,
            color = if (isActive) Color(0xFFC4B5FD) else MutedGrey,
            fontSize = 9.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
fun QuickAccessCard(
    title: String,
    subtitle: String,
    emoji: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.03f)),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(Color.White.copy(alpha = 0.05f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(emoji, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(title, color = StarWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(subtitle, color = MutedGrey, fontSize = 9.sp)
            }
        }
    }
}
