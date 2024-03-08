package com.sgalera.gaztelubira.data.network.services

import android.util.Log
import com.sgalera.gaztelubira.data.network.firebase.FirebaseClient
import javax.inject.Inject

class MatchesApiService @Inject constructor(private val firebase: FirebaseClient) {
    companion object {
        const val MATCHES_INFO = "matches"
        const val MATCHES_STATS = "matches_stats"
    }

    suspend fun getMatchesInfo() {
        val docRef = firebase.db.collection(MATCHES_INFO).get()
            .addOnSuccessListener { collection ->
                if (collection != null) {
                    Log.d("sgalera", "DocumentSnapshot data: ${collection.documents}")
                } else {
                    Log.i("sgalera", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("sgalera", "get failed with", exception)
            }
    }
}

