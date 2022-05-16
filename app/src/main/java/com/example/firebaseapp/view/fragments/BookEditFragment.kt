package com.example.firebaseapp.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.firebaseapp.viewmodel.BookEditViewModel
import com.example.firebaseapp.R
import com.example.firebaseapp.model.Book
import com.example.firebaseapp.model.controllers.BooksDataController
import com.example.firebaseapp.viewmodel.ViewModelFactory

class BookEditFragment : Fragment() {private lateinit var viewModel: BookEditViewModel
    private val args: BookEditFragmentArgs by navArgs()
    private var index: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.book_edit_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = ViewModelFactory(requireNotNull(this.activity).application)
        viewModel = ViewModelProvider(requireActivity(), factory)[BookEditViewModel::class.java]
        view.findViewById<Button>(R.id.cancel_btn).setOnClickListener{
            requireActivity().onBackPressed()
        }

        index = args.index.toInt()
        val books: MutableLiveData<List<Book>> = BooksDataController.data

        val title = view.findViewById<EditText>(R.id.book_title_input)
        val author = view.findViewById<EditText>(R.id.book_author_input)
        val genre = view.findViewById<EditText>(R.id.book_genre_input)
        val year = view.findViewById<EditText>(R.id.book_year_input)
        val seen = view.findViewById<SwitchCompat>(R.id.book_seen_switch)

        if (index == -1) {
            index = BooksDataController.data.value?.size ?: 0
            view.findViewById<Button>(R.id.remove_btn).let{
                it.isEnabled = false
                it.background.alpha = 40
            }
            view.findViewById<Button>(R.id.save_btn).setOnClickListener {
                if(title.text.toString() != "") {
                    val newBook = Book(
                        title = title.text.toString(),
                        author = author.text.toString(),
                        genre = genre.text.toString(),
                        year = year.text.toString(),
                        seen = seen.isChecked
                    )
                    if(index == 0) books.value = mutableListOf(newBook)
                    else {
                        val newList: MutableList<Book> = books.value!!.toMutableList()
                        newList.add(newBook)
                        books.value = newList
                    }
                    BooksDataController.saveBooks()
                    requireActivity().onBackPressed()
                }
            }
        }
        else {
            books.value!![index].let{
                title.setText(it.title)
                author.setText(it.author)
                genre.setText(it.genre)
                year.setText(it.year)
                seen.isChecked = it.seen
            }

            view.findViewById<Button>(R.id.save_btn).setOnClickListener {
                if(title.text.toString() != "") {
                    books.value!![index].let{
                        it.title = title.text.toString()
                        it.author = author.text.toString()
                        it.genre = genre.text.toString()
                        it.year = year.text.toString()
                        it.seen = seen.isChecked
                    }
                    BooksDataController.saveBooks()
                    requireActivity().onBackPressed()
                }
            }

            view.findViewById<Button>(R.id.remove_btn).setOnClickListener {
                val newList: MutableList<Book> = books.value!!.toMutableList()
                newList.removeAt(index)
                books.value = newList
                BooksDataController.saveBooks()
                requireActivity().onBackPressed()
            }
        }
    }

}