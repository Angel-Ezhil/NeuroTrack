package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyCard
import com.example.ui.theme.StatusDeviation
import com.example.ui.theme.StatusFurtherAssessment
import com.example.ui.theme.StatusWithinBaseline
import com.example.ui.theme.TextMutedDark
import com.example.ui.theme.TextSecondaryDark
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun MetricCard(
    title: String,
    currentValue: String,
    baselineValue: String,
    changePercent: Float,
    isReactionTime: Boolean = false,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    // For reaction time, increase is degraded; for stability scores (0-100), decrease is degraded
    val isDegraded = if (isReactionTime) changePercent > 10f else changePercent < -10f
    val isSeverelyDegraded = if (isReactionTime) changePercent > 25f else changePercent < -22f

    val accentColor = when {
        isSeverelyDegraded -> StatusFurtherAssessment
        isDegraded -> StatusDeviation
        else -> StatusWithinBaseline
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(NavyCard)
            .border(1.dp, NavyBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(NavyBorder.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = title,
                            tint = CyanPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Delta tag
                val changeFormatted = "${if (changePercent > 0) "+" else ""}${changePercent.roundToInt()}%"
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(accentColor.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (changePercent > 0) Icons.Default.ArrowUpward else if (changePercent < 0) Icons.Default.ArrowDownward else Icons.Default.Check,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = changeFormatted,
                        color = accentColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "Current Value",
                        color = TextMutedDark,
                        fontSize = 11.sp
                    )
                    Text(
                        text = currentValue,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Personal Baseline",
                        color = TextMutedDark,
                        fontSize = 11.sp
                    )
                    Text(
                        text = baselineValue,
                        color = CyanPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(NavyBorder)
            ) {
                val progressFrac = if (isReactionTime) {
                    // map 200ms - 600ms to 1f - 0f
                    val rx = currentValue.filter { it.isDigit() }.toFloatOrNull() ?: 300f
                    (1f - ((rx - 200f) / 400f)).coerceIn(0.1f, 1f)
                } else {
                    val score = currentValue.filter { it.isDigit() }.toFloatOrNull() ?: 80f
                    (score / 100f).coerceIn(0f, 1f)
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth(progressFrac)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(accentColor)
                )
            }
        }
    }
}
