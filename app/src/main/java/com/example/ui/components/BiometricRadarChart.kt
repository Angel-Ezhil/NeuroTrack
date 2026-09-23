package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
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
import kotlin.math.cos
import kotlin.math.sin

data class RadarAxisData(
    val label: String,
    val baselineValue: Float, // 0 to 100
    val currentValue: Float   // 0 to 100
)

@Composable
fun BiometricRadarChart(
    axes: List<RadarAxisData>,
    currentStatusColor: Color = StatusDeviation,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(NavyCard)
            .border(1.dp, NavyBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Multimodal Biometric Comparison",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Legend
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(CyanPrimary)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Personal Baseline",
                    color = CyanPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.width(20.dp))

                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(currentStatusColor)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Current Assessment",
                    color = currentStatusColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
            ) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val radius = (minOf(size.width, size.height) / 2f) * 0.72f
                val count = axes.size
                val angleStep = (2 * Math.PI / count).toFloat()

                // Draw concentric background web
                val webRings = listOf(0.25f, 0.5f, 0.75f, 1.0f)
                for (frac in webRings) {
                    val ringPath = Path()
                    for (i in 0 until count) {
                        val angle = (i * angleStep) - (Math.PI / 2).toFloat()
                        val x = center.x + (radius * frac * cos(angle))
                        val y = center.y + (radius * frac * sin(angle))
                        if (i == 0) ringPath.moveTo(x, y) else ringPath.lineTo(x, y)
                    }
                    ringPath.close()
                    drawPath(
                        path = ringPath,
                        color = NavyBorder.copy(alpha = 0.6f),
                        style = Stroke(width = 1.dp.toPx())
                    )
                }

                // Draw radial spokes and axis labels
                for (i in 0 until count) {
                    val angle = (i * angleStep) - (Math.PI / 2).toFloat()
                    val x = center.x + (radius * cos(angle))
                    val y = center.y + (radius * sin(angle))
                    drawLine(
                        color = NavyBorder,
                        start = center,
                        end = Offset(x, y),
                        strokeWidth = 1.dp.toPx()
                    )

                    // Draw text label on native canvas
                    val labelRadius = radius * 1.25f
                    val lx = center.x + (labelRadius * cos(angle))
                    val ly = center.y + (labelRadius * sin(angle)) + 4.dp.toPx()

                    drawContext.canvas.nativeCanvas.apply {
                        val paint = android.graphics.Paint().apply {
                            color = android.graphics.Color.WHITE
                            textSize = 24f
                            textAlign = android.graphics.Paint.Align.CENTER
                            isAntiAlias = true
                        }
                        drawText(axes[i].label, lx, ly, paint)
                    }
                }

                // Draw Baseline Polygon (Cyan)
                val basePath = Path()
                for (i in 0 until count) {
                    val angle = (i * angleStep) - (Math.PI / 2).toFloat()
                    val frac = (axes[i].baselineValue / 100f).coerceIn(0.1f, 1f)
                    val x = center.x + (radius * frac * cos(angle))
                    val y = center.y + (radius * frac * sin(angle))
                    if (i == 0) basePath.moveTo(x, y) else basePath.lineTo(x, y)
                }
                basePath.close()
                drawPath(path = basePath, color = CyanPrimary.copy(alpha = 0.2f))
                drawPath(
                    path = basePath,
                    color = CyanPrimary,
                    style = Stroke(width = 2.dp.toPx())
                )

                // Draw Current Assessment Polygon
                val currPath = Path()
                for (i in 0 until count) {
                    val angle = (i * angleStep) - (Math.PI / 2).toFloat()
                    val frac = (axes[i].currentValue / 100f).coerceIn(0.1f, 1f)
                    val x = center.x + (radius * frac * cos(angle))
                    val y = center.y + (radius * frac * sin(angle))
                    if (i == 0) currPath.moveTo(x, y) else currPath.lineTo(x, y)
                }
                currPath.close()
                drawPath(path = currPath, color = currentStatusColor.copy(alpha = 0.25f))
                drawPath(
                    path = currPath,
                    color = currentStatusColor,
                    style = Stroke(width = 2.5.dp.toPx())
                )
            }
        }
    }
}
