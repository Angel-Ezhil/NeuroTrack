package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assessments")
data class Assessment(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val athleteId: Long,
    val athleteName: String,
    val sport: String,
    val type: AssessmentType,
    val timestamp: Long = System.currentTimeMillis(),
    
    // Measured Current Metrics
    val eyeStability: Float,
    val balanceStability: Float,
    val gaitConsistency: Float,
    val movementSymmetry: Float,
    val reactionTimeMs: Int,
    val headStability: Float,
    
    // Personal Baseline Snapshot at time of assessment
    val baselineEyeStability: Float = 92f,
    val baselineBalanceStability: Float = 94f,
    val baselineGaitConsistency: Float = 90f,
    val baselineMovementSymmetry: Float = 93f,
    val baselineReactionTimeMs: Int = 310,
    val baselineHeadStability: Float = 91f,
    
    // Deviations (%)
    val eyeDeviationPercent: Float = 0f,
    val balanceDeviationPercent: Float = 0f,
    val gaitDeviationPercent: Float = 0f,
    val reactionDeviationPercent: Float = 0f,
    val symmetryDeviationPercent: Float = 0f,
    val overallDeviationPercent: Float = 0f,
    
    // Screening outcome & Explainable AI
    val status: AssessmentStatus = AssessmentStatus.WITHIN_BASELINE,
    val primaryContributingIndicator: String = "All parameters within baseline variance",
    val secondaryContributingIndicator: String = "Postural sway within normal threshold",
    val notes: String = "",
    val isDemoData: Boolean = true,
    val beforeVideoLabel: String = "Pre-game Baseline Video (Recorded)",
    val afterVideoLabel: String = "Post-impact Sideline Video (Recorded)"
)
