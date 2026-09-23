package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.StatusDeviation
import com.example.ui.theme.StatusDeviationBg
import com.example.ui.theme.StatusFurtherAssessment
import com.example.ui.theme.StatusFurtherAssessmentBg
import com.example.ui.theme.StatusWithinBaseline
import com.example.ui.theme.StatusWithinBaselineBg

enum class AssessmentStatus(
    val label: String,
    val description: String,
    val color: Color,
    val bgTint: Color
) {
    WITHIN_BASELINE(
        label = "WITHIN BASELINE",
        description = "Movement, ocular, and reaction metrics align with the athlete's documented baseline profile.",
        color = StatusWithinBaseline,
        bgTint = StatusWithinBaselineBg
    ),
    DEVIATION_DETECTED(
        label = "DEVIATION DETECTED",
        description = "Measurable variances observed compared to baseline. Continued observation and secondary screening recommended.",
        color = StatusDeviation,
        bgTint = StatusDeviationBg
    ),
    FURTHER_ASSESSMENT_RECOMMENDED(
        label = "FURTHER CLINICAL ASSESSMENT RECOMMENDED",
        description = "Elevated multi-modal deviations detected across ocular, postural, or reaction metrics. Direct medical evaluation advised.",
        color = StatusFurtherAssessment,
        bgTint = StatusFurtherAssessmentBg
    )
}

enum class AssessmentType(val label: String) {
    BASELINE("Healthy Baseline"),
    BEFORE_AFTER("Before vs After Comparison"),
    LIVE_CAMERA("Live Camera Assessment"),
    FOLLOW_UP("Follow-up Assessment")
}
