package com.example.a216487_cikguizwan_lab01

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// 1. Data Model matching the JSON payload structure from the public API
data class RemoteJobResponse(
    val jobs: List<NetworkJob>
)

data class NetworkJob(
    val id: String,
    val title: String,
    val company_name: String,
    val candidate_required_location: String,
    val salary: String?,
    val url: String
)

// 2. Retrofit API Service Interface definition
interface RemoteJobApiService {
    // We are hitting the free, public remotive.com API endpoint for software dev jobs
    @GET("api/remote-jobs?category=software-dev&limit=10")
    suspend fun fetchRemoteJobs(): RemoteJobResponse

    companion object {
        private const val BASE_URL = "https://remotive.com/"

        fun create(): RemoteJobApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RemoteJobApiService::class.java)
        }
    }
}