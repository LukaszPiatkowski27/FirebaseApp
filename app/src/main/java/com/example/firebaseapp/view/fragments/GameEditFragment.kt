package com.example.firebaseapp.view.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.example.firebaseapp.viewmodel.GameEditViewModel
import com.example.firebaseapp.R
import com.example.firebaseapp.model.Game
import com.example.firebaseapp.model.Movie
import com.example.firebaseapp.model.controllers.GamesDataController
import com.example.firebaseapp.model.controllers.MoviesDataController
import com.example.firebaseapp.viewmodel.ViewModelFactory

class GameEditFragment : Fragment() {
    private lateinit var viewModel: GameEditViewModel
    private val args: GameEditFragmentArgs by navArgs()
    private var index: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.game_edit_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = ViewModelFactory(requireNotNull(this.activity).application)
        viewModel = ViewModelProvider(requireActivity(), factory)[GameEditViewModel::class.java]
        view.findViewById<Button>(R.id.cancel_btn).setOnClickListener{
            requireActivity().onBackPressed()
        }

        index = args.index.toInt()
        val games: MutableLiveData<List<Game>> = GamesDataController.data

        val title = view.findViewById<EditText>(R.id.game_title_input)
        val author = view.findViewById<EditText>(R.id.game_author_input)
        val genre = view.findViewById<EditText>(R.id.game_genre_input)
        val year = view.findViewById<EditText>(R.id.game_year_input)
        val seen = view.findViewById<SwitchCompat>(R.id.game_seen_switch)

        if (index == -1) {
            index = GamesDataController.data.value?.size ?: 0
            view.findViewById<Button>(R.id.remove_btn).let{
                it.isEnabled = false
                it.background.alpha = 40
            }
            view.findViewById<Button>(R.id.save_btn).setOnClickListener {
                if(title.text.toString() != "") {
                    val newGame = Game(
                        title = title.text.toString(),
                        studio = author.text.toString(),
                        genre = genre.text.toString(),
                        year = year.text.toString(),
                        played = seen.isChecked
                    )
                    if(index == 0) games.value = mutableListOf(newGame)
                    else {
                        val newList: MutableList<Game> = games.value!!.toMutableList()
                        newList.add(newGame)
                        games.value = newList
                    }
                    GamesDataController.saveGames()
                    requireActivity().onBackPressed()
                }
            }
        }
        else {
            games.value!![index].let{
                title.setText(it.title)
                author.setText(it.studio)
                genre.setText(it.genre)
                year.setText(it.year)
                seen.isChecked = it.played
            }

            view.findViewById<Button>(R.id.save_btn).setOnClickListener {
                if(title.text.toString() != "") {
                    games.value!![index].let{
                        it.title = title.text.toString()
                        it.studio = author.text.toString()
                        it.genre = genre.text.toString()
                        it.year = year.text.toString()
                        it.played = seen.isChecked
                    }
                    GamesDataController.saveGames()
                    requireActivity().onBackPressed()
                }
            }

            view.findViewById<Button>(R.id.remove_btn).setOnClickListener {
                val newList: MutableList<Game> = games.value!!.toMutableList()
                newList.removeAt(index)
                games.value = newList
                GamesDataController.saveGames()
                requireActivity().onBackPressed()
            }
        }
    }
}