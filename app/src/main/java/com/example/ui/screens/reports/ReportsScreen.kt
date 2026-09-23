package com.example.ui.screens.reports

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Assessment
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun ReportsScreen(
    preselectedAssessmentId: Long?,
    assessmentViewModel: AssessmentViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val assessments by assessmentViewModel.allAssessments.collectAsState()

    var selectedAssessment by remember {
        mutableStateOf(assessments.find { it.id == preselectedAssessmentId } ?: assessments.firstOrNull())
    }

    LaunchedEffect(assessments) {
        if (selectedAssessment == null && assessments.isNotEmpty()) {
            selectedAssessment = assessments.find { it.id == preselectedAssessmentId } ?: assessments.first()
        }
    }

    val dateFormat = SimpleDateFormat("MMMM dd, yyyy • hh:mm a", Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyDarkest)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Bar
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
                        text = "Clinical Screening Dossier",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Standardized Athletic Concussion Decision Support",
                        color = CyanPrimary,
                        fontSize = 11.sp
                    )
                }
            }

            IconButton(
                onClick = {
                    Toast.makeText(context, "Exporting report PDF...", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(NavyDark)
            ) {
                Icon(Icons.Default.Download, contentDescription = "Download", tint = CyanPrimary)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedAssessment == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No assessment reports available", color = TextMutedDark)
            }
            return
        }

        val item = selectedAssessment!!

        // Document Paper Container (Medical Report Styling)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(NavyCard)
                .border(1.dp, NavyBorder, RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            Column {
                // Formal Report Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = "NEUROTRACK AI ATHLETIC HEALTH",
                            color = CyanPrimary,
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "CONCUSSION SCREENING SUMMARY",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "Report ID: NT-${item.id}-${item.athleteId}",
                            color = TextMutedDark,
                            fontSize = 11.sp
                        )
                    }

                    StatusBadge(status = item.status)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Athlete Info Grid
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(NavyDarkest)
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        ReportFieldRow("Athlete Name:", item.athleteName)
                        ReportFieldRow("Sport / Discipline:", item.sport)
                        ReportFieldRow("Assessment Date:", dateFormat.format(Date(item.timestamp)))
                        ReportFieldRow("Evaluation Protocol:", item.type.label)
                        ReportFieldRow("Supervising Evaluator:", "Dr. Marcus Vance (Sports Trainer)")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Executive Screening Result
                Text(
                    text = "Executive Screening Determination",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = item.status.description,
                    color = TextSecondaryDark,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Quantitative Biometric Comparison Table
                Text(
                    text = "Biometric Variance vs Personal Baseline",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Table Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(NavyBorder.copy(alpha = 0.5f))
                        .padding(vertical = 6.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Parameter", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(2f))
                    Text("Base", color = CyanPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                    Text("Current", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                    Text("Variance", color = TextSecondaryDark, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                }

                TableRow("Reaction Latency", "${item.baselineReactionTimeMs}ms", "${item.reactionTimeMs}ms", "${if (item.reactionDeviationPercent > 0) "+" else ""}${item.reactionDeviationPercent.roundToInt()}%", item.reactionDeviationPercent > 15f)
                TableRow("Postural Sway Stability", "${item.baselineBalanceStability.roundToInt()}%", "${item.balanceStability.roundToInt()}%", "${item.balanceDeviationPercent.roundToInt()}%", item.balanceDeviationPercent < -15f)
                TableRow("Ocular Saccades", "${item.baselineEyeStability.roundToInt()}%", "${item.eyeStability.roundToInt()}%", "${item.eyeDeviationPercent.roundToInt()}%", item.eyeDeviationPercent < -15f)
                TableRow("Gait Cadence", "${item.baselineGaitConsistency.roundToInt()}%", "${item.gaitConsistency.roundToInt()}%", "${item.gaitDeviationPercent.roundToInt()}%", item.gaitDeviationPercent < -12f)
                TableRow("Bilateral Symmetry", "${item.baselineMovementSymmetry.roundToInt()}%", "${item.movementSymmetry.roundToInt()}%", "${item.symmetryDeviationPercent.roundToInt()}%", item.symmetryDeviationPercent < -12f)

                Spacer(modifier = Modifier.height(16.dp))

                // Contributing Factors
                Text(
                    text = "Primary Contributing Indicators",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "• ${item.primaryContributingIndicator}\n• ${item.secondaryContributingIndicator}",
                    color = TextSecondaryDark,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Non-Diagnostic Safety Attestation
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(NavyDarkest)
                        .border(1.dp, NavyBorder, RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Text(
                        text = "CLINICAL DISCLAIMER: This computer-vision biometric report is an AI-assisted screening prototype designed for clinical decision support. It does NOT make a clinical diagnosis of concussion and does NOT grant medical clearance to return to athletic activity.",
                        color = TextMutedDark,
                        fontSize = 10.sp,
                        lineHeight = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Bottom Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    Toast.makeText(context, "Downloading PDF Report NT-${item.id}...", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f).height(48.dp)
            ) {
                Icon(Icons.Default.Download, contentDescription = null, tint = NavyDarkest, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Export PDF", color = NavyDarkest, fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = {
                    Toast.makeText(context, "Sharing secure report link...", Toast.LENGTH_SHORT).show()
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
                modifier = Modifier.weight(1f).height(48.dp)
            ) {
                Icon(Icons.Default.Share, contentDescription = null, tint = CyanPrimary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Share Report", fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
private fun ReportFieldRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = TextMutedDark, fontSize = 11.sp)
        Text(value, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
    }
}

@Composable
private fun TableRow(
    param: String,
    base: String,
    current: String,
    variance: String,
    isFlagged: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(param, color = Color.White, fontSize = 11.sp, modifier = Modifier.weight(2f))
        Text(base, color = CyanPrimary, fontSize = 11.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        Text(current, color = Color.White, fontSize = 11.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        Text(
            text = variance,
            color = if (isFlagged) StatusFurtherAssessment else StatusWithinBaseline,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End
        )
    }
}
