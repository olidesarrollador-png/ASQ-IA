package com.example.model

import androidx.compose.runtime.*

object AppSettings {
    // Current Language: "ES" (Español) or "EN" (English)
    var language by mutableStateOf("ES")

    // Active wallpaper background: "STARS" or "WAVE"
    var selectedBackground by mutableStateOf("STARS")

    // Clock configurations
    var is24HourFormat by mutableStateOf(true)
    var timeOffsetHours by mutableStateOf(0) // hours to add/subtract from current time

    // Active Widgets Toggles
    var showLargeTime by mutableStateOf(true)
    var showWeather by mutableStateOf(true)
    var showSpotify by mutableStateOf(true)
    var isSpotifyConfigured by mutableStateOf(false)

    // Connections Toggles
    var isWhatsAppConnected by mutableStateOf(false)
    var isGmailConnected by mutableStateOf(false)
    var isGoogleDriveConnected by mutableStateOf(false)
    var isCallsConnected by mutableStateOf(false)

    // Translations Dictionary for ultra premium localized feel
    private val dictionary = mapOf(
        "BACKGROUND_LABEL" to Pair("Fondo de Pantalla", "Wallpaper Background"),
        "BG_STARS" to Pair("Espacio Estelar", "Stellar Space"),
        "BG_FORTNITE" to Pair("Puntos Fortnite (Dos juntos)", "Fortnite Dots (Two together)"),
        "BG_GLOW" to Pair("Niebla Cuántica Neón", "Neon Quantum Fog"),
        "APP_TITLE" to Pair("ASK AI", "ASK AI"),
        "SUBTITLE" to Pair("Por Oliver • Premium Portal", "By Oliver • Premium Portal"),
        "WORKSPACE_ACTIVE" to Pair("Workspace Ask AI Activo", "Ask AI Workspace Active"),
        "GOOGLE_DISCONNECTED" to Pair("Desconectado", "Disconnected"),
        "DESVINCULAR" to Pair("Desvincular", "Disconnect"),
        "CONECTAR" to Pair("Conectar", "Connect"),
        "LICENSE_TITLE" to Pair("Sello de Verificación Ask AI", "Ask AI Verification Seal"),
        "UNDERSTOOD" to Pair("Entendido", "Understood"),
        "METEOROLOGÍA" to Pair("METEOROLOGÍA", "WEATHER"),
        "SIGUIENTE" to Pair("SIGUIENTE", "NEXT"),
        "SETTINGS_TITLE" to Pair("Ajustes del Sistema", "System Settings"),
        "LANG_LABEL" to Pair("Idioma", "Language"),
        "TIME_LABEL" to Pair("Reloj", "Clock"),
        "TIME_FORMAT_24" to Pair("Formato 24H", "24H Format"),
        "TIME_OFFSET" to Pair("Desplazamiento", "Offset"),
        "WIDGETS_LABEL" to Pair("Secciones del Sistema", "System Sections"),
        "WIDGET_CLOCK" to Pair("Reloj Grande", "Large Clock"),
        "WIDGET_WEATHER" to Pair("Clima / Meteorología", "Weather Info"),
        "WIDGET_SPOTIFY" to Pair("Canales de Audio", "Audio Channels"),
        "CONNECTIONS_LABEL" to Pair("Integraciones Inteligentes", "Smart Integrations"),
        "CONN_WHATSAPP" to Pair("WhatsApp Messenger", "WhatsApp Messenger"),
        "CONN_GMAIL" to Pair("Gmail Connect (Google)", "Gmail Connect (Google)"),
        "CONN_DRIVE" to Pair("Google Drive Workspace", "Google Drive Workspace"),
        "CONN_CALLS" to Pair("Llamadas de Voz", "Voice Calls"),
        "CONNECTED_STATUS" to Pair("Vinculado", "Linked"),
        "DISCONNECTED_STATUS" to Pair("Desconectado", "Disconnected"),
        "LICENSE_TEXT_1" to Pair("Esta aplicación premium ha sido imaginada, diseñada y programada de forma exclusiva por Oliver para ofrecer un portal libre de anuncios y altamente minimalista.", "This premium application was exclusively envisioned, designed and programmed by Oliver to provide an ad-free, deeply minimalist experience."),
        "LICENSE_TEXT_2" to Pair("• 100% Sin anuncios, gratis y rápido.\n• Entorno oscuro minimalista al estilo de iOS.\n• Filtro de partículas con opción de pares de puntos Fortnite.\n• Sello azul de verificación de desarrollador licenciado.", "• 100% Ad-free, free and fast.\n• Minimalist deep dark iOS-style interface.\n• Particle filters including drifting Fortnite dot-pairs.\n• Licensed developer verified blue seal."),
        "AI_WELCOME_MSG" to Pair(
            "¡Saludos! Soy tu asistente inteligente Ask AI, programado con total gratitud por Oliver. ¿Qué datos te gustaría analizar hoy?",
            "Greetings! I am Ask AI, your smart digital assistant, built with total gratitude by Oliver. What data would you like to analyze today?"
        )
    )

    fun translate(key: String): String {
        val pair = dictionary[key] ?: return key
        return if (language == "ES") pair.first else pair.second
    }
}
