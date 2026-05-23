package com.example.view

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppSettings
import com.example.model.WorkspaceData
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun AppsScreen(
    onSendNotification: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var activeSubView by remember { mutableStateOf("LIST") } // "LIST", "GMAIL_ACTIVE", "YTMUSIC_ACTIVE"
    var isYtMusicConnected by remember { mutableStateOf(false) }

    AnimatedContent(
        targetState = activeSubView,
        transitionSpec = {
            slideInHorizontally { it } + fadeIn() togetherWith slideOutHorizontally { -it } + fadeOut()
        },
        label = "AppsSubViewTransition"
    ) { view ->
        when (view) {
            "GMAIL_ACTIVE" -> GmailActiveWorkspace(
                onBack = { activeSubView = "LIST" },
                onSendNotification = onSendNotification
            )
            "YTMUSIC_ACTIVE" -> YtMusicActiveWorkspace(
                onBack = { activeSubView = "LIST" },
                onSendNotification = onSendNotification
            )
            else -> ServicesList(
                isGmailConnected = AppSettings.isGmailConnected,
                isYtMusicConnected = isYtMusicConnected,
                onConnectGmail = {
                    AppSettings.isGmailConnected = !AppSettings.isGmailConnected
                    if (AppSettings.isGmailConnected) {
                        onSendNotification("Gmail vinculado correctamente.")
                    }
                },
                onConnectYtMusic = {
                    isYtMusicConnected = !isYtMusicConnected
                    if (isYtMusicConnected) {
                        onSendNotification("YouTube Music vinculado correctamente.")
                    }
                },
                onEnterGmail = { activeSubView = "GMAIL_ACTIVE" },
                onEnterYtMusic = { activeSubView = "YTMUSIC_ACTIVE" }
            )
        }
    }
}

@Composable
fun ServicesList(
    isGmailConnected: Boolean,
    isYtMusicConnected: Boolean,
    onConnectGmail: () -> Unit,
    onConnectYtMusic: () -> Unit,
    onEnterGmail: () -> Unit,
    onEnterYtMusic: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Column {
            Text(
                text = "Enlaces",
                color = StarWhite,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Conecta tus servicios favoritos",
                color = MutedGrey,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            )
        }

        // --- 1. YouTube Music Integrator ---
        ServiceRowItem(
            id = "ytmusic",
            title = "YouTube Music",
            desc = "Conecta tu cuenta para disfrutar tu música y listas.",
            emoji = "🎵",
            iconColor = Color(0xFFFF0000),
            isConnected = isYtMusicConnected,
            onToggleConnect = onConnectYtMusic,
            onEnter = onEnterYtMusic,
            badgeText = "Vincular"
        )

        // --- 2. Gmail Connect ---
        ServiceRowItem(
            id = "gmail",
            title = "Gmail",
            desc = "Gestiona tus correos de forma inteligente.",
            emoji = "✉️",
            iconColor = Color(0xFFEA4335),
            isConnected = isGmailConnected,
            onToggleConnect = onConnectGmail,
            onEnter = onEnterGmail,
            badgeText = "Vincular"
        )

        // --- 3. Google Drive (Próximamente) ---
        LockedServiceRowItem(
            title = "Google Drive",
            desc = "Accede a tus archivos desde cualquier lugar.",
            emoji = "📁",
            iconColor = Color(0xFFFBBC05)
        )

        // --- 4. Google Calendar (Próximamente) ---
        LockedServiceRowItem(
            title = "Google Calendar",
            desc = "Organiza tu día y recibe alertas.",
            emoji = "📅",
            iconColor = Color(0xFF4285F4)
        )

        // --- 5. Google Keep (Próximamente) ---
        LockedServiceRowItem(
            title = "Google Keep",
            desc = "Guarda tus notas e ideas siempre contigo.",
            emoji = "💡",
            iconColor = Color(0xFFFBC02D)
        )
    }
}

@Composable
fun ServiceRowItem(
    id: String,
    title: String,
    desc: String,
    emoji: String,
    iconColor: Color,
    isConnected: Boolean,
    onToggleConnect: () -> Unit,
    onEnter: () -> Unit,
    badgeText: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { if (isConnected) onEnter() else onToggleConnect() },
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.03f)),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(iconColor.copy(alpha = 0.15f), CircleShape)
                    .border(BorderStroke(1.2.dp, iconColor.copy(alpha = 0.4f)), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (isConnected) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Conectado",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
                Text(
                    text = desc,
                    color = MutedGrey,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Connected Interactive Badge or "Vincular" indicator
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isConnected) Color(0xFF8B5CF6).copy(alpha = 0.2f)
                        else Color.White.copy(alpha = 0.05f)
                    )
                    .clickable { onToggleConnect() }
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isConnected) "Entrar" else badgeText,
                        color = if (isConnected) Color(0xFFC4B5FD) else Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun LockedServiceRowItem(
    title: String,
    desc: String,
    emoji: String,
    iconColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black.copy(alpha = 0.4f)),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.01f)),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.04f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(Color.White.copy(alpha = 0.02f), CircleShape)
                    .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 20.sp, modifier = Modifier.background(Color.Transparent))
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = StarWhite.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = desc,
                    color = MutedGrey.copy(alpha = 0.7f),
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Próximamente Purple badge (Strict screen mockup sync!)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF8B5CF6).copy(alpha = 0.12f))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    text = "Próximamente",
                    color = Color(0xFFA78BFA),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// --- ACTIVE REPLICAS OF SERVICES (INTERACTIVE FUN!) ---

