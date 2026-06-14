package com.example.a216487_cikguizwan_lab01

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {

    @Upsert // 👈 Changed from @Update to @Upsert to eliminate row conflicts
    suspend fun saveUserProfile(profile: UserProfileEntity)

    @Query("SELECT * FROM user_profile_table WHERE id = 1")
    fun getUserProfileFlow(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile_table WHERE id = 1 LIMIT 1")
    suspend fun getUserProfile(): UserProfileEntity?

}