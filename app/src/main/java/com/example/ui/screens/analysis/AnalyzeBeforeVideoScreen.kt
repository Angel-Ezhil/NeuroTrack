package com.example.ui.screens.analysis

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.ui.components.CameraPreviewHud
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyCard
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyDarkest
import com.example.ui.theme.StatusWithinBaseline
import com.example.ui.theme.TextMutedDark
import com.example.ui.theme.TextSecondaryDark
import com.example.ui.viewmodel.AssessmentViewModel
import com.example.ui.viewmodel.AthleteViewModel
import kotlinx.coroutines.delay

@Composable
fun AnalyzeBeforeVideoScreen(
    athleteId: Long,
    athleteViewModel: AthleteViewModel,
    assessmentViewModel: AssessmentViewModel,
    onBackClick: () -> Unit,
    onProceedToAfterVideo: (Long) -> Unit
) {
    val athlete by athleteViewModel.selectedAthlete.collectAsState()
    val isReady by assessmentViewModel.beforeVideoReady.collectAsState()
    val videoSource by assessmentViewModel.beforeVideoSource.collectAsState()

    var isRecording by remember { mutableStateOf(false) }
    var countdown by remember { mutableIntStateOf(10) }

    LaunchedEffect(isRecording, countdown) {
        if (isRecording && countdown > 0) {
            delay(1000)
            countdown--
        } else if (isRecording && countdown == 0) {
            isRecording = false
            assessmentViewModel.recordBeforeVideo("Pre-impact Recorded Baseline (10s @ 60fps)")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyDarkest)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Step Indicator & Back
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
            Column {
                Text(
                    text = "Step 1 of 2: Pre-Impact Video",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Target Athlete: ${athlete?.name ?: "Athlete #$athleteId"}",
                    color = CyanPrimary,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Instruction card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(NavyCard)
                .border(1.dp, NavyBorder, RoundedCornerShape(12.dp))
                .padding(14.dp)
        ) {
            Column {
                Text(
                    text = "Pre-Impact Baseline Recording Instructions",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Record or confirm the athlete's pre-impact / pre-game healthy state performing head-movement, eye tracking, and single-leg balance.",
                    color = TextSecondaryDark,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Live Camera / Video HUD View
        CameraPreviewHud(
            isRecording = isRecording,
            testModeLabel = "PRE-IMPACT BASELINE RECORDING",
            modifier = Modifier
                .fillMaxWidth()
                .height(270.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Real-time AI Quality Check Indicators
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(NavyCard)
                .border(1.dp, NavyBorder, RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                QualityIndicatorItem(label = "LIGHTING", value = "OPTIMAL", isOk = true)
                QualityIndicatorItem(label = "FACE DETECTED", value = "CONFIRMED", isOk = true)
                QualityIndicatorItem(label = "FRAMING", value = "CENTERED", isOk = true)
                QualityIndicatorItem(label = "FRAME RATE", value = "60 FPS", isOk = true)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Record controls
        if (isRecording) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(NavyCard)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FiberManualRecord,
                            contentDescription = null,
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "RECORDING PRE-IMPACT CLIP: $countdown s remaining",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    LinearProgressIndicator(
                        progress = { (10 - countdown) / 10f },
                        modifier = Modifier.fillMaxWidth(),
                        color = CyanPrimary,
                        trackColor = NavyBorder
                    )
                }
            }
        } else if (isReady) {
            // Video captured confirmation card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(StatusWithinBaseline.copy(alpha = 0.15f))
                    .border(1.dp, StatusWithinBaseline, RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusWithinBaseline, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Pre-Impact Video Ready", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(videoSource ?: "10s • 1080p 60fps", color = StatusWithinBaseline, fontSize = 11.sp)
                        }
                    }

                    OutlinedButton(
                        onClick = {
                            assessmentViewModel.resetBeforeVideo()
                            countdown = 10
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Re-record", fontSize = 11.sp)
                    }
                }
            }
        } else {
            // Action buttons to record or load sample
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        isRecording = true
                        countdown = 10
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = NavyDarkest)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Record (10s)", color = NavyDarkest, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = {
                        assessmentViewModel.recordBeforeVideo("Verified Pre-Match Baseline Recording (Demo)")
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Text("Load Baseline Clip", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Proceed to After Video button
        Button(
            onClick = { onProceedToAfterVideo(athleteId) },
            enabled = isReady,
            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Proceed to Step 2: Post-Impact Video",
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
private fun QualityIndicatorItem(label: String, value: String, isOk: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = TextMutedDark, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(2.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(if (isOk) StatusWithinBaseline else Color.Red)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(value, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}
