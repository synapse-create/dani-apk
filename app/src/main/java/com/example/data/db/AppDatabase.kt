package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.models.UserProfile
import com.example.data.models.ReadingHistory
import com.example.data.models.TestResult
import com.example.data.models.ActivityProgress

@Database(
    entities = [
        UserProfile::class,
        ReadingHistory::class,
        TestResult::class,
        ActivityProgress::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun readingHistoryDao(): ReadingHistoryDao
    abstract fun testResultDao(): TestResultDao
    abstract fun activityProgressDao(): ActivityProgressDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "spiritual_sanctuary_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
