package com.example.ui.screens.athletes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Assessment
import com.example.ui.components.BiometricRadarChart
import com.example.ui.components.RadarAxisData
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyCard
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyDarkest
import com.example.ui.theme.StatusDeviation
import com.example.ui.theme.StatusFurtherAssessment
import com.example.ui.theme.StatusWithinBaseline
import com.example.ui.theme.TextMutedDark
import com.example.ui.theme.TextSecondaryDark
import com.example.ui.viewmodel.AssessmentViewModel
import com.example.ui.viewmodel.AthleteViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun AthleteDetailScreen(
    athleteId: Long,
    athleteViewModel: AthleteViewModel,
    assessmentViewModel: AssessmentViewModel,
    onBackClick: () -> Unit,
    onStartAssessment: (Long) -> Unit,
    onNavigateToBaseline: (Long) -> Unit,
    onAssessmentClick: (Long) -> Unit
) {
    LaunchedEffect(athleteId) {
        athleteViewModel.loadAthleteById(athleteId)
    }

    val athlete by athleteViewModel.selectedAthlete.collectAsState()
    val allAssessments by assessmentViewModel.allAssessments.collectAsState()
    val athleteAssessments = allAssessments.filter { it.athleteId == athleteId }

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Overview", "Baseline", "Assessments", "Trends")
    val dateFormat = SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault())

    if (athlete == null) {
        Box(
            modifier = Modifier.fillMaxSize().background(NavyDarkest),
            contentAlignment = Alignment.Center
        ) {
            Text("Athlete not found", color = Color.White)
        }
        return
    }

    val currentAthlete = athlete!!

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyDarkest)
            .padding(horizontal = 16.dp)
    ) {
        // Back Navigation
        item {
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                Text(
                    text = "Back to Roster",
                    color = TextSecondaryDark,
                    fontSize = 13.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Athlete Profile Hero Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(NavyCard)
                    .border(1.dp, NavyBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(CyanPrimary.copy(alpha = 0.15f))
                                    .border(2.dp, CyanPrimary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                val initials = currentAthlete.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("")
                                Text(
                                    text = initials,
                                    color = CyanPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Text(
                                    text = currentAthlete.name,
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = "${currentAthlete.sport} • Age ${currentAthlete.age} • #${currentAthlete.jerseyNumber.ifEmpty { "10" }}",
                                    color = CyanPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        StatusBadge(status = currentAthlete.latestStatus)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Quick buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onStartAssessment(currentAthlete.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Analytics, contentDescription = null, tint = NavyDarkest, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Start Analysis", color = NavyDarkest, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = { onNavigateToBaseline(currentAthlete.id) },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Speed, contentDescription = null, tint = CyanPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (currentAthlete.hasBaseline) "Update Baseline" else "Create Baseline", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Navigation Tabs (Overview, Baseline, Assessments, Trends)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(NavyDark)
                    .border(1.dp, NavyBorder, RoundedCornerShape(10.dp))
                    .padding(3.dp)
            ) {
                tabs.forEachIndexed { index, tabName ->
                    val isSelected = selectedTab == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) CyanPrimary else Color.Transparent)
                            .clickable { selectedTab = index }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tabName,
                            color = if (isSelected) NavyDarkest else TextSecondaryDark,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Tab Content
        when (selectedTab) {
            0 -> {
                // OVERVIEW
                item {
                    // Biometric Radar Comparison
                    val latestAssess = athleteAssessments.firstOrNull()
                    val radarAxes = listOf(
                        RadarAxisData("Eye Track", currentAthlete.baselineEyeStability, latestAssess?.eyeStability ?: currentAthlete.baselineEyeStability),
                        RadarAxisData("Balance", currentAthlete.baselineBalanceStability, latestAssess?.balanceStability ?: currentAthlete.baselineBalanceStability),
                        RadarAxisData("Gait", currentAthlete.baselineGaitConsistency, latestAssess?.gaitConsistency ?: currentAthlete.baselineGaitConsistency),
                        RadarAxisData("Symmetry", currentAthlete.baselineMovementSymmetry, latestAssess?.movementSymmetry ?: currentAthlete.baselineMovementSymmetry),
                        RadarAxisData("Reaction", (100f - ((currentAthlete.baselineReactionTimeMs - 200f) / 4f)).coerceIn(10f, 100f), (100f - (((latestAssess?.reactionTimeMs ?: currentAthlete.baselineReactionTimeMs) - 200f) / 4f)).coerceIn(10f, 100f)),
                        RadarAxisData("Head Fix", currentAthlete.baselineHeadStability, latestAssess?.headStability ?: currentAthlete.baselineHeadStability)
                    )

                    BiometricRadarChart(
                        axes = radarAxes,
                        currentStatusColor = currentAthlete.latestStatus.color
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Text(
                        text = "Recent Assessments for ${currentAthlete.name}",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                if (athleteAssessments.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(NavyCard)
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No screening assessments recorded for this athlete yet.", color = TextMutedDark, fontSize = 12.sp)
                        }
                    }
                } else {
                    items(athleteAssessments) { item ->
                        AssessmentRowItem(assessment = item, dateFormat = dateFormat, onClick = { onAssessmentClick(item.id) })
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            1 -> {
                // BASELINE
                item {
                    if (currentAthlete.hasBaseline) {
                        BaselineMetricsSummary(athlete = currentAthlete)
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(NavyCard)
                                .border(1.dp, NavyBorder, RoundedCornerShape(14.dp))
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = StatusDeviation, modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.height(10.dp))
                                Text("No Healthy Baseline Established", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Record a baseline while the athlete is healthy and symptom-free to enable comparative screening.", color = TextSecondaryDark, fontSize = 12.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { onNavigateToBaseline(currentAthlete.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
                                ) {
                                    Text("Calibrate Healthy Baseline", color = NavyDarkest, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                // ASSESSMENTS LIST
                if (athleteAssessments.isEmpty()) {
                    item {
                        Text("No assessment history available.", color = TextMutedDark, fontSize = 13.sp)
                    }
                } else {
                    items(athleteAssessments) { item ->
                        AssessmentRowItem(assessment = item, dateFormat = dateFormat, onClick = { onAssessmentClick(item.id) })
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            3 -> {
                // TRENDS
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(NavyCard)
                            .border(1.dp, NavyBorder, RoundedCornerShape(14.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Text("Longitudinal Stability Trends", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Tracking biometric changes across ${athleteAssessments.size} sessions", color = TextSecondaryDark, fontSize = 11.sp)
                            Spacer(modifier = Modifier.height(14.dp))

                            athleteAssessments.forEach { assess ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(dateFormat.format(Date(assess.timestamp)), color = TextMutedDark, fontSize = 11.sp)
                                    Text("Rx: ${assess.reactionTimeMs}ms • Eye: ${assess.eyeStability.roundToInt()}%", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                    Text("${if (assess.overallDeviationPercent > 0) "+" else ""}${assess.overallDeviationPercent.roundToInt()}% dev", color = assess.status.color, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun BaselineMetricsSummary(athlete: com.example.data.model.Athlete) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(NavyCard)
            .border(1.dp, NavyBorder, RoundedCornerShape(14.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Healthy Baseline Metrics", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(StatusWithinBaseline.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text("ACTIVE BASELINE", color = StatusWithinBaseline, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            BaselineRow(label = "Eye Movement Stability", value = "${athlete.baselineEyeStability.roundToInt()}%", icon = Icons.Default.RemoveRedEye)
            BaselineRow(label = "Postural Balance Stability", value = "${athlete.baselineBalanceStability.roundToInt()}%", icon = Icons.Default.Shield)
            BaselineRow(label = "Walking Gait Consistency", value = "${athlete.baselineGaitConsistency.roundToInt()}%", icon = Icons.Default.DirectionsWalk)
            BaselineRow(label = "Movement Bilateral Symmetry", value = "${athlete.baselineMovementSymmetry.roundToInt()}%", icon = Icons.Default.Analytics)
            BaselineRow(label = "Reaction Time Benchmark", value = "${athlete.baselineReactionTimeMs} ms", icon = Icons.Default.FlashOn)
            BaselineRow(label = "Head / Cervical Fixation", value = "${athlete.baselineHeadStability.roundToInt()}%", icon = Icons.Default.Speed)
        }
    }
}

@Composable
private fun BaselineRow(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = CyanPrimary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, color = TextSecondaryDark, fontSize = 13.sp)
        }
        Text(text = value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun AssessmentRowItem(
    assessment: Assessment,
    dateFormat: SimpleDateFormat,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(NavyCard)
            .border(1.dp, NavyBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = assessment.type.label,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = dateFormat.format(Date(assessment.timestamp)),
                    color = TextMutedDark,
                    fontSize = 11.sp
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                StatusBadge(status = assessment.status)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = TextMutedDark,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
