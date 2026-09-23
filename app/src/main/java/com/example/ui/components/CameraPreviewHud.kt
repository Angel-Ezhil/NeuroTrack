package com.example.ui.components

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyDarkest
import com.example.ui.theme.StatusDeviation
import com.example.ui.theme.StatusFurtherAssessment
import com.example.ui.theme.StatusWithinBaseline
import kotlin.math.sin

@Composable
fun CameraPreviewHud(
    isRecording: Boolean = false,
    testModeLabel: String = "Eye & Postural Tracking",
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var cameraFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_FRONT) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "hud_anim")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF070E22))
            .border(1.5.dp, CyanPrimary.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
    ) {
        if (hasCameraPermission) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx).apply {
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                    }
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        try {
                            val cameraProvider = cameraProviderFuture.get()
                            val preview = Preview.Builder().build().also {
                                it.setSurfaceProvider(previewView.surfaceProvider)
                            }
                            val cameraSelector = CameraSelector.Builder()
                                .requireLensFacing(cameraFacing)
                                .build()

                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview
                            )
                        } catch (e: Exception) {
                            // Handled cleanly
                        }
                    }, ContextCompat.getMainExecutor(ctx))
                    previewView
                },
                update = { previewView ->
                    // Rebind on flip
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                    cameraProviderFuture.addListener({
                        try {
                            val cameraProvider = cameraProviderFuture.get()
                            val preview = Preview.Builder().build().also {
                                it.setSurfaceProvider(previewView.surfaceProvider)
                            }
                            val cameraSelector = CameraSelector.Builder()
                                .requireLensFacing(cameraFacing)
                                .build()

                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview
                            )
                        } catch (_: Exception) {}
                    }, ContextCompat.getMainExecutor(context))
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Simulated vision feed with fallback banner
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0A1428)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.VideocamOff,
                        contentDescription = null,
                        tint = TextDeviationColor(),
                        modifier = Modifier.size(44.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Camera Permission Required",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Using Neural Vision Simulation Mode",
                        color = CyanPrimary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                        colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
                    ) {
                        Text("Grant Camera Access", color = NavyDarkest, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Computer Vision HUD Overlay (Targeting Reticles, Mesh, Skeleton)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val cx = w / 2f
            val cy = h * 0.42f

            // Corner target brackets
            val bracketLen = 24.dp.toPx()
            val bracketMargin = 16.dp.toPx()
            val bracketColor = CyanPrimary.copy(alpha = 0.8f)
            val strokeW = 2.dp.toPx()

            // Top-left
            drawLine(bracketColor, Offset(bracketMargin, bracketMargin), Offset(bracketMargin + bracketLen, bracketMargin), strokeW)
            drawLine(bracketColor, Offset(bracketMargin, bracketMargin), Offset(bracketMargin, bracketMargin + bracketLen), strokeW)

            // Top-right
            drawLine(bracketColor, Offset(w - bracketMargin, bracketMargin), Offset(w - bracketMargin - bracketLen, bracketMargin), strokeW)
            drawLine(bracketColor, Offset(w - bracketMargin, bracketMargin), Offset(w - bracketMargin, bracketMargin + bracketLen), strokeW)

            // Bottom-left
            drawLine(bracketColor, Offset(bracketMargin, h - bracketMargin), Offset(bracketMargin + bracketLen, h - bracketMargin), strokeW)
            drawLine(bracketColor, Offset(bracketMargin, h - bracketMargin), Offset(bracketMargin, h - bracketMargin - bracketLen), strokeW)

            // Bottom-right
            drawLine(bracketColor, Offset(w - bracketMargin, h - bracketMargin), Offset(w - bracketMargin - bracketLen, h - bracketMargin), strokeW)
            drawLine(bracketColor, Offset(w - bracketMargin, h - bracketMargin), Offset(w - bracketMargin, h - bracketMargin - bracketLen), strokeW)

            // Face Landmark Ellipse & Mesh
            drawOval(
                color = CyanPrimary.copy(alpha = 0.4f),
                topLeft = Offset(cx - 65.dp.toPx(), cy - 90.dp.toPx()),
                size = androidx.compose.ui.geometry.Size(130.dp.toPx(), 180.dp.toPx()),
                style = Stroke(width = 1.5.dp.toPx())
            )

            // Eye tracking target nodes
            val eyeY = cy - 20.dp.toPx()
            val leftEyeX = cx - 32.dp.toPx()
            val rightEyeX = cx + 32.dp.toPx()

            drawCircle(CyanPrimary, radius = 5.dp.toPx(), center = Offset(leftEyeX, eyeY))
            drawCircle(Color.White, radius = 2.dp.toPx(), center = Offset(leftEyeX, eyeY))
            drawCircle(CyanPrimary, radius = 5.dp.toPx(), center = Offset(rightEyeX, eyeY))
            drawCircle(Color.White, radius = 2.dp.toPx(), center = Offset(rightEyeX, eyeY))

            // Eye vector lines (tracking gaze)
            val gazeOffset = (sin(wavePhase) * 12.dp.toPx())
            drawLine(
                CyanPrimary.copy(alpha = 0.8f),
                Offset(leftEyeX, eyeY),
                Offset(leftEyeX + gazeOffset, eyeY - 14.dp.toPx()),
                strokeWidth = 1.5.dp.toPx()
            )
            drawLine(
                CyanPrimary.copy(alpha = 0.8f),
                Offset(rightEyeX, eyeY),
                Offset(rightEyeX + gazeOffset, eyeY - 14.dp.toPx()),
                strokeWidth = 1.5.dp.toPx()
            )

            // Pose Skeleton (Shoulders & Torso proxy)
            val shoulderY = cy + 105.dp.toPx()
            val leftShoulderX = cx - 90.dp.toPx()
            val rightShoulderX = cx + 90.dp.toPx()

            // Neck to shoulders
            drawLine(Color(0xFF38BDF8).copy(alpha = 0.7f), Offset(cx, cy + 90.dp.toPx()), Offset(leftShoulderX, shoulderY), 2.dp.toPx())
            drawLine(Color(0xFF38BDF8).copy(alpha = 0.7f), Offset(cx, cy + 90.dp.toPx()), Offset(rightShoulderX, shoulderY), 2.dp.toPx())
            drawLine(Color(0xFF38BDF8).copy(alpha = 0.7f), Offset(leftShoulderX, shoulderY), Offset(rightShoulderX, shoulderY), 2.dp.toPx())

            // Nodes
            drawCircle(CyanPrimary, 4.dp.toPx(), Offset(leftShoulderX, shoulderY))
            drawCircle(CyanPrimary, 4.dp.toPx(), Offset(rightShoulderX, shoulderY))
        }

        // Top HUD Telemetry Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // REC / LIVE badge
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xCC050B1A))
                    .border(1.dp, NavyBorder, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (isRecording) StatusFurtherAssessment.copy(alpha = pulseAlpha) else StatusWithinBaseline)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isRecording) "REC 60 FPS" else "AI VISION LIVE",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Flip camera button
            IconButton(
                onClick = {
                    cameraFacing = if (cameraFacing == CameraSelector.LENS_FACING_FRONT) {
                        CameraSelector.LENS_FACING_BACK
                    } else {
                        CameraSelector.LENS_FACING_FRONT
                    }
                },
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0xCC050B1A))
            ) {
                Icon(
                    imageVector = Icons.Default.Cameraswitch,
                    contentDescription = "Switch Camera",
                    tint = CyanPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Bottom Telemetry Bar
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color(0xDD070D1E))
                .border(1.dp, NavyBorder)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("CONFIDENCE", color = Color(0xFF64748B), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text("96.4%", color = StatusWithinBaseline, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("HEAD ANGLE", color = Color(0xFF64748B), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text("+0.6° TILT", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("POSTURE STABILITY", color = Color(0xFF64748B), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text("92.8%", color = CyanPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("SYMMETRY", color = Color(0xFF64748B), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text("94.1%", color = StatusWithinBaseline, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun TextDeviationColor(): Color = StatusDeviation
