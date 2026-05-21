package com.example.a216487_cikguizwan_lab01

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "applied_jobs_table")
data class JobApplicationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val jobTitle: String,
    val companyName: String,
    val location: String,
    val salaryRange: String,
    val status: String
)

@Dao
interface JobDao {
    @Query("SELECT * FROM jobs_table")
    suspend fun getAllJobs(): List<JobEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInitialJobs(jobs: List<JobEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppliedJob(appliedJob: JobApplicationEntity)

    @Query("SELECT * FROM applied_jobs_table ORDER BY id DESC")
    suspend fun getAllAppliedJobs(): List<JobApplicationEntity>

    // In your JobDao interface:

    @Query("DELETE FROM applied_jobs_table WHERE jobTitle = :title AND companyName = :company")
    suspend fun deleteAppliedJobByDetails(title: String, company: String)

    @androidx.room.Delete
    suspend fun deleteAppliedJob(appliedJob: JobApplicationEntity)
}