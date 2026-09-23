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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AssessmentType
import com.example.data.model.Athlete
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyCard
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyDarkest
import com.example.ui.theme.StatusDeviation
import com.example.ui.theme.StatusWithinBaseline
import com.example.ui.theme.TextMutedDark
import com.example.ui.theme.TextSecondaryDark
import com.example.ui.viewmodel.AssessmentViewModel
import com.example.ui.viewmodel.AthleteViewModel

@Composable
fun AnalyzeSetupScreen(
    preselectedAthleteId: Long?,
    athleteViewModel: AthleteViewModel,
    assessmentViewModel: AssessmentViewModel,
    onBackClick: () -> Unit,
    onProceedToBeforeVideo: (Long) -> Unit
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

    var selectedType by remember { mutableStateOf(AssessmentType.BEFORE_AFTER) }
    var notes by remember { mutableStateOf("Sideline assessment following head impact event.") }

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
                    text = "Start Screening Assessment",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Configure assessment protocol and target athlete",
                    color = CyanPrimary,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Step 1: Select Athlete
        Text(
            text = "1. Select Athlete",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
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
                        Text(
                            text = athlete.name,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "${athlete.sport} • Age ${athlete.age} • #${athlete.jerseyNumber.ifEmpty { "10" }}",
                            color = TextSecondaryDark,
                            fontSize = 12.sp
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (athlete.hasBaseline) Icons.Default.CheckCircle else Icons.Default.Warning,
                            contentDescription = null,
                            tint = if (athlete.hasBaseline) StatusWithinBaseline else StatusDeviation,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (athlete.hasBaseline) "Baseline Ready" else "No Baseline",
                            color = if (athlete.hasBaseline) StatusWithinBaseline else StatusDeviation,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Step 2: Assessment Type
        Text(
            text = "2. Assessment Protocol Type",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        AssessmentTypeCard(
            title = "Before vs After Comparison",
            description = "Direct synchronized evaluation comparing healthy pre-impact baseline with post-impact sideline video.",
            icon = Icons.Default.CompareArrows,
            isSelected = selectedType == AssessmentType.BEFORE_AFTER,
            onClick = { selectedType = AssessmentType.BEFORE_AFTER }
        )

        Spacer(modifier = Modifier.height(8.dp))

        AssessmentTypeCard(
            title = "Live Camera Assessment",
            description = "Real-time interactive camera evaluation with biometric landmark analysis.",
            icon = Icons.Default.Videocam,
            isSelected = selectedType == AssessmentType.LIVE_CAMERA,
            onClick = { selectedType = AssessmentType.LIVE_CAMERA }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Step 3: Session Notes
        Text(
            text = "3. Incident / Session Notes",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            placeholder = { Text("Enter context (e.g. impact time, observed signs)...", color = TextMutedDark) },
            modifier = Modifier.fillMaxWidth().height(90.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CyanPrimary,
                unfocusedBorderColor = NavyBorder,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = NavyCard,
                unfocusedContainerColor = NavyCard
            ),
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Next Action
        Button(
            onClick = {
                selectedAthlete?.let { athlete ->
                    assessmentViewModel.setActiveAthlete(athlete)
                    onProceedToBeforeVideo(athlete.id)
                }
            },
            enabled = selectedAthlete != null,
            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Step 1: Record Pre-Impact Video",
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
private fun AssessmentTypeCard(
    title: String,
    description: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) CyanPrimary.copy(alpha = 0.15f) else NavyCard)
            .border(1.dp, if (isSelected) CyanPrimary else NavyBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) CyanPrimary else NavyBorder.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) NavyDarkest else CyanPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    color = TextSecondaryDark,
                    fontSize = 12.sp
                )
            }
        }
    }
}
