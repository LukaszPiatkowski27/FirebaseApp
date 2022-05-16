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
import com.example.firebaseapp.model.Book

class BooksListAdapter(private val books: MutableLiveData<List<Book>>,
                       val navigationToBookEdit: (position: Int) -> Unit) :
    RecyclerView.Adapter<BooksListAdapter.BooksListHolder>() {

    inner class BooksListHolder(view: View): RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.title_tv)
        val author: TextView = view.findViewById(R.id.author_tv)
        val genre: TextView = view.findViewById(R.id.genre_tv)
        val year: TextView = view.findViewById(R.id.year_tv)
        val icon: ImageView = view.findViewById(R.id.record_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksListAdapter.BooksListHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.one_book_record,parent,false)
        return BooksListHolder(view)
    }

    override fun onBindViewHolder(holder: BooksListAdapter.BooksListHolder, position: Int) {
        holder.title.text = books.value!![position].title
        holder.author.text = books.value!![position].author
        holder.genre.text = books.value!![position].genre
        holder.year.text = books.value!![position].year
        if(!books.value!![position].seen) holder.icon.setColorFilter(Color.argb(255,100,200,200))
        holder.itemView.setOnClickListener{ navigationToBookEdit(position) }
    }

    override fun getItemCount(): Int = books.value?.size?:0
}