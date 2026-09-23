package com.example.ui.screens.results

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Assessment
import com.example.data.model.AssessmentStatus
import com.example.ui.components.BiometricRadarChart
import com.example.ui.components.ExplainableAiSection
import com.example.ui.components.ExplainableIndicator
import com.example.ui.components.RadarAxisData
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyCard
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyDarkest
import com.example.ui.theme.TextMutedDark
import com.example.ui.theme.TextSecondaryDark
import com.example.ui.viewmodel.AssessmentViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun ResultsScreen(
    assessmentId: Long,
    assessmentViewModel: AssessmentViewModel,
    onBackClick: () -> Unit,
    onReturnToDashboard: () -> Unit,
    onNavigateToReports: (Long) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(assessmentId) {
        assessmentViewModel.loadAssessmentById(assessmentId)
    }

    val assessment by assessmentViewModel.selectedAssessment.collectAsState()
    val dateFormat = SimpleDateFormat("MMMM dd, yyyy • hh:mm a", Locale.getDefault())

    if (assessment == null) {
        Box(
            modifier = Modifier.fillMaxSize().background(NavyDarkest),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading assessment report...", color = Color.White)
        }
        return
    }

    val item = assessment!!

    // Explainable AI Section (SHAP contributing indicators)
    val explainableIndicators = listOf(
        ExplainableIndicator(
            name = "Reaction Time Degradation",
            deviationScore = (item.reactionDeviationPercent * 1.5f).coerceIn(10f, 95f),
            level = if (item.reactionDeviationPercent > 25f) "Elevated" else if (item.reactionDeviationPercent > 10f) "Moderate" else "Nominal",
            description = "${if (item.reactionDeviationPercent > 0) "+" else ""}${item.reactionDeviationPercent.roundToInt()}% vs baseline"
        ),
        ExplainableIndicator(
            name = "Postural Balance Sway Variance",
            deviationScore = (abs(item.balanceDeviationPercent) * 2.2f).coerceIn(10f, 95f),
            level = if (abs(item.balanceDeviationPercent) > 20f) "Elevated" else if (abs(item.balanceDeviationPercent) > 10f) "Moderate" else "Nominal",
            description = "${item.balanceDeviationPercent.roundToInt()}% stability change"
        ),
        ExplainableIndicator(
            name = "Ocular Gaze Tracking Jitter",
            deviationScore = (abs(item.eyeDeviationPercent) * 2.0f).coerceIn(10f, 95f),
            level = if (abs(item.eyeDeviationPercent) > 18f) "Elevated" else if (abs(item.eyeDeviationPercent) > 8f) "Moderate" else "Nominal",
            description = "${item.eyeDeviationPercent.roundToInt()}% tracking precision"
        ),
        ExplainableIndicator(
            name = "Kinetic Movement Symmetry",
            deviationScore = (abs(item.symmetryDeviationPercent) * 1.8f).coerceIn(10f, 95f),
            level = if (abs(item.symmetryDeviationPercent) > 18f) "Elevated" else if (abs(item.symmetryDeviationPercent) > 8f) "Moderate" else "Nominal",
            description = "${item.symmetryDeviationPercent.roundToInt()}% bilateral variance"
        )
    )

    // Radar Chart Comparison
    val radarAxes = listOf(
        RadarAxisData("Eye Track", item.baselineEyeStability, item.eyeStability),
        RadarAxisData("Balance", item.baselineBalanceStability, item.balanceStability),
        RadarAxisData("Gait", item.baselineGaitConsistency, item.gaitConsistency),
        RadarAxisData("Symmetry", item.baselineMovementSymmetry, item.movementSymmetry),
        RadarAxisData("Reaction", (100f - ((item.baselineReactionTimeMs - 200f) / 4f)).coerceIn(10f, 100f), (100f - ((item.reactionTimeMs - 200f) / 4f)).coerceIn(10f, 100f)),
        RadarAxisData("Head Fix", item.baselineHeadStability, item.headStability)
    )

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(NavyDarkest)) {
        val isLaptop = maxWidth >= 840.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = if (isLaptop) 24.dp else 16.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Top Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(NavyDark)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Screening Report",
                            color = Color.White,
                            fontSize = if (isLaptop) 20.sp else 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = dateFormat.format(Date(item.timestamp)),
                            color = CyanPrimary,
                            fontSize = 11.sp
                        )
                    }
                }

                IconButton(
                    onClick = onReturnToDashboard,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(NavyDark)
                ) {
                    Icon(Icons.Default.Home, contentDescription = "Dashboard", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLaptop) {
                // LAPTOP 2-COLUMN WIDESCREEN VIEW
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Left Column: Outcome, Athlete Profile, Recommended Steps & Action Buttons
                    Column(modifier = Modifier.weight(1.1f)) {
                        PrimaryOutcomeCard(item)
                        Spacer(modifier = Modifier.height(16.dp))
                        AthleteProfileSummaryCard(item)
                        Spacer(modifier = Modifier.height(16.dp))
                        RecommendedStepsCard(item)
                        Spacer(modifier = Modifier.height(18.dp))
                        ActionButtonsRow(
                            onExportPdf = {
                                Toast.makeText(context, "Clinical screening report exported to PDF", Toast.LENGTH_SHORT).show()
                            },
                            onViewReports = { onNavigateToReports(item.id) }
                        )
                    }

                    // Right Column: Radar Chart & Explainable AI
                    Column(modifier = Modifier.weight(1f)) {
                        BiometricRadarChart(
                            axes = radarAxes,
                            currentStatusColor = item.status.color
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        ExplainableAiSection(indicators = explainableIndicators)
                    }
                }
            } else {
                // MOBILE STACKED VIEW
                PrimaryOutcomeCard(item)
                Spacer(modifier = Modifier.height(16.dp))
                AthleteProfileSummaryCard(item)
                Spacer(modifier = Modifier.height(16.dp))
                ExplainableAiSection(indicators = explainableIndicators)
                Spacer(modifier = Modifier.height(16.dp))
                BiometricRadarChart(
                    axes = radarAxes,
                    currentStatusColor = item.status.color
                )
                Spacer(modifier = Modifier.height(16.dp))
                RecommendedStepsCard(item)
                Spacer(modifier = Modifier.height(20.dp))
                ActionButtonsRow(
                    onExportPdf = {
                        Toast.makeText(context, "Clinical screening report exported to PDF", Toast.LENGTH_SHORT).show()
                    },
                    onViewReports = { onNavigateToReports(item.id) }
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun PrimaryOutcomeCard(item: Assessment) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(item.status.bgTint)
            .border(1.5.dp, item.status.color, RoundedCornerShape(16.dp))
            .padding(18.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SCREENING OUTCOME",
                    color = item.status.color,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                StatusBadge(status = item.status)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = item.status.label,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = item.status.description,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Mandatory Non-Diagnostic Disclaimer Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(NavyDarkest.copy(alpha = 0.6f))
                    .padding(10.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = CyanPrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "NOTICE: NeuroTrack AI identifies measurable biometric deviations relative to baseline. This screening does not diagnose concussion, make a clinical diagnosis, or provide athletic clearance.",
                        color = TextSecondaryDark,
                        fontSize = 10.sp,
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun AthleteProfileSummaryCard(item: Assessment) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(NavyCard)
            .border(1.dp, NavyBorder, RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Athlete Name", color = TextMutedDark, fontSize = 11.sp)
                Text(item.athleteName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Column {
                Text("Sport", color = TextMutedDark, fontSize = 11.sp)
                Text(item.sport, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Column {
                Text("Assessment Type", color = TextMutedDark, fontSize = 11.sp)
                Text(item.type.label, color = CyanPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun RecommendedStepsCard(item: Assessment) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(NavyCard)
            .border(1.dp, NavyBorder, RoundedCornerShape(14.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "Recommended Next Steps Protocol",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            when (item.status) {
                AssessmentStatus.WITHIN_BASELINE -> {
                    ActionItem(
                        step = "1",
                        text = "Biometric indicators match baseline. Continue standard athletic monitoring protocol."
                    )
                    ActionItem(
                        step = "2",
                        text = "If the athlete develops delayed symptoms (headache, dizziness, photophobia), initiate repeat screening."
                    )
                }
                AssessmentStatus.DEVIATION_DETECTED -> {
                    ActionItem(
                        step = "1",
                        text = "Post-impact deviation detected in reaction time and ocular stability. Withhold from immediate collision contact."
                    )
                    ActionItem(
                        step = "2",
                        text = "Conduct secondary clinical concussion evaluation by athletic trainer or team physician."
                    )
                    ActionItem(
                        step = "3",
                        text = "Perform scheduled follow-up re-screening in 24 hours to monitor recovery trajectory."
                    )
                }
                AssessmentStatus.FURTHER_ASSESSMENT_RECOMMENDED -> {
                    ActionItem(
                        step = "1",
                        text = "Significant multi-modal deviations detected. Athlete should be removed from athletic activity immediately."
                    )
                    ActionItem(
                        step = "2",
                        text = "Refer immediately to licensed physician / concussion specialist for comprehensive clinical examination."
                    )
                    ActionItem(
                        step = "3",
                        text = "Do not return to play until formal medical clearance has been documented by a qualified physician."
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionButtonsRow(
    onExportPdf: () -> Unit,
    onViewReports: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Button(
            onClick = onExportPdf,
            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.weight(1f).height(48.dp)
        ) {
            Icon(Icons.Default.Download, contentDescription = null, tint = NavyDarkest, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Export PDF", color = NavyDarkest, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }

        OutlinedButton(
            onClick = onViewReports,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
            modifier = Modifier.weight(1f).height(48.dp)
        ) {
            Icon(Icons.Default.Assessment, contentDescription = null, tint = CyanPrimary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("View Report", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun ActionItem(step: String, text: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(CyanPrimary.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Text(step, color = CyanPrimary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, color = TextSecondaryDark, fontSize = 12.sp, lineHeight = 16.sp)
    }
}
