package com.example.view

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun EnlacesScreen(
    onSendNotification: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("Todos") }
    val categories = listOf("Todos", "Productividad", "Entretenimiento", "Utilidades")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                text = "Integraciones",
                color = StarWhite,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Enlaza tus ecosistemas de datos externos",
                color = MutedGrey,
                fontSize = 12.sp
            )
        }

        // Horizontal filter chips matching iOS style exactly
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { category ->
                val isSelected = category == selectedCategory
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (isSelected) Color(0xFF8B5CF6).copy(alpha = 0.2f)
                            else Color.White.copy(alpha = 0.04f)
                        )
                        .border(
                            BorderStroke(
                                1.dp,
                                if (isSelected) Color(0xFFC4B5FD).copy(alpha = 0.6f)
                                else Color.White.copy(alpha = 0.08f)
                            ),
                            RoundedCornerShape(14.dp)
                        )
                        .clickable {
                            selectedCategory = category
                            onSendNotification("Filtro: $category seleccionado.")
                        }
                        .padding(horizontal = 16.dp, vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category,
                        color = if (isSelected) Color(0xFFE9D5FF) else StarWhite.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Screen 3 Mockup Glassmorphic purple lock card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.03f)),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(
                1.5.dp,
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF8B5CF6).copy(alpha = 0.2f),
                        Color(0xFF3B82F6).copy(alpha = 0.05f)
                    )
                )
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Giant purple locked shield aura icon
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color(0xFF8B5CF6).copy(alpha = 0.15f), CircleShape)
                        .border(BorderStroke(2.dp, Color(0xFFC4B5FD).copy(alpha = 0.4f)), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🔒", fontSize = 38.sp)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF8B5CF6).copy(alpha = 0.25f))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Próximamente",
                        color = Color(0xFFDDD6FE),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Más integraciones de camino",
                    color = StarWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Estamos trabajando arduamente en nuestro laboratorio de ingeniería informática para traerte más integraciones inteligentes. Muy pronto podrás sincronizar de forma nativa tus cuentas de servicios de productividad, notas y calendarios.",
                    color = MutedGrey,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}
