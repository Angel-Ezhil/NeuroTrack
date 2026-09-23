package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.User
import com.example.ui.navigation.Screen
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyCard
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyDarkest
import com.example.ui.theme.TextMutedDark
import com.example.ui.theme.TextSecondaryDark

data class WebNavTab(
    val label: String,
    val route: String,
    val icon: ImageVector
)

val webNavTabs = listOf(
    WebNavTab("Dashboard", Screen.Dashboard.route, Icons.Default.Dashboard),
    WebNavTab("Athletes", Screen.Athletes.route, Icons.Default.People),
    WebNavTab("Baseline Test", Screen.Baseline.createRoute(), Icons.Default.Speed),
    WebNavTab("Sideline Screening", Screen.AnalyzeSetup.createRoute(), Icons.Default.PlayArrow),
    WebNavTab("History", Screen.History.route, Icons.Default.History),
    WebNavTab("Clinical Reports", Screen.Reports.createRoute(), Icons.Default.Assessment),
    WebNavTab("Settings", Screen.Settings.route, Icons.Default.Settings)
)

@Composable
fun WebPortalTopBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    currentUser: User? = null,
    onLogoutClick: (() -> Unit)? = null,
    isWebsiteMode: Boolean = true,
    onToggleWebsiteMode: () -> Unit = {},
    isDemoMode: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(NavyDark)
            .border(width = 0.5.dp, color = NavyBorder)
            .statusBarsPadding()
    ) {
        // TOP LEVEL NAVBAR: Brand, Website Mode Toggle, Doctor Profile, Logout
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (showBackButton) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                }

                // Web Portal Logo & Title
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(NavyDarkest)
                        .border(1.2.dp, CyanPrimary, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
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
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(CyanPrimary.copy(alpha = 0.15f))
                                .border(0.5.dp, CyanPrimary.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "WEB PORTAL",
                                color = CyanPrimary,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = "Clinical Athlete Baseline & Concussion Screening Website",
                        color = TextMutedDark,
                        fontSize = 10.sp
                    )
                }
            }

            // Right side: View Mode Toggle + User Info
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Interactive Website / Mobile Toggle button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(NavyDarkest)
                        .border(1.dp, CyanPrimary.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                        .clickable { onToggleWebsiteMode() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (isWebsiteMode) "🌐 Website View" else "📱 Mobile View",
                            color = CyanPrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                if (currentUser != null) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(end = 6.dp)
                    ) {
                        Text(
                            text = currentUser.fullName,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = currentUser.role.displayName,
                            color = CyanPrimary,
                            fontSize = 9.sp
                        )
                    }

                    if (onLogoutClick != null) {
                        IconButton(
                            onClick = onLogoutClick,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(NavyDarkest)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Logout",
                                tint = TextSecondaryDark,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // HORIZONTAL WEB NAVIGATION TABS ROW
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(NavyDarkest.copy(alpha = 0.7f))
                .border(width = 0.5.dp, color = NavyBorder)
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                webNavTabs.forEach { tab ->
                    val isSelected = when {
                        tab.route.startsWith("dashboard") -> currentRoute?.startsWith("dashboard") == true
                        tab.route.startsWith("athletes") -> currentRoute?.startsWith("athletes") == true || currentRoute?.startsWith("athlete_detail") == true
                        tab.route.startsWith("baseline") -> currentRoute?.startsWith("baseline") == true
                        tab.route.startsWith("analyze") -> currentRoute?.startsWith("analyze") == true
                        tab.route.startsWith("history") -> currentRoute?.startsWith("history") == true
                        tab.route.startsWith("reports") -> currentRoute?.startsWith("reports") == true
                        tab.route.startsWith("settings") -> currentRoute?.startsWith("settings") == true
                        else -> false
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) CyanPrimary.copy(alpha = 0.15f) else Color.Transparent)
                            .border(
                                width = if (isSelected) 1.dp else 0.dp,
                                color = if (isSelected) CyanPrimary.copy(alpha = 0.6f) else Color.Transparent,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .clickable { onNavigate(tab.route) }
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.label,
                                tint = if (isSelected) CyanPrimary else TextSecondaryDark,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = tab.label,
                                color = if (isSelected) Color.White else TextSecondaryDark,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}