@Composable
fun GmailActiveWorkspace(
    onBack: () -> Unit,
    onSendNotification: (String) -> Unit
) {
    var isNewMailOpened by remember { mutableStateOf(false) }
    var mailTo by remember { mutableStateOf("") }
    var mailSubject by remember { mutableStateOf("") }
    var mailBody by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Gmail Inbox",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Sincronizado vía Google APIs",
                    color = Color(0xFF34A853),
                    fontSize = 11.sp
                )
            }
            Text(
                text = "Volver",
                color = NeonCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable { onBack() }
                    .padding(8.dp)
            )
        }

        if (isNewMailOpened) {
            // Write email card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.04f)),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.2.dp, Color.White.copy(alpha = 0.12f))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("Redactar Nuevo Correo", color = StarWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)

                    TextField(
                        value = mailTo,
                        onValueChange = { mailTo = it },
                        placeholder = { Text("Destinatario (ej. oliver@host.com)") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Black,
                            unfocusedContainerColor = Color.Black.copy(alpha = 0.4f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    TextField(
                        value = mailSubject,
                        onValueChange = { mailSubject = it },
                        placeholder = { Text("Asunto") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Black,
                            unfocusedContainerColor = Color.Black.copy(alpha = 0.4f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    TextField(
                        value = mailBody,
                        onValueChange = { mailBody = it },
                        placeholder = { Text("Tu mensaje...") },
                        minLines = 3,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Black,
                            unfocusedContainerColor = Color.Black.copy(alpha = 0.4f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Cancelar",
                            color = MutedGrey,
                            modifier = Modifier
                                .clickable { isNewMailOpened = false }
                                .padding(12.dp)
                        )
                        Button(
                            onClick = {
                                isNewMailOpened = false
                                onSendNotification("Correo enviado al servidor de colas.")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA4335))
                        ) {
                            Text("Enviar Correo")
                        }
                    }
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { isNewMailOpened = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0x221D9BF0)),
                    border = BorderStroke(1.dp, Color(0xFF1D9BF0).copy(alpha = 0.3f))
                ) {
                    Text("✏️ Redactar", color = Color(0xFF1D9BF0), fontSize = 12.sp)
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                WorkspaceData.items.filter { it.type == "GMAIL" }.forEach { mail ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.03f)),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(mail.senderOrPath, color = ElectricBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text(mail.dateOrSize, color = MutedGrey, fontSize = 10.sp)
                            }
                            Text(mail.title, color = StarWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 4.dp))
                            Text(mail.snippet, color = MutedGrey, fontSize = 11.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun YtMusicActiveWorkspace(
    onBack: () -> Unit,
    onSendNotification: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var playingSong by remember { mutableStateOf<String?>("Fortnite Lobby Theme (Cosmic Remix)") }
    var isPlaying by remember { mutableStateOf(false) }
    var songList by remember {
        mutableStateOf(
            listOf(
                "Fortnite Lobby Theme (Cosmic Remix)",
                "Space Stars & Ambient - Oliver Special",
                "Chrome RGB Synthwave Groove",
                "Galaxy Nebula Whispers"
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "YouTube Music Player",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Portal Activo por Oliver",
                    color = Color(0xFFFF0000),
                    fontSize = 11.sp
                )
            }
            Text(
                text = "Volver",
                color = NeonCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable { onBack() }
                    .padding(8.dp)
            )
        }

        // Search Bar Player
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar canciones, artistas...") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White.copy(alpha = 0.05f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.02f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                trailingIcon = {
                    IconButton(onClick = {
                        if (searchQuery.isNotEmpty()) {
                            songList = listOf("$searchQuery (Gemini Remix)") + songList
                            playingSong = "$searchQuery (Gemini Remix)"
                            onSendNotification("Buscando e inyectando pista.")
                        }
                    }) {
                        Icon(Icons.Default.Search, "Search", tint = Color.Red)
                    }
                },
                modifier = Modifier.weight(1f)
            )
        }

        // Active equalizer and visual card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.04f)),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color.Red.copy(alpha = 0.25f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Red glowing circle
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.Red.copy(alpha = 0.2f), CircleShape)
                        .border(BorderStroke(1.2.dp, Color.Red), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("💿", fontSize = 24.sp)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = playingSong ?: "Silencio",
                        color = StarWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = if (isPlaying) "Reproduciendo en streaming..." else "Pausado",
                        color = MutedGrey,
                        fontSize = 11.sp
                    )
                }

                Button(
                    onClick = { isPlaying = !isPlaying },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text(if (isPlaying) "⏸️" else "▶️", color = Color.White)
                }
            }
        }

        Text("Tu Biblioteca Sincronizada", color = StarWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            songList.forEachIndexed { index, song ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (playingSong == song) Color.Red.copy(alpha = 0.08f)
                            else Color.White.copy(alpha = 0.02f)
                        )
                        .clickable {
                            playingSong = song
                            isPlaying = true
                        }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("${index + 1}", color = MutedGrey, modifier = Modifier.width(28.dp), fontWeight = FontWeight.Bold)
                    Text(
                        text = song,
                        color = if (playingSong == song) Color.Red else StarWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(Icons.Default.PlayArrow, "Play", tint = MutedGrey, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}
