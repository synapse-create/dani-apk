package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.*
import com.example.ui.viewmodel.SpiritualViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SanctuaryScreen(
    viewModel: SpiritualViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val readings by viewModel.allReadings.collectAsStateWithLifecycle()
    val progressList by viewModel.allActivityProgress.collectAsStateWithLifecycle()
    val profileState by viewModel.userProfile.collectAsStateWithLifecycle()

    val favorites = progressList.filter { it.isFavorite }

    var selectedReadingDetail by remember { mutableStateOf<com.example.data.models.ReadingHistory?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightSpace)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Celestial screen title
        Text(
            text = "🕯️ El Santuario de Sanación",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = RadiantGold
            )
        )
        Text(
            text = "Un espacio sagrado privado de introspección donde se resguarda tu biblioteca divina: lecturas históricas, tus actividades favoritas consagradas y las reflexiones del éter.",
            style = MaterialTheme.typography.bodySmall,
            color = CelestialGrey
        )

        Divider(color = DeepAmethyst, thickness = 1.dp)

        // 1. REFLEXION DIARIA BANNER
        Card(
            colors = CardDefaults.cardColors(containerColor = CelestialViolet),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, RadiantGold.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = RadiantGold)
                    Text(
                        "Sabiduría del Éter de Hoy",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        color = RadiantGold
                    )
                }
                Text(
                    text = "\"El universo no responde a tu ansiedad; responde a tu claridad y alineación sagrada de intenciones. Entra en el santuario de tu respiración, sana tu sombra y deja fluir las sincronicidades del Gran Sol.\"",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontStyle = FontStyle.Italic,
                        fontSize = 14.sp
                    ),
                    color = MoonWhite
                )
                profileState?.let { profile ->
                    Text(
                        text = "— Susurro cósmico para tu sol en ${profile.zodiacSign}",
                        style = MaterialTheme.typography.bodySmall,
                        color = CelestialGrey,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // 2. FAVORITES COMPONENT
        Text(
            text = "💖 Tus Prácticas Consagradas",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = RadiantGold
            )
        )

        if (favorites.isEmpty()) {
            Card(
                colors = CardDefaults.cardColors(containerColor = CelestialViolet),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DeepAmethyst, RoundedCornerShape(12.dp))
            ) {
                Text(
                    text = "Aún no has consagrado prácticas como favoritas. Toca el símbolo del corazón en cualquier meditación, ritual o terapia de tu santuario principal para anclarlas aquí.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CelestialGrey,
                    modifier = Modifier.padding(14.dp),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            favorites.forEach { fav ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = CelestialViolet),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, DeepAmethyst, RoundedCornerShape(12.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = when (fav.type) {
                                    "Meditación" -> Icons.Default.Spa
                                    "Ritual" -> Icons.Default.BrightnessHigh
                                    else -> Icons.Default.Diamond
                                },
                                contentDescription = null,
                                tint = RadiantGold
                            )
                            Column {
                                Text(
                                    text = fav.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                                Text(
                                    text = "Categoría: ${fav.type} • Practicada ${fav.completedTimes} veces",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = CelestialGrey
                                )
                            }
                        }
                        IconButton(
                            onClick = {
                                // Simple mock item build to untoggle
                                val dummy = com.example.data.repository.SpiritualActivityItem(
                                    id = fav.id, title = fav.title, description = "", type = fav.type,
                                    duration = "", audioUrl = "", iconRes = "", personalizationTip = ""
                                )
                                viewModel.toggleFavorite(dummy)
                            }
                        ) {
                            Icon(Icons.Default.Favorite, contentDescription = "Eliminar de favoritos", tint = SacredPink)
                        }
                    }
                }
            }
        }

        // 3. TAROT LOGS HISTORICAL JOURNAL
        Text(
            text = "📔 Diario de Oráculos del Destino",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = RadiantGold
            )
        )

        if (readings.isEmpty()) {
            Card(
                colors = CardDefaults.cardColors(containerColor = CelestialViolet),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DeepAmethyst, RoundedCornerShape(12.dp))
            ) {
                Text(
                    text = "No has realizado consultas de cartas todavía hoy. Conéctate en la pestaña de Tarot y baraja las místicas verdades.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CelestialGrey,
                    modifier = Modifier.padding(14.dp),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            readings.forEach { record ->
                val cardList = viewModel.parseDrawnCards(record.drawnCardsJson)
                Card(
                    colors = CardDefaults.cardColors(containerColor = CelestialViolet),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, DeepAmethyst, RoundedCornerShape(14.dp))
                        .clickable { selectedReadingDetail = record }
                        .testTag("historical_reading_record")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = record.spreadType,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = RadiantGold
                            )
                            Text(
                                text = "Luz Celestial",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                color = CelestialGrey
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "\"${record.question}\"",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Italic
                            ),
                            color = Color.White,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        // Little chips representing cards drawn
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            cardList.forEach { card ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(DeepAmethyst)
                                        .padding(horizontal = 6.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = card.name,
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 9.sp),
                                        color = MoonWhite
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(60.dp))
    }

    // Modal dialog displaying full detailed historical readings
    selectedReadingDetail?.let { record ->
        val cardList = viewModel.parseDrawnCards(record.drawnCardsJson)

        AlertDialog(
            onDismissRequest = { selectedReadingDetail = null },
            title = {
                Text(
                    text = record.spreadType,
                    style = MaterialTheme.typography.titleLarge.copy(color = RadiantGold)
                )
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Pregunta realizada:",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = CelestialGrey
                    )
                    Text(
                        text = "\"${record.question}\"",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic
                        ),
                        color = Color.White
                    )

                    // Display mapped list of drawn card layouts
                    Text(
                        text = "Cartas extraídas:",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = CelestialGrey
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        cardList.forEach { card ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(DeepAmethyst)
                                    .border(1.dp, RadiantGold.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                    .padding(6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = card.name,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        textAlign = TextAlign.Center,
                                        color = Color.White
                                    )
                                    Text(
                                        text = if (card.isUpright) "Al Derecho" else "Invertido",
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 7.sp),
                                        color = RadiantGold,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }

                    Divider(color = DeepAmethyst, thickness = 1.dp)

                    Text(
                        text = "La Interpretación de las Estrellas:",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = CelestialGrey
                    )
                    Text(
                        text = record.interpretation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MoonWhite,
                        lineHeight = 20.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { selectedReadingDetail = null },
                    colors = ButtonDefaults.buttonColors(containerColor = RadiantGold, contentColor = MidnightSpace)
                ) {
                    Text("Cerrar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteReading(record.id)
                        selectedReadingDetail = null
                    }
                ) {
                    Text("Eliminar Registro del Templo", color = SacredPink)
                }
            },
            containerColor = CelestialViolet
        )
    }
}
