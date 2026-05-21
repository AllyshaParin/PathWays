package com.example.a216487_cikguizwan_lab01

import kotlin.math.sqrt

object KnnRecommender {

    /**
     * Calculates Euclidean Distance: sqrt( (x1-x2)^2 + (y1-y2)^2 + (z1-z2)^2 )
     */
    private fun calculateEuclideanDistance(vector1: DoubleArray, vector2: DoubleArray): Double {
        var sum = 0.0
        // Features must match exactly in size
        for (i in vector1.indices) {
            val difference = vector1[i] - vector2[i]
            sum += difference * difference
        }
        return sqrt(sum)
    }

    /**
     * Loops through all records in Room, computes distances, sorts them, and returns top K matches.
     */
    fun findTopMatches(
        userAge: Int,
        userEdu: Int,
        userLoc: Int,
        allJobs: List<JobEntity>,
        k: Int = 5
    ): List<JobEntity> {

        if (allJobs.isEmpty()) {
            return emptyList()
        }
        val userVector = doubleArrayOf(userAge.toDouble(), userEdu.toDouble(), userLoc.toDouble())

        return allJobs
            .map { job ->
                val distance = calculateEuclideanDistance(userVector, job.toFeatureVector())
                Pair(job, distance)
            }
            // Sort ascending: smallest mathematical distance = highest matching job
            .sortedBy { it.second }
            .map { it.first }
            .take(k)
    }
}