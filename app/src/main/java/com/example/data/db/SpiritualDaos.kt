package com.example.data.db

import androidx.room.*
import com.example.data.models.UserProfile
import com.example.data.models.ReadingHistory
import com.example.data.models.TestResult
import com.example.data.models.ActivityProgress
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    suspend fun getUserProfileOneShot(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfile)
}

@Dao
interface ReadingHistoryDao {
    @Query("SELECT * FROM reading_history ORDER BY timestamp DESC")
    fun getAllReadings(): Flow<List<ReadingHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReading(reading: ReadingHistory)

    @Query("DELETE FROM reading_history WHERE id = :id")
    suspend fun deleteReadingById(id: Int)

    @Query("DELETE FROM reading_history")
    suspend fun clearHistory()
}

@Dao
interface TestResultDao {
    @Query("SELECT * FROM test_result ORDER BY timestamp DESC")
    fun getAllTestResults(): Flow<List<TestResult>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestResult(result: TestResult)
}

@Dao
interface ActivityProgressDao {
    @Query("SELECT * FROM activity_progress")
    fun getAllProgress(): Flow<List<ActivityProgress>>

    @Query("SELECT * FROM activity_progress WHERE id = :activityId LIMIT 1")
    suspend fun getProgressForActivity(activityId: String): ActivityProgress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: ActivityProgress)

    @Query("UPDATE activity_progress SET isFavorite = :favorite WHERE id = :activityId")
    suspend fun updateFavorite(activityId: String, favorite: Boolean)
}
