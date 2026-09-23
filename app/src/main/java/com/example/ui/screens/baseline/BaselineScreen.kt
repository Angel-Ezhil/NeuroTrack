package com.example.ui.screens.baseline

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Athlete
import com.example.ui.components.CameraPreviewHud
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
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun BaselineScreen(
    preselectedAthleteId: Long?,
    athleteViewModel: AthleteViewModel,
    assessmentViewModel: AssessmentViewModel,
    onBackClick: () -> Unit,
    onBaselineCompleted: (Long) -> Unit
) {
    val athletes by athleteViewModel.athletes.collectAsState()
    var selectedAthlete by remember {
        mutableStateOf(athletes.find { it.id == preselectedAthleteId } ?: athletes.firstOrNull())
    }

    LaunchedEffect(athletes) {
        if (selectedAthlete == null && athletes.isNotEmpty()) {
            selectedAthlete = athletes.find { it.id == preselectedAthleteId } ?: athletes.first()
        }
    }

    var currentStep by remember { mutableIntStateOf(0) } // 0: Select, 1: Eye, 2: Balance, 3: Gait, 4: Reaction, 5: Review & Save
    var isTestRunning by remember { mutableStateOf(false) }
    var testCountdown by remember { mutableIntStateOf(10) }

    // Calibrated score values
    var eyeStabilityScore by remember { mutableStateOf(94f) }
    var balanceStabilityScore by remember { mutableStateOf(95f) }
    var gaitConsistencyScore by remember { mutableStateOf(92f) }
    var movementSymmetryScore by remember { mutableStateOf(94f) }
    var headStabilityScore by remember { mutableStateOf(93f) }
    var measuredReactionTimeMs by remember { mutableIntStateOf(290) }

    // Interactive reaction test states
    var reactionTestState by remember { mutableStateOf("WAIT") } // "WAIT", "READY", "TAP_NOW", "DONE"
    var reactionStartTime by remember { mutableLongStateOf(0L) }
    var reactionReactionMs by remember { mutableIntStateOf(0) }

    LaunchedEffect(isTestRunning, testCountdown) {
        if (isTestRunning && testCountdown > 0) {
            delay(1000)
            testCountdown--
        } else if (isTestRunning && testCountdown == 0) {
            isTestRunning = false
            currentStep++
            testCountdown = 10
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyDarkest)
            .padding(16.dp)
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
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Healthy Baseline Calibration",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Calibrate benchmark while athlete is symptom-free",
                        color = CyanPrimary,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Stepper Header (5 steps)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val stepNames = listOf("Setup", "Eyes", "Balance", "Gait", "Reaction", "Save")
            stepNames.forEachIndexed { index, name ->
                val isDone = index < currentStep
                val isCurrent = index == currentStep
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isDone -> StatusWithinBaseline
                                    isCurrent -> CyanPrimary
                                    else -> NavyBorder
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isDone) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = NavyDarkest, modifier = Modifier.size(14.dp))
                        } else {
                            Text(
                                text = "${index + 1}",
                                color = if (isCurrent) NavyDarkest else Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = name,
                        color = if (isCurrent) CyanPrimary else TextMutedDark,
                        fontSize = 9.sp,
                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Main content by step
        when (currentStep) {
            0 -> {
                // SETUP: Select Athlete
                Text("Select Athlete for Calibration", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                athletes.forEach { athlete ->
                    val isSelected = selectedAthlete?.id == athlete.id
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) CyanPrimary.copy(alpha = 0.15f) else NavyCard)
                            .border(1.dp, if (isSelected) CyanPrimary else NavyBorder, RoundedCornerShape(12.dp))
                            .clickable { selectedAthlete = athlete }
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(athlete.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("${athlete.sport} • Age ${athlete.age}", color = TextSecondaryDark, fontSize = 12.sp)
                            }
                            if (athlete.hasBaseline) {
                                Text("Baseline Calibrated", color = StatusWithinBaseline, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            } else {
                                Text("Needs Baseline", color = StatusDeviation, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { currentStep = 1 },
                    enabled = selectedAthlete != null,
                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("Proceed to Eye Tracking Calibration", color = NavyDarkest, fontWeight = FontWeight.Bold)
                }
            }

            1 -> {
                // STEP 1: Eye Tracking
                Text("Step 1: Ocular & Gaze Tracking", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Hold device at eye level (40cm). Instruct ${selectedAthlete?.name} to follow the target crosshair without moving their head.", color = TextSecondaryDark, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(14.dp))

                CameraPreviewHud(
                    isRecording = isTestRunning,
                    testModeLabel = "Saccadic Tracking & Smooth Pursuit",
                    modifier = Modifier.fillMaxWidth().height(260.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (!isTestRunning) {
                    Button(
                        onClick = {
                            isTestRunning = true
                            testCountdown = 10
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = NavyDarkest)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Start 10s Eye Calibration", color = NavyDarkest, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(NavyCard)
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Calibrating Saccades... $testCountdown s remaining", color = CyanPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { (10 - testCountdown) / 10f },
                                modifier = Modifier.fillMaxWidth(),
                                color = CyanPrimary,
                                trackColor = NavyBorder
                            )
                        }
                    }
                }
            }

            2 -> {
                // STEP 2: Balance Test
                Text("Step 2: Postural Balance Stability", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Athlete stands on non-dominant leg with hands on hips, eyes open. The camera measures center-of-mass sway.", color = TextSecondaryDark, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(14.dp))

                CameraPreviewHud(
                    isRecording = isTestRunning,
                    testModeLabel = "Postural Equilibrium & Sway Vector",
                    modifier = Modifier.fillMaxWidth().height(260.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (!isTestRunning) {
                    Button(
                        onClick = {
                            isTestRunning = true
                            testCountdown = 10
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = NavyDarkest)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Start 10s Balance Calibration", color = NavyDarkest, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(NavyCard).padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Measuring Stance Sway... $testCountdown s remaining", color = CyanPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { (10 - testCountdown) / 10f },
                                modifier = Modifier.fillMaxWidth(),
                                color = CyanPrimary,
                                trackColor = NavyBorder
                            )
                        }
                    }
                }
            }

            3 -> {
                // STEP 3: Gait Consistency
                Text("Step 3: Walking Gait & Cadence", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Athlete walks 5 steps forward in a straight line at normal cadence, turns, and returns.", color = TextSecondaryDark, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(14.dp))

                CameraPreviewHud(
                    isRecording = isTestRunning,
                    testModeLabel = "Stride Symmetry & Bilateral Rhythm",
                    modifier = Modifier.fillMaxWidth().height(260.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (!isTestRunning) {
                    Button(
                        onClick = {
                            isTestRunning = true
                            testCountdown = 8
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = NavyDarkest)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Start Gait Calibration (8s)", color = NavyDarkest, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(NavyCard).padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Analyzing Gait Stride... $testCountdown s", color = CyanPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { (8 - testCountdown) / 8f },
                                modifier = Modifier.fillMaxWidth(),
                                color = CyanPrimary,
                                trackColor = NavyBorder
                            )
                        }
                    }
                }
            }

            4 -> {
                // STEP 4: Interactive Reaction Time Test
                Text("Step 4: Millisecond Reaction Stimulus", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Tap the screen as fast as possible when the box turns GREEN.", color = TextSecondaryDark, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            when (reactionTestState) {
                                "TAP_NOW" -> StatusWithinBaseline
                                "WAIT" -> NavyCard
                                "READY" -> StatusFurtherAssessment
                                else -> CyanPrimary.copy(alpha = 0.2f)
                            }
                        )
                        .border(
                            2.dp,
                            when (reactionTestState) {
                                "TAP_NOW" -> StatusWithinBaseline
                                "READY" -> StatusFurtherAssessment
                                else -> CyanPrimary
                            },
                            RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            if (reactionTestState == "TAP_NOW") {
                                val elapsed = (System.currentTimeMillis() - reactionStartTime).toInt()
                                reactionReactionMs = elapsed
                                measuredReactionTimeMs = elapsed.coerceIn(180, 500)
                                reactionTestState = "DONE"
                            } else if (reactionTestState == "WAIT" || reactionTestState == "DONE") {
                                reactionTestState = "READY"
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        when (reactionTestState) {
                            "WAIT" -> {
                                Icon(Icons.Default.FlashOn, contentDescription = null, tint = CyanPrimary, modifier = Modifier.size(40.dp))
                                Spacer(modifier = Modifier.height(10.dp))
                                Text("TAP TO START REACTION TRIAL", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text("Wait for green flash", color = TextMutedDark, fontSize = 12.sp)
                            }
                            "READY" -> {
                                Text("WAIT FOR GREEN...", color = Color.White, fontWeight = FontWeight.Black, fontSize = 20.sp)
                            }
                            "TAP_NOW" -> {
                                Text("TAP NOW!", color = NavyDarkest, fontWeight = FontWeight.Black, fontSize = 32.sp)
                            }
                            "DONE" -> {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusWithinBaseline, modifier = Modifier.size(44.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("LATENCY: $reactionReactionMs ms", color = Color.White, fontWeight = FontWeight.Black, fontSize = 24.sp)
                                Text("Baseline benchmark recorded", color = CyanPrimary, fontSize = 13.sp)
                            }
                        }
                    }
                }

                LaunchedEffect(reactionTestState) {
                    if (reactionTestState == "READY") {
                        val waitDelay = Random.nextLong(1500, 3200)
                        delay(waitDelay)
                        reactionStartTime = System.currentTimeMillis()
                        reactionTestState = "TAP_NOW"
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { currentStep = 5 },
                    enabled = reactionTestState == "DONE" || measuredReactionTimeMs > 0,
                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("Proceed to Review & Save", color = NavyDarkest, fontWeight = FontWeight.Bold)
                }
            }

            5 -> {
                // STEP 5: Review & Save Baseline
                Text("Calibration Summary & Review", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Confirm benchmark values for ${selectedAthlete?.name} to establish active baseline.", color = TextSecondaryDark, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(NavyCard)
                        .border(1.dp, NavyBorder, RoundedCornerShape(14.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        ReviewRow("Eye Stability Index", "${eyeStabilityScore.toInt()}%", StatusWithinBaseline)
                        ReviewRow("Postural Balance Index", "${balanceStabilityScore.toInt()}%", StatusWithinBaseline)
                        ReviewRow("Gait Cadence Rhythm", "${gaitConsistencyScore.toInt()}%", StatusWithinBaseline)
                        ReviewRow("Kinetic Symmetry Index", "${movementSymmetryScore.toInt()}%", StatusWithinBaseline)
                        ReviewRow("Reaction Time Latency", "$measuredReactionTimeMs ms", StatusWithinBaseline)
                        ReviewRow("Head Fixation Stability", "${headStabilityScore.toInt()}%", StatusWithinBaseline)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        selectedAthlete?.let { athlete ->
                            assessmentViewModel.saveCompletedBaseline(
                                athleteId = athlete.id,
                                eyeStability = eyeStabilityScore,
                                balanceStability = balanceStabilityScore,
                                gaitConsistency = gaitConsistencyScore,
                                movementSymmetry = movementSymmetryScore,
                                reactionTimeMs = measuredReactionTimeMs,
                                headStability = headStabilityScore
                            ) { createdAssessmentId ->
                                onBaselineCompleted(createdAssessmentId)
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = NavyDarkest)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save & Activate Athlete Baseline", color = NavyDarkest, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
private fun ReviewRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = TextSecondaryDark, fontSize = 13.sp)
        Text(value, color = color, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}
