package com.example.ui.screens.analysis

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyCard
import com.example.ui.theme.NavyDarkest
import com.example.ui.theme.StatusWithinBaseline
import com.example.ui.theme.TextMutedDark
import com.example.ui.theme.TextSecondaryDark
import com.example.ui.viewmodel.AssessmentViewModel
import kotlin.math.roundToInt

@Composable
fun AnalyzeProcessingScreen(
    athleteId: Long,
    assessmentViewModel: AssessmentViewModel,
    onProcessingFinished: (Long) -> Unit
) {
    val progress by assessmentViewModel.processingProgress.collectAsState()
    val currentStepText by assessmentViewModel.processingCurrentStep.collectAsState()

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    LaunchedEffect(Unit) {
        assessmentViewModel.runAiAnalysis(athleteId) { createdAssessmentId ->
            onProcessingFinished(createdAssessmentId)
        }
    }

    val pipelineStages = listOf(
        "Secure Video Ingestion & Frame Normalization",
        "Computer Vision Facial Mesh & Iris Localization",
        "Saccadic Eye Movement & Pursuit Latency Extraction",
        "Kinematic Pose Landmark Extraction (33 Points)",
        "Postural Center-of-Pressure Sway Quantification",
        "Gait Cadence & Bilateral Limb Symmetry Modeling",
        "Multimodal Neural Fusion & Temporal Alignment",
        "Athlete Personal Baseline Delta Calculation",
        "Explainable SHAP Contributing Feature Synthesis"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyDarkest)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Rotating AI Neural Visualizer
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(CyanPrimary.copy(alpha = 0.1f))
                .border(2.dp, CyanPrimary.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .rotate(rotation)
                    .clip(CircleShape)
                    .border(2.dp, CyanPrimary, CircleShape)
            )
            Icon(
                imageVector = Icons.Default.Psychology,
                contentDescription = null,
                tint = CyanPrimary,
                modifier = Modifier.size(44.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Multimodal AI Inference Engine",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 0.5.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Analyzing video frames, eye metrics, and movement dynamics...",
            color = TextSecondaryDark,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Progress card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(NavyCard)
                .border(1.dp, NavyBorder, RoundedCornerShape(16.dp))
                .padding(18.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Current Stage",
                        color = TextMutedDark,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${(progress * 100).roundToInt()}%",
                        color = CyanPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = currentStepText,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(14.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = CyanPrimary,
                    trackColor = NavyBorder
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Pipeline stage checklist
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Processing Subsystems",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            pipelineStages.forEachIndexed { index, stage ->
                val stageThreshold = (index + 1) / pipelineStages.size.toFloat()
                val isCompleted = progress >= stageThreshold
                val isRunning = progress < stageThreshold && (index == 0 || progress >= (index / pipelineStages.size.toFloat()))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isCompleted -> StatusWithinBaseline
                                    isRunning -> CyanPrimary.copy(alpha = 0.3f)
                                    else -> NavyBorder
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = NavyDarkest, modifier = Modifier.size(12.dp))
                        } else if (isRunning) {
                            CircularProgressIndicator(modifier = Modifier.size(10.dp), strokeWidth = 1.5.dp, color = CyanPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = stage,
                        color = when {
                            isCompleted -> Color.White
                            isRunning -> CyanPrimary
                            else -> TextMutedDark
                        },
                        fontSize = 12.sp,
                        fontWeight = if (isRunning) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
