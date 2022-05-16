package com.example.firebaseapp.view.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.navArgs
import com.example.firebaseapp.viewmodel.MovieEditViewModel
import com.example.firebaseapp.R
import com.example.firebaseapp.model.Movie
import com.example.firebaseapp.model.controllers.MoviesDataController
import com.example.firebaseapp.viewmodel.ViewModelFactory

class MovieEditFragment : Fragment() {
    private lateinit var viewModel: MovieEditViewModel
    private val args: MovieEditFragmentArgs by navArgs()
    private var index: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.movie_edit_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = ViewModelFactory(requireNotNull(this.activity).application)
        viewModel = ViewModelProvider(requireActivity(),factory)[MovieEditViewModel::class.java]
        view.findViewById<Button>(R.id.cancel_btn).setOnClickListener{
            requireActivity().onBackPressed()
        }

        index = args.index.toInt()
        val movies: MutableLiveData<List<Movie>> = MoviesDataController.data

        val title = view.findViewById<EditText>(R.id.movie_title_input)
        val author = view.findViewById<EditText>(R.id.movie_author_input)
        val genre = view.findViewById<EditText>(R.id.movie_genre_input)
        val year = view.findViewById<EditText>(R.id.movie_year_input)
        val seen = view.findViewById<SwitchCompat>(R.id.movie_seen_switch)

        if (index == -1) {
            index = MoviesDataController.data.value?.size ?: 0
            view.findViewById<Button>(R.id.remove_btn).let{
                it.isEnabled = false
                it.background.alpha = 40
            }
            view.findViewById<Button>(R.id.save_btn).setOnClickListener {
                if(title.text.toString() != "") {
                    val newMovie = Movie(
                        title = title.text.toString(),
                        author = author.text.toString(),
                        genre = genre.text.toString(),
                        year = year.text.toString(),
                        seen = seen.isChecked
                    )
                    if(index == 0) movies.value = mutableListOf(newMovie)
                    else {
                        val newList: MutableList<Movie> = movies.value!!.toMutableList()
                        newList.add(newMovie)
                        movies.value = newList
                    }
                    MoviesDataController.saveMovies()
                    requireActivity().onBackPressed()
                }
            }
        }
        else {
            movies.value!![index].let{
                title.setText(it.title)
                author.setText(it.author)
                genre.setText(it.genre)
                year.setText(it.year)
                seen.isChecked = it.seen
            }

            view.findViewById<Button>(R.id.save_btn).setOnClickListener {
                if(title.text.toString() != "") {
                    movies.value!![index].let{
                        it.title = title.text.toString()
                        it.author = author.text.toString()
                        it.genre = genre.text.toString()
                        it.year = year.text.toString()
                        it.seen = seen.isChecked
                    }
                    MoviesDataController.saveMovies()
                    requireActivity().onBackPressed()
                }
            }

            view.findViewById<Button>(R.id.remove_btn).setOnClickListener {
                val newList: MutableList<Movie> = movies.value!!.toMutableList()
                newList.removeAt(index)
                movies.value = newList
                MoviesDataController.saveMovies()
                requireActivity().onBackPressed()
            }
        }


    }

}