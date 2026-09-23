package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.Assessment
import kotlinx.coroutines.flow.Flow

@Dao
interface AssessmentDao {
    @Query("SELECT * FROM assessments ORDER BY timestamp DESC")
    fun getAllAssessments(): Flow<List<Assessment>>

    @Query("SELECT * FROM assessments WHERE athleteId = :athleteId ORDER BY timestamp DESC")
    fun getAssessmentsForAthlete(athleteId: Long): Flow<List<Assessment>>

    @Query("SELECT * FROM assessments WHERE id = :id")
    fun getAssessmentById(id: Long): Flow<Assessment?>

    @Query("SELECT * FROM assessments WHERE id = :id")
    suspend fun getAssessmentByIdSync(id: Long): Assessment?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssessment(assessment: Assessment): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(assessments: List<Assessment>)

    @Query("DELETE FROM assessments WHERE id = :id")
    suspend fun deleteAssessment(id: Long)

    @Query("SELECT COUNT(*) FROM assessments")
    fun getAssessmentCount(): Flow<Int>
}
