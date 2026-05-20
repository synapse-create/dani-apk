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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.SpiritualViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    viewModel: SpiritualViewModel,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }
    
    // Dropdown States
    var zodiacSign by remember { mutableStateOf("Leo") }
    var ascendantSign by remember { mutableStateOf("Sagitario") }
    var moonSign by remember { mutableStateOf("Cáncer") }
    var spiritualGoal by remember { mutableStateOf("Sanación & Amor Propio") }
    var elementFocus by remember { mutableStateOf("Fuego") }

    // Dropdown expanded flags
    var zodiacExpanded by remember { mutableStateOf(false) }
    var ascendantExpanded by remember { mutableStateOf(false) }
    var moonExpanded by remember { mutableStateOf(false) }
    var goalExpanded by remember { mutableStateOf(false) }
    var elementExpanded by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    val zodiacSigns = listOf(
        "Aries", "Tauro", "Géminis", "Cáncer", "Leo", "Virgo",
        "Libra", "Escorpio", "Sagitario", "Capricornio", "Acuario", "Piscis"
    )
    val elements = listOf("Fuego", "Tierra", "Aire", "Agua")
    val goals = listOf(
        "Sanación & Amor Propio",
        "Paz Interior & Calma",
        "Éxito, Abundancia & Propósito",
        "Sabiduría & Intuición",
        "Exploración de la Sombra"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(MidnightSpace, CelestialViolet)
                )
            )
            .padding(24.dp)
            .verticalScroll(scrollState),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 500.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Sacred logo or symbol animation
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(40))
                    .background(
                        Brush.radialGradient(
                            listOf(RadiantGold.copy(alpha = 0.5f), Color.Transparent)
                        )
                    )
                    .border(2.dp, RadiantGold, RoundedCornerShape(40)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Símbolo Sagrado",
                    tint = RadiantGold,
                    modifier = Modifier.size(40.dp)
                )
            }

            Text(
                text = "Portal de Iniciación",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = RadiantGold
                ),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Desvela las claves cósmicas de tu alma para que la guía astral se sintonice con tu esencia estelar y guíe tus pasos hoy.",
                style = MaterialTheme.typography.bodyMedium,
                color = CelestialGrey,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            // Name text field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre Terrenal", color = CelestialGrey) },
                leadingIcon = { Icon(Icons.Default.AccountCircle, contentDescription = null, tint = RadiantGold) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RadiantGold,
                    unfocusedBorderColor = DeepAmethyst,
                    focusedTextColor = MoonWhite,
                    unfocusedTextColor = MoonWhite,
                    focusedContainerColor = CelestialViolet.copy(alpha = 0.6f),
                    unfocusedContainerColor = CelestialViolet.copy(alpha = 0.6f)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("onboarding_name_input"),
                singleLine = true
            )

            // Birthday Text Field
            OutlinedTextField(
                value = birthdate,
                onValueChange = { birthdate = it },
                label = { Text("Fecha de Nacimiento (Día/Mes/Año)", color = CelestialGrey) },
                placeholder = { Text("e.g. 15/08/1995", color = CelestialGrey.copy(alpha = 0.5f)) },
                leadingIcon = { Icon(Icons.Default.Cake, contentDescription = null, tint = RadiantGold) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RadiantGold,
                    unfocusedBorderColor = DeepAmethyst,
                    focusedTextColor = MoonWhite,
                    unfocusedTextColor = MoonWhite,
                    focusedContainerColor = CelestialViolet.copy(alpha = 0.6f),
                    unfocusedContainerColor = CelestialViolet.copy(alpha = 0.6f)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("onboarding_birthdate_input"),
                singleLine = true
            )

            // Select Zodiac Sign Dropdown
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = zodiacSign,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Signo Solar (Astral)", color = CelestialGrey) },
                    leadingIcon = { Icon(Icons.Default.WbSunny, contentDescription = null, tint = RadiantGold) },
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = CelestialGrey) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RadiantGold,
                        unfocusedBorderColor = DeepAmethyst,
                        focusedTextColor = MoonWhite,
                        unfocusedTextColor = MoonWhite,
                        focusedContainerColor = CelestialViolet.copy(alpha = 0.6f),
                        unfocusedContainerColor = CelestialViolet.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = true,
                    shape = RoundedCornerShape(12.dp)
                )
                // Invisible overlay clicker
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { zodiacExpanded = true }
                )
                DropdownMenu(
                    expanded = zodiacExpanded,
                    onDismissRequest = { zodiacExpanded = false },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .background(CelestialViolet)
                        .border(1.dp, DeepAmethyst, RoundedCornerShape(8.dp))
                ) {
                    zodiacSigns.forEach { sign ->
                        DropdownMenuItem(
                            text = { Text(sign, color = MoonWhite) },
                            onClick = {
                                zodiacSign = sign
                                zodiacExpanded = false
                            }
                        )
                    }
                }
            }

            // Expand Ascendant Sign selector
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = ascendantSign,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Signo Ascendente (Máscara Espiritual)", color = CelestialGrey) },
                    leadingIcon = { Icon(Icons.Default.TrendingUp, contentDescription = null, tint = RadiantGold) },
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = CelestialGrey) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RadiantGold,
                        unfocusedBorderColor = DeepAmethyst,
                        focusedTextColor = MoonWhite,
                        unfocusedTextColor = MoonWhite,
                        focusedContainerColor = CelestialViolet.copy(alpha = 0.6f),
                        unfocusedContainerColor = CelestialViolet.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = true,
                    shape = RoundedCornerShape(12.dp)
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { ascendantExpanded = true }
                )
                DropdownMenu(
                    expanded = ascendantExpanded,
                    onDismissRequest = { ascendantExpanded = false },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .background(CelestialViolet)
                        .border(1.dp, DeepAmethyst, RoundedCornerShape(8.dp))
                ) {
                    zodiacSigns.forEach { sign ->
                        DropdownMenuItem(
                            text = { Text(sign, color = MoonWhite) },
                            onClick = {
                                ascendantSign = sign
                                ascendantExpanded = false
                            }
                        )
                    }
                }
            }

            // Expand Moon Sign Selector
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = moonSign,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Signo Lunar (Mundo Emocional)", color = CelestialGrey) },
                    leadingIcon = { Icon(Icons.Default.NightsStay, contentDescription = null, tint = RadiantGold) },
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = CelestialGrey) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RadiantGold,
                        unfocusedBorderColor = DeepAmethyst,
                        focusedTextColor = MoonWhite,
                        unfocusedTextColor = MoonWhite,
                        focusedContainerColor = CelestialViolet.copy(alpha = 0.6f),
                        unfocusedContainerColor = CelestialViolet.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = true,
                    shape = RoundedCornerShape(12.dp)
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { moonExpanded = true }
                )
                DropdownMenu(
                    expanded = moonExpanded,
                    onDismissRequest = { moonExpanded = false },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .background(CelestialViolet)
                        .border(1.dp, DeepAmethyst, RoundedCornerShape(8.dp))
                ) {
                    zodiacSigns.forEach { sign ->
                        DropdownMenuItem(
                            text = { Text(sign, color = MoonWhite) },
                            onClick = {
                                moonSign = sign
                                moonExpanded = false
                            }
                        )
                    }
                }
            }

            // Select Element Focus Dropdown
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = elementFocus,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Elemento Afin de tu Alma", color = CelestialGrey) },
                    leadingIcon = { Icon(Icons.Default.Opacity, contentDescription = null, tint = RadiantGold) },
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = CelestialGrey) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RadiantGold,
                        unfocusedBorderColor = DeepAmethyst,
                        focusedTextColor = MoonWhite,
                        unfocusedTextColor = MoonWhite,
                        focusedContainerColor = CelestialViolet.copy(alpha = 0.6f),
                        unfocusedContainerColor = CelestialViolet.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = true,
                    shape = RoundedCornerShape(12.dp)
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { elementExpanded = true }
                )
                DropdownMenu(
                    expanded = elementExpanded,
                    onDismissRequest = { elementExpanded = false },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .background(CelestialViolet)
                        .border(1.dp, DeepAmethyst, RoundedCornerShape(8.dp))
                ) {
                    elements.forEach { element ->
                        DropdownMenuItem(
                            text = { Text(element, color = MoonWhite) },
                            onClick = {
                                elementFocus = element
                                elementExpanded = false
                            }
                        )
                    }
                }
            }

            // Select Spiritual Goal Dropdown
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = spiritualGoal,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tu Propósito de Búsqueda Sagrada", color = CelestialGrey) },
                    leadingIcon = { Icon(Icons.Default.SelfImprovement, contentDescription = null, tint = RadiantGold) },
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = CelestialGrey) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RadiantGold,
                        unfocusedBorderColor = DeepAmethyst,
                        focusedTextColor = MoonWhite,
                        unfocusedTextColor = MoonWhite,
                        focusedContainerColor = CelestialViolet.copy(alpha = 0.6f),
                        unfocusedContainerColor = CelestialViolet.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = true,
                    shape = RoundedCornerShape(12.dp)
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { goalExpanded = true }
                )
                DropdownMenu(
                    expanded = goalExpanded,
                    onDismissRequest = { goalExpanded = false },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .background(CelestialViolet)
                        .border(1.dp, DeepAmethyst, RoundedCornerShape(8.dp))
                ) {
                    goals.forEach { goal ->
                        DropdownMenuItem(
                            text = { Text(goal, color = MoonWhite) },
                            onClick = {
                                spiritualGoal = goal
                                goalExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        viewModel.registerProfile(
                            name = name,
                            birthdate = birthdate,
                            zodiacSign = zodiacSign,
                            ascendantSign = ascendantSign,
                            moonSign = moonSign,
                            goal = spiritualGoal,
                            element = elementFocus
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("onboarding_submit_button"),
                enabled = name.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RadiantGold,
                    contentColor = MidnightSpace,
                    disabledContainerColor = CelestialGrey.copy(alpha = 0.3f),
                    disabledContentColor = MoonWhite.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 2.dp
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Iniciar Despertar Sagrado",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    )
                    Icon(Icons.Default.Explore, contentDescription = null, tint = MidnightSpace)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
