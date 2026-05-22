package com.example.view

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SlideWindowsDeck(modifier: Modifier = Modifier) {
    // 0 = Clock, 1 = Weather, 2 = Mini-Game Dino
    var currentPage by rememberSaveable { mutableStateOf(0) }
    val totalPages = 3

    var dragOffset by remember { mutableStateOf(0f) }

    fun next() {
        if (currentPage < totalPages - 1) {
            currentPage++
        }
    }

    fun prev() {
        if (currentPage > 0) {
            currentPage--
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        if (dragOffset > 100f) {
                            // Dragged to right -> go to previous
                            prev()
                        } else if (dragOffset < -100f) {
                            // Dragged to left -> go to next
                            next()
                        }
                        dragOffset = 0f
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        dragOffset += dragAmount.x
                    }
                )
            },
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.2.dp, Color.White.copy(alpha = 0.15f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.08f),
                            Color.White.copy(alpha = 0.02f)
                        )
                    )
                )
        ) {
            // Display main page based on slide selections
            AnimatedContent(
                targetState = currentPage,
                transitionSpec = {
                    if (targetState > initialState) {
                        (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                            slideOutHorizontally { width -> -width } + fadeOut())
                    } else {
                        (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(
                            slideOutHorizontally { width -> width } + fadeOut())
                    }
                },
                label = "FloatingCardTransition"
            ) { targetPage ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    when (targetPage) {
                        0 -> ClockSlide()
                        1 -> WeatherSlide()
                        2 -> DinoMiniGameSlide()
                    }
                }
            }

            // Carousel dots indicators at top center
            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                for (i in 0 until totalPages) {
                    Box(
                        modifier = Modifier
                            .size(if (currentPage == i) 14.dp else 6.dp, 6.dp)
                            .clip(CircleShape)
                            .background(
                                if (currentPage == i) NeonCyan else Color.White.copy(alpha = 0.3f)
                            )
                    )
                }
            }

            // Left Navigation Button Indicator
            if (currentPage > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 4.dp)
                        .size(36.dp)
                        .background(Color.White.copy(alpha = 0.05f), CircleShape)
                        .clickable { prev() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "◀",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Right Navigation Button Indicator
            if (currentPage < totalPages - 1) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 4.dp)
                        .size(36.dp)
                        .background(Color.White.copy(alpha = 0.05f), CircleShape)
                        .clickable { next() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "▶",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ClockSlide() {
    var timeString by remember { mutableStateOf("") }
    var secondsString by remember { mutableStateOf("") }
    var dateString by remember { mutableStateOf("") }
    var alarmEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            val now = Calendar.getInstance().time
            timeString = SimpleDateFormat("HH:mm", Locale.getDefault()).format(now)
            secondsString = SimpleDateFormat(":ss", Locale.getDefault()).format(now)
            dateString = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault()).format(now)
            delay(1000)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "H O R A  Y  E S T A D O",
                color = MutedGrey,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Box(
                modifier = Modifier
                    .background(NeonCyan.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "UTC LIVE",
                    color = NeonCyan,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Beautiful Display Clock
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = timeString,
                    color = StarWhite,
                    fontSize = 54.sp,
                    fontWeight = FontWeight.Light,
                    letterSpacing = 1.sp
                )
                Text(
                    text = secondsString,
                    color = NeonCyan,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Thin,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Text(
                text = dateString.uppercase(Locale.getDefault()),
                color = MutedGrey,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.2.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Alarm Switch Simulation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.03f), RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Alarma diaria 07:00",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Switch(
                checked = alarmEnabled,
                onCheckedChange = { alarmEnabled = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = NeonCyan,
                    checkedTrackColor = NeonCyan.copy(alpha = 0.3f),
                    uncheckedThumbColor = MutedGrey,
                    uncheckedTrackColor = Color.White.copy(alpha = 0.1f)
                ),
                modifier = Modifier.height(24.dp)
            )
        }
    }
}

@Composable
fun WeatherSlide() {
    val cities = listOf("Bogotá", "Madrid", "Miami", "Londres")
    var selectedCityIdx by remember { mutableStateOf(0) }
    
    // Custom simulated weather states based on index
    val temperatures = listOf("18°C", "26°C", "31°C", "14°C")
    val weatherStat = listOf("Llovizna Mística", "Soleado Despejado", "Humedad Tropical", "Niebla Clásica")
    val weatherEmojis = listOf("🌧️", "☀️", "🌴", "🌫️")

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "T I E M P O  M E T E O R O L Ó G I C O",
                color = MutedGrey,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            
            // Allow clicking to cycle cities
            Text(
                text = "SIGUIENTE CIUDAD",
                color = NeonCyan,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    selectedCityIdx = (selectedCityIdx + 1) % cities.size
                }
            )
        }

        // Main Weather Display
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color.White.copy(alpha = 0.04f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = weatherEmojis[selectedCityIdx],
                    fontSize = 32.sp
                )
            }

            Column {
                Text(
                    text = cities[selectedCityIdx],
                    color = StarWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = weatherStat[selectedCityIdx],
                    color = MutedGrey,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = temperatures[selectedCityIdx],
                color = StarWhite,
                fontSize = 32.sp,
                fontWeight = FontWeight.Thin
            )
        }

        // Sub weather panel indicators
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            WeatherDetailItem("Viento", "14 km/h")
            WeatherDetailItem("Humedad", "78%")
            WeatherDetailItem("Radiación UV", "Baja")
        }
    }
}

@Composable
fun WeatherDetailItem(label: String, valStr: String) {
    Column {
        Text(text = label, color = MutedGrey, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        Text(text = valStr, color = StarWhite, fontSize = 11.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun DinoMiniGameSlide() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "M I N I J U E G O  F L O T A N T E",
                color = MutedGrey,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Box(
                modifier = Modifier
                    .background(PowerBiYellow.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "DINO retro",
                    color = PowerBiYellow,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        Spacer(modifier = Modifier.height(10.dp))

        // Dino embedded game view
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            DinoMiniGame()
        }
    }
}
