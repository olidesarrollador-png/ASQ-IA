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
        if (key.isEmpty() || key == "MY_GEMINI_API_KEY") {
            return@withContext "¡Hola! Soy tu asistente de Power BI Google.\n\nPara comunicarme con la API real de Gemini, configura tu clave de API `GEMINI_API_KEY` en el panel de Secrets de AI Studio.\n\n(Simulación activa): Recibí tu mensaje: \"$prompt\""
        }

        // Base system instructions
        val systemInstruction = """
            Eres "Power BI Google Space AI", un asistente de inteligencia artificial místico y futurista creado y diseñado por Oliver. No tienes publicidad y eres gratuito. 
            Te encuentras en un entorno elegante de color negro absoluto con estrellas y partículas flotantes.
            Tu creador Oliver implementó una consola licenciada (FLX App Studio) que se muestra en la intro.
            Si el usuario pregunta por Gmail o Google Drive: 
            ${if (isGoogleConnected) {
                "El usuario TIENE conectada su cuenta de Google. Puedes responder basándote y analizando los siguientes documentos simulados que Oliver tiene en su Gmail y Drive:\n" +
                WorkspaceData.items.joinToString("\n") { 
                    "- Tipo: ${it.type}, Título: ${it.title}, Origen: ${it.senderOrPath}, Contenido: ${it.snippet}"
                }
            } else {
                "El usuario NO tiene conectada su cuenta de Google en este momento. Sugiérele de forma amigable conectar su Gmail y Google Drive usando el panel de conexiones en la parte inferior para habilitar búsquedas inteligentes en su Workspace."
            }}
            Responde de manera concisa, educada y en español, usando terminología científica/tecnológica refinada (ej. 'órbita de datos', 'canal estelar', 'módulo analítico', etc.) y muestra mucho respeto hacia Oliver y su app "Power BI Google".
        """.trimIndent()

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
