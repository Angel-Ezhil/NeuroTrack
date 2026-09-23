package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Athlete
import kotlinx.coroutines.flow.Flow

@Dao
interface AthleteDao {
    @Query("SELECT * FROM athletes ORDER BY name ASC")
    fun getAllAthletes(): Flow<List<Athlete>>

    @Query("SELECT * FROM athletes WHERE id = :id")
    fun getAthleteById(id: Long): Flow<Athlete?>

    @Query("SELECT * FROM athletes WHERE id = :id")
    suspend fun getAthleteByIdSync(id: Long): Athlete?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAthlete(athlete: Athlete): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(athletes: List<Athlete>)

    @Update
    suspend fun updateAthlete(athlete: Athlete)

    @Query("DELETE FROM athletes WHERE id = :id")
    suspend fun deleteAthlete(id: Long)

    @Query("SELECT COUNT(*) FROM athletes")
    fun getAthleteCount(): Flow<Int>
}
