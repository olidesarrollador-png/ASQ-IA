package com.example.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.model.AppSettings

@Composable
fun WeatherWidget(modifier: Modifier = Modifier) {
    val cities = listOf("Bogotá", "Madrid", "Miami", "Londres")
    var selectedCityIdx by remember { mutableStateOf(0) }
    
    val temperatures = listOf("18°C", "26°C", "31°C", "14°C")
    val weatherStatES = listOf("Llovizna Mística", "Soleado Despejado", "Humedad Tropical", "Niebla Clásica")
    val weatherStatEN = listOf("Mystical Drizzle", "Sunny & Clear", "Tropical Humidity", "Classic Fog")
    val weatherEmojis = listOf("🌧️", "☀️", "🌴", "🌫️")

    val currentStatus = if (AppSettings.language == "ES") weatherStatES[selectedCityIdx] else weatherStatEN[selectedCityIdx]

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                selectedCityIdx = (selectedCityIdx + 1) % cities.size
            },
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.2.dp, Color.White.copy(alpha = 0.12f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.05f),
                            Color.White.copy(alpha = 0.01f)
                        )
                    )
                )
                .padding(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = AppSettings.translate("METEOROLOGÍA"),
                        color = MutedGrey,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = AppSettings.translate("SIGUIENTE"),
                        color = NeonCyan,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = weatherEmojis[selectedCityIdx],
                        fontSize = 28.sp
                    )
                    Column {
                        Text(
                            text = cities[selectedCityIdx],
                            color = StarWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = currentStatus,
                            color = MutedGrey,
                            fontSize = 10.sp
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = temperatures[selectedCityIdx],
                        color = StarWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Light
                    )
                }
            }
        }
    }
}
