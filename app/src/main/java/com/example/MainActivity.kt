package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horuscode.data.UserProgressManager
import com.example.horuscode.ui.components.HorusStrings
import com.example.horuscode.ui.screens.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch

enum class AppDestination(val iconName: String, val titleKey: String) {
    STUDIO("☥", "Studio"),
    LEVELS("🎯", "Levels"),
    MAZE("𓊗", "Temple Maze"),
    ACADEMY("𓏛", "Academy"),
    DEBUG("🔍", "Debug Lab"),
    SYMBOLS("𓂀", "Symbols"),
    DASHBOARD("🏆", "Dashboard"),
    JUDGE("👑", "Judge Demo"),
    EVALUATION("📊", "Evaluation"),
    SETTINGS("⚙️", "Settings")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val progressManager = UserProgressManager(this)

        setContent {
            val progress by progressManager.progressFlow.collectAsState()
            val isArabic = progress.isArabic
            val isDarkTheme = progress.isDarkTheme

            // Cinematic Opening sequence plays on launch
            var showCinematicIntro by remember { mutableStateOf(progress.showIntroOnLaunch) }

            MyApplicationTheme(darkTheme = isDarkTheme) {
                CompositionLocalProvider(
                    LocalLayoutDirection provides if (isArabic) LayoutDirection.Rtl else LayoutDirection.Ltr
                ) {
                    if (showCinematicIntro) {
                        HorusCinematicIntroScreen(
                            onFinish = {
                                progressManager.markIntroSeen()
                                showCinematicIntro = false
                            },
                            onSkip = {
                                progressManager.markIntroSeen()
                                showCinematicIntro = false
                            },
                            isArabic = isArabic,
                            onToggleLanguage = {
                                progressManager.setLanguage(!isArabic)
                            }
                        )
                    } else {
                        HorusAppScaffold(
                            progressManager = progressManager,
                            isArabic = isArabic,
                            isDarkTheme = isDarkTheme,
                            onReplayIntro = {
                                showCinematicIntro = true
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorusAppScaffold(
    progressManager: UserProgressManager,
    isArabic: Boolean,
    isDarkTheme: Boolean,
    onReplayIntro: () -> Unit = {}
) {
    val progress by progressManager.progressFlow.collectAsState()
    var currentDestination by remember { mutableStateOf(AppDestination.STUDIO) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    // Handle system back navigation to return to Home (Studio)
    BackHandler(enabled = currentDestination != AppDestination.STUDIO) {
        currentDestination = AppDestination.STUDIO
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = HorusMidnight,
                drawerContentColor = HorusIvory,
                modifier = Modifier.width(300.dp)
            ) {
                // Drawer Header
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(HorusCardNavy)
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(HorusGold.copy(alpha = 0.2f))
                                .border(1.5.dp, HorusGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("𓂀", fontSize = 26.sp, color = HorusGold, fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text(
                                text = "HORUS CODE",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = HorusIvory
                            )
                            Text(
                                text = "شِفرة حورس",
                                style = MaterialTheme.typography.titleSmall,
                                color = HorusGold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (isArabic) "«من رموز الماضي… إلى شيفرة المستقبل»" else "\"From past symbols to future code\"",
                        style = MaterialTheme.typography.labelSmall,
                        color = HorusCyan
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "${progress.totalXp} XP • Amr Ahmed | عمرو أحمد",
                        style = MaterialTheme.typography.labelSmall,
                        color = HorusMutedIvory
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Replay Cinematic Intro button in Drawer
                    Button(
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            onReplayIntro()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = HorusElevatedNavy, contentColor = HorusGold),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("drawer_replay_intro_button")
                    ) {
                        Text("🎬", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isArabic) "العرض السينمائي (صحوة حورس)" else "Cinematic Intro",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                HorizontalDivider(color = HorusSubtleBorder.copy(alpha = 0.5f))

                Spacer(modifier = Modifier.height(8.dp))

                // Drawer Destinations List
                AppDestination.values().forEach { dest ->
                    val isSelected = currentDestination == dest
                    val label = when (dest) {
                        AppDestination.STUDIO -> HorusStrings.navStudio(isArabic)
                        AppDestination.LEVELS -> HorusStrings.navLevels(isArabic)
                        AppDestination.MAZE -> HorusStrings.navMaze(isArabic)
                        AppDestination.ACADEMY -> HorusStrings.navAcademy(isArabic)
                        AppDestination.DEBUG -> HorusStrings.navDebug(isArabic)
                        AppDestination.SYMBOLS -> HorusStrings.navSymbols(isArabic)
                        AppDestination.DASHBOARD -> HorusStrings.navDashboard(isArabic)
                        AppDestination.JUDGE -> HorusStrings.navJudge(isArabic)
                        AppDestination.EVALUATION -> HorusStrings.navEvaluation(isArabic)
                        AppDestination.SETTINGS -> HorusStrings.navSettings(isArabic)
                    }

                    NavigationDrawerItem(
                        icon = {
                            Text(dest.iconName, fontSize = 18.sp)
                        },
                        label = {
                            Text(label, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                        },
                        selected = isSelected,
                        onClick = {
                            currentDestination = dest
                            coroutineScope.launch { drawerState.close() }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = HorusElevatedNavy,
                            selectedTextColor = HorusCyan,
                            unselectedTextColor = HorusIvory
                        ),
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 2.dp)
                            .testTag("drawer_item_${dest.name}")
                    )
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("𓂀", fontSize = 20.sp, color = HorusGold)
                            Text(
                                text = if (isArabic) "شِفرة حورس" else "HORUS CODE",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = HorusIvory
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { coroutineScope.launch { drawerState.open() } },
                            modifier = Modifier.testTag("open_drawer_button")
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = HorusIvory)
                        }
                    },
                    actions = {
                        // Quick Replay Cinematic Intro button
                        IconButton(
                            onClick = onReplayIntro,
                            modifier = Modifier.testTag("topbar_replay_intro_button")
                        ) {
                            Text("🎬", fontSize = 16.sp)
                        }

                        // Quick Language Switcher
                        TextButton(
                            onClick = { progressManager.setLanguage(!isArabic) },
                            modifier = Modifier.testTag("topbar_language_toggle")
                        ) {
                            Text(
                                text = if (isArabic) "EN" else "عربي",
                                fontWeight = FontWeight.Bold,
                                color = HorusGold,
                                fontSize = 13.sp
                            )
                        }

                        // XP Badge
                        Box(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(HorusElevatedNavy)
                                .border(1.dp, HorusSubtleBorder, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${progress.totalXp} XP",
                                color = HorusLightGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = HorusMidnight
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = HorusMidnight,
                    contentColor = HorusIvory,
                    modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars).testTag("bottom_nav_bar")
                ) {
                    val bottomItems = listOf(
                        AppDestination.STUDIO,
                        AppDestination.LEVELS,
                        AppDestination.MAZE,
                        AppDestination.ACADEMY,
                        AppDestination.DASHBOARD
                    )

                    bottomItems.forEach { item ->
                        val isSelected = currentDestination == item
                        val label = when (item) {
                            AppDestination.STUDIO -> HorusStrings.navStudio(isArabic)
                            AppDestination.LEVELS -> HorusStrings.navLevels(isArabic)
                            AppDestination.MAZE -> HorusStrings.navMaze(isArabic)
                            AppDestination.ACADEMY -> HorusStrings.navAcademy(isArabic)
                            AppDestination.DASHBOARD -> HorusStrings.navDashboard(isArabic)
                            else -> item.name
                        }

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { currentDestination = item },
                            icon = {
                                Text(
                                    text = item.iconName,
                                    fontSize = if (isSelected) 20.sp else 16.sp
                                )
                            },
                            label = {
                                Text(
                                    text = label,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = HorusCyan,
                                selectedTextColor = HorusCyan,
                                indicatorColor = HorusElevatedNavy,
                                unselectedIconColor = HorusMutedIvory,
                                unselectedTextColor = HorusMutedIvory
                            ),
                            modifier = Modifier.testTag("nav_item_${item.name}")
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentDestination) {
                    AppDestination.STUDIO -> StudioScreen(progressManager, isArabic)
                    AppDestination.LEVELS -> LevelsScreen(progressManager, isArabic)
                    AppDestination.MAZE -> TempleMazeScreen(progressManager, isArabic)
                    AppDestination.ACADEMY -> AcademyScreen(progressManager, isArabic)
                    AppDestination.DEBUG -> DebuggingScreen(progressManager, isArabic)
                    AppDestination.SYMBOLS -> SymbolsScreen(isArabic)
                    AppDestination.DASHBOARD -> DashboardScreen(progressManager, isArabic)
                    AppDestination.JUDGE -> JudgeDemoScreen(progressManager, isArabic, onReplayIntro)
                    AppDestination.EVALUATION -> EvaluationScreen(progressManager, isArabic)
                    AppDestination.SETTINGS -> SettingsScreen(progressManager, isArabic, isDarkTheme, onReplayIntro)
                }
            }
        }
    }
}
