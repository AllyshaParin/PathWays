package com.example.a216487_cikguizwan_lab01

import android.content.Context
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

// =====================================================================
// 1. DATA ENTITIES
// =====================================================================

@Entity(tableName = "user_profile_table")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val phone: String,
    val email: String,
    val gender: String,
    val dob: String,
    val nationality: String,
    val country: String,
    val cityState: String,
    val postcode: String,
    val address: String,
    val maritalStatus: String,
    val workPermit: String,
    val age: Int,
    val educationLevel: Int, // 1=High School, 2=Diploma, 3=Bachelor, 4=Master/PhD
    val locationCode: Int    // 1=Selangor/KL, 2=Johor, 3=Penang, 4=Others
)

@Entity(tableName = "jobs_table")
data class JobEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val company: String,
    val salary: String,
    val logoUrl: String,
    val requiredAge: Int,
    val educationLevel: Int,
    val locationCode: Int
) {
    fun toFeatureVector(): DoubleArray {
        return doubleArrayOf(
            requiredAge.toDouble(),
            educationLevel.toDouble(),
            locationCode.toDouble()
        )
    }
}

// NOTE: JobApplicationEntity has been removed from this file
// to prevent the "Redeclaration" error since it is defined in JobDao.kt.

// =====================================================================
// 2. DATABASE CONFIGURATION
// =====================================================================

@Database(
    entities = [UserProfileEntity::class, JobEntity::class, JobApplicationEntity::class],
    version = 2,
    exportSchema = false
)
abstract class ProfileDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun jobDao(): JobDao

    companion object {
        @Volatile
        private var INSTANCE: ProfileDatabase? = null

        fun getDatabase(context: Context): ProfileDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProfileDatabase::class.java,
                    "profile_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            try {
                                db.beginTransaction()
                                getMockJobsList().forEach { job ->
                                    db.execSQL(
                                        """
                                        INSERT INTO jobs_table (title, company, salary, logoUrl, requiredAge, educationLevel, locationCode) 
                                        VALUES (?, ?, ?, ?, ?, ?, ?)
                                        """.trimIndent(),
                                        arrayOf<Any>(
                                            job.title,
                                            job.company,
                                            job.salary,
                                            job.logoUrl,
                                            job.requiredAge,
                                            job.educationLevel,
                                            job.locationCode
                                        )
                                    )
                                }
                                db.setTransactionSuccessful()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            } finally {
                                db.endTransaction()
                            }
                        }
                    })
                    .build()

                INSTANCE = instance
                instance
            }
        }

        private fun getMockJobsList(): List<JobEntity> {
            return listOf(
                JobEntity(title = "IT Helpdesk Specialist", company = "Grab", salary = "MYR 3,500 - 4,500", logoUrl = "https://logo.clearbit.com/grab.com", requiredAge = 23, educationLevel = 3, locationCode = 1),
                JobEntity(title = "F&B Supervisor", company = "Texas Chicken", salary = "MYR 3,200 - 4,500", logoUrl = "https://logo.clearbit.com/texaschickenmalaysia.com", requiredAge = 21, educationLevel = 1, locationCode = 1),
                JobEntity(title = "Retail Assistant", company = "Uniqlo", salary = "MYR 2,100 - 2,800", logoUrl = "https://logo.clearbit.com/uniqlo.com", requiredAge = 20, educationLevel = 1, locationCode = 1),
                JobEntity(title = "Mobile Software Engineer", company = "Grab", salary = "MYR 5,000 - 7,000", logoUrl = "https://logo.clearbit.com/grab.com", requiredAge = 25, educationLevel = 3, locationCode = 1),
                JobEntity(title = "Bank Operations Executive", company = "Maybank", salary = "MYR 3,800 - 4,800", logoUrl = "https://logo.clearbit.com/maybank.com", requiredAge = 24, educationLevel = 3, locationCode = 1),
                JobEntity(title = "Account Clerk", company = "Maybank", salary = "MYR 2,500 - 3,200", logoUrl = "https://logo.clearbit.com/maybank.com", requiredAge = 22, educationLevel = 2, locationCode = 2),
                JobEntity(title = "Production Operator", company = "Petronas", salary = "MYR 2,000 - 2,800", logoUrl = "https://logo.clearbit.com/petronas.com", requiredAge = 19, educationLevel = 1, locationCode = 4),
                JobEntity(title = "Data Analyst", company = "Petronas", salary = "MYR 4,500 - 6,000", logoUrl = "https://logo.clearbit.com/petronas.com", requiredAge = 26, educationLevel = 4, locationCode = 3)
            )
        }
    }
}