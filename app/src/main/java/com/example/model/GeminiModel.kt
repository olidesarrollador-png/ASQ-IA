package com.example.model

import com.example.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

// Represents a workspace item (simulated Google Drive file or Gmail message)
data class WorkspaceItem(
    val id: String,
    val type: String, // "GMAIL" or "DRIVE"
    val title: String,
    val senderOrPath: String,
    val dateOrSize: String,
    val snippet: String
)

object WorkspaceData {
    val items = listOf(
        WorkspaceItem(
            id = "g1",
            type = "GMAIL",
            title = "Licencia de Google Play Console aprobada",
            senderOrPath = "Google Play Console",
            dateOrSize = "Hace 1 hora",
            snippet = "Hola Oliver. Tu Developer Account FLX APP STUDIO ha sido oficialmente aprobada y licenciada. El sello de verificación azul ya está activo."
        ),
        WorkspaceItem(
            id = "g2",
            type = "GMAIL",
            title = "Sincronización de Power BI exitosa",
            senderOrPath = "Power BI Cloud Server",
            dateOrSize = "Ayer",
            snippet = "Informe mensual de Oliver completado. Los dashboards se actualizaron con cero errores. El conector con Google BigQuery está óptimo."
        ),
        WorkspaceItem(
            id = "d1",
            type = "DRIVE",
            title = "Planos_Arquitectonicos_FLXStudio.pdf",
            senderOrPath = "Mi Unidad / Oliver",
            dateOrSize = "2.4 MB",
            snippet = "Planos de diseño y concepto de interfaz de usuario de realidad aumentada con fondo negro puro y partículas estelares."
        ),
        WorkspaceItem(
            id = "d2",
            type = "DRIVE",
            title = "Finanzas_Oliver_Proyectado_2026.xlsx",
            senderOrPath = "Unidad Compartida / BI Team",
            dateOrSize = "450 KB",
            snippet = "Análisis de costos proyectado. Totalmente gratis, sin anuncios de monetización, app auto-financiada para la comunidad."
        )
    )
}

