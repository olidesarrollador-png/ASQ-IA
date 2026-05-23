package com.example.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppSettings
import com.example.ui.theme.*

@Composable
fun AjustesScreen(
    onSendNotification: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                text = AppSettings.translate("SETTINGS_TITLE"),
                color = StarWhite,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Modifica y personaliza el hub premium Ask AI",
                color = MutedGrey,
                fontSize = 12.sp
            )
        }

        // --- SECTION 1: WALLPAPER BACKGROUNDS ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.03f)),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = AppSettings.translate("BACKGROUND_LABEL"),
                    color = Color(0xFFA78BFA),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                // 1. STARS Option
                BackgroundSelectionRow(
                    label = AppSettings.translate("BG_STARS"),
                    desc = "Estrellas estelares titilantes clásicas.",
                    isSelected = AppSettings.selectedBackground == "STARS",
                    onClick = {
                        AppSettings.selectedBackground = "STARS"
                        onSendNotification("Fondo de pantalla: Espacio Estelar activado.")
                    }
                )

                // 2. FORTNITE Option
                BackgroundSelectionRow(
                    label = AppSettings.translate("BG_FORTNITE"),
                    desc = "Parejas de puntos blancos (Fortnite anime estilo).",
                    isSelected = AppSettings.selectedBackground == "FORTNITE",
                    onClick = {
                        AppSettings.selectedBackground = "FORTNITE"
                        onSendNotification("Fondo de pantalla: Puntos Fortnite activado.")
                    }
                )

                // 3. GLOW Option
                BackgroundSelectionRow(
                    label = AppSettings.translate("BG_GLOW"),
                    desc = "Niebla cuántica neón púrpura y azul.",
                    isSelected = AppSettings.selectedBackground == "GLOW",
                    onClick = {
                        AppSettings.selectedBackground = "GLOW"
                        onSendNotification("Fondo de pantalla: Niebla de Neón activada.")
                    }
                )
            }
        }

        // --- SECTION 2: INTERFACE LANGUAGE ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.03f)),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = AppSettings.translate("LANG_LABEL"),
                    color = Color(0xFFA78BFA),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val isEs = AppSettings.language == "ES"
                    // ES Chip
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isEs) Color(0xFF8B5CF6).copy(alpha = 0.2f) else Color.White.copy(alpha = 0.04f))
                            .border(BorderStroke(1.dp, if (isEs) Color(0xFFC4B5FD) else Color.White.copy(alpha = 0.08f)), RoundedCornerShape(12.dp))
                            .clickable {
                                AppSettings.language = "ES"
                                onSendNotification("Idioma del sistema cambiado a Español.")
                            }
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Español (ES)", color = StarWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    // EN Chip
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (!isEs) Color(0xFF8B5CF6).copy(alpha = 0.2f) else Color.White.copy(alpha = 0.04f))
                            .border(BorderStroke(1.dp, if (!isEs) Color(0xFFC4B5FD) else Color.White.copy(alpha = 0.08f)), RoundedCornerShape(12.dp))
                            .clickable {
                                AppSettings.language = "EN"
                                onSendNotification("System language set to English.")
                            }
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("English (EN)", color = StarWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // --- SECTION 3: WIDGET CONFIG ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.03f)),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = AppSettings.translate("WIDGETS_LABEL"),
                    color = Color(0xFFA78BFA),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                WidgetSettingToggle(
                    label = AppSettings.translate("WIDGET_CLOCK"),
                    desc = "Visualiza el reloj digital ampliado de alta fidelidad.",
                    checked = AppSettings.showLargeTime,
                    onCheckedChange = { AppSettings.showLargeTime = it }
                )

                WidgetSettingToggle(
                    label = AppSettings.translate("WIDGET_WEATHER"),
                    desc = "Muestra el estado climatológico dinámico sincronizado.",
                    checked = AppSettings.showWeather,
                    onCheckedChange = { AppSettings.showWeather = it }
                )
            }
        }
    }
}

@Composable
fun BackgroundSelectionRow(
    label: String,
    desc: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) Color.White.copy(alpha = 0.04f) else Color.Transparent)
            .clickable { onClick() }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .border(BorderStroke(1.2.dp, if (isSelected) Color(0xFFC4B5FD) else MutedGrey), CircleShape)
                .background(if (isSelected) Color(0xFF8B5CF6) else Color.Transparent, CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(text = label, color = StarWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(text = desc, color = MutedGrey, fontSize = 10.sp)
        }
    }
}

@Composable
fun WidgetSettingToggle(
    label: String,
    desc: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, color = StarWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(text = desc, color = MutedGrey, fontSize = 10.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF8B5CF6),
                uncheckedThumbColor = MutedGrey,
                uncheckedTrackColor = Color.White.copy(alpha = 0.05f)
            )
        )
    }
}
