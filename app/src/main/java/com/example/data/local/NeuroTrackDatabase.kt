package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.Assessment
import com.example.data.model.AssessmentStatus
import com.example.data.model.AssessmentType
import com.example.data.model.Athlete
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Athlete::class, Assessment::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class NeuroTrackDatabase : RoomDatabase() {
    abstract fun athleteDao(): AthleteDao
    abstract fun assessmentDao(): AssessmentDao

    companion object {
        @Volatile
        private var INSTANCE: NeuroTrackDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): NeuroTrackDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NeuroTrackDatabase::class.java,
                    "neurotrack_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.athleteDao(), database.assessmentDao())
                    }
                }
            }
        }

        private suspend fun populateInitialData(athleteDao: AthleteDao, assessmentDao: AssessmentDao) {
            val athlete1 = Athlete(
                id = 1,
                name = "Tyler Montgomery",
                sport = "Football",
                age = 21,
                position = "Wide Receiver",
                jerseyNumber = "84",
                avatarIndex = 0,
                hasBaseline = true,
                baselineEyeStability = 94f,
                baselineBalanceStability = 95f,
                baselineGaitConsistency = 92f,
                baselineMovementSymmetry = 95f,
                baselineReactionTimeMs = 285,
                baselineHeadStability = 93f,
                assessmentCount = 3,
                latestStatus = AssessmentStatus.DEVIATION_DETECTED
            )

            val athlete2 = Athlete(
                id = 2,
                name = "Elena Rostova",
                sport = "Soccer",
                age = 19,
                position = "Midfielder",
                jerseyNumber = "10",
                avatarIndex = 1,
                hasBaseline = true,
                baselineEyeStability = 92f,
                baselineBalanceStability = 93f,
                baselineGaitConsistency = 94f,
                baselineMovementSymmetry = 92f,
                baselineReactionTimeMs = 295,
                baselineHeadStability = 94f,
                assessmentCount = 2,
                latestStatus = AssessmentStatus.WITHIN_BASELINE
            )

            val athlete3 = Athlete(
                id = 3,
                name = "Marcus De Silva",
                sport = "Ice Hockey",
                age = 23,
                position = "Defenseman",
                jerseyNumber = "4",
                avatarIndex = 2,
                hasBaseline = true,
                baselineEyeStability = 93f,
                baselineBalanceStability = 91f,
                baselineGaitConsistency = 89f,
                baselineMovementSymmetry = 90f,
                baselineReactionTimeMs = 310,
                baselineHeadStability = 92f,
                assessmentCount = 4,
                latestStatus = AssessmentStatus.FURTHER_ASSESSMENT_RECOMMENDED
            )

            val athlete4 = Athlete(
                id = 4,
                name = "Kelsey Jordan",
                sport = "Basketball",
                age = 20,
                position = "Point Guard",
                jerseyNumber = "3",
                avatarIndex = 3,
                hasBaseline = false,
                assessmentCount = 0,
                latestStatus = AssessmentStatus.WITHIN_BASELINE
            )

            athleteDao.insertAll(listOf(athlete1, athlete2, athlete3, athlete4))

            // Seed initial assessments
            val now = System.currentTimeMillis()
            val day = 86400000L

            val assess1 = Assessment(
                id = 101,
                athleteId = 1,
                athleteName = "Tyler Montgomery",
                sport = "Football",
                type = AssessmentType.BEFORE_AFTER,
                timestamp = now - day * 1,
                eyeStability = 79f,
                balanceStability = 78f,
                gaitConsistency = 84f,
                movementSymmetry = 81f,
                reactionTimeMs = 410,
                headStability = 76f,
                baselineEyeStability = 94f,
                baselineBalanceStability = 95f,
                baselineGaitConsistency = 92f,
                baselineMovementSymmetry = 95f,
                baselineReactionTimeMs = 285,
                baselineHeadStability = 93f,
                eyeDeviationPercent = -16.0f,
                balanceDeviationPercent = -17.9f,
                gaitDeviationPercent = -8.7f,
                reactionDeviationPercent = 43.8f,
                symmetryDeviationPercent = -14.7f,
                overallDeviationPercent = 20.2f,
                status = AssessmentStatus.DEVIATION_DETECTED,
                primaryContributingIndicator = "Elevated reaction time latency (+125ms from baseline)",
                secondaryContributingIndicator = "Ocular saccade velocity reduction & tracking jitter",
                notes = "Sideline assessment following second-quarter helmet-to-turf contact.",
                isDemoData = true
            )

            val assess2 = Assessment(
                id = 102,
                athleteId = 2,
                athleteName = "Elena Rostova",
                sport = "Soccer",
                type = AssessmentType.LIVE_CAMERA,
                timestamp = now - day * 2,
                eyeStability = 91f,
                balanceStability = 92f,
                gaitConsistency = 93f,
                movementSymmetry = 91f,
                reactionTimeMs = 302,
                headStability = 93f,
                baselineEyeStability = 92f,
                baselineBalanceStability = 93f,
                baselineGaitConsistency = 94f,
                baselineMovementSymmetry = 92f,
                baselineReactionTimeMs = 295,
                baselineHeadStability = 94f,
                eyeDeviationPercent = -1.1f,
                balanceDeviationPercent = -1.1f,
                gaitDeviationPercent = -1.1f,
                reactionDeviationPercent = 2.4f,
                symmetryDeviationPercent = -1.1f,
                overallDeviationPercent = 1.4f,
                status = AssessmentStatus.WITHIN_BASELINE,
                primaryContributingIndicator = "All biometrics within acceptable baseline bounds",
                secondaryContributingIndicator = "Saccadic tracking & balance stability nominal",
                notes = "Routine mid-season monitoring assessment.",
                isDemoData = true
            )

            val assess3 = Assessment(
                id = 103,
                athleteId = 3,
                athleteName = "Marcus De Silva",
                sport = "Ice Hockey",
                type = AssessmentType.BEFORE_AFTER,
                timestamp = now - day * 3,
                eyeStability = 68f,
                balanceStability = 65f,
                gaitConsistency = 71f,
                movementSymmetry = 70f,
                reactionTimeMs = 485,
                headStability = 67f,
                baselineEyeStability = 93f,
                baselineBalanceStability = 91f,
                baselineGaitConsistency = 89f,
                baselineMovementSymmetry = 90f,
                baselineReactionTimeMs = 310,
                baselineHeadStability = 92f,
                eyeDeviationPercent = -26.9f,
                balanceDeviationPercent = -28.6f,
                gaitDeviationPercent = -20.2f,
                reactionDeviationPercent = 56.5f,
                symmetryDeviationPercent = -22.2f,
                overallDeviationPercent = 30.8f,
                status = AssessmentStatus.FURTHER_ASSESSMENT_RECOMMENDED,
                primaryContributingIndicator = "Pronounced postural instability during single-leg stance",
                secondaryContributingIndicator = "Severe reaction time degradation (+175ms)",
                notes = "Evaluated after hard collision into rink board. Player reporting neck soreness.",
                isDemoData = true
            )

            assessmentDao.insertAll(listOf(assess1, assess2, assess3))
        }
    }
}
