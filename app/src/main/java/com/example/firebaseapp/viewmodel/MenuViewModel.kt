package com.example.firebaseapp.viewmodel

import android.app.Application
import android.view.View
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.findNavController
import com.example.firebaseapp.model.controllers.DatabaseController
import com.example.firebaseapp.view.fragments.MenuFragmentDirections

class MenuViewModel(application: Application) : AndroidViewModel(application) {
    fun setGreetings(view: TextView){
        "Hello ${DatabaseController.getUsername()}".also { view.text = it }
    }

    fun navigateToListFragment(view: View, mode: Int){
        val action = MenuFragmentDirections.actionMenuFragmentToListFragment(mode.toLong())
        view.findNavController().navigate(action)
    }
}