package com.example.a216487_cikguizwan_lab01

import com.google.firebase.Timestamp

data class CommunityPost(
    val id: String = "",
    val jobTitle: String = "",
    val companyName: String = "",
    val reviewText: String = "",
    val authorName: String = "Anonymous Candidate",
    val timestamp: Timestamp? = null
) {
    // Helper function to map Firestore document data easily back into Kotlin objects
    constructor(id: String, map: Map<String, Any>) : this(
        id = id,
        jobTitle = map["jobTitle"] as? String ?: "",
        companyName = map["companyName"] as? String ?: "",
        reviewText = map["reviewText"] as? String ?: "",
        authorName = map["authorName"] as? String ?: "Anonymous Candidate",
        timestamp = map["timestamp"] as? Timestamp
    )

    // Helper to turn our object into a clean HashMap for Firestore uploading
    fun toHashMap(): Map<String, Any> {
        val postMap = mutableMapOf<String, Any>()

        postMap["jobTitle"] = jobTitle
        postMap["companyName"] = companyName
        postMap["reviewText"] = reviewText
        postMap["authorName"] = authorName
        postMap["timestamp"] = timestamp ?: Timestamp.now()

        return postMap
    }
}