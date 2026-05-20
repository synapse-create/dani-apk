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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.*
import com.example.ui.viewmodel.SpiritualViewModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SpiritualTestsScreen(
    viewModel: SpiritualViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val allResults by viewModel.allTestResults.collectAsStateWithLifecycle()

    var activeTestType by remember { mutableStateOf<String?>(null) } // "Chakras", "Aura", "Animal"
    var currentQuestionIdx by remember { mutableStateOf(0) }
    var selectedAnswerIdx by remember { mutableStateOf<Int?>(null) }
    var userAnswers = remember { mutableStateListOf<Int>() }

    // Defined questionnaires
    val chakraQuestions = listOf(
        TestQuestion(
            "¿Cómo manejas las situaciones de alto estrés o miedo en tu vida?",
            listOf("Siento pánico y me paralizo físicamente", "Busco meditar o canalizar mi respiración sagrada", "Planifico de manera lógica y establezco límites", "Me guío puramente por mi intuición psíquica")
        ),
        TestQuestion(
            "¿En qué zona de tu cuerpo sientes mayor tensión acumulada actualmente?",
            listOf("En la zona lumbar o piernas (raíz bloqueada)", "En el pecho y plexo cardíaco (corazón herido)", "En los hombros, cuello y garganta (expresión trabada)", "En la cabeza y entrecejo (tercer ojo sobrecargado)")
        ),
        TestQuestion(
            "¿Qué nivel de honestidad sostienes con tus verdaderos deseos del alma?",
            listOf("Tengo miedo de decirlos en voz alta por rechazo", "Los asumo con amor y plenitud sagrada", "Dudo todo el tiempo y cambio de dirección continuamente", "Dejo que la rutina decida por mí")
        )
    )

    val auraQuestions = listOf(
        TestQuestion(
            "Cuando entras a una habitación, ¿cuál es tu primera reacción inconsciente?",
            listOf("Percibo las sutilezas de las energías ambientales", "Busco socializar y contagiar alegría solar", "Siento timidez y prefiero mantenerme en observación", "Enfoco mi mente en buscar armonía u orden")
        ),
        TestQuestion(
            "¿Qué elemento de la naturaleza regenera más rápido tu energía vital?",
            listOf("Caminar descalzo en bosques o tierra húmeda", "Un baño en el río, mar o lluvia sagrada", "Sentarse ante una fogata sintiendo el calor", "La brisa del viento en la cima de una gran montaña")
        ),
        TestQuestion(
            "¿Cuál es tu color preferido cuando visualizas serenidad absoluta?",
            listOf("Un violeta cósmico profundo de trascendencia", "Un azul turquesa pacífico", "Un dorado místico o amarillo brillante", "Un verde esmeralda sanador de sanación")
        )
    )

    val animalQuestions = listOf(
        TestQuestion(
            "¿Cuál es tu mayor fortaleza en momentos difíciles?",
            listOf("Mi paciencia silenciosa e introspección", "Mi astucia analítica y visión de largo alcance", "Mi valentía protectora feroz", "Mi facilidad para fluir alegremente")
        ),
        TestQuestion(
            "¿Cómo prefieres pasar tus momentos de ocio espiritual?",
            listOf("En soledad profunda leyendo o meditando en paz", "Viajando e investigando lugares desconocidos", "Encuentros íntimos con mis seres de luz queridos", "Creando, pintando o escribiendo en mi santuario")
        )
    )

    val questions = when (activeTestType) {
        "Chakras" -> chakraQuestions
        "Aura" -> auraQuestions
        "Animal" -> animalQuestions
        else -> emptyList()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightSpace)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (activeTestType == null) {
            // TEST SELECTOR MAIN HUB
            Text(
                text = "✨ Tests del Despertar Interior",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = RadiantGold,
                    fontSize = 24.sp
                )
            )
            Text(
                text = "Evalúa la alineación de tu campo biomagnético, descubre las virtudes cromáticas de tu aura o sintoniza con tu animal de poder.",
                style = MaterialTheme.typography.bodySmall,
                color = CelestialGrey
            )

            Divider(color = DeepAmethyst, thickness = 1.dp)

            // Test 1: Chakras
            TestSelectionCard(
                title = "Alineación de Chakras",
                description = "Identifica bloqueos energéticos en tus 7 ruedas de luz y recibe el mantra curativo de balance.",
                icon = Icons.Default.BlurOn,
                color = RadiantGold,
                onClick = {
                    activeTestType = "Chakras"
                    currentQuestionIdx = 0
                    selectedAnswerIdx = null
                    userAnswers.clear()
                },
                tag = "test_selector_chakras"
            )

            // Test 2: Aura
            TestSelectionCard(
                title = "Descubrimiento de Aura",
                description = "Desvela la radiación áurica cromática que proyectas al cosmos y su sabiduría sanadora.",
                icon = Icons.Default.ColorLens,
                color = SacredPink,
                onClick = {
                    activeTestType = "Aura"
                    currentQuestionIdx = 0
                    selectedAnswerIdx = null
                    userAnswers.clear()
                },
                tag = "test_selector_aura"
            )

            // Test 3: Spirit Animal
            TestSelectionCard(
                title = "Descubre tu Espíritu de Poder",
                description = "Develamos el tótem salvaje de luz animal que camina a tu lado vigilando tus pasos terrenales.",
                icon = Icons.Default.Pets,
                color = StarlightBlue,
                onClick = {
                    activeTestType = "Animal"
                    currentQuestionIdx = 0
                    selectedAnswerIdx = null
                    userAnswers.clear()
                },
                tag = "test_selector_animal"
            )

            // HISTORICAL RESULTS CONTAINER
            if (allResults.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Tus Descubrimientos Históricos",
                    style = MaterialTheme.typography.titleLarge.copy(color = Color.White)
                )

                allResults.forEach { result ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CelestialViolet),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, DeepAmethyst, RoundedCornerShape(12.dp))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = result.testType,
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = RadiantGold
                                )
                                Text(
                                    text = "Resultado: ${result.scoreOrCategory}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = SacredPink
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = result.resultText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MoonWhite
                            )
                        }
                    }
                }
            }
        } else {
            // ENGAGED QUESTIONNAIRE INTERFACE
            val currentQuestion = questions[currentQuestionIdx]

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Test de $activeTestType",
                    style = MaterialTheme.typography.titleLarge.copy(color = RadiantGold)
                )
                Text(
                    text = "Pregunta ${currentQuestionIdx + 1} de ${questions.size}",
                    style = MaterialTheme.typography.bodySmall,
                    color = CelestialGrey
                )
            }

            // Visual progressive bar
            LinearProgressIndicator(
                progress = (currentQuestionIdx + 1) / questions.size.toFloat(),
                color = RadiantGold,
                trackColor = DeepAmethyst,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Main structured question card
            Card(
                colors = CardDefaults.cardColors(containerColor = CelestialViolet),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DeepAmethyst, RoundedCornerShape(16.dp))
            ) {
                Text(
                    text = currentQuestion.questionText,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        lineHeight = 24.sp
                    ),
                    color = Color.White,
                    modifier = Modifier.padding(18.dp),
                    textAlign = TextAlign.Center
                )
            }

            // Interactive answer columns
            currentQuestion.answers.forEachIndexed { index, ans ->
                val isAnswerSel = selectedAnswerIdx == index
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isAnswerSel) DeepAmethyst else CelestialViolet
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = if (isAnswerSel) 2.dp else 1.dp,
                            color = if (isAnswerSel) RadiantGold else DeepAmethyst,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { selectedAnswerIdx = index }
                        .testTag("test_answer_$index")
                ) {
                    Text(
                        text = ans,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        color = MoonWhite,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Return button
                TextButton(
                    onClick = { activeTestType = null },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar Test", color = CelestialGrey)
                }

                Button(
                    onClick = {
                        userAnswers.add(selectedAnswerIdx ?: 0)
                        if (currentQuestionIdx < questions.size - 1) {
                            currentQuestionIdx += 1
                            selectedAnswerIdx = null
                        } else {
                            // CALCULATE OUTCOME AND SUBMIT DB
                            val (score, text) = calculateTestOutcome(activeTestType!!, userAnswers)
                            viewModel.submitTestResult(activeTestType!!, score, text)
                            activeTestType = null // return and show results
                        }
                    },
                    modifier = Modifier
                        .weight(1.5f)
                        .height(48.dp)
                        .testTag("test_next_button"),
                    enabled = selectedAnswerIdx != null,
                    colors = ButtonDefaults.buttonColors(containerColor = RadiantGold, contentColor = MidnightSpace),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (currentQuestionIdx == questions.size - 1) "Sellar Resultados" else "Siguiente Portal",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

data class TestQuestion(
    val questionText: String,
    val answers: List<String>
)

@Composable
fun TestSelectionCard(
    title: String,
    description: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    tag: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CelestialViolet),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DeepAmethyst, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag(tag)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12))
                    .background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(28.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontSize = 17.sp),
                    color = Color.White
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = CelestialGrey
                )
            }
            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, tint = color)
        }
    }
}

