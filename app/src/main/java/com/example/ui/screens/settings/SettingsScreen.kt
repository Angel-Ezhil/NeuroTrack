package com.example.ui.screens.settings

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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.User
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyCard
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyDarkest
import com.example.ui.theme.StatusDeviation
import com.example.ui.theme.StatusFurtherAssessment
import com.example.ui.theme.TextMutedDark
import com.example.ui.theme.TextSecondaryDark
import com.example.ui.viewmodel.AssessmentViewModel
import com.example.ui.viewmodel.AuthViewModel

@Composable
fun SettingsScreen(
    currentUser: User?,
    authViewModel: AuthViewModel,
    assessmentViewModel: AssessmentViewModel,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val isDemoMode by assessmentViewModel.isDemoMode.collectAsState()

    var cameraFpsHigh by remember { mutableStateOf(true) }
    var showTelemetryMesh by remember { mutableStateOf(true) }
    var soundFeedback by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyDarkest)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "System Settings & Profile",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Application preferences, user credentials, and clinical policies",
            color = TextMutedDark,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 1. User Profile Section
        Text(
            text = "AUTHENTICATED PRACTITIONER",
            color = CyanPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(NavyCard)
                .border(1.dp, NavyBorder, RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(CyanPrimary.copy(alpha = 0.15f))
                        .border(1.5.dp, CyanPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = CyanPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = currentUser?.fullName ?: "Dr. Marcus Vance",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "${currentUser?.role?.displayName ?: "Sports Trainer"} • ${currentUser?.organization ?: "Apex Athletic Institute"}",
                        color = CyanPrimary,
                        fontSize = 12.sp
                    )
                    Text(
                        text = currentUser?.email ?: "m.vance@neurotrack.ai",
                        color = TextMutedDark,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 2. Demo Mode Settings (Section 27)
        Text(
            text = "DEMO & SIMULATION CONFIGURATION",
            color = CyanPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

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
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Interactive Demo Mode",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Enables sample video streams and simulated biomechanical deviation scenarios when testing without live athletes.",
                            color = TextSecondaryDark,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }

                    Switch(
                        checked = isDemoMode,
                        onCheckedChange = { assessmentViewModel.setDemoMode(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = NavyDarkest,
                            checkedTrackColor = CyanPrimary,
                            uncheckedThumbColor = TextMutedDark,
                            uncheckedTrackColor = NavyBorder
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 3. Vision & Sensor Settings
        Text(
            text = "CAMERA & COMPUTER VISION",
            color = CyanPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(NavyCard)
                .border(1.dp, NavyBorder, RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("High-Rate Capture (60 FPS)", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        Text("Captures rapid micro-saccades and jitter", color = TextMutedDark, fontSize = 11.sp)
                    }
                    Switch(
                        checked = cameraFpsHigh,
                        onCheckedChange = { cameraFpsHigh = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = NavyDarkest, checkedTrackColor = CyanPrimary)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Biometric HUD Landmark Mesh", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        Text("Displays facial iris nodes and pose skeleton on camera feed", color = TextMutedDark, fontSize = 11.sp)
                    }
                    Switch(
                        checked = showTelemetryMesh,
                        onCheckedChange = { showTelemetryMesh = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = NavyDarkest, checkedTrackColor = CyanPrimary)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 4. Clinical Governance & Legal Disclaimers (Section 4, 48)
        Text(
            text = "CLINICAL POLICIES & GOVERNANCE",
            color = CyanPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(NavyCard)
                .border(1.dp, NavyBorder, RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = CyanPrimary, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Non-Diagnostic Decision Support Notice", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "NeuroTrack AI is strictly an athletic screening tool and decision support system. It measures biomechanical variations against calibrated baselines. It does not replace clinical judgment or comprehensive neurological evaluations, and does not diagnose concussions.",
                    color = TextSecondaryDark,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = TextMutedDark, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Protocol Alignment: VOMS & SCAT6 Research Guidelines", color = TextMutedDark, fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 5. Logout Button
        Button(
            onClick = {
                authViewModel.logout(onLogout)
            },
            colors = ButtonDefaults.buttonColors(containerColor = StatusFurtherAssessment.copy(alpha = 0.2f)),
            shape = RoundedCornerShape(10.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, StatusFurtherAssessment),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = StatusFurtherAssessment)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Sign Out of Practitioner Portal", color = StatusFurtherAssessment, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
