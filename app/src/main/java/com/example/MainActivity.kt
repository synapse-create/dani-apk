package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.SpiritualViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MidnightSpace
                ) {
                    SpiritualAppRoot()
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SpiritualAppRoot() {
    val viewModel: SpiritualViewModel = viewModel()
    val profileState by viewModel.userProfile.collectAsStateWithLifecycle()

    var activeTab by remember { mutableStateOf(0) } // 0: Dashboard, 1: Tarot, 2: Tests, 3: Sanctuary

    val profile = profileState

    if (profile == null || !profile.isRegistered) {
        // Show onboarding Questionnaire
        OnboardingScreen(
            viewModel = viewModel,
            modifier = Modifier.fillMaxSize()
        )
    } else {
        // Draw primary App Interface with custom animated tab switching which avoids any complex routing bugs!
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                NavigationBar(
                    containerColor = CelestialViolet,
                    tonalElevation = 8.dp,
                    modifier = Modifier
                        .navigationBarsPadding()
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .testTag("app_navigation_bar")
                ) {
                    NavigationBarItem(
                        selected = activeTab == 0,
                        onClick = { activeTab = 0 },
                        label = { Text("Templo") },
                        icon = { Icon(Icons.Default.Spa, contentDescription = "Templo") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MidnightSpace,
                            selectedTextColor = RadiantGold,
                            indicatorColor = RadiantGold,
                            unselectedIconColor = CelestialGrey,
                            unselectedTextColor = CelestialGrey
                        ),
                        modifier = Modifier.testTag("tab_templo")
                    )
                    NavigationBarItem(
                        selected = activeTab == 1,
                        onClick = { activeTab = 1 },
                        label = { Text("Tarot") },
                        icon = { Icon(Icons.Default.AutoFixHigh, contentDescription = "Tarot") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MidnightSpace,
                            selectedTextColor = RadiantGold,
                            indicatorColor = RadiantGold,
                            unselectedIconColor = CelestialGrey,
                            unselectedTextColor = CelestialGrey
                        ),
                        modifier = Modifier.testTag("tab_tarot")
                    )
                    NavigationBarItem(
                        selected = activeTab == 2,
                        onClick = { activeTab = 2 },
                        label = { Text("Alineación") },
                        icon = { Icon(Icons.Default.BlurOn, contentDescription = "Alineación") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MidnightSpace,
                            selectedTextColor = RadiantGold,
                            indicatorColor = RadiantGold,
                            unselectedIconColor = CelestialGrey,
                            unselectedTextColor = CelestialGrey
                        ),
                        modifier = Modifier.testTag("tab_alineacion")
                    )
                    NavigationBarItem(
                        selected = activeTab == 3,
                        onClick = { activeTab = 3 },
                        label = { Text("Santuario") },
                        icon = { Icon(Icons.Default.AccountBalance, contentDescription = "Santuario") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MidnightSpace,
                            selectedTextColor = RadiantGold,
                            indicatorColor = RadiantGold,
                            unselectedIconColor = CelestialGrey,
                            unselectedTextColor = CelestialGrey
                        ),
                        modifier = Modifier.testTag("tab_santuario")
                    )
                }
            }
        ) { innerPadding ->
            // Animated, cross-faded content switching room!
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AnimatedContent(
                    targetState = activeTab,
                    transitionSpec = {
                        fadeIn() with fadeOut()
                    },
                    modifier = Modifier.fillMaxSize()
                ) { targetState ->
                    when (targetState) {
                        0 -> DashboardScreen(viewModel = viewModel, modifier = Modifier.fillMaxSize())
                        1 -> TarotScreen(viewModel = viewModel, modifier = Modifier.fillMaxSize())
                        2 -> SpiritualTestsScreen(viewModel = viewModel, modifier = Modifier.fillMaxSize())
                        3 -> SanctuaryScreen(viewModel = viewModel, modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}
