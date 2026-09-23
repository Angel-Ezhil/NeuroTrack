package com.example.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.ui.components.NeuroBottomNav
import com.example.ui.components.NeuroNavigationRail
import com.example.ui.components.NeuroTopBar
import com.example.ui.screens.analysis.AnalyzeAfterVideoScreen
import com.example.ui.screens.analysis.AnalyzeBeforeVideoScreen
import com.example.ui.screens.analysis.AnalyzeComparisonScreen
import com.example.ui.screens.analysis.AnalyzeProcessingScreen
import com.example.ui.screens.analysis.AnalyzeSetupScreen
import com.example.ui.screens.athletes.AthleteDetailScreen
import com.example.ui.screens.athletes.AthletesScreen
import com.example.ui.screens.auth.AuthScreen
import com.example.ui.screens.baseline.BaselineScreen
import com.example.ui.screens.dashboard.DashboardScreen
import com.example.ui.screens.history.HistoryScreen
import com.example.ui.screens.landing.LandingScreen
import com.example.ui.screens.reports.ReportsScreen
import com.example.ui.screens.results.ResultsScreen
import com.example.ui.screens.settings.SettingsScreen
import com.example.ui.theme.NavyDarkest
import com.example.ui.viewmodel.AssessmentViewModel
import com.example.ui.viewmodel.AthleteViewModel
import com.example.ui.viewmodel.AuthViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    athleteViewModel: AthleteViewModel,
    assessmentViewModel: AssessmentViewModel
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()
    val isDemoMode by assessmentViewModel.isDemoMode.collectAsState()

    // Determine if TopBar and BottomNav should be visible
    val isPublicScreen = currentRoute == Screen.Landing.route || currentRoute == Screen.Login.route
    val showScaffoldBars = isAuthenticated && !isPublicScreen

    val handleLogout: () -> Unit = {
        authViewModel.logout {
            navController.navigate(Screen.Landing.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(NavyDarkest)) {
        val isWidescreen = maxWidth >= 768.dp

        if (isWidescreen && showScaffoldBars) {
            // WIDESCREEN / LAPTOP DESKTOP LAYOUT (Side Rail + Main Canvas)
            Row(modifier = Modifier.fillMaxSize()) {
                NeuroNavigationRail(
                    currentRoute = currentRoute,
                    onNavigate = { targetRoute ->
                        navController.navigate(targetRoute) {
                            popUpTo(Screen.Dashboard.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    currentUser = currentUser,
                    isDemoMode = isDemoMode,
                    onLogoutClick = handleLogout
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    val title = when {
                        currentRoute?.startsWith("dashboard") == true -> "NEUROTRACK AI"
                        currentRoute?.startsWith("athletes") == true -> "ATHLETE ROSTER"
                        currentRoute?.startsWith("athlete_detail") == true -> "ATHLETE PROFILE"
                        currentRoute?.startsWith("baseline") == true -> "BASELINE CALIBRATION"
                        currentRoute?.startsWith("analyze") == true -> "NEUROTRACK ANALYSIS"
                        currentRoute?.startsWith("results") == true -> "SCREENING RESULTS"
                        currentRoute?.startsWith("history") == true -> "ASSESSMENT HISTORY"
                        currentRoute?.startsWith("reports") == true -> "CLINICAL REPORT"
                        currentRoute?.startsWith("settings") == true -> "SYSTEM SETTINGS"
                        else -> "NEUROTRACK AI"
                    }

                    val showBack = currentRoute?.startsWith("athlete_detail") == true ||
                            currentRoute?.startsWith("analyze_") == true ||
                            currentRoute?.startsWith("results") == true

                    NeuroTopBar(
                        title = title,
                        subtitle = if (currentRoute?.startsWith("dashboard") == true) "Sports-Health Technology Prototype" else null,
                        showBackButton = showBack,
                        onBackClick = { navController.popBackStack() },
                        currentUser = currentUser,
                        isDemoMode = isDemoMode,
                        onLogoutClick = handleLogout
                    )

                    Box(modifier = Modifier.weight(1f).fillMaxSize()) {
                        MainNavigationContent(
                            navController = navController,
                            authViewModel = authViewModel,
                            athleteViewModel = athleteViewModel,
                            assessmentViewModel = assessmentViewModel,
                            currentUser = currentUser
                        )
                    }
                }
            }
        } else {
            // MOBILE / COMPACT SCREEN LAYOUT (Top Bar + Bottom Bar)
            Scaffold(
                topBar = {
                    if (showScaffoldBars) {
                        val title = when {
                            currentRoute?.startsWith("dashboard") == true -> "NEUROTRACK AI"
                            currentRoute?.startsWith("athletes") == true -> "ATHLETE ROSTER"
                            currentRoute?.startsWith("athlete_detail") == true -> "ATHLETE PROFILE"
                            currentRoute?.startsWith("baseline") == true -> "BASELINE CALIBRATION"
                            currentRoute?.startsWith("analyze") == true -> "NEUROTRACK ANALYSIS"
                            currentRoute?.startsWith("results") == true -> "SCREENING RESULTS"
                            currentRoute?.startsWith("history") == true -> "ASSESSMENT HISTORY"
                            currentRoute?.startsWith("reports") == true -> "CLINICAL REPORT"
                            currentRoute?.startsWith("settings") == true -> "SYSTEM SETTINGS"
                            else -> "NEUROTRACK AI"
                        }

                        val showBack = currentRoute?.startsWith("athlete_detail") == true ||
                                currentRoute?.startsWith("analyze_") == true ||
                                currentRoute?.startsWith("results") == true

                        NeuroTopBar(
                            title = title,
                            subtitle = if (currentRoute?.startsWith("dashboard") == true) "Sports-Health Technology Prototype" else null,
                            showBackButton = showBack,
                            onBackClick = { navController.popBackStack() },
                            currentUser = currentUser,
                            isDemoMode = isDemoMode,
                            onLogoutClick = handleLogout
                        )
                    }
                },
                bottomBar = {
                    if (showScaffoldBars) {
                        NeuroBottomNav(
                            currentRoute = currentRoute,
                            onNavigate = { targetRoute ->
                                navController.navigate(targetRoute) {
                                    popUpTo(Screen.Dashboard.route) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    MainNavigationContent(
                        navController = navController,
                        authViewModel = authViewModel,
                        athleteViewModel = athleteViewModel,
                        assessmentViewModel = assessmentViewModel,
                        currentUser = currentUser
                    )
                }
            }
        }
    }
}

@Composable
private fun MainNavigationContent(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    athleteViewModel: AthleteViewModel,
    assessmentViewModel: AssessmentViewModel,
    currentUser: com.example.data.model.User?
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Landing.route
    ) {
        // 1. Landing Screen (Strict entry point)
        composable(Screen.Landing.route) {
            LandingScreen(
                onGetStartedClick = { navController.navigate(Screen.Login.route) },
                onLoginClick = { navController.navigate(Screen.Login.route) }
            )
        }

        // 2. Auth Screen
        composable(Screen.Login.route) {
            AuthScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Landing.route) { inclusive = false }
                    }
                },
                onBackToLanding = { navController.navigate(Screen.Landing.route) }
            )
        }

        // 3. Dashboard
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                currentUser = currentUser,
                athleteViewModel = athleteViewModel,
                assessmentViewModel = assessmentViewModel,
                onNavigateToAddAthlete = { navController.navigate(Screen.Athletes.route) },
                onNavigateToBaseline = { navController.navigate(Screen.Baseline.createRoute()) },
                onNavigateToAnalyze = { navController.navigate(Screen.AnalyzeSetup.createRoute()) },
                onNavigateToHistory = { navController.navigate(Screen.History.route) },
                onAssessmentClick = { assessmentId ->
                    navController.navigate(Screen.Results.createRoute(assessmentId))
                }
            )
        }

        // 4. Athletes List
        composable(Screen.Athletes.route) {
            AthletesScreen(
                athleteViewModel = athleteViewModel,
                onAthleteClick = { athleteId ->
                    navController.navigate(Screen.AthleteDetail.createRoute(athleteId))
                },
                onStartAssessmentForAthlete = { athleteId ->
                    navController.navigate(Screen.AnalyzeSetup.createRoute(athleteId))
                }
            )
        }

        // 5. Athlete Detail
        composable(
            route = Screen.AthleteDetail.route,
            arguments = listOf(navArgument("athleteId") { type = NavType.LongType })
        ) { backStackEntry ->
            val athleteId = backStackEntry.arguments?.getLong("athleteId") ?: 1L
            AthleteDetailScreen(
                athleteId = athleteId,
                athleteViewModel = athleteViewModel,
                assessmentViewModel = assessmentViewModel,
                onBackClick = { navController.popBackStack() },
                onStartAssessment = { id ->
                    navController.navigate(Screen.AnalyzeSetup.createRoute(id))
                },
                onNavigateToBaseline = { id ->
                    navController.navigate(Screen.Baseline.createRoute(id))
                },
                onAssessmentClick = { assessId ->
                    navController.navigate(Screen.Results.createRoute(assessId))
                }
            )
        }

        // 6. Baseline Calibration
        composable(
            route = Screen.Baseline.route,
            arguments = listOf(navArgument("athleteId") {
                type = NavType.LongType
                defaultValue = -1L
            })
        ) { backStackEntry ->
            val athleteIdArg = backStackEntry.arguments?.getLong("athleteId")
            val athleteId = if (athleteIdArg != null && athleteIdArg > 0) athleteIdArg else null
            BaselineScreen(
                preselectedAthleteId = athleteId,
                athleteViewModel = athleteViewModel,
                assessmentViewModel = assessmentViewModel,
                onBackClick = { navController.popBackStack() },
                onBaselineCompleted = { createdAssessmentId ->
                    navController.navigate(Screen.Results.createRoute(createdAssessmentId)) {
                        popUpTo(Screen.Dashboard.route)
                    }
                }
            )
        }

        // 7. Analyze Setup
        composable(
            route = Screen.AnalyzeSetup.route,
            arguments = listOf(navArgument("athleteId") {
                type = NavType.LongType
                defaultValue = -1L
            })
        ) { backStackEntry ->
            val athleteIdArg = backStackEntry.arguments?.getLong("athleteId")
            val athleteId = if (athleteIdArg != null && athleteIdArg > 0) athleteIdArg else null
            AnalyzeSetupScreen(
                preselectedAthleteId = athleteId,
                athleteViewModel = athleteViewModel,
                assessmentViewModel = assessmentViewModel,
                onBackClick = { navController.popBackStack() },
                onProceedToBeforeVideo = { selectedId ->
                    navController.navigate(Screen.AnalyzeBefore.createRoute(selectedId))
                }
            )
        }

        // 8. Analyze: Before Video
        composable(
            route = Screen.AnalyzeBefore.route,
            arguments = listOf(navArgument("athleteId") { type = NavType.LongType })
        ) { backStackEntry ->
            val athleteId = backStackEntry.arguments?.getLong("athleteId") ?: 1L
            AnalyzeBeforeVideoScreen(
                athleteId = athleteId,
                athleteViewModel = athleteViewModel,
                assessmentViewModel = assessmentViewModel,
                onBackClick = { navController.popBackStack() },
                onProceedToAfterVideo = { id ->
                    navController.navigate(Screen.AnalyzeAfter.createRoute(id))
                }
            )
        }

        // 9. Analyze: After Video
        composable(
            route = Screen.AnalyzeAfter.route,
            arguments = listOf(navArgument("athleteId") { type = NavType.LongType })
        ) { backStackEntry ->
            val athleteId = backStackEntry.arguments?.getLong("athleteId") ?: 1L
            AnalyzeAfterVideoScreen(
                athleteId = athleteId,
                athleteViewModel = athleteViewModel,
                assessmentViewModel = assessmentViewModel,
                onBackClick = { navController.popBackStack() },
                onProceedToProcessing = { id ->
                    navController.navigate(Screen.AnalyzeProcessing.createRoute(id))
                }
            )
        }

        // 10. Analyze: Processing Pipeline
        composable(
            route = Screen.AnalyzeProcessing.route,
            arguments = listOf(navArgument("athleteId") { type = NavType.LongType })
        ) { backStackEntry ->
            val athleteId = backStackEntry.arguments?.getLong("athleteId") ?: 1L
            AnalyzeProcessingScreen(
                athleteId = athleteId,
                assessmentViewModel = assessmentViewModel,
                onProcessingFinished = { assessmentId ->
                    navController.navigate(Screen.AnalyzeComparison.createRoute(assessmentId)) {
                        popUpTo(Screen.AnalyzeSetup.route) { inclusive = true }
                    }
                }
            )
        }

        // 11. Analyze: Comparison
        composable(
            route = Screen.AnalyzeComparison.route,
            arguments = listOf(navArgument("assessmentId") { type = NavType.LongType })
        ) { backStackEntry ->
            val assessmentId = backStackEntry.arguments?.getLong("assessmentId") ?: 101L
            AnalyzeComparisonScreen(
                assessmentId = assessmentId,
                assessmentViewModel = assessmentViewModel,
                onBackClick = { navController.popBackStack() },
                onViewResults = { id ->
                    navController.navigate(Screen.Results.createRoute(id))
                }
            )
        }

        // 12. Screening Results
        composable(
            route = Screen.Results.route,
            arguments = listOf(navArgument("assessmentId") { type = NavType.LongType })
        ) { backStackEntry ->
            val assessmentId = backStackEntry.arguments?.getLong("assessmentId") ?: 101L
            ResultsScreen(
                assessmentId = assessmentId,
                assessmentViewModel = assessmentViewModel,
                onBackClick = { navController.popBackStack() },
                onReturnToDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                },
                onNavigateToReports = { id ->
                    navController.navigate(Screen.Reports.createRoute(id))
                }
            )
        }

        // 13. History
        composable(Screen.History.route) {
            HistoryScreen(
                assessmentViewModel = assessmentViewModel,
                onAssessmentClick = { assessmentId ->
                    navController.navigate(Screen.Results.createRoute(assessmentId))
                }
            )
        }

        // 14. Reports Dossier
        composable(
            route = Screen.Reports.route,
            arguments = listOf(navArgument("assessmentId") {
                type = NavType.LongType
                defaultValue = -1L
            })
        ) { backStackEntry ->
            val assessmentIdArg = backStackEntry.arguments?.getLong("assessmentId")
            val assessmentId = if (assessmentIdArg != null && assessmentIdArg > 0) assessmentIdArg else null
            ReportsScreen(
                preselectedAssessmentId = assessmentId,
                assessmentViewModel = assessmentViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        // 15. Settings
        composable(Screen.Settings.route) {
            SettingsScreen(
                currentUser = currentUser,
                authViewModel = authViewModel,
                assessmentViewModel = assessmentViewModel,
                onLogout = {
                    navController.navigate(Screen.Landing.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}

