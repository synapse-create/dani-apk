package com.example.data.repository

import com.example.data.db.*
import com.example.data.models.*
import kotlinx.coroutines.flow.Flow

class SpiritualRepository(
    private val userProfileDao: UserProfileDao,
    private val readingHistoryDao: ReadingHistoryDao,
    private val testResultDao: TestResultDao,
    private val activityProgressDao: ActivityProgressDao
) {
    val userProfile: Flow<UserProfile?> = userProfileDao.getUserProfile()
    val allReadings: Flow<List<ReadingHistory>> = readingHistoryDao.getAllReadings()
    val allTestResults: Flow<List<TestResult>> = testResultDao.getAllTestResults()
    val allActivityProgress: Flow<List<ActivityProgress>> = activityProgressDao.getAllProgress()

    suspend fun getUserProfileOneShot(): UserProfile? = userProfileDao.getUserProfileOneShot()

    suspend fun saveUserProfile(profile: UserProfile) {
        userProfileDao.insertOrUpdateProfile(profile)
    }

    suspend fun insertReading(reading: ReadingHistory) {
        readingHistoryDao.insertReading(reading)
    }

    suspend fun deleteReading(id: Int) {
        readingHistoryDao.deleteReadingById(id)
    }

    suspend fun clearReadingHistory() {
        readingHistoryDao.clearHistory()
    }

    suspend fun insertTestResult(testType: String, score: String, text: String) {
        testResultDao.insertTestResult(TestResult(testType = testType, scoreOrCategory = score, resultText = text))
    }

    suspend fun updateActivityProgress(id: String, title: String, type: String, completedIncrement: Boolean) {
        val current = activityProgressDao.getProgressForActivity(id)
        if (current != null) {
            val updated = current.copy(
                completedTimes = if (completedIncrement) current.completedTimes + 1 else current.completedTimes,
                lastCompleted = if (completedIncrement) System.currentTimeMillis() else current.lastCompleted
            )
            activityProgressDao.insertProgress(updated)
        } else {
            activityProgressDao.insertProgress(
                ActivityProgress(
                    id = id,
                    title = title,
                    type = type,
                    completedTimes = if (completedIncrement) 1 else 0,
                    lastCompleted = if (completedIncrement) System.currentTimeMillis() else 0
                )
            )
        }
    }

    suspend fun toggleFavorite(activityId: String, title: String, type: String) {
        val current = activityProgressDao.getProgressForActivity(activityId)
        if (current != null) {
            activityProgressDao.updateFavorite(activityId, !current.isFavorite)
        } else {
            activityProgressDao.insertProgress(
                ActivityProgress(
                    id = activityId,
                    title = title,
                    type = type,
                    isFavorite = true
                )
            )
        }
    }

    // Predefined rich content that we personalize dynamically for user depending on their details!
    fun getMeditationsForUser(profile: UserProfile): List<SpiritualActivityItem> {
        val isWater = profile.elementFocus == "Agua" || profile.zodiacSign in listOf("Cáncer", "Escorpio", "Piscis")
        val isEarth = profile.elementFocus == "Tierra" || profile.zodiacSign in listOf("Tauro", "Virgo", "Capricornio")
        val isFire = profile.elementFocus == "Fuego" || profile.zodiacSign in listOf("Aries", "Leo", "Sagitario")
        
        return listOf(
            SpiritualActivityItem(
                id = "med_chakra",
                title = "Meditación del Chakra ${profile.activeChakra}",
                description = "Enfoca tu respiración sagrada para balancear tu chakra ${profile.activeChakra}, permitiendo fluir la energía vital acumulada.",
                type = "Meditación",
                duration = "10 min",
                audioUrl = "chakra_align",
                iconRes = "spa",
                personalizationTip = "Recomendada especialmente hoy para balancear tu foco actual en el Chakra ${profile.activeChakra}."
            ),
            SpiritualActivityItem(
                id = "med_element",
                title = "Respiración del Elemento ${profile.elementFocus.ifEmpty { "Éter" }}",
                description = "Sintoniza tu respiración pránica con el poder del elemento ${profile.elementFocus}. Activa tus virtudes astrales naturales.",
                type = "Meditación",
                duration = "12 min",
                audioUrl = "element_med",
                iconRes = "air",
                personalizationTip = "Diseñada para tu elemento regente ancestral: ${profile.elementFocus}."
            ),
            SpiritualActivityItem(
                id = "med_inner_child",
                title = "Conexión con el Yo Interior y Niño Divino",
                description = "Un viaje guiado al templo interno donde yace tu inocencia creadora y tus talentos espirituales dormidos.",
                type = "Meditación",
                duration = "15 min",
                audioUrl = "inner_child",
                iconRes = "face",
                personalizationTip = "Ideal para expandir el autodescubrimiento para tu signo natal ${profile.zodiacSign}."
            ),
            SpiritualActivityItem(
                id = "med_auric_wash",
                title = "Baño de Luz y Limpieza Áurica",
                description = "Visualiza destellos de luz de frecuencia sagrada despejando bloqueos y fortaleciendo tu escudo de energía etérica.",
                type = "Meditación",
                duration = "8 min",
                audioUrl = "auric_wash",
                iconRes = "wb_sunny",
                personalizationTip = "Aconsejado para realzar tu vibración áurica natural (${profile.auraColor.ifEmpty { "Luminosa" }})."
            )
        )
    }

    fun getRitualsForUser(profile: UserProfile): List<SpiritualActivityItem> {
        return listOf(
            SpiritualActivityItem(
                id = "rit_altar",
                title = "Consagración de tu Altar Celestial",
                description = "Crea un espacio sagrado con una vela, un cristal consagrado, incienso nativo y un vaso de agua pura para invocar tus guías de luz.",
                type = "Ritual",
                duration = "20 min",
                audioUrl = "",
                iconRes = "brightness_high",
                personalizationTip = "Para consagrar tus intenciones de: ${profile.spiritualGoal.lowercase()}."
            ),
            SpiritualActivityItem(
                id = "rit_luna",
                title = "Ritual de Liberación Lunar",
                description = "Escribe bajo el cielo nocturno aquellos miedos, apegos u obstáculos que deseas transmutar. Quema el papel con fuego sagrado y esparce las cenizas.",
                type = "Ritual",
                duration = "15 min",
                audioUrl = "",
                iconRes = "nights_stay",
                personalizationTip = "Sintonizado con las emociones profundas de tu signo lunar (${profile.moonSign}) y tu sol en ${profile.zodiacSign}."
            ),
            SpiritualActivityItem(
                id = "rit_abundance",
                title = "Ritual de Sembra Celestial y Abundancia",
                description = "Atrae la prosperidad física y espiritual sembrando semillas de trigo o arroz en tierra con tres monedas sagradas cargadas bajo tu mantra sagrado.",
                type = "Ritual",
                duration = "25 min",
                audioUrl = "",
                iconRes = "grain",
                personalizationTip = "Fomenta la estabilidad y el crecimiento para canalizar tu propósito vital."
            )
        )
    }

    fun getTherapiesForUser(profile: UserProfile): List<SpiritualActivityItem> {
        val recommendedCrystal = when (profile.elementFocus) {
            "Fuego" -> "Esmeralda Roja o Jaspe Rojo (activa vitalidad física y transmutación de ira)"
            "Tierra" -> "Pirita o Turmalina Negra (anclaje de abundancia limpia y escudo del aura)"
            "Aire" -> "Fluorita o Amatista (claridad mental expandida y calma emocional)"
            "Agua" -> "Aguamarina o Selenita (fluidez psíquica, limpieza estelar y paz mística)"
            else -> "Cuarzo Blanco Maestro (activación integral)"
        }
        
        return listOf(
            SpiritualActivityItem(
                id = "ther_crystal",
                title = "Gemoterapia Sagrada de Conexión",
                description = "Coloca tu gema recomendada en tu pecho o frente mientras decretas autoaceptación. Limpia la piedra con sal marina seca y luz lunar.",
                type = "Terapia",
                duration = "10 min",
                audioUrl = "",
                iconRes = "diamond",
                personalizationTip = "Tu gema aliada sagrada es: $recommendedCrystal."
            ),
            SpiritualActivityItem(
                id = "ther_sound",
                title = "Terapia Vibracional de Frecuencia Solfeggio",
                description = "Escucha frecuencias curativas en 528Hz (reparación de ADN) o 432Hz (afinación con el latido planetario de Gaia).",
                type = "Terapia",
                duration = "15 min",
                audioUrl = "",
                iconRes = "music_note",
                personalizationTip = "Estimula la sanación celular de tu elemento regente: ${profile.elementFocus}."
            ),
            SpiritualActivityItem(
                id = "ther_reiki",
                title = "Autotratamiento Reiki de Transmutación",
                description = "Coloca tus manos con suavidad canalizando energía divina universal desde tu coronilla hacia tu corazón sintiendo la pureza.",
                type = "Terapia",
                duration = "12 min",
                audioUrl = "",
                iconRes = "volunteer_activism",
                personalizationTip = "Muy indicado para elevar tu nivel de energía vital actual del ${profile.energyLevelToday}%."
            )
        )
    }
}

data class SpiritualActivityItem(
    val id: String,
    val title: String,
    val description: String,
    val type: String, // "Meditación", "Ritual", "Terapia"
    val duration: String,
    val audioUrl: String,
    val iconRes: String,
    val personalizationTip: String
)
