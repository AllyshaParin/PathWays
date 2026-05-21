package com.example.a216487_cikguizwan_lab01

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

// --- DATA MODELS ---
data class UserProfile(
    val name: String = "NURALLYSHA AYUNI BINTI SHAPARIN",
    val phone: String = "+60123456789",
    val email: String = "allysha@example.com",
    val gender: String = "Female",
    val dob: String = "12/03/2004",
    val nationality: String = "Malaysian",
    val country: String = "Malaysia",
    val cityState: String = "Hulu Langat, Selangor",
    val postcode: String = "43500",
    val address: String = "NO 9 JALAN 4/5 TAMAN SRI HANECO",
    val maritalStatus: String = "Single",
    val workPermit: String = "No, I don't need"
)

data class SalaryResult(
    val jobTitle: String = "",
    val location: String = "",
    val minPay: Int = 0,
    val maxPay: Int = 0,
    val averagePay: Int = 0
)

data class JobApplication(
    val jobTitle: String = "",
    val location: String = "",
    val salaryRange: String = "",
    val status: String = ""
)

class ProfileViewModel : ViewModel() {

    // 1. Profile State
    private val _uiState = mutableStateOf(UserProfile())
    val uiState: State<UserProfile> = _uiState

    // 2. Salary State
    private val _salaryState = mutableStateOf(SalaryResult())
    val salaryState: State<SalaryResult> = _salaryState

    fun updateProfile(updatedProfile: UserProfile) {
        _uiState.value = updatedProfile
    }

    fun calculateSalary(job: String, state: String) {
        val basePay = when (job.lowercase()) {
            "it technician" -> 2500
            "rider" -> 1800
            "accountant" -> 3500
            "general worker" -> 1600
            else -> 1500
        }

        val multiplier = when (state) {
            "Selangor", "Kuala Lumpur", "Putrajaya" -> 1.30
            "Pulau Pinang", "Johor" -> 1.15
            "Sarawak", "Sabah", "Melaka" -> 1.10
            else -> 1.0
        }

        val avg = (basePay * multiplier).toInt()

        _salaryState.value = SalaryResult(
            jobTitle = job,
            location = state,
            minPay = (avg * 0.8).toInt(),
            maxPay = (avg * 1.4).toInt(),
            averagePay = avg
        )
    }

    // 3. Application State
    private val _appliedJobs = mutableStateListOf<JobApplication>()
    val appliedJobs: List<JobApplication> = _appliedJobs

    // Profile Details
    var aboutMe = mutableStateOf("I am a hardworking individual with a passion for learning.")
    var skills = mutableStateOf(listOf("Programming Languages", "Problem-Solving", "Teamwork"))

    // --- FIX FOR LANGUAGES ---
    val languages = mutableStateListOf("Malay - Good", "English - Average")

    fun addLanguage(newEntry: String) {
        if (!languages.contains(newEntry)) {
            languages.add(newEntry)
        }
    }

    fun applyForJob(title: String, location: String, salary: String) {
        val newJob = JobApplication(
            jobTitle = title,
            location = location,
            salaryRange = salary,
            status = "Application Submitted"
        )
        _appliedJobs.add(0, newJob)
    }
}