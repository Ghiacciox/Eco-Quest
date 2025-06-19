package com.ingegneriasoftware.ecoquest.data.repositories

import android.content.Context
import android.net.Uri
import android.util.Base64
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiApi @Inject constructor() {
    // Chiave API per accedere al servizio Gemini (da nascondere in un file sicuro come un vault o un file di configurazione).
    private val apiKey = "" // da nascondere
    // URL base per le richieste all'API di Gemini.
    private val baseUrl = ""
    // Configurazione del client HTTP con timeout per connessione, lettura e scrittura.
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // Metodo principale per analizzare un'immagine utilizzando l'API Gemini.
    suspend fun geminiAnalizer(context: Context, imageUri: Uri, prompt: String): String {
        return withContext(Dispatchers.IO) { // Esegue il lavoro in un dispatcher IO per operazioni di rete.
            try {
                // Ottiene il file dall'URI fornito.
                val file = getFileFromUri(context, imageUri) ?:
                return@withContext "Could not access image file"

                // Legge e codifica l'immagine in Base64.
                val bytes = file.readBytes()
                val base64Image = Base64.encodeToString(bytes, Base64.NO_WRAP)
                val mimeType = getMimeType(file) ?: "image/jpeg"

                // Crea il corpo della richiesta in formato JSON.
                val requestBody = """
                    {
                        "contents": [{
                            "parts": [
                                {"text": "$prompt"},
                                {
                                    "inline_data": {
                                        "mime_type": "$mimeType",
                                        "data": "$base64Image"
                                    }
                                }
                            ]
                        }]
                    }
                """.trimIndent().toRequestBody("application/json".toMediaType())

                // Crea e invia la richiesta HTTP POST.
                val request = Request.Builder()
                    .url("${baseUrl}gemini-2.0-flash:generateContent?key=$apiKey")
                    .post(requestBody)
                    .build()

                // Gestisce la risposta dell'API.
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        val errorBody = response.body?.string() ?: "No error details"
                        Log.e("GeminiAPI", "API Error: $errorBody")
                        return@withContext "API Error: ${response.code} - $errorBody"
                    }

                    // Analizza il corpo della risposta JSON.
                    response.body?.use { responseBody ->
                        val responseText = responseBody.string()
                        try {
                            val jsonResponse = JSONObject(responseText)
                            jsonResponse.getJSONArray("candidates")
                                .getJSONObject(0)
                                .getJSONObject("content")
                                .getJSONArray("parts")
                                .getJSONObject(0)
                                .getString("text")
                        } catch (e: JSONException) {
                            Log.e("GeminiAPI", "JSON parsing error", e)
                            "Error parsing response: ${e.message}"
                        }
                    } ?: "Empty response body"
                }
            } catch (e: Exception) {
                Log.e("GeminiAPI", "Error analyzing image", e)
                "Error analyzing image: ${e.message}"
            }
        }
    }

    // Metodo per ottenere un file da un URI.
    private fun getFileFromUri(context: Context, uri: Uri): File? {
        return when (uri.scheme) {
            "file" -> File(uri.path ?: return null) // Gestisce URI di tipo file.
            "content" -> { // Gestisce URI di tipo content.
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val tempFile = File.createTempFile("upload", ".tmp", context.cacheDir)
                    tempFile.outputStream().use { output ->
                        inputStream.copyTo(output)
                    }
                    tempFile
                }
            }
            else -> null // Restituisce null per schemi non supportati.
        }
    }

    // Metodo per ottenere il MIME type di un file in base all'estensione.
    private fun getMimeType(file: File): String? {
        return when (file.extension.lowercase()) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "gif" -> "image/gif"
            "webp" -> "image/webp"
            else -> null // Restituisce null per estensioni non supportate.
        }
    }
}