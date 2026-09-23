package com.example.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Assessment
import com.example.data.model.AssessmentStatus
import com.example.data.model.User
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CyanGlow
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
fun DashboardScreen(
    currentUser: User?,
    athleteViewModel: AthleteViewModel,
    assessmentViewModel: AssessmentViewModel,
    onNavigateToAddAthlete: () -> Unit,
    onNavigateToBaseline: () -> Unit,
    onNavigateToAnalyze: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onAssessmentClick: (Long) -> Unit
) {
    val athletes by athleteViewModel.athletes.collectAsState()
    val assessments by assessmentViewModel.allAssessments.collectAsState()

    val totalAthletes = athletes.size
    val completedAssessments = assessments.size
    val baselineProfiles = athletes.count { it.hasBaseline }
    val requiringReview = assessments.count { it.status != AssessmentStatus.WITHIN_BASELINE }

    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(NavyDarkest)) {
        val isLaptop = maxWidth >= 840.dp

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = if (isLaptop) 24.dp else 16.dp)
        ) {
            // 1. Header Greeting
            item {
                Spacer(modifier = Modifier.height(18.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Good Morning, ${currentUser?.fullName ?: "Sports Trainer"}",
                            color = Color.White,
                            fontSize = if (isLaptop) 24.sp else 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Monitor athlete kinematic assessments and identify meaningful deviations from baseline.",
                            color = TextSecondaryDark,
                            fontSize = 13.sp
                        )
                    }

                    if (isLaptop) {
                        Button(
                            onClick = onNavigateToAnalyze,
                            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Analytics, contentDescription = null, tint = NavyDarkest, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("New Screening", color = NavyDarkest, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // 2. Top 4 Metric Cards (Single 4-column row on Laptop)
            item {
                if (isLaptop) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        DashboardMetricCard(
                            title = "Total Athletes",
                            value = "$totalAthletes",
                            subtitle = "Roster registered",
                            icon = Icons.Default.People,
                            color = CyanPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        DashboardMetricCard(
                            title = "Assessments",
                            value = "$completedAssessments",
                            subtitle = "Screenings logged",
                            icon = Icons.Default.Assessment,
                            color = CyanGlow,
                            modifier = Modifier.weight(1f)
                        )
                        DashboardMetricCard(
                            title = "Baseline Profiles",
                            value = "$baselineProfiles",
                            subtitle = "${if (totalAthletes > 0) (baselineProfiles * 100 / totalAthletes) else 0}% calibrated",
                            icon = Icons.Default.Speed,
                            color = StatusWithinBaseline,
                            modifier = Modifier.weight(1f)
                        )
                        DashboardMetricCard(
                            title = "Requiring Review",
                            value = "$requiringReview",
                            subtitle = "Deviations detected",
                            icon = Icons.Default.Analytics,
                            color = if (requiringReview > 0) StatusFurtherAssessment else StatusWithinBaseline,
                            modifier = Modifier.weight(1f)
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        DashboardMetricCard(
                            title = "Total Athletes",
                            value = "$totalAthletes",
                            subtitle = "Registered",
                            icon = Icons.Default.People,
                            color = CyanPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        DashboardMetricCard(
                            title = "Assessments",
                            value = "$completedAssessments",
                            subtitle = "Completed",
                            icon = Icons.Default.Assessment,
                            color = CyanGlow,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        DashboardMetricCard(
                            title = "Baseline Profiles",
                            value = "$baselineProfiles",
                            subtitle = "${if (totalAthletes > 0) (baselineProfiles * 100 / totalAthletes) else 0}% calibrated",
                            icon = Icons.Default.Speed,
                            color = StatusWithinBaseline,
                            modifier = Modifier.weight(1f)
                        )
                        DashboardMetricCard(
                            title = "Requiring Review",
                            value = "$requiringReview",
                            subtitle = "Deviations",
                            icon = Icons.Default.Analytics,
                            color = if (requiringReview > 0) StatusFurtherAssessment else StatusWithinBaseline,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // 3. Desktop 2-Column Split or Mobile Stack
            if (isLaptop) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Left Column: Quick Actions & Benchmarks & Protocol Notice
                        Column(modifier = Modifier.weight(1.2f)) {
                            Text(
                                text = "Quick Actions",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                QuickActionButton(
                                    title = "Start Analysis",
                                    subtitle = "Before vs After",
                                    icon = Icons.Default.Analytics,
                                    isPrimary = true,
                                    onClick = onNavigateToAnalyze,
                                    modifier = Modifier.weight(1f)
                                )
                                QuickActionButton(
                                    title = "Create Baseline",
                                    subtitle = "Calibrate healthy",
                                    icon = Icons.Default.Speed,
                                    isPrimary = false,
                                    onClick = onNavigateToBaseline,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                QuickActionButton(
                                    title = "Add Athlete",
                                    subtitle = "Create profile",
                                    icon = Icons.Default.Add,
                                    isPrimary = false,
                                    onClick = onNavigateToAddAthlete,
                                    modifier = Modifier.weight(1f)
                                )
                                QuickActionButton(
                                    title = "View History",
                                    subtitle = "Trend timeline",
                                    icon = Icons.Default.History,
                                    isPrimary = false,
                                    onClick = onNavigateToHistory,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Assessment Metrics Benchmark Overview
                            Text(
                                text = "Assessment Metrics Benchmark Overview",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(NavyCard)
                                    .border(1.dp, NavyBorder, RoundedCornerShape(14.dp))
                                    .padding(18.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    MetricOverviewItem(title = "Eye Stability", score = "91%", icon = Icons.Default.RemoveRedEye, color = CyanPrimary)
                                    MetricOverviewItem(title = "Postural Balance", score = "93%", icon = Icons.Default.Shield, color = CyanGlow)
                                    MetricOverviewItem(title = "Symmetry", score = "92%", icon = Icons.Default.Analytics, color = StatusWithinBaseline)
                                    MetricOverviewItem(title = "Reaction Time", score = "298 ms", icon = Icons.Default.FlashOn, color = StatusDeviation)
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Protocol Notice Box
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(NavyDark)
                                    .border(1.dp, NavyBorder, RoundedCornerShape(12.dp))
                                    .padding(14.dp)
                            ) {
                                Row(verticalAlignment = Alignment.Top) {
                                    Icon(Icons.Default.Info, contentDescription = null, tint = CyanPrimary, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text("Baseline Comparison Guidance", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            "NeuroTrack AI scores an athlete's post-event screening against their own pre-season baseline. Deviations above 15% in 2 or more modalities trigger clinical review recommendation.",
                                            color = TextSecondaryDark,
                                            fontSize = 11.sp,
                                            lineHeight = 15.sp
                                        )
                                    }
                                }
                            }
                        }

                        // Right Column: Recent Screenings List
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Recent Athlete Screenings",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "View All (${assessments.size})",
                                    color = CyanPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.clickable { onNavigateToHistory() }
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            if (assessments.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(NavyCard)
                                        .padding(32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("No assessments recorded yet", color = TextMutedDark, fontSize = 13.sp)
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Button(
                                            onClick = onNavigateToAnalyze,
                                            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
                                        ) {
                                            Text("Start First Assessment", color = NavyDarkest, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            } else {
                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    assessments.take(6).forEach { item ->
                                        RecentAssessmentCard(
                                            assessment = item,
                                            dateFormat = dateFormat,
                                            onClick = { onAssessmentClick(item.id) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            } else {
                // MOBILE STACKED VIEW
                item {
                    Text(
                        text = "Quick Actions",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        QuickActionButton(
                            title = "Start Analysis",
                            subtitle = "Before vs After",
                            icon = Icons.Default.Analytics,
                            isPrimary = true,
                            onClick = onNavigateToAnalyze,
                            modifier = Modifier.weight(1f)
                        )
                        QuickActionButton(
                            title = "Create Baseline",
                            subtitle = "Calibrate healthy",
                            icon = Icons.Default.Speed,
                            isPrimary = false,
                            onClick = onNavigateToBaseline,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        QuickActionButton(
                            title = "Add Athlete",
                            subtitle = "Create profile",
                            icon = Icons.Default.Add,
                            isPrimary = false,
                            onClick = onNavigateToAddAthlete,
                            modifier = Modifier.weight(1f)
                        )
                        QuickActionButton(
                            title = "View History",
                            subtitle = "Trend timeline",
                            icon = Icons.Default.History,
                            isPrimary = false,
                            onClick = onNavigateToHistory,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(22.dp))
                }

                item {
                    Text(
                        text = "Assessment Metrics Benchmark Overview",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(NavyCard)
                            .border(1.dp, NavyBorder, RoundedCornerShape(14.dp))
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            MetricOverviewItem(title = "Eye Stability", score = "91%", icon = Icons.Default.RemoveRedEye, color = CyanPrimary)
                            MetricOverviewItem(title = "Postural Balance", score = "93%", icon = Icons.Default.Shield, color = CyanGlow)
                            MetricOverviewItem(title = "Symmetry", score = "92%", icon = Icons.Default.Analytics, color = StatusWithinBaseline)
                            MetricOverviewItem(title = "Reaction Time", score = "298 ms", icon = Icons.Default.FlashOn, color = StatusDeviation)
                        }
                    }

                    Spacer(modifier = Modifier.height(22.dp))
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Recent Athlete Screenings",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "View All (${assessments.size})",
                            color = CyanPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable { onNavigateToHistory() }
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                if (assessments.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(NavyCard)
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("No assessments recorded yet", color = TextMutedDark, fontSize = 13.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = onNavigateToAnalyze,
                                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
                                ) {
                                    Text("Start First Assessment", color = NavyDarkest, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                } else {
                    items(assessments.take(5)) { item ->
                        RecentAssessmentCard(
                            assessment = item,
                            dateFormat = dateFormat,
                            onClick = { onAssessmentClick(item.id) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun DashboardMetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(NavyCard)
            .border(1.dp, NavyBorder, RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = TextSecondaryDark,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = TextMutedDark,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun QuickActionButton(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isPrimary: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isPrimary) CyanPrimary else NavyCard)
            .border(1.dp, if (isPrimary) CyanPrimary else NavyBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isPrimary) NavyDarkest else CyanPrimary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    color = if (isPrimary) NavyDarkest else Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    color = if (isPrimary) NavyDarkest.copy(alpha = 0.8f) else TextMutedDark,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun MetricOverviewItem(
    title: String,
    score: String,
    icon: ImageVector,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(17.dp))
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = score, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text(text = title, color = TextMutedDark, fontSize = 10.sp)
    }
}

@Composable
private fun RecentAssessmentCard(
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
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = assessment.athleteName,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${assessment.sport} • ${dateFormat.format(Date(assessment.timestamp))}",
                        color = TextMutedDark,
                        fontSize = 11.sp
                    )
                }

                StatusBadge(status = assessment.status)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Deviation: ${if (assessment.overallDeviationPercent > 0) "+" else ""}${assessment.overallDeviationPercent.roundToInt()}% from baseline",
                    color = TextSecondaryDark,
                    fontSize = 11.sp
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "View Result",
                        color = CyanPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = CyanPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
