package com.example.firebaseapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.firebaseapp.model.Book
import com.example.firebaseapp.model.Game
import com.example.firebaseapp.model.Movie
import com.example.firebaseapp.model.controllers.BooksDataController
import com.example.firebaseapp.model.controllers.GamesDataController
import com.example.firebaseapp.model.controllers.MoviesDataController

class ListViewModel(application: Application) : AndroidViewModel(application) {

    fun loadMovies() : MutableLiveData<List<Movie>> {
        MoviesDataController.readMovies()
        return MoviesDataController.data
    }

    fun loadGames() : MutableLiveData<List<Game>> {
        GamesDataController.readGames()
        return GamesDataController.data
    }

    fun loadBooks() : MutableLiveData<List<Book>> {
        BooksDataController.readBooks()
        return BooksDataController.data
    }
}