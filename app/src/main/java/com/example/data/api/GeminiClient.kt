package com.example.data.api

import android.util.Log
import com.example.BuildConfig
import com.example.data.models.TarotCard
import com.example.data.models.UserProfile
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

object GeminiClient {
    private const val TAG = "GeminiClient"
    private const val MODEL_NAME = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent"

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    /**
     * Checks if a real, valid API key is present.
     */
    fun hasValidApiKey(): Boolean {
        val key = BuildConfig.GEMINI_API_KEY
        return key.isNotEmpty() && key != "MY_GEMINI_API_KEY" && !key.contains("PLACEHOLDER")
    }

    /**
     * General content generation via direct REST API.
     */
    suspend fun generateContent(prompt: String, systemInstruction: String? = null): String = withContext(Dispatchers.IO) {
        if (!hasValidApiKey()) {
            Log.w(TAG, "API Key is missing or placeholder. Falling back to intuitive local oracle.")
            return@withContext getLocalFallbackReading(prompt)
        }

        val jsonRequestAdapter = moshi.adapter(GeminiRequestDto::class.java)
        val jsonResponseAdapter = moshi.adapter(GeminiResponseDto::class.java)

        val requestParts = listOf(PartDto(text = prompt))
        val contents = listOf(ContentDto(parts = requestParts))
        
        val systemInstructionContent = systemInstruction?.let {
            ContentDto(parts = listOf(PartDto(text = it)))
        }

        val requestBodyDto = GeminiRequestDto(
            contents = contents,
            generationConfig = GenerationConfigDto(temperature = 0.9f),
            systemInstruction = systemInstructionContent
        )

        val requestJson = jsonRequestAdapter.toJson(requestBodyDto)
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = requestJson.toRequestBody(mediaType)

        val apiKey = BuildConfig.GEMINI_API_KEY
        val url = "$BASE_URL?key=$apiKey"

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        try {
            okHttpClient.newCall(request).execute().use { response ->
                val responseBodyStr = response.body?.string()
                if (!response.isSuccessful || responseBodyStr == null) {
                    Log.e(TAG, "Request failed: ${response.code} - ${response.message}")
                    return@withContext getLocalFallbackReading(prompt)
                }

                val apiResponse = jsonResponseAdapter.fromJson(responseBodyStr)
                val textResponse = apiResponse?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                if (textResponse != null) {
                    return@withContext textResponse
                } else {
                    Log.e(TAG, "Empty text in Gemini response. Raw: $responseBodyStr")
                    return@withContext getLocalFallbackReading(prompt)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception during Gemini call", e)
            return@withContext getLocalFallbackReading(prompt)
        }
    }

    /**
     * High-quality Tarot Reading generator that connects user profile details and chosen cards.
     */
    suspend fun generateTarotReading(
        profile: UserProfile,
        question: String,
        spreadType: String,
        cards: List<TarotCard>
    ): String {
        val userContext = """
            Usuario: ${profile.name}
            Signo Solar: ${profile.zodiacSign}
            Signo Ascendente: ${profile.ascendantSign}
            Signo Lunar: ${profile.moonSign}
            Elemento Regente del Alma: ${profile.elementFocus}
            Enfoque Espiritual de Vida: ${profile.spiritualGoal}
            Color de Aura Actual: ${profile.auraColor}
            Chakra Predominante Hoy: ${profile.activeChakra}
            Energía Vital: ${profile.energyLevelToday}%
        """.trimIndent()

        val cardsDetails = cards.joinToString(separator = "\n") { card ->
            val alignment = if (card.isUpright) "Al Derecho (Luz)" else "Invertida (Sombra)"
            val meaning = if (card.isUpright) card.meaningUpright else card.meaningReversed
            "- **${card.name}** ($alignment): $meaning. Elemento: ${card.element}. Sabiduría ancestral: ${card.advice}"
        }

        val systemInstruction = """
            Eres un oráculo ancestral, sabio, místico, compasivo y profundamente conectado con la espiritualidad universal. 
            Te expresas en un español fluido, poético, sanador y misterioso pero accesible. Use un tono cálido y de sabiduría mágica.
            Tu misión es interpretar las cartas del Tarot dándole un enfoque 100% personalizado al perfil del consultante, analizando la sincronicidad de su signo zodiacal, su elemento, sus chakras y su estado de ánimo energético actual.
            Estructura tu lectura con:
            1. Un saludo espiritual llamándolo por su nombre de nacimiento.
            2. Análisis cósmico de hoy: cómo conecta tu lectura con su signo astral (${profile.zodiacSign}) y su vibración.
            3. Interpretación profunda de cada carta y su posición en la tirada ($spreadType).
            4. Respuesta directa a su pregunta: "$question".
            5. Un consejo sanador estelar y ritual diario breve específico para el consultante.
        """.trimIndent()

        val prompt = """
            Realiza una lectura espiritual profunda e íntima para la tirada "$spreadType".
            
            PREGUNTA DEL CONSULTANTE:
            "$question"

            INFORMACIÓN DEL CONSULTANTE:
            $userContext

            CARTAS DE TAROT EXTRAÍDAS:
            $cardsDetails

            Por favor, genera una devolución mística y sanadora de alta vibración.
        """.trimIndent()

        return generateContent(prompt, systemInstruction)
    }

    /**
     * Local backup oracle in case Gemini is unavailable or API key is not yet set up.
     * Beautifully crafted rule-based generator that ensures a perfect experience!
     */
    private fun getLocalFallbackReading(prompt: String): String {
        // Simple analysis of keywords to direct fallback text beautifully
        val isLove = prompt.contains("amor", ignoreCase = true) || prompt.contains("relacion", ignoreCase = true) || prompt.contains("pareja", ignoreCase = true)
        val isMoney = prompt.contains("dinero", ignoreCase = true) || prompt.contains("trabajo", ignoreCase = true) || prompt.contains("proyecto", ignoreCase = true) || prompt.contains("abundancia", ignoreCase = true)
        val isHealth = prompt.contains("salud", ignoreCase = true) || prompt.contains("cuerpo", ignoreCase = true) || prompt.contains("bienestar", ignoreCase = true)

        val header = "✨ **Oráculo del Santuario Interior (Sabiduría Silenciosa)** ✨\n\n*Nota: Tu gema cósmica está operando en modo intuitivo de reserva local. Conecta tu API Key de Gemini en el panel de secretos para desbloquear la total personalización inteligente cósmica.*\n\n"

        return if (isLove) {
            header + """
                Querida alma andante, las corrientes del cosmos susurran que tu corazón está experimentando una hermosa fase de transmutación. Las energías polares a tu alrededor buscan un nuevo equilibrio.
                
                **Interpretación del Oráculo Sagrado:**
                Los astros indican que has estado entregando tu fuego vital a otros sin recargar tu río interno. No temas poner límites celestes. En el amor profundo, el primer templo a consagrar es el de tu propio yo.
                
                **Consejo Práctico y Místico:**
                Un ritual de limpieza suave con incienso de rosas o ámbar ayudará a liberar la tensión acumulada. Visualiza un capullo protector de luz dorada a tu alrededor. Permítete recibir el mismo amor incondicional que irradias.
            """.trimIndent()
        } else if (isMoney) {
            header + """
                Saludos al visionario cósmico. Las raíces de tu abundancia física están conectadas a tu anclaje celestial y a la fe en tus propias visiones sagradas.
                
                **Interpretación del Oráculo Sagrado:**
                La tirada proyecta que tus miedos a la escasez material están bloqueando el flujo natural cósmico. Ha llegado el momento de dar el salto de fe en tu proyecto actual. Posees el carisma natal y la determinación del elemento Tierra para manifestar valor tangible.
                
                **Consejo Práctico y Místico:**
                Coloca tres monedas doradas en forma de triángulo en tu altar de trabajo y visualiza el crecimiento próspero de tus ideas. Confía en la sincronicidad de los tiempos divinos.
            """.trimIndent()
        } else if (isHealth) {
            header + """
                Guerrero de luz, tu vehículo físico es un templo sagrado que refleja las corrientes más sutiles de tus emociones celestes y tus pensamientos acumulados.
                
                **Interpretación del Oráculo Sagrado:**
                Tu nivel de vitalidad indica fatiga áurica de los chakras inferiores. Has estado absorbiendo el estrés electromagnético y las mareas de tu entorno. Es urgente drenar esta pesadez tocando tierra húmeda con tus pies descalzos.
                
                **Consejo Práctico y Místico:**
                Acuéstate descalzo en meditación de 10 minutos imaginando que el chakra Raíz se conecta con el centro de cristal líquido verde esmeralda sanadora de Gaia. Bebe abundante agua pura consagrada bajo la luz polar.
            """.trimIndent()
        } else {
            header + """
                Bendita alma divina, tu yo interior te ha traído hoy al santuario en busca de una respuesta profunda a tu despertar existencial.
                
                **Interpretación del Oráculo Sagrado:**
                Las cartas extraídas te invitan a reconciliarte con la incertidumbre sagrada. El camino no se revela completo milagrosamente; se despliega paso a paso con cada respiración consciente. Tus ancestros vigilan con amor y alegría infinita tu camino de sanación.
                
                **Consejo Práctico y Místico:**
                Realiza el ejercicio de silencio sagrado hoy por 5 minutos. Enciende una vela de color violeta y decreta internamente: *"Suelto lo que ya pasó, confío plenamente en el misterio creador del hoy. Mi alma sana en paz entera."*
            """.trimIndent()
        }
    }
}

// --- Moshi Data Classes for Gemini Request Dto ----

@JsonClass(generateAdapter = true)
data class GeminiRequestDto(
    val contents: List<ContentDto>,
    val generationConfig: GenerationConfigDto? = null,
    val systemInstruction: ContentDto? = null
)

@JsonClass(generateAdapter = true)
data class ContentDto(
    val parts: List<PartDto>
)

@JsonClass(generateAdapter = true)
data class PartDto(
    val text: String
)

@JsonClass(generateAdapter = true)
data class GenerationConfigDto(
    val temperature: Float? = null,
    val responseMimeType: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiResponseDto(
    val candidates: List<CandidateDto>?
)

@JsonClass(generateAdapter = true)
data class CandidateDto(
    val content: ContentDto?
)
