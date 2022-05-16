package com.example.firebaseapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseapp.R
import com.example.firebaseapp.model.Book
import com.example.firebaseapp.model.Game
import com.example.firebaseapp.model.Movie
import com.example.firebaseapp.viewmodel.ListViewModel
import com.example.firebaseapp.viewmodel.ViewModelFactory
import com.example.firebaseapp.viewmodel.adapters.BooksListAdapter
import com.example.firebaseapp.viewmodel.adapters.GamesListAdapter
import com.example.firebaseapp.viewmodel.adapters.MoviesListAdapter
import com.google.android.material.navigation.NavigationBarView


class ListFragment : Fragment() {
    companion object{
        private var listMode: MutableLiveData<Int> = MutableLiveData()
    }
    private lateinit var viewModel: ListViewModel
    private val args: ListFragmentArgs by navArgs()
    private lateinit var movies: MutableLiveData<List<Movie>>
    private lateinit var games: MutableLiveData<List<Game>>
    private lateinit var books: MutableLiveData<List<Book>>
    private lateinit var nsmovies: MutableLiveData<List<Movie>>
    private lateinit var nsgames: MutableLiveData<List<Game>>
    private lateinit var nsbooks: MutableLiveData<List<Book>>
    private var flag : Boolean = false
    private lateinit var recycler: RecyclerView
    private lateinit var moviesListAdapter: MoviesListAdapter
    private lateinit var gamesListAdapter: GamesListAdapter
    private lateinit var booksListAdapter: BooksListAdapter
    private lateinit var notSeenMoviesListAdapter: MoviesListAdapter
    private lateinit var notSeenGamesListAdapter: GamesListAdapter
    private lateinit var notSeenBooksListAdapter: BooksListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)
        val factory = ViewModelFactory(requireNotNull(this.activity).application)
        viewModel = ViewModelProvider(requireActivity(),factory)[ListViewModel::class.java]
        view.findViewById<ImageView>(R.id.back_arrow_btn).setOnClickListener{
            requireActivity().onBackPressed()
        }
        // MOVIES
        movies = viewModel.loadMovies()
        moviesListAdapter = MoviesListAdapter(movies, fun(position: Int){
            val action = ListFragmentDirections.actionListFragmentToMovieEditFragment(position.toLong())
            view.findNavController().navigate(action)
        })
        movies.observe(viewLifecycleOwner){ moviesListAdapter.notifyDataSetChanged() }
        
        val notSeenMovies: MutableList<Movie> = mutableListOf()
        movies.value?.forEach{ if(!it.seen) notSeenMovies.add(it) }
        nsmovies = MutableLiveData(notSeenMovies)
        notSeenMoviesListAdapter = MoviesListAdapter(nsmovies,fun(position: Int) {
            val index = movies.value?.indexOf((nsmovies.value as MutableList<Movie>)[position]) ?: -1
            val action = ListFragmentDirections.actionListFragmentToMovieEditFragment(index.toLong())
            view.findNavController().navigate(action)
        })
        nsmovies.observe(viewLifecycleOwner){ notSeenMoviesListAdapter.notifyDataSetChanged() }

        // GAMES
        games = viewModel.loadGames()
        gamesListAdapter = GamesListAdapter(games, fun(position: Int){
            val action = ListFragmentDirections.actionListFragmentToGameEditFragment(position.toLong())
            view.findNavController().navigate(action)
        })
        games.observe(viewLifecycleOwner){ gamesListAdapter.notifyDataSetChanged() }

        val notSeenGames: MutableList<Game> = mutableListOf()
        games.value?.forEach{ if(!it.played) notSeenGames.add(it) }
        nsgames = MutableLiveData(notSeenGames)
        notSeenGamesListAdapter = GamesListAdapter(nsgames,fun(position: Int) {
            val index = games.value?.indexOf((nsgames.value as MutableList<Game>)[position]) ?: -1
            val action = ListFragmentDirections.actionListFragmentToGameEditFragment(index.toLong())
            view.findNavController().navigate(action)
        })
        nsgames.observe(viewLifecycleOwner){ notSeenGamesListAdapter.notifyDataSetChanged() }
        
        // BOOKS
        books = viewModel.loadBooks()
        booksListAdapter = BooksListAdapter(books, fun(position: Int){
            val action = ListFragmentDirections.actionListFragmentToBookEditFragment(position.toLong())
            view.findNavController().navigate(action)
        })
        books.observe(viewLifecycleOwner){ booksListAdapter.notifyDataSetChanged() }

        val notSeenBooks: MutableList<Book> = mutableListOf()
        books.value?.forEach{ if(!it.seen) notSeenBooks.add(it) }
        nsbooks = MutableLiveData(notSeenBooks)
        notSeenBooksListAdapter = BooksListAdapter(nsbooks, fun(position: Int) {
            val index = books.value?.indexOf((nsbooks.value as MutableList<Book>)[position]) ?: -1
            val action = ListFragmentDirections.actionListFragmentToBookEditFragment(index.toLong())
            view.findNavController().navigate(action)
        })
        nsbooks.observe(viewLifecycleOwner){ notSeenBooksListAdapter.notifyDataSetChanged() }


        val nsSwitch = view.findViewById<SwitchCompat>(R.id.only_not_seen)
        nsSwitch.let { switch ->
            switch.setOnClickListener {
                setMode(view, listMode.value!!, switch)
            }
        }


        val layoutManager = LinearLayoutManager(view.context)
        recycler = view.findViewById(R.id.recycler)
        recycler.layoutManager = layoutManager

        listMode.observe(viewLifecycleOwner) { mode ->
            setMode(view, mode, nsSwitch)
        }

        val bottomNavBar = view.findViewById<NavigationBarView>(R.id.bottom_nav_bar)
        args.mode.toInt().let{ if(!flag){ listMode.value = it; flag = true } }
        bottomNavBar.menu.getItem(listMode.value!!).isChecked = true
        bottomNavBar.setOnItemSelectedListener {
            listMode.value = when (it.itemId) {
                R.id.nav_movies -> 0
                R.id.nav_games -> 1
                else -> 2
            }
            true
        }
        val searchView = view.findViewById<SearchView>(R.id.action_search)
        searchView.queryHint = "Search by Title"
        searchView.setQuery("", true)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                filter(view, query, nsSwitch.isChecked)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filter(view, newText, nsSwitch.isChecked)
                return true
            }
        })
    }

    private fun filter(view: View, query: String, notSeen: Boolean){
        if(query != "") {
            when (listMode.value) {
                0 -> {
                    val filteredMovies: MutableList<Movie> = mutableListOf()
                    movies.value?.forEach {
                        if (it.title.lowercase().contains(query) && (!it.seen || !notSeen))
                            filteredMovies.add(it)
                    }
                    val ld: MutableLiveData<List<Movie>> = MutableLiveData()
                    ld.value = filteredMovies
                    val newAdapter = MoviesListAdapter(ld, fun(position: Int) {
                        val index =
                            movies.value?.indexOf((ld.value as MutableList<Movie>)[position]) ?: -1
                        val action =
                            ListFragmentDirections.actionListFragmentToMovieEditFragment(index.toLong())
                        view.findNavController().navigate(action)
                    })
                    recycler.adapter = newAdapter
                }
                1 -> {
                    val filteredGames: MutableList<Game> = mutableListOf()
                    games.value?.forEach {
                        if (it.title.lowercase().contains(query) && (!it.played || !notSeen))
                            filteredGames.add(it)
                    }
                    val ld: MutableLiveData<List<Game>> = MutableLiveData()
                    ld.value = filteredGames
                    val newAdapter = GamesListAdapter(ld, fun(position: Int) {
                        val index =
                            games.value?.indexOf((ld.value as MutableList<Game>)[position]) ?: -1
                        val action =
                            ListFragmentDirections.actionListFragmentToGameEditFragment(index.toLong())
                        view.findNavController().navigate(action)
                    })
                    recycler.adapter = newAdapter
                }
                2 -> {
                    val filteredBooks: MutableList<Book> = mutableListOf()
                    books.value?.forEach {
                        if (it.title.lowercase().contains(query) && (!it.seen || !notSeen))
                            filteredBooks.add(it)
                    }
                    val ld: MutableLiveData<List<Book>> = MutableLiveData()
                    ld.value = filteredBooks
                    val newAdapter = BooksListAdapter(ld, fun(position: Int) {
                        val index =
                            books.value?.indexOf((ld.value as MutableList<Book>)[position]) ?: -1
                        val action =
                            ListFragmentDirections.actionListFragmentToBookEditFragment(index.toLong())
                        view.findNavController().navigate(action)
                    })
                    recycler.adapter = newAdapter
                }
            }
        } else {
            when(listMode.value){
                0 -> recycler.adapter = if (!notSeen) moviesListAdapter else notSeenMoviesListAdapter
                1 -> recycler.adapter = if (!notSeen) gamesListAdapter else notSeenGamesListAdapter
                2 -> recycler.adapter = if (!notSeen) booksListAdapter else notSeenBooksListAdapter
            }
        }
    }

    private fun setMode(view: View, mode: Int, nsSwitch: SwitchCompat){
        val addBtn = view.findViewById<Button>(R.id.add_record_btn)
        when(mode){
            0 -> { // MOVIES
                recycler.adapter = if(!nsSwitch.isChecked) moviesListAdapter else notSeenMoviesListAdapter
                addBtn?.setOnClickListener {
                    val action = ListFragmentDirections.actionListFragmentToMovieEditFragment()
                    view.findNavController().navigate(action)
                }
            }
            1 -> { // GAMES
                recycler.adapter = if(!nsSwitch.isChecked) gamesListAdapter else notSeenGamesListAdapter
                addBtn.setOnClickListener {
                    val action = ListFragmentDirections.actionListFragmentToGameEditFragment()
                    view.findNavController().navigate(action)
                }
            }
            2 -> { // BOOKS
                recycler.adapter = if(!nsSwitch.isChecked) booksListAdapter else notSeenBooksListAdapter
                addBtn.setOnClickListener {
                    val action = ListFragmentDirections.actionListFragmentToBookEditFragment()
                    view.findNavController().navigate(action)
                }
            }
            else -> throw Exception()
        }
    }
}