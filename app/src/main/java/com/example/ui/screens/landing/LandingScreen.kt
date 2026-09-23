package com.example.ui.screens.landing

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyCard
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyDarkest
import com.example.ui.theme.StatusDeviation
import com.example.ui.theme.StatusWithinBaseline
import com.example.ui.theme.TextMutedDark
import com.example.ui.theme.TextSecondaryDark

@Composable
fun LandingScreen(
    onGetStartedClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    var showInfoDialog by remember { mutableStateOf<String?>(null) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyDarkest)
            .statusBarsPadding()
    ) {
        val isLaptop = maxWidth >= 840.dp
        val isTablet = maxWidth >= 600.dp && !isLaptop

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. TOP NAVBAR (Laptop Desktop Header)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavyDark.copy(alpha = 0.95f))
                    .border(0.5.dp, NavyBorder)
                    .padding(horizontal = if (isLaptop) 32.dp else 16.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 1360.dp)
                        .align(Alignment.Center),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo & Branding
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(NavyDarkest)
                                .border(1.5.dp, CyanPrimary, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .clip(CircleShape)
                                    .background(CyanPrimary)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "NEUROTRACK AI",
                                    color = Color.White,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.2.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(CyanPrimary.copy(alpha = 0.15f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "CLINICAL PROTOCOL",
                                        color = CyanPrimary,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Text(
                                text = "Track. Compare. Understand.",
                                color = TextMutedDark,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Desktop Navigation Links (Visible on Laptop & Tablet)
                    if (isLaptop) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(24.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            DesktopNavLink(title = "Overview", onClick = { showInfoDialog = "Overview: NeuroTrack AI provides objective, computer-vision powered screening comparing athlete metrics against their healthy personal baseline." })
                            DesktopNavLink(title = "Biometric Modules", onClick = { showInfoDialog = "Modules: 4 objective assessment pillars: Eye Movement (Saccades), Pose & Balance (Center of pressure), Dynamic Gait (Stride symmetry), and Reaction Latency." })
                            DesktopNavLink(title = "Pipeline", onClick = { showInfoDialog = "Pipeline: End-to-end workflow from standard 60fps video capture through 33-point pose mesh, baseline comparison, and automated clinical report generation." })
                            DesktopNavLink(title = "Clinical Safety", onClick = { showInfoDialog = "Clinical Safety: NeuroTrack AI is an assistive decision-support prototype. It does not diagnose concussions or provide medical clearance without licensed physician evaluation." })
                        }
                    }

                    // Top Action Buttons (Login & Get Started)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = onLoginClick,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(40.dp)
                        ) {
                            Text("Sign In", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }

                        Button(
                            onClick = onGetStartedClick,
                            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(40.dp)
                        ) {
                            Text(
                                text = "Get Started",
                                color = NavyDarkest,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = NavyDarkest,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(if (isLaptop) 40.dp else 24.dp))

            // 2. HERO SECTION
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 1360.dp)
                    .padding(horizontal = if (isLaptop) 32.dp else 16.dp)
            ) {
                if (isLaptop) {
                    // 2-COLUMN WIDESCREEN HERO FOR LAPTOP
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(36.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left Column: Copy & CTAs
                        Column(modifier = Modifier.weight(1.2f)) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(CyanPrimary.copy(alpha = 0.12f))
                                    .border(1.dp, CyanPrimary.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(7.dp)
                                            .clip(CircleShape)
                                            .background(CyanPrimary)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "AI-ASSISTED ATHLETE SCREENING & MOVEMENT ANALYSIS",
                                        color = CyanPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            Text(
                                text = "Precision Concussion Screening & Personal Baseline Comparison",
                                color = Color.White,
                                fontSize = 38.sp,
                                fontWeight = FontWeight.Black,
                                lineHeight = 46.sp,
                                letterSpacing = (-0.5).sp
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            Text(
                                text = "Analyze measurable eye, movement, balance, and reaction indicators against healthy personal baselines. Compare athlete assessments over time using edge computer vision and explainable AI-assisted analysis.",
                                color = TextSecondaryDark,
                                fontSize = 16.sp,
                                lineHeight = 24.sp
                            )

                            Spacer(modifier = Modifier.height(28.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = onGetStartedClick,
                                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.height(52.dp)
                                ) {
                                    Text(
                                        text = "Get Started Now",
                                        color = NavyDarkest,
                                        fontSize = 15.sp,
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

                                OutlinedButton(
                                    onClick = onLoginClick,
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
                                    modifier = Modifier.height(52.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Analytics,
                                        contentDescription = null,
                                        tint = CyanPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Explore Roster & Demo", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }

                            Spacer(modifier = Modifier.height(32.dp))

                            // 4 Quick Clinical KPI Badges
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                HeroKpiBadge("60 FPS", "Realtime Video Mesh", Icons.Default.Speed, Modifier.weight(1f))
                                HeroKpiBadge("33-Pt", "Pose Kinematics", Icons.Default.DirectionsWalk, Modifier.weight(1f))
                                HeroKpiBadge("< 1 ms", "Gaze Latency", Icons.Default.RemoveRedEye, Modifier.weight(1f))
                                HeroKpiBadge("Edge AI", "Zero Cloud Latency", Icons.Default.Shield, Modifier.weight(1f))
                            }
                        }

                        // Right Column: Interactive Real-time Telemetry Console Card
                        Column(modifier = Modifier.weight(1f)) {
                            HeroTelemetryCard(onLaunchClick = onGetStartedClick)
                        }
                    }
                } else {
                    // MOBILE / COMPACT HERO
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(CyanPrimary.copy(alpha = 0.12f))
                                .border(1.dp, CyanPrimary.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "AI-ASSISTED ATHLETE SCREENING & MOVEMENT ANALYSIS",
                                color = CyanPrimary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Precision Concussion Screening & Personal Baseline Comparison",
                            color = Color.White,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center,
                            lineHeight = 32.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Analyze measurable eye, movement, balance and reaction indicators against healthy personal baselines using computer vision.",
                            color = TextSecondaryDark,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 19.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                onClick = onGetStartedClick,
                                colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.height(48.dp)
                            ) {
                                Text(
                                    text = "Get Started",
                                    color = NavyDarkest,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = NavyDarkest,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            OutlinedButton(
                                onClick = onLoginClick,
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
                                modifier = Modifier.height(48.dp)
                            ) {
                                Text("Sign In", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }

                        Spacer(modifier = Modifier.height(22.dp))

                        HeroTelemetryCard(onLaunchClick = onGetStartedClick)
                    }
                }
            }

            Spacer(modifier = Modifier.height(if (isLaptop) 48.dp else 28.dp))

            // 3. WHY BASELINE COMPARISON MATTERS (Dual-Card Architecture)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 1360.dp)
                    .padding(horizontal = if (isLaptop) 32.dp else 16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                text = "THE BASELINE COMPARISON PARADIGM",
                                color = CyanPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Why Personal Delta Outperforms Static Population Averages",
                                color = Color.White,
                                fontSize = if (isLaptop) 22.sp else 17.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (isLaptop || isTablet) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            BaselineComparisonCard(
                                isProblem = true,
                                title = "Traditional Sideline Checks",
                                points = listOf(
                                    "Subjective observational guesswork without objective metrics",
                                    "Compares athlete against population averages, ignoring natural variations",
                                    "High false-negative rates for subtle ocular and balance deficits",
                                    "Manual stopwatch reaction timing vulnerable to human reflex delays"
                                ),
                                modifier = Modifier.weight(1f)
                            )

                            BaselineComparisonCard(
                                isProblem = false,
                                title = "NeuroTrack AI Baseline Protocol",
                                points = listOf(
                                    "Calibrates athlete's healthy personal benchmark during pre-season",
                                    "Detects micro-deviations in saccadic velocity & postural sway delta",
                                    "Objective 60fps computer vision removes human observer bias",
                                    "Explainable AI shows exact contributing biometrics with SHAP factor analysis"
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            BaselineComparisonCard(
                                isProblem = true,
                                title = "Traditional Sideline Checks",
                                points = listOf(
                                    "Subjective observational guesswork without objective metrics",
                                    "Compares athlete against population averages",
                                    "High false-negative rates for subtle ocular deficits"
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            BaselineComparisonCard(
                                isProblem = false,
                                title = "NeuroTrack AI Baseline Protocol",
                                points = listOf(
                                    "Calibrates athlete's healthy personal benchmark",
                                    "Detects micro-deviations in saccadic velocity & sway",
                                    "Explainable AI shows exact contributing biometrics"
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(if (isLaptop) 48.dp else 28.dp))

            // 4. MULTIMODAL SCREENING MODULES GRID (3 columns on Laptop)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 1360.dp)
                    .padding(horizontal = if (isLaptop) 32.dp else 16.dp)
            ) {
                Column {
                    Text(
                        text = "CORE SCREENING ARCHITECTURE",
                        color = CyanPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Multimodal Biometric Screening Tools",
                        color = Color.White,
                        fontSize = if (isLaptop) 22.sp else 18.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Four objective computer vision modules integrated into a single unified assessment workflow.",
                        color = TextSecondaryDark,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    val features = listOf(
                        FeatureItem("👁 Eye Movement Analysis", "Tracks gaze fixation, saccadic velocity, smooth pursuit jitter, and pupil stability relative to baseline.", Icons.Default.RemoveRedEye, "Ocular Kinematics"),
                        FeatureItem("🧍 Pose & Balance Analysis", "Measures center-of-mass sway, single-leg stance wobble, and postural equilibrium variance.", Icons.Default.Shield, "Postural Kinematics"),
                        FeatureItem("🚶 Dynamic Gait Analysis", "Analyzes dynamic stride symmetry, bilateral step timing, and walking cadence regularity.", Icons.Default.DirectionsWalk, "Gait Kinematics"),
                        FeatureItem("⚡ Millisecond Reaction Trials", "High-precision visual stimulus reaction trials measuring millisecond response latency & consistency.", Icons.Default.FlashOn, "Cognitive Latency"),
                        FeatureItem("📊 Baseline Delta Comparison", "Direct side-by-side synchronized evaluation against athlete's healthy personal benchmark.", Icons.Default.Analytics, "Delta Engine"),
                        FeatureItem("🧠 Explainable AI Indicators", "Transparent SHAP-weighted factor breakdown explaining exact biometric contributors without black boxes.", Icons.Default.Psychology, "Explainable AI")
                    )

                    if (isLaptop) {
                        // 3-COLUMN RESPONSIVE GRID FOR LAPTOP
                        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                features.take(3).forEach { feature ->
                                    FeatureCardItem(feature, modifier = Modifier.weight(1f))
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                features.drop(3).take(3).forEach { feature ->
                                    FeatureCardItem(feature, modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    } else if (isTablet) {
                        // 2-COLUMN GRID FOR TABLET
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            features.chunked(2).forEach { pair ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    pair.forEach { feature ->
                                        FeatureCardItem(feature, modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    } else {
                        // 1-COLUMN LIST FOR PHONE
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            features.forEach { feature ->
                                FeatureCardItem(feature, modifier = Modifier.fillMaxWidth())
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(if (isLaptop) 48.dp else 28.dp))

            // 5. HORIZONTAL 6-STEP VISUAL FLOW PIPELINE
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 1360.dp)
                    .padding(horizontal = if (isLaptop) 32.dp else 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(NavyDark, NavyCard)
                            )
                        )
                        .border(1.dp, NavyBorder, RoundedCornerShape(16.dp))
                        .padding(if (isLaptop) 24.dp else 16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "END-TO-END PIPELINE",
                                    color = CyanPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "From Video Input to Clinical Screening Dossier",
                                    color = Color.White,
                                    fontSize = if (isLaptop) 18.sp else 15.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(CyanPrimary.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "60 FPS ON-DEVICE PIPELINE",
                                    color = CyanPrimary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        val pipelineSteps = listOf(
                            Triple("1. Athlete Enrollment", "Register profile with healthy baseline", Icons.Default.CheckCircle),
                            Triple("2. Video Capture", "Standard 60fps front camera recording", Icons.Default.Speed),
                            Triple("3. AI Mesh Extraction", "Face, gaze & 33-pt pose kinematic landmarking", Icons.Default.RemoveRedEye),
                            Triple("4. Kinematic Analysis", "Sway variance, stride cadence, reaction time", Icons.Default.DirectionsWalk),
                            Triple("5. Baseline Comparison", "Personal delta calculation vs benchmark", Icons.Default.Analytics),
                            Triple("6. Clinical Dossier", "Decision-support indicators & PDF dossier", Icons.Default.Psychology)
                        )

                        if (isLaptop) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                pipelineSteps.forEach { (title, subtitle, icon) ->
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(NavyDarkest.copy(alpha = 0.7f))
                                            .border(1.dp, NavyBorder, RoundedCornerShape(10.dp))
                                            .padding(12.dp)
                                    ) {
                                        Column {
                                            Box(
                                                modifier = Modifier
                                                    .size(28.dp)
                                                    .clip(CircleShape)
                                                    .background(CyanPrimary.copy(alpha = 0.2f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = icon,
                                                    contentDescription = null,
                                                    tint = CyanPrimary,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = title,
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = subtitle,
                                                color = TextMutedDark,
                                                fontSize = 11.sp,
                                                lineHeight = 15.sp
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                pipelineSteps.forEach { (title, subtitle, icon) ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(NavyDarkest.copy(alpha = 0.6f))
                                            .padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clip(CircleShape)
                                                .background(CyanPrimary.copy(alpha = 0.2f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = icon,
                                                contentDescription = null,
                                                tint = CyanPrimary,
                                                modifier = Modifier.size(15.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(text = title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            Text(text = subtitle, color = TextMutedDark, fontSize = 10.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(if (isLaptop) 48.dp else 28.dp))

            // 6. CLINICAL SAFETY & LEGAL DISCLAIMER (Mandatory per section 4, 32, 48)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 1360.dp)
                    .padding(horizontal = if (isLaptop) 32.dp else 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(StatusDeviation.copy(alpha = 0.12f))
                        .border(1.dp, StatusDeviation.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(if (isLaptop) 20.dp else 14.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(StatusDeviation.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = StatusDeviation,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Clinical Screening Prototype Notice",
                                color = StatusDeviation,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "NeuroTrack AI is an AI-assisted screening prototype and decision-support technology designed for athletic trainers and team physicians. It does NOT diagnose concussion, provide medical clearance, or substitute for comprehensive examination by a licensed medical professional.",
                                color = Color.White.copy(alpha = 0.95f),
                                fontSize = 12.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(if (isLaptop) 48.dp else 28.dp))

            // 7. DESKTOP FOOTER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavyDark)
                    .border(0.5.dp, NavyBorder)
                    .padding(horizontal = if (isLaptop) 32.dp else 16.dp, vertical = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 1360.dp)
                        .align(Alignment.Center)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "NEUROTRACK AI",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "AI-Assisted Concussion Screening & Personal Baseline Protocol",
                                color = TextMutedDark,
                                fontSize = 11.sp
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text(
                                text = "Sign In",
                                color = CyanPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.clickable { onLoginClick() }
                            )
                            Text(
                                text = "Get Started",
                                color = CyanPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.clickable { onGetStartedClick() }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "© 2026 NeuroTrack AI • Precision Biometrics Protocol • All rights reserved.",
                        color = TextMutedDark,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }

    // Info Dialog for Top Nav Links
    if (showInfoDialog != null) {
        AlertDialog(
            onDismissRequest = { showInfoDialog = null },
            containerColor = NavyDark,
            title = {
                Text("NeuroTrack AI Specifications", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            },
            text = {
                Text(showInfoDialog ?: "", color = TextSecondaryDark, fontSize = 13.sp, lineHeight = 18.sp)
            },
            confirmButton = {
                Button(
                    onClick = { showInfoDialog = null },
                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
                ) {
                    Text("Close", color = NavyDarkest, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
private fun DesktopNavLink(
    title: String,
    onClick: () -> Unit
) {
    Text(
        text = title,
        color = TextSecondaryDark,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

@Composable
private fun HeroKpiBadge(
    metric: String,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(NavyCard)
            .border(1.dp, NavyBorder, RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        Column {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CyanPrimary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = metric, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Black)
            Text(text = label, color = TextMutedDark, fontSize = 10.sp, lineHeight = 13.sp)
        }
    }
}

@Composable
private fun HeroTelemetryCard(
    onLaunchClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(NavyCard, NavyDark)
                )
            )
            .border(1.5.dp, CyanPrimary.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .padding(18.dp)
    ) {
        Column {
            // Header bar with pulsing live indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(StatusWithinBaseline)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "REAL-TIME KINEMATICS TELEMETRY",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(CyanPrimary.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "BASELINE MATCH: 98.4%",
                        color = CyanPrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Simulated Gaze Crosshair & Radar Visualizer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF060B18))
                    .border(1.dp, NavyBorder, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Radar circles
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(1.dp, CyanPrimary.copy(alpha = 0.2f), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .border(1.dp, CyanPrimary.copy(alpha = 0.4f), CircleShape)
                )
                // Center tracking dot with ring
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(CyanPrimary.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(CyanPrimary)
                    )
                }

                Text(
                    text = "OCULAR GAZE TRACKING (X: +0.4°, Y: -0.2°)",
                    color = CyanPrimary.copy(alpha = 0.8f),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                )

                Text(
                    text = "60 FPS CAPTURE",
                    color = TextMutedDark,
                    fontSize = 9.sp,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4 Live Telemetry Gauges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TelemetryStatGauge("Saccades", "420 °/s", "Normal", StatusWithinBaseline, Modifier.weight(1f))
                TelemetryStatGauge("Balance Sway", "1.2 mm", "Stable", StatusWithinBaseline, Modifier.weight(1f))
                TelemetryStatGauge("Stride Symmetry", "98.4%", "Calibrated", StatusWithinBaseline, Modifier.weight(1f))
                TelemetryStatGauge("Reaction", "218 ms", "Baseline", CyanPrimary, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onLaunchClick,
                colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
            ) {
                Text(
                    text = "Launch Assessment Console",
                    color = NavyDarkest,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun TelemetryStatGauge(
    label: String,
    value: String,
    status: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(NavyDarkest)
            .border(0.8.dp, NavyBorder, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Column {
            Text(text = label, color = TextMutedDark, fontSize = 9.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = status, color = accent, fontSize = 9.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun BaselineComparisonCard(
    isProblem: Boolean,
    title: String,
    points: List<String>,
    modifier: Modifier = Modifier
) {
    val accentColor = if (isProblem) StatusDeviation else CyanPrimary
    val bgColor = if (isProblem) StatusDeviation.copy(alpha = 0.08f) else CyanPrimary.copy(alpha = 0.08f)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .border(1.dp, accentColor.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
            .padding(18.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isProblem) Icons.Default.Close else Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(15.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            points.forEach { point ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = if (isProblem) "✕" else "✓",
                        color = accentColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = point,
                        color = TextSecondaryDark,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun FeatureCardItem(
    feature: FeatureItem,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(NavyCard)
            .border(1.dp, NavyBorder, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(CyanPrimary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = feature.icon,
                        contentDescription = null,
                        tint = CyanPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(NavyDarkest)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = feature.category,
                        color = CyanPrimary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = feature.title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = feature.description,
                color = TextSecondaryDark,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )
        }
    }
}

private data class FeatureItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val category: String
)
