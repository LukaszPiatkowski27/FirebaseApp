package com.example.firebaseapp.viewmodel.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseapp.R
import com.example.firebaseapp.model.Game

class GamesListAdapter(private val games: MutableLiveData<List<Game>>,
                       val navigationToGameEdit: (position: Int) -> Unit) :
    RecyclerView.Adapter<GamesListAdapter.GamesListHolder>() {

    inner class GamesListHolder(view: View): RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.title_tv)
        val author: TextView = view.findViewById(R.id.author_tv)
        val genre: TextView = view.findViewById(R.id.genre_tv)
        val year: TextView = view.findViewById(R.id.year_tv)
        val icon: ImageView = view.findViewById(R.id.record_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GamesListAdapter.GamesListHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.one_game_record,parent,false)
        return GamesListHolder(view)
    }

    override fun onBindViewHolder(holder: GamesListAdapter.GamesListHolder, position: Int) {
        holder.title.text = games.value!![position].title
        holder.author.text = games.value!![position].studio
        holder.genre.text = games.value!![position].genre
        holder.year.text = games.value!![position].year
        if(!games.value!![position].played) holder.icon.setColorFilter(Color.argb(255,100,200,200))
        holder.itemView.setOnClickListener{ navigationToGameEdit(position) }
    }

    override fun getItemCount(): Int = games.value?.size?:0
}