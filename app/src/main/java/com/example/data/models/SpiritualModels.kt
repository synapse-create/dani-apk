package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val name: String = "",
    val birthdate: String = "",
    val zodiacSign: String = "",
    val ascendantSign: String = "",
    val moonSign: String = "",
    val spiritualGoal: String = "",
    val elementFocus: String = "",
    val auraColor: String = "",
    val energyLevelToday: Int = 80, // %
    val activeChakra: String = "Corazón",
    val isRegistered: Boolean = false,
    val dateRegistered: Long = System.currentTimeMillis()
)

@Entity(tableName = "reading_history")
data class ReadingHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val spreadType: String, // "Carta Diaria", "SÍ/NO", "Pasado, Presente, Futuro", "Herradura"
    val question: String,
    val drawnCardsJson: String, // JSON list of drawn cards
    val interpretation: String
)

@Entity(tableName = "test_result")
data class TestResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val testType: String, // "Chakras", "Aura", "Espíritu Animal"
    val scoreOrCategory: String, // e.g. "Chakra Raíz bloqueado, Chakra Corazón abierto" or "Aura Violeta" or "Búho"
    val resultText: String
)

@Entity(tableName = "activity_progress")
data class ActivityProgress(
    @PrimaryKey val id: String, // unique slug of exercise, e.g. "meditacion_chakra_corona", "ritual_luna_llena"
    val title: String,
    val type: String, // "Ritual", "Meditación", "Terapia"
    val completedTimes: Int = 0,
    val lastCompleted: Long = 0,
    val isFavorite: Boolean = false
)
