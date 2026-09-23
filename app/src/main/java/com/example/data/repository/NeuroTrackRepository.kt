package com.example.data.repository

import com.example.data.local.AssessmentDao
import com.example.data.local.AthleteDao
import com.example.data.model.Assessment
import com.example.data.model.AssessmentStatus
import com.example.data.model.AssessmentType
import com.example.data.model.Athlete
import kotlinx.coroutines.flow.Flow
import kotlin.math.abs
import kotlin.math.roundToInt

data class RawScreeningMetrics(
    val eyeStability: Float,
    val balanceStability: Float,
    val gaitConsistency: Float,
    val movementSymmetry: Float,
    val reactionTimeMs: Int,
    val headStability: Float,
    val notes: String = "",
    val beforeVideoLabel: String = "Pre-assessment Recording",
    val afterVideoLabel: String = "Post-assessment Recording"
)

class NeuroTrackRepository(
    private val athleteDao: AthleteDao,
    private val assessmentDao: AssessmentDao
) {
    val athletes: Flow<List<Athlete>> = athleteDao.getAllAthletes()
    val assessments: Flow<List<Assessment>> = assessmentDao.getAllAssessments()
    val athleteCount: Flow<Int> = athleteDao.getAthleteCount()
    val assessmentCount: Flow<Int> = assessmentDao.getAssessmentCount()

    fun getAthleteById(id: Long): Flow<Athlete?> = athleteDao.getAthleteById(id)
    suspend fun getAthleteByIdSync(id: Long): Athlete? = athleteDao.getAthleteByIdSync(id)

    fun getAssessmentsForAthlete(athleteId: Long): Flow<List<Assessment>> =
        assessmentDao.getAssessmentsForAthlete(athleteId)

    fun getAssessmentById(id: Long): Flow<Assessment?> = assessmentDao.getAssessmentById(id)
    suspend fun getAssessmentByIdSync(id: Long): Assessment? = assessmentDao.getAssessmentByIdSync(id)

    suspend fun insertAthlete(athlete: Athlete): Long = athleteDao.insertAthlete(athlete)
    suspend fun updateAthlete(athlete: Athlete) = athleteDao.updateAthlete(athlete)
    suspend fun deleteAthlete(id: Long) = athleteDao.deleteAthlete(id)

    suspend fun saveBaseline(
        athleteId: Long,
        eyeStability: Float,
        balanceStability: Float,
        gaitConsistency: Float,
        movementSymmetry: Float,
        reactionTimeMs: Int,
        headStability: Float
    ): Long {
        val athlete = athleteDao.getAthleteByIdSync(athleteId) ?: return -1L
        val updatedAthlete = athlete.copy(
            hasBaseline = true,
            baselineEyeStability = eyeStability,
            baselineBalanceStability = balanceStability,
            baselineGaitConsistency = gaitConsistency,
            baselineMovementSymmetry = movementSymmetry,
            baselineReactionTimeMs = reactionTimeMs,
            baselineHeadStability = headStability,
            latestAssessmentDate = System.currentTimeMillis()
        )
        athleteDao.updateAthlete(updatedAthlete)

        val baselineAssessment = Assessment(
            athleteId = athleteId,
            athleteName = athlete.name,
            sport = athlete.sport,
            type = AssessmentType.BASELINE,
            timestamp = System.currentTimeMillis(),
            eyeStability = eyeStability,
            balanceStability = balanceStability,
            gaitConsistency = gaitConsistency,
            movementSymmetry = movementSymmetry,
            reactionTimeMs = reactionTimeMs,
            headStability = headStability,
            baselineEyeStability = eyeStability,
            baselineBalanceStability = balanceStability,
            baselineGaitConsistency = gaitConsistency,
            baselineMovementSymmetry = movementSymmetry,
            baselineReactionTimeMs = reactionTimeMs,
            baselineHeadStability = headStability,
            status = AssessmentStatus.WITHIN_BASELINE,
            primaryContributingIndicator = "Healthy personal baseline established and recorded",
            secondaryContributingIndicator = "Calibrated benchmark for future comparative assessments",
            notes = "Supervised baseline calibration under healthy, symptom-free conditions.",
            isDemoData = false
        )
        return assessmentDao.insertAssessment(baselineAssessment)
    }

    suspend fun recordAssessment(
        athleteId: Long,
        type: AssessmentType,
        metrics: RawScreeningMetrics
    ): Long {
        val athlete = athleteDao.getAthleteByIdSync(athleteId) ?: return -1L

        val bEye = athlete.baselineEyeStability
        val bBalance = athlete.baselineBalanceStability
        val bGait = athlete.baselineGaitConsistency
        val bSym = athlete.baselineMovementSymmetry
        val bReaction = athlete.baselineReactionTimeMs.toFloat()
        val bHead = athlete.baselineHeadStability

        // Compute percentage deviations
        val eyeDev = ((metrics.eyeStability - bEye) / bEye) * 100f
        val balDev = ((metrics.balanceStability - bBalance) / bBalance) * 100f
        val gaitDev = ((metrics.gaitConsistency - bGait) / bGait) * 100f
        val symDev = ((metrics.movementSymmetry - bSym) / bSym) * 100f
        val rxDev = ((metrics.reactionTimeMs - bReaction) / bReaction) * 100f // positive is slower
        val headDev = ((metrics.headStability - bHead) / bHead) * 100f

        // Explainable indicators & classification logic (multimodal feature aggregation)
        val degradations = listOf(
            "Reaction Latency" to rxDev, // > 0 is bad
            "Ocular Stability" to -eyeDev, // negative is bad
            "Postural Sway" to -balDev,
            "Gait Rhythm" to -gaitDev,
            "Bilateral Symmetry" to -symDev,
            "Cervical/Head Stability" to -headDev
        ).sortedByDescending { it.second }

        val worstDegradation = degradations.first()
        val secondWorst = degradations[1]

        val overallDev = (
            abs(eyeDev) * 0.2f +
            abs(balDev) * 0.25f +
            abs(gaitDev) * 0.15f +
            abs(symDev) * 0.15f +
            abs(rxDev) * 0.25f
        )

        val status = when {
            worstDegradation.second >= 25f || (worstDegradation.second >= 18f && secondWorst.second >= 15f) ->
                AssessmentStatus.FURTHER_ASSESSMENT_RECOMMENDED
            worstDegradation.second >= 12f || overallDev >= 12f ->
                AssessmentStatus.DEVIATION_DETECTED
            else ->
                AssessmentStatus.WITHIN_BASELINE
        }

        val primaryIndicator = when {
            status == AssessmentStatus.WITHIN_BASELINE ->
                "All parameters within standard baseline variance threshold (±10%)"
            worstDegradation.first == "Reaction Latency" ->
                "Elevated reaction time latency (+${(metrics.reactionTimeMs - athlete.baselineReactionTimeMs)}ms vs baseline)"
            worstDegradation.first == "Postural Sway" ->
                "Measurable decrease in single-leg postural balance stability (${balDev.roundToInt()}%)"
            worstDegradation.first == "Ocular Stability" ->
                "Ocular gaze fixation instability and saccadic tracking variance (${eyeDev.roundToInt()}%)"
            worstDegradation.first == "Bilateral Symmetry" ->
                "Asymmetrical kinetic gait/movement distribution observed (${symDev.roundToInt()}%)"
            else ->
                "${worstDegradation.first} deviation (${worstDegradation.second.roundToInt()}%) outside baseline"
        }

        val secondaryIndicator = when {
            status == AssessmentStatus.WITHIN_BASELINE ->
                "Bilateral coordination and gaze consistency fully nominal"
            else ->
                "Secondary contributing factor: ${secondWorst.first} (${secondWorst.second.roundToInt()}% variance)"
        }

        val assessment = Assessment(
            athleteId = athleteId,
            athleteName = athlete.name,
            sport = athlete.sport,
            type = type,
            timestamp = System.currentTimeMillis(),
            eyeStability = metrics.eyeStability,
            balanceStability = metrics.balanceStability,
            gaitConsistency = metrics.gaitConsistency,
            movementSymmetry = metrics.movementSymmetry,
            reactionTimeMs = metrics.reactionTimeMs,
            headStability = metrics.headStability,
            baselineEyeStability = bEye,
            baselineBalanceStability = bBalance,
            baselineGaitConsistency = bGait,
            baselineMovementSymmetry = bSym,
            baselineReactionTimeMs = athlete.baselineReactionTimeMs,
            baselineHeadStability = bHead,
            eyeDeviationPercent = eyeDev,
            balanceDeviationPercent = balDev,
            gaitDeviationPercent = gaitDev,
            reactionDeviationPercent = rxDev,
            symmetryDeviationPercent = symDev,
            overallDeviationPercent = overallDev,
            status = status,
            primaryContributingIndicator = primaryIndicator,
            secondaryContributingIndicator = secondaryIndicator,
            notes = metrics.notes,
            isDemoData = false,
            beforeVideoLabel = metrics.beforeVideoLabel,
            afterVideoLabel = metrics.afterVideoLabel
        )

        val id = assessmentDao.insertAssessment(assessment)

        // Update athlete's latest status and assessment count
        athleteDao.updateAthlete(
            athlete.copy(
                assessmentCount = athlete.assessmentCount + 1,
                latestAssessmentDate = System.currentTimeMillis(),
                latestStatus = status
            )
        )

        return id
    }
}
