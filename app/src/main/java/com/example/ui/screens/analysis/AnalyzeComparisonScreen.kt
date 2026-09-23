package com.example.ui.screens.analysis

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.example.ui.components.MetricCard
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
import kotlin.math.roundToInt

@Composable
fun AnalyzeComparisonScreen(
    assessmentId: Long,
    assessmentViewModel: AssessmentViewModel,
    onBackClick: () -> Unit,
    onViewResults: (Long) -> Unit
) {
    LaunchedEffect(assessmentId) {
        assessmentViewModel.loadAssessmentById(assessmentId)
    }

    val assessment by assessmentViewModel.selectedAssessment.collectAsState()
    var isPlayingSync by remember { mutableStateOf(true) }

    if (assessment == null) {
        Box(
            modifier = Modifier.fillMaxSize().background(NavyDarkest),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading comparison data...", color = Color.White)
        }
        return
    }

    val item = assessment!!

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyDarkest)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Back Header
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
                        text = "Before vs After Comparison",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${item.athleteName} • ${item.sport}",
                        color = CyanPrimary,
                        fontSize = 11.sp
                    )
                }
            }

            StatusBadge(status = item.status)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Side-by-side synchronized video comparison boxes
        Text(
            text = "Synchronized Video & Biometric Feed",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Before Video Box
            VideoSyncBox(
                label = "PRE-IMPACT BASELINE",
                timestamp = "00:08 / 00:10",
                fps = "60 FPS",
                accent = CyanPrimary,
                subText = "Saccades: Nominal",
                modifier = Modifier.weight(1f)
            )

            // After Video Box
            VideoSyncBox(
                label = "POST-IMPACT SIDELINE",
                timestamp = "00:08 / 00:10",
                fps = "60 FPS",
                accent = item.status.color,
                subText = "Saccades: Jitter + Latency",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Playback control
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(NavyCard)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { isPlayingSync = !isPlayingSync },
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        imageVector = if (isPlayingSync) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = CyanPrimary
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isPlayingSync) "Synchronized Playback (Active)" else "Playback Paused",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Text("Side-by-side 1080p", color = TextMutedDark, fontSize = 10.sp)
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Radar Chart Comparison
        val radarAxes = listOf(
            RadarAxisData("Eye Track", item.baselineEyeStability, item.eyeStability),
            RadarAxisData("Balance", item.baselineBalanceStability, item.balanceStability),
            RadarAxisData("Gait", item.baselineGaitConsistency, item.gaitConsistency),
            RadarAxisData("Symmetry", item.baselineMovementSymmetry, item.movementSymmetry),
            RadarAxisData("Reaction", (100f - ((item.baselineReactionTimeMs - 200f) / 4f)).coerceIn(10f, 100f), (100f - ((item.reactionTimeMs - 200f) / 4f)).coerceIn(10f, 100f)),
            RadarAxisData("Head Fix", item.baselineHeadStability, item.headStability)
        )

        BiometricRadarChart(
            axes = radarAxes,
            currentStatusColor = item.status.color
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Multi-modal metric cards with deltas
        Text(
            text = "Comparative Metric Deltas",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))

        MetricCard(
            title = "Reaction Time Latency",
            currentValue = "${item.reactionTimeMs} ms",
            baselineValue = "${item.baselineReactionTimeMs} ms",
            changePercent = item.reactionDeviationPercent,
            isReactionTime = true,
            icon = Icons.Default.FlashOn
        )

        Spacer(modifier = Modifier.height(8.dp))

        MetricCard(
            title = "Postural Balance Stability",
            currentValue = "${item.balanceStability.roundToInt()}%",
            baselineValue = "${item.baselineBalanceStability.roundToInt()}%",
            changePercent = item.balanceDeviationPercent,
            icon = Icons.Default.Shield
        )

        Spacer(modifier = Modifier.height(8.dp))

        MetricCard(
            title = "Ocular Gaze Stability",
            currentValue = "${item.eyeStability.roundToInt()}%",
            baselineValue = "${item.baselineEyeStability.roundToInt()}%",
            changePercent = item.eyeDeviationPercent,
            icon = Icons.Default.RemoveRedEye
        )

        Spacer(modifier = Modifier.height(8.dp))

        MetricCard(
            title = "Kinetic Movement Symmetry",
            currentValue = "${item.movementSymmetry.roundToInt()}%",
            baselineValue = "${item.baselineMovementSymmetry.roundToInt()}%",
            changePercent = item.symmetryDeviationPercent,
            icon = Icons.Default.Analytics
        )

        Spacer(modifier = Modifier.height(20.dp))

        // View Results Button
        Button(
            onClick = { onViewResults(item.id) },
            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "View Comprehensive Screening Report",
                color = NavyDarkest,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = NavyDarkest,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
private fun VideoSyncBox(
    label: String,
    timestamp: String,
    fps: String,
    accent: Color,
    subText: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(160.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF070D1E))
            .border(1.dp, accent.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = label,
                    color = accent,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = fps,
                    color = Color.White,
                    fontSize = 9.sp
                )
            }

            // Mock biometric target reticle
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .border(1.dp, accent.copy(alpha = 0.6f), CircleShape)
                )
            }

            Column {
                Text(
                    text = subText,
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = timestamp,
                    color = TextMutedDark,
                    fontSize = 9.sp
                )
            }
        }
    }
}
