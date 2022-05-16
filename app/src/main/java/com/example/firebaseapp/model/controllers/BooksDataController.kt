package com.example.firebaseapp.model.controllers

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.firebaseapp.model.Book

class BooksDataController {
    companion object{
        val data: MutableLiveData<List<Book>> = MutableLiveData()

        private fun getBooksUrl(): String{
            return "users/${DatabaseController.getUserUID()}/books"
        }

        fun readBooks(){
            val url = getBooksUrl()
            DatabaseController.getInstance().getReference("${url}/value").get().addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    val list: MutableList<Book> = mutableListOf()
                    val movies = task.result.children
                    movies.forEach{
                        list.add(it.getValue(Book::class.java)!!)
                    }
                    data.value = list
                }else{
                    Log.d("DBG","Database: retrieve data failed!")
                }
            }
        }

        fun saveBooks(){
            val url = getBooksUrl()
            DatabaseController.getInstance().getReference(url).setValue(data)
        }
    }
}