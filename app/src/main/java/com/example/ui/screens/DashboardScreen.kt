package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.api.GeminiClient
import com.example.data.models.UserProfile
import com.example.data.models.ActivityProgress
import com.example.data.repository.SpiritualActivityItem
import androidx.compose.animation.core.animateDpAsState
import com.example.ui.theme.*
import com.example.ui.viewmodel.SpiritualViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DashboardScreen(
    viewModel: SpiritualViewModel,
    modifier: Modifier = Modifier
) {
    val profileState by viewModel.userProfile.collectAsStateWithLifecycle()
    val allProgress by viewModel.allActivityProgress.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    // Dialog state
    var selectedActivity by remember { mutableStateOf<SpiritualActivityItem?>(null) }
    var showBreathingTimerDialog by remember { mutableStateOf(false) }

    val profile = profileState ?: return

    val meditations = viewModel.getMeditationsForUser(profile)
    val rituals = viewModel.getRitualsForUser(profile)
    val therapies = viewModel.getTherapiesForUser(profile)

    // Breathing timer states
    val timerSeconds by viewModel.activeTimerSeconds.collectAsStateWithLifecycle()
    val isTimerRunning by viewModel.isTimerRunning.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightSpace)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Warning Banner if Gemini API client lacks valid API key
        if (!GeminiClient.hasValidApiKey()) {
            Card(
                colors = CardDefaults.cardColors(containerColor = CelestialViolet),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, RadiantGold.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = RadiantGold,
                        modifier = Modifier.size(24.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Sabiduría Cósmica Desconectada",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = RadiantGold
                        )
                        Text(
                            text = "Configura tu GEMINI_API_KEY en el panel de secretos en AI Studio para liberar la IA espiritual personalizada.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MoonWhite.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // Welcome Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Bienvenido, ${profile.name}",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Text(
                    text = "Santuario de calma y transmutación de tu alma",
                    style = MaterialTheme.typography.bodySmall,
                    color = CelestialGrey
                )
            }
            // Quick resets button
            IconButton(
                onClick = { viewModel.resetSpiritualPath() },
                modifier = Modifier.testTag("reset_profile_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Reiniciar camino",
                    tint = SacredPink.copy(alpha = 0.6f)
                )
            }
        }

        // Core Astrology Row Cards
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            maxItemsInEachRow = Int.MAX_VALUE
        ) {
            AstroPill(icon = Icons.Default.WbSunny, label = "Sol", value = profile.zodiacSign)
            AstroPill(icon = Icons.Default.TrendingUp, label = "Asc", value = profile.ascendantSign)
            AstroPill(icon = Icons.Default.NightsStay, label = "Luna", value = profile.moonSign)
            AstroPill(icon = Icons.Default.Opacity, label = "Afinidad", value = profile.elementFocus)
        }

        // Vital Spiritual Energy Gauge & Insights
        Card(
            colors = CardDefaults.cardColors(containerColor = CelestialViolet),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(SacredPink)
                        )
                        Text(
                            text = "Fuerza Vital de tu Aura",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MoonWhite
                        )
                    }
                    Text(
                        text = "${profile.energyLevelToday}%",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = SacredPink
                        )
                    )
                }

                // Progress Bar
                LinearProgressIndicator(
                    progress = profile.energyLevelToday / 100f,
                    color = SacredPink,
                    trackColor = DeepAmethyst,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(DeepAmethyst),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.BlurOn,
                            contentDescription = null,
                            tint = StarlightBlue,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Aura en tono ${profile.auraColor}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MoonWhite
                        )
                        Text(
                            text = "Chakra Foco activo: ${profile.activeChakra}. Realiza actividades de alta vibración para elevar el poder cósmico de tu templo.",
                            style = MaterialTheme.typography.bodySmall,
                            color = CelestialGrey
                        )
                    }
                }
            }
        }

        // Quick connect Breathing Banner
        Card(
            colors = CardDefaults.cardColors(containerColor = CelestialViolet),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, StarlightBlue.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                .clickable { showBreathingTimerDialog = true }
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(StarlightBlue.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Air,
                        contentDescription = null,
                        tint = StarlightBlue,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Ejercitador de Respiración Pránica",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        color = MoonWhite
                    )
                    Text(
                        text = "Conecta un minuto con tu vibración divina de calma y libera miedos.",
                        style = MaterialTheme.typography.bodySmall,
                        color = CelestialGrey
                    )
                }
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = StarlightBlue,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // 1. RECOMMENDED MEDITATIONS SECTION
        Text(
            text = "🧘 Meditaciones del Despertar",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = RadiantGold
            )
        )
        meditations.forEach { med ->
            ActivityRowItem(
                activity = med,
                allProgress = allProgress,
                onClick = { selectedActivity = med },
                onFavoriteToggle = { viewModel.toggleFavorite(med) }
            )
        }

        // 2. RECOMMENDED RITUALS SECTION
        Text(
            text = "✨ Sabios Rituales de Conexión",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = RadiantGold
            )
        )
        rituals.forEach { rit ->
            ActivityRowItem(
                activity = rit,
                allProgress = allProgress,
                onClick = { selectedActivity = rit },
                onFavoriteToggle = { viewModel.toggleFavorite(rit) }
            )
        }

        // 3. ALTERNATIVE THERAPIES
        Text(
            text = "💎 Terapias Alternativas Alquímicas",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = RadiantGold
            )
        )
        therapies.forEach { ther ->
            ActivityRowItem(
                activity = ther,
                allProgress = allProgress,
                onClick = { selectedActivity = ther },
                onFavoriteToggle = { viewModel.toggleFavorite(ther) }
            )
        }

        Spacer(modifier = Modifier.height(60.dp))
    }

    // Detail dialog for Activity Detail and complete feedback!
    selectedActivity?.let { activity ->
        val progress = allProgress.firstOrNull { it.id == activity.id }
        val timesCompleted = progress?.completedTimes ?: 0

        AlertDialog(
            onDismissRequest = { selectedActivity = null },
            title = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = when (activity.iconRes) {
                            "spa" -> Icons.Default.Spa
                            "air" -> Icons.Default.Air
                            "face" -> Icons.Default.Face
                            "nights_stay" -> Icons.Default.NightsStay
                            "brightness_high" -> Icons.Default.BrightnessHigh
                            "grain" -> Icons.Default.EnergySavingsLeaf
                            "diamond" -> Icons.Default.Diamond
                            "music_note" -> Icons.Default.MusicNote
                            "volunteer_activism" -> Icons.Default.VolunteerActivism
                            else -> Icons.Default.SelfImprovement
                        },
                        contentDescription = null,
                        tint = RadiantGold
                    )
                    Text(
                        text = activity.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = activity.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MoonWhite
                    )

                    // Personalization Tip
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DeepAmethyst.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                "🌿 Guía Celestial Especial:",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = RadiantGold
                            )
                            Text(
                                text = activity.personalizationTip,
                                style = MaterialTheme.typography.bodySmall,
                                color = MoonWhite.copy(alpha = 0.9f)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Duración aproximada: ${activity.duration}",
                            style = MaterialTheme.typography.bodySmall,
                            color = CelestialGrey
                        )
                        Text(
                            text = "Veces practicadas: $timesCompleted",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = SacredPink
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.completeActivity(activity)
                        selectedActivity = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RadiantGold, contentColor = MidnightSpace),
                    modifier = Modifier.testTag("dialog_complete_button")
                ) {
                    Text("Marcar como Completado (+8% Energía)")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { selectedActivity = null }
                ) {
                    Text("Cerrar", color = CelestialGrey)
                }
            },
            containerColor = CelestialViolet
        )
    }

    // Breathing prana exercises interactive loop dialog
    if (showBreathingTimerDialog) {
        AlertDialog(
            onDismissRequest = {
                viewModel.stopTimer()
                showBreathingTimerDialog = false
            },
            title = {
                Text(
                    text = "Respiración Pránica Conectiva",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Une tu conciencia al movimiento celeste de expansión. Inhala profundamente cuando sientas expansión, retén un instante y exhala con amor.",
                        style = MaterialTheme.typography.bodySmall,
                        color = CelestialGrey,
                        textAlign = TextAlign.Center
                    )

                    // Big visual breathing circle pulsing
                    val animatedSize by animateDpAsState(
                        targetValue = if (isTimerRunning && (timerSeconds % 8 in 0..3)) 160.dp else 100.dp,
                        animationSpec = androidx.compose.animation.core.tween(4000)
                    )

                    Box(
                        modifier = Modifier
                            .size(180.dp)
                            .height(180.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Background glow ring
                        Box(
                            modifier = Modifier
                                .size(animatedSize)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        listOf(StarlightBlue.copy(alpha = 0.4f), Color.Transparent)
                                    )
                                )
                                .border(2.dp, StarlightBlue, CircleShape)
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = if (isTimerRunning) {
                                    if (timerSeconds % 8 in 0..3) "INHALE" else "EXHALE"
                                } else "Listo",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 20.sp
                                ),
                                color = StarlightBlue
                            )
                            if (isTimerRunning) {
                                Text(
                                    text = "$timerSeconds s",
                                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { viewModel.startTimer(60) },
                            enabled = !isTimerRunning,
                            colors = ButtonDefaults.buttonColors(containerColor = StarlightBlue, contentColor = MidnightSpace),
                            modifier = Modifier.weight(1f).testTag("breathing_start_60s")
                        ) {
                            Text("Iniciar 1 M", fontSize = 12.sp)
                        }
                        Button(
                            onClick = { viewModel.startTimer(180) },
                            enabled = !isTimerRunning,
                            colors = ButtonDefaults.buttonColors(containerColor = RadiantGold, contentColor = MidnightSpace),
                            modifier = Modifier.weight(1f).testTag("breathing_start_180s")
                        ) {
                            Text("Iniciar 3 M", fontSize = 12.sp)
                        }
                    }
                }
            },
            confirmButton = {
                if (isTimerRunning) {
                    TextButton(
                        onClick = {
                            viewModel.stopTimer()
                        }
                    ) {
                        Text("Detener", color = SacredPink)
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.stopTimer()
                        showBreathingTimerDialog = false
                    }
                ) {
                    Text("Cerrar", color = CelestialGrey)
                }
            },
            containerColor = CelestialViolet
        )
    }
}

