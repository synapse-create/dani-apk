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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.models.TarotCard
import com.example.data.models.TarotDeck
import com.example.ui.theme.*
import com.example.ui.viewmodel.SpiritualViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TarotScreen(
    viewModel: SpiritualViewModel,
    modifier: Modifier = Modifier
) {
    var question by remember { mutableStateOf("") }
    var selectedSpread by remember { mutableStateOf("Pasado, Presente, Futuro") }
    var step by remember { mutableStateOf(1) } // 1: Setup, 2: Shuffling, 3: Drawing, 4: Reading loaded

    val isGenerating by viewModel.isGeneratingReading.collectAsStateWithLifecycle()
    val currentDrawn by viewModel.currentDrawnCards.collectAsStateWithLifecycle()
    val currentReading by viewModel.currentGeneratedReading.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // Spread configurations
    val spreads = listOf(
        SpreadConfig("Carta Diaria", 1, "Un solo arcano para guiar tu energía en las próximas horas."),
        SpreadConfig("SÍ o NO", 1, "Una respuesta cósmica directa de luz o sombra a tu pregunta."),
        SpreadConfig("Pasado, Presente, Futuro", 3, "Un hilo temporal sagrado para desvelar origen, momento actual e impulso futuro."),
        SpreadConfig("Herradura Celestial", 5, "Análisis completo: situación, miedos ocultos, ayuda terrenal, obstáculos y desenlace cósmico.")
    )

    val activeSpread = spreads.find { it.name == selectedSpread } ?: spreads[2]

    // Track tapped card face views (face down first, clicked flipper to open)
    var flippedStates = remember { mutableStateListOf<Boolean>() }

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
            text = "🔮 Oráculo del Tarot Ancestral",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = RadiantGold
            )
        )
        Text(
            text = "Sincroniza tus intenciones, escribe tu consulta interior y deja que las sincronicidades cósmicas revelen la guía divina de los arcanos mayores en conexión con tu Sol.",
            style = MaterialTheme.typography.bodySmall,
            color = CelestialGrey
        )

        Divider(color = DeepAmethyst, thickness = 1.dp)

        if (step == 1) {
            // STEP 1: SETUP QUESTION AND SPREAD
            Text(
                text = "1. Consagra tu Consulta Mental",
                style = MaterialTheme.typography.titleLarge.copy(color = Color.White)
            )

            OutlinedTextField(
                value = question,
                onValueChange = { question = it },
                label = { Text("¿Qué deseas preguntar u orientar hoy en tu camino?", color = CelestialGrey) },
                placeholder = { Text("e.g. ¿Cómo alineo mi proyecto profesional con mi camino de alma?", color = CelestialGrey.copy(alpha = 0.5f)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RadiantGold,
                    unfocusedBorderColor = DeepAmethyst,
                    focusedTextColor = MoonWhite,
                    unfocusedTextColor = MoonWhite,
                    focusedContainerColor = CelestialViolet.copy(alpha = 0.5f),
                    unfocusedContainerColor = CelestialViolet.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .testTag("tarot_question_input")
            )

            Text(
                text = "2. Selecciona la Tirada Cósmica",
                style = MaterialTheme.typography.titleLarge.copy(color = Color.White)
            )

            // Spreads list cards selection
            spreads.forEach { spread ->
                val isSel = spread.name == selectedSpread
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSel) DeepAmethyst else CelestialViolet
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = if (isSel) 2.dp else 1.dp,
                            color = if (isSel) RadiantGold else DeepAmethyst,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .clickable { selectedSpread = spread.name }
                        .testTag("spread_option_${spread.name.replace(" ", "_")}")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSel,
                            onClick = { selectedSpread = spread.name },
                            colors = RadioButtonDefaults.colors(selectedColor = RadiantGold, unselectedColor = CelestialGrey)
                        )
                        Column {
                            Text(
                                text = spread.name + " (${spread.cardCount} ${if (spread.cardCount == 1) "carta" else "cartas"})",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                ),
                                color = Color.White
                            )
                            Text(
                                text = spread.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = CelestialGrey
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    scope.launch {
                        step = 2 // Barajando state
                        delay(2500)
                        // Trigger card deck draw
                        viewModel.drawTarotReading(
                            question = question.ifEmpty { "Guía general para mi evolución espiritual" },
                            spreadType = selectedSpread,
                            count = activeSpread.cardCount
                        )
                        // Initialize all face-down states
                        flippedStates.clear()
                        repeat(activeSpread.cardCount) {
                            flippedStates.add(false)
                        }
                        step = 3 // Drawing room
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("tarot_shuffle_button"),
                colors = ButtonDefaults.buttonColors(containerColor = RadiantGold, contentColor = MidnightSpace),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Waves, contentDescription = null, tint = MidnightSpace)
                    Text("Barajar y Encauzar Energía Terrenal", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                }
            }
        }

        if (step == 2) {
            // STEP 2: BARANDO CELESTIAL ANIMATION
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(color = RadiantGold, modifier = Modifier.size(50.dp))
                    Text(
                        text = "Sincronizando el Oráculo Ancestral...",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "La sincronicidad cósmica está seleccionando los arcanos perfectos para responder a tu vibración actual.",
                        style = MaterialTheme.typography.bodySmall,
                        color = CelestialGrey,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            }
        }

        if (step == 3) {
            // STEP 3: DRAWN CARDS DISPLAYING ROOM
            Text(
                text = "Cartas extraídas para ti",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = Color.White),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Toca cada carta para desvelar su secreto astral, luego lee la interpretación mágica generada.",
                style = MaterialTheme.typography.bodySmall,
                color = CelestialGrey,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            // Cards row/grid view
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                currentDrawn.forEachIndexed { idx, card ->
                    val isFlipped = flippedStates.getOrElse(idx) { false }
                    CardBackOrFrontItem(
                        card = card,
                        isFlipped = isFlipped,
                        onClick = {
                            if (idx < flippedStates.size) {
                                flippedStates[idx] = true
                            }
                        },
                        index = idx
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Check if all cards are flipped, or provide autocomplete reader
            val allFlipped = flippedStates.all { it }

            if (isGenerating) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CelestialViolet),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator(color = SacredPink, modifier = Modifier.size(40.dp))
                        Text(
                            text = "Canalizando sabiduría de la IA Espiritual...",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MoonWhite
                        )
                        Text(
                            text = "Nuestros servidores de luz están interpretando la sincronicidad astrológica de tus arcanos con tu perfil natal.",
                            style = MaterialTheme.typography.bodySmall,
                            color = CelestialGrey,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                currentReading?.let { reading ->
                    // Reading generated successfully! Let's display it
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CelestialViolet),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SacredPink.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                            .testTag("tarot_reading_display")
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
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
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = RadiantGold)
                                    Text(
                                        "Lectura del Oráculo Ancestral",
                                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                        color = RadiantGold
                                    )
                                }
                            }

                            Text(
                                text = reading,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MoonWhite,
                                lineHeight = 22.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "✨ La sabiduría celestial ha hablado y resuena hoy en tu aura.",
                                style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                                color = SacredPink,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Button(
                        onClick = {
                            question = ""
                            step = 1
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("tarot_start_over_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepAmethyst, contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Iniciar Nueva Consulta Astral", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                    }
                } ?: run {
                    // Button to manually reveal reading if not load yet or manually force it
                    Button(
                        onClick = {
                            // Flip all cards and ask
                            flippedStates.indices.forEach { flippedStates[it] = true }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("reveal_all_cards_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = RadiantGold, contentColor = MidnightSpace),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Desvelar Todas las Cartas Faltantes", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
    }
}

data class SpreadConfig(
    val name: String,
    val cardCount: Int,
    val description: String
)

@Composable
fun CardBackOrFrontItem(
    card: TarotCard,
    isFlipped: Boolean,
    onClick: () -> Unit,
    index: Int
) {
    Box(
        modifier = Modifier
            .width(135.dp)
            .height(210.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag("tarot_card_item_$index"),
        contentAlignment = Alignment.Center
    ) {
        if (!isFlipped) {
            // CARD BACK CELSTIAL DESIGN
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            listOf(CelestialViolet, DeepAmethyst)
                        )
                    )
                    .border(2.dp, RadiantGold.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(1.dp, RadiantGold.copy(alpha = 0.2f), RoundedCornerShape(10.dp)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AllInclusive,
                        contentDescription = "Reverso de Tarot",
                        tint = RadiantGold,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "TOCAR PARA",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                        color = RadiantGold
                    )
                    Text(
                        "DESVELAR",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                        color = RadiantGold
                    )
                }
            }
        } else {
            // CARD FRONT DESIGNED
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CelestialViolet)
                    .border(2.dp, if (card.isUpright) RadiantGold else SacredPink, RoundedCornerShape(14.dp))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (card.isUpright) "▲" else "▼",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                            color = if (card.isUpright) RadiantGold else SacredPink
                        )
                        Text(
                            text = card.element,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 8.sp),
                            color = CelestialGrey
                        )
                    }

                    // Simulated central graphic representing card identity
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(DeepAmethyst),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (card.id) {
                                0 -> Icons.Default.DirectionsWalk
                                1 -> Icons.Default.AutoFixHigh
                                2 -> Icons.Default.Spa
                                3 -> Icons.Default.Park
                                4 -> Icons.Default.Shield
                                5 -> Icons.Default.MenuBook
                                6 -> Icons.Default.Favorite
                                7 -> Icons.Default.DirectionsCar
                                8 -> Icons.Default.FitnessCenter
                                9 -> Icons.Default.Visibility
                                10 -> Icons.Default.Autorenew
                                11 -> Icons.Default.Balance
                                12 -> Icons.Default.HourglassEmpty
                                13 -> Icons.Default.Co2 // transformation
                                14 -> Icons.Default.WaterDrop
                                15 -> Icons.Default.Key
                                16 -> Icons.Default.FlashOn
                                17 -> Icons.Default.Star
                                18 -> Icons.Default.NightsStay
                                19 -> Icons.Default.WbSunny
                                20 -> Icons.Default.Campaign
                                21 -> Icons.Default.Language
                                else -> Icons.Default.WbCloudy
                            },
                            contentDescription = card.name,
                            tint = if (card.isUpright) RadiantGold else SacredPink,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = card.name,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            ),
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                        Text(
                            text = if (card.isUpright) "Al Derecho" else "Invertido",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 8.sp,
                                fontStyle = FontStyle.Italic
                            ),
                            color = if (card.isUpright) RadiantGold else SacredPink,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
