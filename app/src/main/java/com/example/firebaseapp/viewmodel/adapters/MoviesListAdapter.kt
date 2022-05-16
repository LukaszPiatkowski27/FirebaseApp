package com.example.firebaseapp.viewmodel.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseapp.R
import com.example.firebaseapp.model.Movie
import java.util.*

class MoviesListAdapter(private val movies: LiveData<List<Movie>>,
                        val navigationToMovieEdit: (position: Int) -> Unit) :
    RecyclerView.Adapter<MoviesListAdapter.MoviesListHolder>() {

    inner class MoviesListHolder(view: View): RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.title_tv)
        val author: TextView = view.findViewById(R.id.author_tv)
        val genre: TextView = view.findViewById(R.id.genre_tv)
        val year: TextView = view.findViewById(R.id.year_tv)
        val icon: ImageView = view.findViewById(R.id.record_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesListAdapter.MoviesListHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.one_movie_record,parent,false)
        return MoviesListHolder(view)
    }

    override fun onBindViewHolder(holder: MoviesListAdapter.MoviesListHolder, position: Int) {
        holder.title.text = movies.value!![position].title
        holder.author.text = movies.value!![position].author
        holder.genre.text = movies.value!![position].genre
        holder.year.text = movies.value!![position].year
        if(!movies.value!![position].seen) holder.icon.setColorFilter(Color.argb(255,100,200,200))
        holder.itemView.setOnClickListener{ navigationToMovieEdit(position) }
    }

    override fun getItemCount(): Int = movies.value?.size?:0
}