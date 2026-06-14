package com.example.a216487_cikguizwan_lab01

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

// --- DATA MODELS ---
data class UserProfile(
    val id: Int = 1,
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
    val workPermit: String = "No, I don't need",
    // --- KNN Profile Vector States ---
    val age: Int = 22,
    val educationLevel: Int = 3, // Default: Bachelor's Degree
    val locationCode: Int = 1,    // Default: Selangor / KL
    val profilePicturePath: String? = null
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

class ProfileViewModel(
    private val userProfileDao: UserProfileDao,
    private val jobDao: JobDao
) : ViewModel() {

    // --- KNN MATCHING & ROOM STATES ---
    val recommendedJobs = mutableStateOf<List<JobEntity>>(emptyList())

    // 1. Profile State Framework
    private val _uiState = mutableStateOf(UserProfile())
    val uiState: State<UserProfile> = _uiState

    // StateFlow wrapper pipeline used explicitly by EditPersonalDetailsScreen tracking states
    private val _userProfileState = MutableStateFlow(UserProfile())
    val userProfileState: StateFlow<UserProfile> = _userProfileState.asStateFlow()

    // 2. Salary State Framework
    private val _salaryState = mutableStateOf(SalaryResult())
    val salaryState: State<SalaryResult> = _salaryState

    // 3. Application State Framework
    private val _appliedJobs = mutableStateListOf<JobApplication>()
    val appliedJobs: List<JobApplication> = _appliedJobs

    // Additional Profile Specific Metadata
    var aboutMe = mutableStateOf("I am a hardworking individual with a passion for learning.")
    var skills = mutableStateOf(listOf("Programming Languages", "Problem-Solving", "Teamwork"))
    val languages = mutableStateListOf("Malay - Good", "English - Average")

    init {
        // Hydrate UI flows with our default payload representations upon instantiation
        _userProfileState.value = _uiState.value

        // Automatically fetch data historical states from room disk upon initialization
        loadAppliedJobsHistory()

        // FIXED: Automatically observe and load real saved profiles from your Room database on startup
        observeLocalProfile()
    }

    /**
     * Collects real-time updates from Room Database and maps them back into UI States safely
     */
    private fun observeLocalProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            userProfileDao.getUserProfileFlow().collect { entity ->
                if (entity != null) {
                    // Convert Database entity fields back into our UI data model class object structure
                    val domainProfile = UserProfile(
                        id = entity.id,
                        name = entity.name,
                        phone = entity.phone,
                        email = entity.email,
                        gender = entity.gender,
                        dob = entity.dob,
                        nationality = entity.nationality,
                        country = entity.country,
                        cityState = entity.cityState,
                        postcode = entity.postcode,
                        address = entity.address,
                        maritalStatus = entity.maritalStatus,
                        workPermit = entity.workPermit,
                        age = entity.age,
                        educationLevel = entity.educationLevel,
                        locationCode = entity.locationCode,
                        profilePicturePath = entity.profilePicturePath
                    )
                    // Post values back onto the main UI memory thread safely
                    withContext(Dispatchers.Main) {
                        _uiState.value = domainProfile
                        _userProfileState.value = domainProfile
                    }
                }
            }
        }
    }

    fun loadKnnRecommendations() {
        viewModelScope.launch {
            try {
                // 1. Fetch the active user vector from the database
                val userEntity = withContext(Dispatchers.IO) { userProfileDao.getUserProfile() }
                val localJobs = withContext(Dispatchers.IO) { jobDao.getAllJobs() }

                if (userEntity != null && localJobs.isNotEmpty()) {
                    val userAge = userEntity.age.toDouble()
                    val userEdu = userEntity.educationLevel.toDouble()
                    val userLoc = userEntity.locationCode.toDouble()

                    // 2. Compute Euclidean distance for each job entity item
                    val sortedJobs = localJobs.map { job ->
                        val jobVector = job.toFeatureVector() // [requiredAge, educationLevel, locationCode]

                        // Distance formula calculation: √((x2-x1)² + (y2-y1)² + (z2-z1)²)
                        val distance = java.lang.Math.sqrt(
                            java.lang.Math.pow(jobVector[0] - userAge, 2.0) +
                                    java.lang.Math.pow(jobVector[1] - userEdu, 2.0) +
                                    java.lang.Math.pow(jobVector[2] - userLoc, 2.0)
                        )
                        Pair(job, distance)
                    }
                        // 3. Sort by closest match (smallest mathematical distance) and take the Top 5
                        .sortedBy { it.second }
                        .map { it.first }
                        .take(5)

                    recommendedJobs.value = sortedJobs
                } else if (localJobs.isNotEmpty()) {
                    // Fallback safe mechanism if user isn't found yet
                    recommendedJobs.value = localJobs.take(5)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun loadAppliedJobsHistory() {
        viewModelScope.launch {
            try {
                val dbHistory = withContext(Dispatchers.IO) { jobDao.getAllAppliedJobs() }
                _appliedJobs.clear()
                dbHistory.forEach { entity ->
                    _appliedJobs.add(
                        JobApplication(
                            jobTitle = entity.jobTitle,
                            location = entity.location,
                            salaryRange = entity.salaryRange,
                            status = "Application Submitted"
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // FIXED: Maps and executes disk storage insertion routines using your UserProfileDao interface
    fun updateProfile(updatedProfile: UserProfile) {
        // 1. Instantly update responsive UI states in device RAM
        _uiState.value = updatedProfile
        _userProfileState.value = updatedProfile

        // 2. Map and drop into Room background worker coroutine pipeline permanently
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val profileEntity = UserProfileEntity(
                    id = updatedProfile.id, // Primary Key value = 1
                    name = updatedProfile.name,
                    phone = updatedProfile.phone,
                    email = updatedProfile.email,
                    gender = updatedProfile.gender,
                    dob = updatedProfile.dob,
                    nationality = updatedProfile.nationality,
                    country = updatedProfile.country,
                    cityState = updatedProfile.cityState,
                    postcode = updatedProfile.postcode,
                    address = updatedProfile.address,
                    maritalStatus = updatedProfile.maritalStatus,
                    workPermit = updatedProfile.workPermit,
                    age = updatedProfile.age,
                    educationLevel = updatedProfile.educationLevel,
                    locationCode = updatedProfile.locationCode,
                    profilePicturePath = updatedProfile.profilePicturePath
                )
                userProfileDao.saveUserProfile(profileEntity)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
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

    fun addLanguage(newEntry: String) {
        if (!languages.contains(newEntry)) {
            languages.add(newEntry)
        }
    }

    fun applyForJob(title: String, company: String, location: String, salary: String) {
        applyForJob(title, "$company, $location", salary)
    }

    fun applyForJob(title: String, location: String, salary: String) {
        val newJob = JobApplication(
            jobTitle = title,
            location = location,
            salaryRange = salary,
            status = "Application Submitted"
        )
        _appliedJobs.add(0, newJob)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val entity = JobApplicationEntity(
                    jobTitle = title,
                    companyName = location.substringBefore(","),
                    location = location.substringAfter(", "),
                    salaryRange = salary,
                    status = "Application Submitted"
                )
                jobDao.insertAppliedJob(entity)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // --- FIREBASE CLOUD INTEGRATION BLOCK ---
    private val firestore = FirebaseFirestore.getInstance()

    private val _communityPosts = mutableStateListOf<CommunityPost>()
    val communityPosts: List<CommunityPost> get() = _communityPosts

    fun listenToCommunityPosts() {
        firestore.collection("community_posts")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                val posts = snapshot.documents.map { document ->
                    val data = document.data ?: emptyMap()
                    CommunityPost(id = document.id, map = data)
                }

                _communityPosts.clear()
                _communityPosts.addAll(posts)
            }
    }

    fun uploadCommunityPost(jobTitle: String, companyName: String, reviewText: String, onComplete: () -> Unit) {
        val newPost = CommunityPost(
            jobTitle = jobTitle,
            companyName = companyName,
            reviewText = reviewText,
            authorName = "User (PathWays Community)",
            timestamp = Timestamp.now()
        )

        firestore.collection("community_posts")
            .add(newPost.toHashMap())
            .addOnSuccessListener {
                onComplete()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    fun deleteCommunityPost(postId: String, onComplete: () -> Unit) {
        firestore.collection("community_posts")
            .document(postId)
            .delete()
            .addOnSuccessListener {
                onComplete()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    fun cancelJobApplication(job: JobApplication) {
        _appliedJobs.remove(job)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val extractedCompany = job.location.substringBefore(",")
                jobDao.deleteAppliedJobByDetails(
                    title = job.jobTitle,
                    company = extractedCompany
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // --- WEB API SERVICE INTEGRATION BLOCK ---
    private val apiService = RemoteJobApiService.create()

    private val _remoteJobsList = mutableStateListOf<NetworkJob>()
    val remoteJobsList: List<NetworkJob> get() = _remoteJobsList

    var isApiLoading = mutableStateOf(false)
        private set

    fun fetchLiveRemoteJobs() {
        viewModelScope.launch {
            isApiLoading.value = true
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.fetchRemoteJobs()
                }
                _remoteJobsList.clear()
                _remoteJobsList.addAll(response.jobs)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isApiLoading.value = false
            }
        }
    }

    // --- HARDWARE SENSOR INTEGRATION BLOCK ---
    var scannedQrResult = mutableStateOf("No code scanned yet")
        private set

    fun startBarcodeScanner(context: Context, onScanSuccess: (String) -> Unit = {}) {
        val activity = context.findActivity()
        if (activity == null) {
            scannedQrResult.value = "Scanner Error: No Activity Context"
            return
        }

        val scanner = GmsBarcodeScanning.getClient(activity)

        scanner.startScan()
            .addOnSuccessListener { barcode ->
                val rawValue = barcode.rawValue ?: "Empty QR Code"
                scannedQrResult.value = rawValue
                onScanSuccess(rawValue)
            }
            .addOnFailureListener { e ->
                scannedQrResult.value = "Scan failed: ${e.localizedMessage}"
                e.printStackTrace()
            }
    }

    private fun Context.findActivity(): Activity? {
        var context = this
        while (context is ContextWrapper) {
            if (context is Activity) return context
            context = context.baseContext
        }
        return null
    }
}