package com.example.view

import androidx.compose.animation.*
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GeminiManager
import com.example.model.WorkspaceData
import com.example.model.WorkspaceItem
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun AiAssistantWorkspace(
    modifier: Modifier = Modifier,
    isGoogleConnected: Boolean,
    onGoogleConnectionChanged: (Boolean) -> Unit
) {
    var queryText by rememberSaveable { mutableStateOf("") }
    val chatMessages = remember {
        mutableStateListOf(
            Pair(
                "¡Saludos! Soy tu asistente estelar de *Power BI Google*, programado con total gratitud de forma 100% gratuita y sin anuncios.\n\nHe sido imaginado por **Oliver**. ¿Qué órbita de análisis de datos te gustaría explorar hoy? Si conectas tu cuenta arriba, analizaré de inmediato tus correos y Drive.",
                false
            )
        )
    }
    var isAiLoading by remember { mutableStateOf(false) }
    var showConnectDialog by remember { mutableStateOf(false) }
    
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    // Automatically scroll to latest message when dialogue grows
    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
    ) {
        // --- 1. Connection Header & Workspace Status ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "A S I S T E N T E  I N T E L I G E N T E",
                    color = MutedGrey,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.8.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(if (isGoogleConnected) GoogleGreen else GoogleRed)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isGoogleConnected) "Google Workspace Activo" else "Google Desconectado",
                        color = if (isGoogleConnected) StarWhite else MutedGrey,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Connection Action button
            Button(
                onClick = {
                    if (isGoogleConnected) {
                        onGoogleConnectionChanged(false)
                    } else {
                        showConnectDialog = true
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isGoogleConnected) GoogleRed.copy(alpha = 0.15f) else GoogleBlue.copy(alpha = 0.2f),
                    contentColor = if (isGoogleConnected) GoogleRed else GoogleBlue
                ),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(
                    1.dp, 
                    if (isGoogleConnected) GoogleRed.copy(alpha = 0.3f) else GoogleBlue.copy(alpha = 0.4f)
                ),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Icon(
                    imageVector = if (isGoogleConnected) Icons.Default.Check else Icons.Default.Add,
                    contentDescription = "Vincular",
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isGoogleConnected) "Desvincular" else "Conectar",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // --- 2. Google Workspace Mini-Slider if Connected ---
        AnimatedVisibility(
            visible = isGoogleConnected,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = "Bases de datos vinculadas de Oliver:",
                    color = NeonCyan,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    WorkspaceData.items.take(2).forEach { item ->
                        WorkspaceItemMiniCard(item = item)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // --- 3. Chat Messages stream ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.02f)),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
        ) {
            Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(chatMessages) { msg ->
                        MessageBubble(text = msg.first, isUser = msg.second)
                    }
                    if (isAiLoading) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(NeonCyan)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Sincronizando con Gemini AI...",
                                    color = MutedGrey,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- 4. Input Area ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = queryText,
                onValueChange = { queryText = it },
                placeholder = {
                    Text(
                        text = "Pregunta a Power BI Google...",
                        color = MutedGrey,
                        fontSize = 13.sp
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.04f)),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = StarWhite,
                    unfocusedTextColor = StarWhite,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = NeonCyan
                ),
                maxLines = 2,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (queryText.isNotBlank()) {
                            val msgToSend = queryText
                            queryText = ""
                            keyboardController?.hide()
                            
                            chatMessages.add(Pair(msgToSend, true))
                            isAiLoading = true
                            
                            coroutineScope.launch {
                                val reply = GeminiManager.generateAiResponse(
                                    prompt = msgToSend,
                                    isGoogleConnected = isGoogleConnected,
                                    chatHistory = chatMessages.toList()
                                )
                                isAiLoading = false
                                chatMessages.add(Pair(reply, false))
                            }
                        }
                    }
                )
            )

            // Send icon
            IconButton(
                onClick = {
                    if (queryText.isNotBlank()) {
                        val msgToSend = queryText
                        queryText = ""
                        keyboardController?.hide()
                        
                        chatMessages.add(Pair(msgToSend, true))
                        isAiLoading = true
                        
                        coroutineScope.launch {
                            val reply = GeminiManager.generateAiResponse(
                                prompt = msgToSend,
                                isGoogleConnected = isGoogleConnected,
                                chatHistory = chatMessages.toList()
                            )
                            isAiLoading = false
                            chatMessages.add(Pair(reply, false))
                        }
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .background(NeonCyan.copy(alpha = 0.15f), CircleShape)
                    .border(BorderStroke(1.dp, NeonCyan.copy(alpha = 0.3f)), CircleShape),
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Enviar",
                    tint = NeonCyan,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }

    // --- Google Workspace Link Dialog ---
    if (showConnectDialog) {
        AlertDialog(
            onDismissRequest = { showConnectDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Google Authenticator",
                        tint = GoogleBlue,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Vincular Google Account",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = StarWhite
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = "La aplicación Power BI Google de Oliver solicita permisos de análisis de solo lectura sobre:",
                        fontSize = 12.sp,
                        color = MutedGrey,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    BulletPermissionItem(text = "Gmail (Buscar emails de Power BI Server y de Play Console)")
                    BulletPermissionItem(text = "Google Drive (Acceder a Hojas de cálculo y Blueprints)")
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text(
                        text = "Esto es 100% gratuito, seguro y sin publicidad. Los datos se procesan localmente e implementan análisis inteligentes automáticos e inteligentes de Gemini.",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onGoogleConnectionChanged(true)
                        showConnectDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoogleBlue)
                ) {
                    Text(text = "Conceder Acceso", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConnectDialog = false }) {
                    Text(text = "Cancelar", color = MutedGrey)
                }
            },
            containerColor = Color(0xFF0F0F12),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.border(BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)), RoundedCornerShape(16.dp))
        )
    }
}

@Composable
fun BulletPermissionItem(text: String) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(text = "• ", color = NeonCyan, fontSize = 14.sp)
        Text(text = text, color = StarWhite, fontSize = 12.sp, fontWeight = FontWeight.Normal)
    }
}

@Composable
fun WorkspaceItemMiniCard(item: WorkspaceItem) {
    Card(
        modifier = Modifier.width(160.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.04f)),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            if (item.type == "GMAIL") GoogleRed.copy(alpha = 0.15f) else GoogleGreen.copy(alpha = 0.15f),
                            CircleShape
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = item.type,
                        color = if (item.type == "GMAIL") GoogleRed else GoogleGreen,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(text = item.dateOrSize, color = MutedGrey, fontSize = 8.sp)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.title,
                color = StarWhite,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Text(
                text = item.snippet,
                color = MutedGrey,
                fontSize = 8.sp,
                maxLines = 1
            )
        }
    }
}

@Composable
fun MessageBubble(text: String, isUser: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Card(
            shape = RoundedCornerShape(
                topStart = 14.dp,
                topEnd = 14.dp,
                bottomStart = if (isUser) 14.dp else 2.dp,
                bottomEnd = if (isUser) 2.dp else 14.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (isUser) NeonCyan.copy(alpha = 0.1f) else Color.White.copy(alpha = 0.05f)
            ),
            border = BorderStroke(
                1.dp, 
                if (isUser) NeonCyan.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.08f)
            )
        ) {
            Text(
                text = text,
                color = StarWhite,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }
    }
}
