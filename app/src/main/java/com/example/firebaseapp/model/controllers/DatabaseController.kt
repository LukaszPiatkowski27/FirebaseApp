package com.example.firebaseapp.model.controllers

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class DatabaseController {
    companion object{
        private var INSTANCE : FirebaseDatabase? = null
        private const val DATABASE_URL : String = "https://fir-app-4a7d3-default-rtdb.firebaseio.com/"

        private val AUTH : FirebaseAuth = Firebase.auth
        val authenticated : MutableLiveData<Boolean> = MutableLiveData()

        fun getInstance() : FirebaseDatabase{
            INSTANCE = INSTANCE ?: FirebaseDatabase.getInstance(DATABASE_URL)
            return INSTANCE!!
        }

        fun authenticate(username: String, password: String){
            if (username != "" && password != "") {
                AUTH.signInWithEmailAndPassword(username, password).addOnCompleteListener {
                    authenticated.value = it.isSuccessful
                }
            }
        }

        fun getUserUID() : String? {
            return if (authenticated.value!!) AUTH.uid else null
        }

        fun getUsername() : String? {
            return if (authenticated.value!!)
                AUTH.currentUser?.email.toString().split('@')[0] else null
        }
    }
}