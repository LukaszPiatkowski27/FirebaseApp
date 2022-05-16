package com.example.firebaseapp.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.findNavController
import com.example.firebaseapp.R
import com.example.firebaseapp.model.controllers.DatabaseController

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    fun tryLogin(email: String, password: String){
        DatabaseController.authenticate(email,password)
    }

    fun navigateToGamesFragment(view: View){
        view.findNavController().navigate(R.id.action_loginFragment_to_gamesFragment)
    }
}