// Custom outcome logic for the tests
fun calculateTestOutcome(type: String, answers: List<Int>): Pair<String, String> {
    val first = answers.getOrNull(0) ?: 0
    val second = answers.getOrNull(1) ?: 0

    return when (type) {
        "Chakras" -> {
            val chakra = when (second) {
                0 -> "Raíz"
                1 -> "Corazón"
                2 -> "Garganta"
                else -> "Tercer Ojo"
            }
            val status = if (first == 0) "Bloqueado por miedos terrenales" else "Alineado e Irradiando"
            Pair(
                "$chakra: $status",
                "Tus respuestas indican que tu Chakra $chakra requiere atención consciente. Te aconsejamos realizar respiraciones y rituales vinculados al portal de tu chakra para transmutar tus nudos acumulados."
            )
        }
        "Aura" -> {
            val aura = when (first) {
                0 -> "Violeta Cósmica"
                1 -> "Amarillo Resplandeciente"
                2 -> "Rojo Carmesí"
                else -> "Azul Índigo"
            }
            Pair(
                aura,
                "Tu vibración áurica dominante se inclina hacia el tono $aura. Esto proyecta una sabiduría natural intuitiva y una gran capacidad de expansión curativa espacial. Coloca flores o sahumerios afines para consagrar tu aura."
            )
        }
        else -> { // Spirit Animal
            val (animal, desc) = when (first) {
                0 -> Pair("Búho Sagrado", "El guardián silencioso de la sabiduría oculta y las visiones nocturnas del inconsciente. Su lección es observar antes de decidir.")
                1 -> Pair("Águila Real", "Símbolo de la visión de largo alcance, el poder mental superior y la fuerza divina para remontar las tormentas de vida.")
                2 -> Pair("Lobo Estelar", "El maestro de los caminos, devoto de la manada cósmica y protector feroz de las libertades del yo interior.")
                else -> Pair("Delfín Azul", "El portador pránico del juego del alma, la comunicación empática y la fluidez pura emocional.")
            }
            Pair(
                animal,
                desc
            )
        }
    }
}