@Composable
fun AstroPill(
    icon: ImageVector,
    label: String,
    value: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CelestialViolet),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .border(1.dp, DeepAmethyst, RoundedCornerShape(12.dp))
            .padding(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = RadiantGold, modifier = Modifier.size(14.dp))
            Text(
                text = "$label: $value",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = MoonWhite,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun ActivityRowItem(
    activity: SpiritualActivityItem,
    allProgress: List<ActivityProgress>,
    onClick: () -> Unit,
    onFavoriteToggle: () -> Unit
) {
    val progress = allProgress.firstOrNull { it.id == activity.id }
    val isFav = progress?.isFavorite ?: false
    val completedTimes = progress?.completedTimes ?: 0

    Card(
        colors = CardDefaults.cardColors(containerColor = CelestialViolet),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DeepAmethyst, RoundedCornerShape(14.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(DeepAmethyst),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (activity.iconRes) {
                        "spa" -> Icons.Default.Spa
                        "air" -> Icons.Default.Air
                        "face" -> Icons.Default.Face
                        "nights_stay" -> Icons.Default.NightsStay
                        "brightness_high" -> Icons.Default.BrightnessHigh
                        "grain" -> Icons.Default.EnergySavingsLeaf
                        "diamond" -> Icons.Default.Diamond
                        "music_note" -> Icons.Default.MusicNote
                        "volunteer_activism" -> Icons.Default.VolunteerActivism
                        else -> Icons.Default.SelfImprovement
                    },
                    contentDescription = null,
                    tint = RadiantGold,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = activity.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        ),
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (completedTimes > 0) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(SacredPink.copy(alpha = 0.2f))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(
                                "Hecho $completedTimes",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 9.sp),
                                color = SacredPink
                            )
                        }
                    }
                }
                Text(
                    text = activity.description,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                    color = CelestialGrey,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(onClick = onFavoriteToggle) {
                Icon(
                    imageVector = if (isFav) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = if (isFav) SacredPink else CelestialGrey
                )
            }
        }
    }
}
