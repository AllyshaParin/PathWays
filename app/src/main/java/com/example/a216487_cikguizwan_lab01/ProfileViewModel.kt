package com.example.a216487_cikguizwan_lab01

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// --- REMAINING DATA MODELS ---
data class SalaryResult(
    val jobTitle: String = "",
    val location: String = "",
    val minPay: Int = 0,
    val maxPay: Int = 0,
    val averagePay: Int = 0
)

data class JobApplication(
    val jobTitle: String = "",
    val companyName: String = "",
    val location: String = "",
    val salaryRange: String = "",
    val status: String = ""
)

class ProfileViewModel(
    private val userDao: UserProfileDao,
    private val jobDao: JobDao
) : ViewModel() {

    // 1. Profile State powered by Room
    val userProfileState: StateFlow<UserProfileEntity> = userDao.getUserProfileFlow()
        .map { entity ->
            entity ?: UserProfileEntity(
                name = "NURALLYSHA AYUNI BINTI SHAPARIN",
                phone = "+60123456789",
                email = "allysha@example.com",
                gender = "Female",
                dob = "12/03/2004",
                nationality = "Malaysian",
                country = "Malaysia",
                cityState = "Selangor",
                postcode = "43500",
                address = "NO 9 JALAN 4/5 TAMAN SRI HANECO",
                maritalStatus = "Single",
                workPermit = "No, I don't need",
                age = 22,
                educationLevel = 3,
                locationCode = 1
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProfileEntity(
                name = "", phone = "", email = "", gender = "", dob = "", nationality = "",
                country = "", cityState = "", postcode = "", address = "", maritalStatus = "",
                workPermit = "", age = 22, educationLevel = 3, locationCode = 1
            )
        )

    // 2. KNN State to hold recommendations
    private val _recommendedJobs = mutableStateOf<List<JobEntity>>(emptyList())
    val recommendedJobs: State<List<JobEntity>> = _recommendedJobs

    // 3. Application State
    private val _appliedJobs = mutableStateListOf<JobApplication>()
    val appliedJobs: List<JobApplication> = _appliedJobs

    // INIT BLOCK: Loads recommendations AND old applied jobs on launch
    init {
        viewModelScope.launch {
            userProfileState.collectLatest { _ ->
                loadKnnRecommendations()
            }
        }
        // Load applied history records from Room database right away
        loadAppliedJobsHistory()
    }

    private fun loadAppliedJobsHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            val savedHistory = jobDao.getAllAppliedJobs()
            // Map the Room entities back to your UI models cleanly
            val uiList = savedHistory.map {
                JobApplication(
                    jobTitle = it.jobTitle,
                    companyName = it.companyName,
                    location = it.location,
                    salaryRange = it.salaryRange,
                    status = it.status
                )
            }
            // Switch back to Main thread to update the UI safely
            viewModelScope.launch(Dispatchers.Main) {
                _appliedJobs.clear()
                _appliedJobs.addAll(uiList)
            }
        }
    }

    fun loadKnnRecommendations() {
        viewModelScope.launch {
            val currentUser = userProfileState.value
            val allJobsFromDb = jobDao.getAllJobs()

            val matches = KnnRecommender.findTopMatches(
                userAge = currentUser.age,
                userEdu = currentUser.educationLevel,
                userLoc = currentUser.locationCode,
                allJobs = allJobsFromDb,
                k = 5
            )
            _recommendedJobs.value = matches
        }
    }

    fun updateProfile(updatedProfile: UserProfileEntity) {
        viewModelScope.launch {
            userDao.saveUserProfile(updatedProfile)
        }
    }

    // Salary Calculation State
    private val _salaryState = mutableStateOf(SalaryResult())
    val salaryState: State<SalaryResult> = _salaryState

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

    var aboutMe = mutableStateOf("I am a hardworking individual with a passion for learning.")
    var skills = mutableStateOf(listOf("Programming Languages", "Problem-Solving", "Teamwork"))
    val languages = mutableStateListOf("Malay - Good", "English - Average")

    fun addLanguage(newEntry: String) {
        if (!languages.contains(newEntry)) {
            languages.add(newEntry)
        }
    }

    fun applyForJob(title: String, company: String, location: String, salary: String) {
        val statusText = "Application Submitted"

        // 1. Immediately push to UI for active presentation reactivity
        val newJobUi = JobApplication(
            jobTitle = title,
            companyName = company,
            location = location,
            salaryRange = salary,
            status = statusText
        )
        _appliedJobs.add(0, newJobUi)

        // 2. Insert into database using your updated Dao to ensure data persistence
        viewModelScope.launch(Dispatchers.IO) {
            val roomEntity = JobApplicationEntity(
                jobTitle = title,
                companyName = company,
                location = location,
                salaryRange = salary,
                status = statusText
            )
            jobDao.insertAppliedJob(roomEntity)
        }
    }

    // FIXED: Replaced cancelJobApplicationById with a safe business key removal handler
    // This updates both the in-memory state tracker and the SQLite Room backing store
    fun cancelJobApplication(job: JobApplication) {
        // 1. Instantly remove from the Compose mutableStateListOf UI tracking engine
        _appliedJobs.remove(job)

        // 2. Drop the matching row from your permanent Room local database safely
        viewModelScope.launch(Dispatchers.IO) {
            jobDao.deleteAppliedJobByDetails(title = job.jobTitle, company = job.companyName)
        }
    }
}

// --- FACTORY CLEANLY APPENDED OUTSIDE CLASS BOUNDS ---
class ProfileViewModelFactory(
    private val userProfileDao: UserProfileDao,
    private val jobDao: JobDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(userProfileDao, jobDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}