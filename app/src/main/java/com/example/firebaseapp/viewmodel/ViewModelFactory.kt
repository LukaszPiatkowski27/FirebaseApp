package com.example.firebaseapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(application)
            modelClass.isAssignableFrom(MenuViewModel::class.java) -> MenuViewModel(application)
            modelClass.isAssignableFrom(ListViewModel::class.java) -> ListViewModel(application)
            modelClass.isAssignableFrom(MovieEditViewModel::class.java) -> MovieEditViewModel(application)
            modelClass.isAssignableFrom(GameEditViewModel::class.java) -> GameEditViewModel(application)
            modelClass.isAssignableFrom(BookEditViewModel::class.java) -> BookEditViewModel(application)
            else -> throw IllegalArgumentException("Unknown viewModel class")
        } as T
    }
}
