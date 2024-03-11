package com.sgalera.gaztelubira.data.network.firebase

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseClient @Inject constructor(){
    val db = Firebase.firestore
}