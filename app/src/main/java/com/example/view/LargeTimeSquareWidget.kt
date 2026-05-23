package com.example.view

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun LargeTimeSquareWidget(modifier: Modifier = Modifier) {
    var timeString by remember { mutableStateOf("") }
    var secondsString by remember { mutableStateOf("") }
    var dateString by remember { mutableStateOf("") }

    val is24Hour = AppSettings.is24HourFormat
    val offset = AppSettings.timeOffsetHours
    val language = AppSettings.language

    LaunchedEffect(is24Hour, offset, language) {
        while (true) {
            val cal = Calendar.getInstance()
            cal.add(Calendar.HOUR_OF_DAY, offset)
            val now = cal.time
            
            val locale = Locale.forLanguageTag(language.lowercase(Locale.getDefault()))
            val timePat = if (is24Hour) "HH:mm" else "hh:mm"
            val amPmSuffix = if (!is24Hour) {
                SimpleDateFormat(" a", locale).format(now)
            } else ""

            timeString = SimpleDateFormat(timePat, locale).format(now) + amPmSuffix
            secondsString = SimpleDateFormat(":ss", locale).format(now)
            dateString = SimpleDateFormat("EEE, d MMM", locale).format(now)
            delay(1000)
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f), // Keep it perfectly square!
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(32.dp), // Extra rounded corners as requested
        border = BorderStroke(1.2.dp, Color.White.copy(alpha = 0.15f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.08f),
                            Color.White.copy(alpha = 0.02f)
                        )
                    )
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // Top right indicator glowing badge
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(NeonCyan.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = if (language == "ES") "EN VIVO" else "LIVE",
                    color = NeonCyan,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Time
                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.padding(bottom = 2.dp)
                ) {
                    Text(
                        text = timeString,
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = secondsString,
                        color = NeonCyan,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier.padding(bottom = 3.dp)
                    )
                }

                // Date
                Text(
                    text = dateString.uppercase(Locale.getDefault()),
                    color = MutedGrey,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}
