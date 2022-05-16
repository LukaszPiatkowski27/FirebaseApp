package com.example.firebaseapp.model.controllers

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.firebaseapp.model.Movie

class MoviesDataController {
    companion object{
        val data: MutableLiveData<List<Movie>> = MutableLiveData()

        private fun getMoviesUrl(): String{
            return "users/${DatabaseController.getUserUID()}/movies"
        }

        fun readMovies(){
            val url = getMoviesUrl()
            DatabaseController.getInstance().getReference(url + "/value").get().addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    val list: MutableList<Movie> = mutableListOf()
                    val movies = task.result.children
                    movies.forEach{
                        list.add(it.getValue(Movie::class.java)!!)
                    }
                    data.value = list
                }else{
                    Log.d("DBG","Database: retrieve data failed!")
                }
            }
        }

        fun saveMovies(){
            val url = getMoviesUrl()
            DatabaseController.getInstance().getReference(url).setValue(data)
        }
    }
}