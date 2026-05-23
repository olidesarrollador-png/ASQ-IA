package com.example.view

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppSettings
import com.example.model.GeminiManager
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun AiScreen(
    onSendNotification: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var promptInput by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Chat History
    val chatHistory = remember {
        mutableStateListOf<Pair<String, Boolean>>(
            Pair(AppSettings.translate("AI_WELCOME_MSG"), false)
        )
    }

    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()

    // Infinite rotation for the Google-colored rotating outline!
    val infiniteTransition = rememberInfiniteTransition(label = "GoogleBorderSpin")
    val borderRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Angle"
    )

    // Animated sweeping color gradient brush
    val googleRgbBrush = Brush.sweepGradient(
        colors = listOf(
            GoogleBlue,
            GoogleRed,
            GoogleYellow,
            GoogleGreen,
            Color(0xFF8B5CF6), // Purple
            GoogleBlue
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // --- Header Section with Spinning Google Contour Around "Ask AI" Icon ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rotating Google Colors border panel around the app logo
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .rotate(borderRotation) // Smooth infinite spin
                    .border(BorderStroke(3.dp, googleRgbBrush), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // Outer circle rotating carries the inner sparkle static so it feels high-tech
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .rotate(-borderRotation) // Keep sparkle vertical while border spins
                        .background(Color(0xFF0C0A1C), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("♊", fontSize = 20.sp, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Ask AI Hub",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFFFB000).copy(alpha = 0.15f))
                            .border(BorderStroke(0.6.dp, Color(0xFFFFB000)), RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("GEMINI ACTIVE", fontSize = 8.sp, color = Color(0xFFFFB000), fontWeight = FontWeight.Bold)
                    }
                }
                Text(
                    text = "Asistente inteligente con contorno Google RGB.",
                    color = MutedGrey,
                    fontSize = 11.sp
                )
            }
        }

        // Chat Bubble Thread list
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White.copy(alpha = 0.02f))
                .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)), RoundedCornerShape(16.dp))
        ) {
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(chatHistory) { message ->
                    val isUser = message.second
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 16.dp,
                                        topEnd = 16.dp,
                                        bottomStart = if (isUser) 16.dp else 2.dp,
                                        bottomEnd = if (isUser) 2.dp else 16.dp
                                    )
                                )
                                .background(
                                    if (isUser) Color(0xFF8B5CF6).copy(alpha = 0.15f)
                                    else Color.White.copy(alpha = 0.04f)
                                )
                                .border(
                                    BorderStroke(
                                        1.dp,
                                        if (isUser) Color(0xFFC4B5FD).copy(alpha = 0.25f)
                                        else Color.White.copy(alpha = 0.08f)
                                    ),
                                    RoundedCornerShape(
                                        topStart = 16.dp,
                                        topEnd = 16.dp,
                                        bottomStart = if (isUser) 16.dp else 2.dp,
                                        bottomEnd = if (isUser) 2.dp else 16.dp
                                    )
                                )
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = if (isUser) "Olíver • Usuario" else "Ask AI • Sistema",
                                    color = if (isUser) Color(0xFFC4B5FD) else NeonCyan,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Text(
                                    text = message.first,
                                    color = StarWhite,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }

                if (isLoading) {
                    item {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                            Card(
                                modifier = Modifier.padding(4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.04f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(14.dp), color = NeonCyan, strokeWidth = 2.dp)
                                    Text("Procesando datos estelares...", color = MutedGrey, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Suggested prompts
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("Comando 'ahora'", "Resumen Gmail", "Planificar día").forEach { suggestion ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.04f))
                        .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.08f)), RoundedCornerShape(10.dp))
                        .clickable {
                            val triggerText = when (suggestion) {
                                "Comando 'ahora'" -> "ahora"
                                "Resumen Gmail" -> "¿Puedes analizar mis últimos correos de Gmail?"
                                else -> "Preguntas sugeridas: ¿Cómo planificar mi día de hoy?"
                            }
                            promptInput = triggerText
                            onSendNotification("Sugerencia cargada.")
                        }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(text = suggestion, color = MutedGrey, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Prompt input and submit
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = promptInput,
                onValueChange = { promptInput = it },
                placeholder = { Text("Escribe un mensaje de datos...", fontSize = 13.sp) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White.copy(alpha = 0.04f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.02f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .weight(1f)
                    .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.06f)), RoundedCornerShape(24.dp)),
                trailingIcon = {
                    if (chatHistory.size > 1) {
                        IconButton(onClick = {
                            chatHistory.clear()
                            chatHistory.add(Pair(AppSettings.translate("AI_WELCOME_MSG"), false))
                        }) {
                            Icon(Icons.Default.Refresh, "Clear chat", tint = MutedGrey)
                        }
                    }
                }
            )

            // Submit Button
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF8B5CF6))
                    .clickable {
                        val trimmed = promptInput.trim()
                        if (trimmed.isNotEmpty() && !isLoading) {
                            coroutineScope.launch {
                                chatHistory.add(Pair(trimmed, true))
                                promptInput = ""
                                isLoading = true
                                scrollState.animateScrollToItem(chatHistory.size - 1)

                                try {
                                    val aiRes = GeminiManager.generateAiResponse(
                                        trimmed,
                                        isGoogleConnected = true,
                                        chatHistory = chatHistory.drop(1).map { it.first to it.second }
                                    )
                                    chatHistory.add(Pair(aiRes, false))
                                    scrollState.animateScrollToItem(chatHistory.size - 1)
                                } catch (e: Exception) {
                                    chatHistory.add(Pair("Excepción: ${e.localizedMessage}", false))
                                } finally {
                                    isLoading = false
                                }
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Enviar",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