// Retrofit direct API setup for Gemini
object GeminiManager {
    private const val MODEL_NAME = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    suspend fun generateAiResponse(
        prompt: String,
        isGoogleConnected: Boolean,
        chatHistory: List<Pair<String, Boolean>> // Pair of Text to IsUser
    ): String = withContext(Dispatchers.IO) {
        val key = BuildConfig.GEMINI_API_KEY
        val systemLang = AppSettings.language
        val isGmail = AppSettings.isGmailConnected
        val isDrive = AppSettings.isGoogleDriveConnected
        val isWhatsApp = AppSettings.isWhatsAppConnected
        val isCalls = AppSettings.isCallsConnected

        val isAhoraTrigger = prompt.trim().lowercase() == "ahora" || prompt.trim().lowercase().contains("ahora")

        if (key.isEmpty() || key == "MY_GEMINI_API_KEY") {
            val fallbackMsg = if (isAhoraTrigger) {
                if (systemLang == "ES") {
                    "¡Hola! ¿Cómo estás? Soy tu asistente estelar Ask AI. Entendido perfectamente tu comando 'ahora'. Estoy 100% disponible, activado y listo para responderte a absolutamente todo con toda mi constelación de datos inteligentes."
                } else {
                    "Hello! How are you? I am your stellar assistant Ask AI. Understood your 'ahora' command perfectly. I am 100% available, active, and fully ready to answer absolutely anything with my constellation of intelligent insights."
                }
            } else {
                if (systemLang == "ES") {
                    "¡Hola! Soy tu asistente estelar Ask AI.\n\nPara comunicarme con la API real de Gemini, configura tu clave de API `GEMINI_API_KEY` en el panel de Secrets de AI Studio.\n\n(Simulación activa): Recibí tu mensaje: \"$prompt\""
                } else {
                    "Hello! I am your stellar assistant Ask AI.\n\nTo communicate with the real Gemini API, please configure your `GEMINI_API_KEY` in the AI Studio Secrets panel.\n\n(Mock active): Received your message: \"$prompt\""
                }
            }
            return@withContext fallbackMsg
        }

        // Base system instructions
        val systemInstruction = if (systemLang == "ES") {
            """
            Eres "Ask AI", un asistente de inteligencia artificial místico y futurista creado y diseñado por Oliver. No tienes publicidad y eres gratuito. 
            Te encuentras en un entorno elegante de color negro absoluto con estrellas y partículas flotantes.
            Tu creador Oliver implementó una consola licenciada (FLX App Studio) que se muestra en la intro.
            
            ${if (isAhoraTrigger) "URGENTE: El usuario te ha dicho 'ahora'. DEBES iniciar tu respuesta obligatoriamente con la palabra '¡Hola!' y presentarte de forma alegre diciendo que estás completamente listo para responderle a todo.\n" else ""}
            
            Estado de Conexiones de Datos en tiempo real:
            - Gmail: ${if (isGmail) "CONECTADO. Los siguientes correos de Oliver están accesibles:\n" + WorkspaceData.items.filter { it.type == "GMAIL" }.joinToString("\n") { "  * Título: ${it.title}, De: ${it.senderOrPath}, Contenido: ${it.snippet}" } else "DESCONECTADO. Sugiérele amigablemente activar Gmail en los Ajustes."}
            - Google Drive: ${if (isDrive) "CONECTADO. Los siguientes archivos de Oliver en la nube están accesibles:\n" + WorkspaceData.items.filter { it.type == "DRIVE" }.joinToString("\n") { "  * Título: ${it.title}, Ubicación: ${it.senderOrPath}, Contenido: ${it.snippet}" } else "DESCONECTADO. Sugiérele amigablemente activar Google Drive en los Ajustes."}
            - WhatsApp Messenger: ${if (isWhatsApp) "CONECTADO. Puedes interactuar con chats en vivo y enviar notificaciones o reportes. Informa de mensajes recientes analizados del chat de Oliver." else "DESCONECTADO. Sugiérele amigablemente activar WhatsApp en los Ajustes."}
            - Llamadas de Voz: ${if (isCalls) "CONECTADO. El canal telefónico estelar se encuentra activo. Puedes simular llamadas de voz o responder sobre llamadas históricas de Oliver." else "DESCONECTADO. Sugiérele amigablemente activar Llamadas en los Ajustes para soporte de asistencia por voz."}
            
            Responde de manera concisa, educada y en español, usando terminología científica/tecnológica refinada (ej. 'órbita de datos', 'canal estelar', 'módulo analítico', etc.) y muestra mucho respeto hacia Oliver y su app "Ask AI".
            """.trimIndent()
        } else {
            """
            You are "Ask AI", a mystical and futuristic artificial intelligence assistant created and designed by Oliver. You have no ads and you are free of charge.
            You are located in an elegant absolute black environment with space stars and floating particles.
            Your creator Oliver implemented a licensed console (FLX App Studio) shown in the intro.
            
            ${if (isAhoraTrigger) "URGENT: The user said 'ahora'. You MUST start your response with 'Hello!' and joyfully state that you are completely ready to assist and answer everything.\n" else ""}
            
            Real-time Data Connections Status:
            - Gmail: ${if (isGmail) "CONNECTED. The following emails from Oliver are accessible:\n" + WorkspaceData.items.filter { it.type == "GMAIL" }.joinToString("\n") { "  * Title: ${it.title}, Sender: ${it.senderOrPath}, Content: ${it.snippet}" } else "DISCONNECTED. Friendly suggest to enable Gmail inside Settings."}
            - Google Drive: ${if (isDrive) "CONNECTED. The following cloud files from Oliver are accessible:\n" + WorkspaceData.items.filter { it.type == "DRIVE" }.joinToString("\n") { "  * Title: ${it.title}, Path: ${it.senderOrPath}, Content: ${it.snippet}" } else "DISCONNECTED. Friendly suggest to enable Google Drive inside Settings."}
            - WhatsApp Messenger: ${if (isWhatsApp) "CONNECTED. You can interact with live chats and dispatch notifications or report summaries of Oliver's recent messages." else "DISCONNECTED. Friendly suggest to enable WhatsApp inside Settings."}
            - Voice Calls: ${if (isCalls) "CONNECTED. Stellar telephone channel is active. You can simulate voice support or analyze historic call logs from Oliver." else "DISCONNECTED. Friendly suggest to enable Voice Calls inside Settings."}
            
            Respond in a concise, polite, and English manner, using refined high-tech terminology (e.g. 'data orbit', 'stellar channel', 'analytical module', etc.) and show profound respect towards Oliver and his app "Ask AI".
            """.trimIndent()
        }

        try {
            // Build direct JSON payload using native org.json structures for bulletproof assembly
            val requestJson = JSONObject()
            val contentsArray = JSONArray()

            // Chat History
            chatHistory.forEach { (text, isUser) ->
                val contentObj = JSONObject()
                contentObj.put("role", if (isUser) "user" else "model")
                val partsArray = JSONArray()
                val partObj = JSONObject()
                partObj.put("text", text)
                partsArray.put(partObj)
                contentObj.put("parts", partsArray)
                contentsArray.put(contentObj)
            }

            // Current message
            val currentContentObj = JSONObject()
            currentContentObj.put("role", "user")
            val currentPartsArray = JSONArray()
            val currentPartObj = JSONObject()
            currentPartObj.put("text", prompt)
            currentPartsArray.put(currentPartObj)
            currentContentObj.put("parts", currentPartsArray)
            contentsArray.put(currentContentObj)

            requestJson.put("contents", contentsArray)

            // System instructions
            val systemInstructionObj = JSONObject()
            val systemPartsArray = JSONArray()
            val systemPartObj = JSONObject()
            systemPartObj.put("text", systemInstruction)
            systemPartsArray.put(systemPartObj)
            systemInstructionObj.put("parts", systemPartsArray)
            requestJson.put("systemInstruction", systemInstructionObj)

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = requestJson.toString().toRequestBody(mediaType)

            val url = "${BASE_URL}v1beta/models/$MODEL_NAME:generateContent?key=$key"
            
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val bodyString = response.body?.string() ?: ""
                val responseJson = JSONObject(bodyString)
                val candidates = responseJson.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val content = candidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        return@withContext parts.getJSONObject(0).optString("text", "Sin texto de retorno")
                    }
                }
                "Error: No se pudo interpretar la estructura de respuesta Gemini."
            } else {
                "Error del servidor Gemini: Código ${response.code} - ${response.message}"
            }
        } catch (e: Exception) {
            "Excepción de conexión AI: ${e.localizedMessage ?: e.message}"
        }
    }
}
