package com.example.mypets.data.layer.firebase.database

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseDatabaseHelper {

    companion object {
        val database: DatabaseReference = Firebase.database.reference

        fun saveUserData(userId: String, userData: AllUserDataDto) {
            database.child(userId).setValue(userData)
        }
    }
}