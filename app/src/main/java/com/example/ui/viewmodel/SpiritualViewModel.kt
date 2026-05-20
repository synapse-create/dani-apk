package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.GeminiClient
import com.example.data.db.AppDatabase
import com.example.data.models.*
import com.example.data.repository.SpiritualRepository
import com.example.data.repository.SpiritualActivityItem
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SpiritualViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository = SpiritualRepository(
        database.userProfileDao(),
        database.readingHistoryDao(),
        database.testResultDao(),
        database.activityProgressDao()
    )

    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val listMyCardType = Types.newParameterizedType(List::class.java, MyCardDto::class.java)
    private val moshiCardAdapter = moshi.adapter<List<MyCardDto>>(listMyCardType)

    // Reactive State
    val userProfile: StateFlow<UserProfile?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allReadings: StateFlow<List<ReadingHistory>> = repository.allReadings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTestResults: StateFlow<List<TestResult>> = repository.allTestResults
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allActivityProgress: StateFlow<List<ActivityProgress>> = repository.allActivityProgress
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UI Interactive States
    val isGeneratingReading = MutableStateFlow(false)
    val currentDrawnCards = MutableStateFlow<List<TarotCard>>(emptyList())
    val currentGeneratedReading = MutableStateFlow<String?>(null)

    // Breathing Timer State
    val activeTimerSeconds = MutableStateFlow(0)
    val isTimerRunning = MutableStateFlow(false)
    private var timerJob: Job? = null

    // Register user profile
    fun registerProfile(
        name: String,
        birthdate: String,
        zodiacSign: String,
        ascendantSign: String,
        moonSign: String,
        goal: String,
        element: String
    ) {
        viewModelScope.launch {
            val auraColor = when (zodiacSign) {
                "Aries", "Leo", "Sagitario" -> "Rojo Dorado"
                "Tauro", "Virgo", "Capricornio" -> "Verde Esmeralda"
                "Géminis", "Libra", "Acuario" -> "Amarillo Resplandeciente"
                "Cáncer", "Escorpio", "Piscis" -> "Azul Índigo"
                else -> "Violeta Cósmica"
            }
            val activeChakra = when (goal) {
                "Paz Interior & Calma" -> "Tercer Ojo"
                "Sanación & Amor Propio" -> "Corazón"
                "Éxito, Abundancia & Propósito" -> "Plexo Solar"
                "Sabiduría & Intuición" -> "Corona"
                else -> "Garganta"
            }
            val profile = UserProfile(
                name = name,
                birthdate = birthdate,
                zodiacSign = zodiacSign,
                ascendantSign = ascendantSign,
                moonSign = moonSign,
                spiritualGoal = goal,
                elementFocus = element,
                auraColor = auraColor,
                activeChakra = activeChakra,
                energyLevelToday = 75,
                isRegistered = true
            )
            repository.saveUserProfile(profile)
        }
    }

    // Reset All Data
    fun resetSpiritualPath() {
        viewModelScope.launch {
            repository.saveUserProfile(UserProfile(isRegistered = false))
            repository.clearReadingHistory()
        }
    }

    // Draw cards simulation and ask Gemini for interpretation
    fun drawTarotReading(question: String, spreadType: String, count: Int) {
        viewModelScope.launch {
            isGeneratingReading.value = true
            currentGeneratedReading.value = null

            // 1. Draw cards
            val drawn = TarotDeck.drawCards(count)
            currentDrawnCards.value = drawn

            // 2. Prepare user context
            val profile = repository.getUserProfileOneShot() ?: UserProfile()

            // 3. Call REST API via OkHttp in GeminiClient
            val reading = GeminiClient.generateTarotReading(
                profile = profile,
                question = question,
                spreadType = spreadType,
                cards = drawn
            )

            // 4. Save to DB
            val myCardsDto = drawn.map {
                MyCardDto(id = it.id, name = it.name, isUpright = it.isUpright)
            }
            val cardsJson = moshiCardAdapter.toJson(myCardsDto) ?: "[]"

            val historyEntry = ReadingHistory(
                spreadType = spreadType,
                question = question,
                drawnCardsJson = cardsJson,
                interpretation = reading
            )
            repository.insertReading(historyEntry)

            // 5. Update UI state
            currentGeneratedReading.value = reading
            isGeneratingReading.value = false

            // Boost vital energy by +10% on successful spiritual feedback
            val updatedEnergy = minOf(100, profile.energyLevelToday + 10)
            repository.saveUserProfile(profile.copy(energyLevelToday = updatedEnergy))
        }
    }

    // Parse drawn cards list back from JSON
    fun parseDrawnCards(json: String): List<TarotCard> {
        return try {
            val list = moshiCardAdapter.fromJson(json) ?: emptyList()
            list.map { dto ->
                val base = TarotDeck.cards.first { it.id == dto.id }
                base.copy(isUpright = dto.isUpright)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getMeditationsForUser(profile: UserProfile): List<SpiritualActivityItem> = repository.getMeditationsForUser(profile)
    fun getRitualsForUser(profile: UserProfile): List<SpiritualActivityItem> = repository.getRitualsForUser(profile)
    fun getTherapiesForUser(profile: UserProfile): List<SpiritualActivityItem> = repository.getTherapiesForUser(profile)

    fun deleteReading(id: Int) {
        viewModelScope.launch {
            repository.deleteReading(id)
        }
    }

    // Submit a completed spiritual test
    fun submitTestResult(testType: String, score: String, resultText: String) {
        viewModelScope.launch {
            repository.insertTestResult(testType, score, resultText)

            // Map outcomes to update profile dynamically depending on test!
            val profile = repository.getUserProfileOneShot() ?: return@launch
            var updatedProfile = profile.copy(
                energyLevelToday = minOf(100, profile.energyLevelToday + 15)
            )

            if (testType == "Aura") {
                updatedProfile = updatedProfile.copy(auraColor = score)
            } else if (testType == "Chakras") {
                val balancedChakra = if (score.contains("Bloqueado", ignoreCase = true)) {
                    score.substringBefore(" ").trim()
                } else {
                    "Corona"
                }
                updatedProfile = updatedProfile.copy(activeChakra = balancedChakra)
            }

            repository.saveUserProfile(updatedProfile)
        }
    }

    // Manage activity completion
    fun toggleFavorite(activity: SpiritualActivityItem) {
        viewModelScope.launch {
            repository.toggleFavorite(activity.id, activity.title, activity.type)
        }
    }

    fun completeActivity(activity: SpiritualActivityItem) {
        viewModelScope.launch {
            repository.updateActivityProgress(activity.id, activity.title, activity.type, completedIncrement = true)
            
            val profile = repository.getUserProfileOneShot() ?: return@launch
            val updatedEnergy = minOf(100, profile.energyLevelToday + 8)
            repository.saveUserProfile(profile.copy(energyLevelToday = updatedEnergy))
        }
    }

    // Breathing Timer operations
    fun startTimer(seconds: Int) {
        timerJob?.cancel()
        activeTimerSeconds.value = seconds
        isTimerRunning.value = true
        timerJob = viewModelScope.launch {
            while (activeTimerSeconds.value > 0) {
                delay(1000)
                activeTimerSeconds.value -= 1
            }
            isTimerRunning.value = false
            // Autocomplete breathing exercise!
            completeActivity(
                SpiritualActivityItem(
                    id = "med_breathing_session",
                    title = "Respiración Rítmica Conectiva",
                    description = "Ejercicio de respiración completado",
                    type = "Meditación",
                    duration = "", audioUrl = "", iconRes = "", personalizationTip = ""
                )
            )
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        isTimerRunning.value = false
        activeTimerSeconds.value = 0
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}

@com.squareup.moshi.JsonClass(generateAdapter = true)
data class MyCardDto(
    val id: Int,
    val name: String,
    val isUpright: Boolean
)
