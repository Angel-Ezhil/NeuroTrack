package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "athletes")
data class Athlete(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val sport: String,
    val age: Int,
    val position: String = "",
    val jerseyNumber: String = "",
    val avatarIndex: Int = 0,
    val hasBaseline: Boolean = false,
    val baselineEyeStability: Float = 92f,
    val baselineBalanceStability: Float = 94f,
    val baselineGaitConsistency: Float = 90f,
    val baselineMovementSymmetry: Float = 93f,
    val baselineReactionTimeMs: Int = 310,
    val baselineHeadStability: Float = 91f,
    val assessmentCount: Int = 0,
    val latestAssessmentDate: Long = System.currentTimeMillis(),
    val latestStatus: AssessmentStatus = AssessmentStatus.WITHIN_BASELINE,
    val createdAt: Long = System.currentTimeMillis()
)
