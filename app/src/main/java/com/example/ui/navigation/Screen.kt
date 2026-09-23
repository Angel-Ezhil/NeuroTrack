package com.example.ui.navigation

sealed class Screen(val route: String) {
    object Landing : Screen("landing")
    object Login : Screen("login")
    object Dashboard : Screen("dashboard")
    object Athletes : Screen("athletes")
    object AthleteDetail : Screen("athlete_detail/{athleteId}") {
        fun createRoute(athleteId: Long) = "athlete_detail/$athleteId"
    }
    object Baseline : Screen("baseline?athleteId={athleteId}") {
        fun createRoute(athleteId: Long? = null) = if (athleteId != null) "baseline?athleteId=$athleteId" else "baseline"
    }
    object AnalyzeSetup : Screen("analyze_setup?athleteId={athleteId}") {
        fun createRoute(athleteId: Long? = null) = if (athleteId != null) "analyze_setup?athleteId=$athleteId" else "analyze_setup"
    }
    object AnalyzeBefore : Screen("analyze_before/{athleteId}") {
        fun createRoute(athleteId: Long) = "analyze_before/$athleteId"
    }
    object AnalyzeAfter : Screen("analyze_after/{athleteId}") {
        fun createRoute(athleteId: Long) = "analyze_after/$athleteId"
    }
    object AnalyzeProcessing : Screen("analyze_processing/{athleteId}") {
        fun createRoute(athleteId: Long) = "analyze_processing/$athleteId"
    }
    object AnalyzeComparison : Screen("analyze_comparison/{assessmentId}") {
        fun createRoute(assessmentId: Long) = "analyze_comparison/$assessmentId"
    }
    object Results : Screen("results/{assessmentId}") {
        fun createRoute(assessmentId: Long) = "results/$assessmentId"
    }
    object History : Screen("history")
    object Reports : Screen("reports?assessmentId={assessmentId}") {
        fun createRoute(assessmentId: Long? = null) = if (assessmentId != null) "reports?assessmentId=$assessmentId" else "reports"
    }
    object Settings : Screen("settings")
}
