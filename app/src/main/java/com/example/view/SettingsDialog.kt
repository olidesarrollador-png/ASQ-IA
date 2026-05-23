package com.example.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppSettings
import com.example.ui.theme.*

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = AppSettings.translate("SETTINGS_TITLE"),
                    fontWeight = FontWeight.Black,
                    color = StarWhite,
                    fontSize = 18.sp,
                    letterSpacing = 1.sp
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MutedGrey,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // --- SECTION 1: LANGUAGE ---
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = AppSettings.translate("LANG_LABEL").uppercase(),
                        color = NeonCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LanguageSegmentButton(
                            label = "Español",
                            isSelected = AppSettings.language == "ES",
                            onClick = { AppSettings.language = "ES" },
                            modifier = Modifier.weight(1f)
                        )
                        LanguageSegmentButton(
                            label = "English",
                            isSelected = AppSettings.language == "EN",
                            onClick = { AppSettings.language = "EN" },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                HorizontalDivider(color = Color.White.copy(alpha = 0.08f))

                // --- SECTION 2: TIME CONFIGURATION ---
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = AppSettings.translate("TIME_LABEL").uppercase(),
                        color = NeonCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    
                    // Switch 24H
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = AppSettings.translate("TIME_FORMAT_24"),
                            color = StarWhite,
                            fontSize = 13.sp
                        )
                        Switch(
                            checked = AppSettings.is24HourFormat,
                            onCheckedChange = { AppSettings.is24HourFormat = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = NeonCyan,
                                checkedTrackColor = NeonCyan.copy(alpha = 0.3f),
                                uncheckedThumbColor = MutedGrey,
                                uncheckedTrackColor = Color.White.copy(alpha = 0.08f)
                            )
                        )
                    }

                    // Time Offset Control
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${AppSettings.translate("TIME_OFFSET")}: ${if (AppSettings.timeOffsetHours >= 0) "+" else ""}${AppSettings.timeOffsetHours}h",
                            color = StarWhite,
                            fontSize = 13.sp
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            OffsetButton(label = "-1h", onClick = { AppSettings.timeOffsetHours -= 1 })
                            OffsetButton(label = "+1h", onClick = { AppSettings.timeOffsetHours += 1 })
                            OffsetButton(label = "Reset", onClick = { AppSettings.timeOffsetHours = 0 })
                        }
                    }
                }

                HorizontalDivider(color = Color.White.copy(alpha = 0.08f))

                // --- SECTION: WALLPAPER BACKGROUND ---
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = AppSettings.translate("BACKGROUND_LABEL").uppercase(),
                        color = NeonCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LanguageSegmentButton(
                            label = if (AppSettings.language == "ES") "Estelar" else "Default Space",
                            isSelected = AppSettings.selectedBackground == "STARS",
                            onClick = { AppSettings.selectedBackground = "STARS" },
                            modifier = Modifier.weight(1f)
                        )
                        LanguageSegmentButton(
                            label = if (AppSettings.language == "ES") "Ola de Puntos" else "Particle Wave",
                            isSelected = AppSettings.selectedBackground == "WAVE",
                            onClick = { AppSettings.selectedBackground = "WAVE" },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                HorizontalDivider(color = Color.White.copy(alpha = 0.08f))

                // --- SECTION 3: WIDGET VISIBILITY ---
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = AppSettings.translate("WIDGETS_LABEL").uppercase(),
                        color = NeonCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        WidgetVisibilityToggle(
                            label = AppSettings.translate("WIDGET_CLOCK"),
                            isVisible = AppSettings.showLargeTime,
                            onToggle = { AppSettings.showLargeTime = it }
                        )
                        WidgetVisibilityToggle(
                            label = AppSettings.translate("WIDGET_WEATHER"),
                            isVisible = AppSettings.showWeather,
                            onToggle = { AppSettings.showWeather = it }
                        )
                        WidgetVisibilityToggle(
                            label = AppSettings.translate("WIDGET_SPOTIFY"),
                            isVisible = AppSettings.showSpotify,
                            onToggle = { AppSettings.showSpotify = it }
                        )
                    }
                }

                HorizontalDivider(color = Color.White.copy(alpha = 0.08f))

                // --- SECTION 4: CONNECTIONS INTEG ---
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = AppSettings.translate("CONNECTIONS_LABEL").uppercase(),
                        color = NeonCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        ConnectionRow(
                            label = AppSettings.translate("CONN_WHATSAPP"),
                            isConnected = AppSettings.isWhatsAppConnected,
                            onToggle = { AppSettings.isWhatsAppConnected = it },
                            color = Color(0xFF25D366) // WhatsApp Green
                        )
                        ConnectionRow(
                            label = AppSettings.translate("CONN_GMAIL"),
                            isConnected = AppSettings.isGmailConnected,
                            onToggle = { AppSettings.isGmailConnected = it },
                            color = GoogleRed
                        )
                        ConnectionRow(
                            label = AppSettings.translate("CONN_DRIVE"),
                            isConnected = AppSettings.isGoogleDriveConnected,
                            onToggle = { AppSettings.isGoogleDriveConnected = it },
                            color = GoogleGreen
                        )
                        ConnectionRow(
                            label = AppSettings.translate("CONN_CALLS"),
                            isConnected = AppSettings.isCallsConnected,
                            onToggle = { AppSettings.isCallsConnected = it },
                            color = NeonCyan
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonCyan,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = AppSettings.translate("UNDERSTOOD"),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        },
        containerColor = Color(0xFF0F0F12),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .border(
                BorderStroke(1.2.dp, Color.White.copy(alpha = 0.15f)),
                RoundedCornerShape(24.dp)
            )
            .widthIn(max = 400.dp)
    )
}

@Composable
fun LanguageSegmentButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(34.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) NeonCyan.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.03f))
            .border(
                BorderStroke(
                    1.dp,
                    if (isSelected) NeonCyan.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.08f)
                ),
                RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isSelected) NeonCyan else MutedGrey,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun OffsetButton(
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color.White.copy(alpha = 0.04f))
            .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)), RoundedCornerShape(6.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = StarWhite,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun WidgetVisibilityToggle(
    label: String,
    isVisible: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = StarWhite,
            fontSize = 12.sp
        )
        Checkbox(
            checked = isVisible,
            onCheckedChange = onToggle,
            colors = CheckboxDefaults.colors(
                checkedColor = NeonCyan,
                checkmarkColor = Color.Black,
                uncheckedColor = MutedGrey
            )
        )
    }
}

@Composable
fun ConnectionRow(
    label: String,
    isConnected: Boolean,
    onToggle: (Boolean) -> Unit,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White.copy(alpha = 0.03f))
            .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)), RoundedCornerShape(10.dp))
            .clickable { onToggle(!isConnected) }
            .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(if (isConnected) color else Color.White.copy(alpha = 0.15f))
            )
            Text(
                text = label,
                color = StarWhite,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
        
        Text(
            text = if (isConnected) {
                AppSettings.translate("CONNECTED_STATUS").uppercase()
            } else {
                AppSettings.translate("DISCONNECTED_STATUS").uppercase()
            },
            color = if (isConnected) color else MutedGrey,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
