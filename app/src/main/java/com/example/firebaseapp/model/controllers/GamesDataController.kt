package com.example.firebaseapp.model.controllers

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.firebaseapp.model.Game

class GamesDataController {
    companion object{
        val data: MutableLiveData<List<Game>> = MutableLiveData()

        private fun getGamesUrl(): String{
            return "users/${DatabaseController.getUserUID()}/games"
        }

        fun readGames(){
            val url = getGamesUrl()
            DatabaseController.getInstance().getReference("${url}/value").get().addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    val list: MutableList<Game> = mutableListOf()
                    val movies = task.result.children
                    movies.forEach{
                        list.add(it.getValue(Game::class.java)!!)
                    }
                    data.value = list
                }else{
                    Log.d("DBG","Database: retrieve data failed!")
                }
            }
        }

        fun saveGames(){
            val url = getGamesUrl()
            DatabaseController.getInstance().getReference(url).setValue(data)
        }
    }
}