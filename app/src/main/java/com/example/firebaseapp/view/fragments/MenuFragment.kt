package com.example.firebaseapp.view.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.example.firebaseapp.viewmodel.MenuViewModel
import com.example.firebaseapp.R
import com.example.firebaseapp.viewmodel.ViewModelFactory

class MenuFragment : Fragment() {
    private lateinit var viewModel: MenuViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.menu_fragment, container, false)
    }

    override fun onViewCreated(view: View,savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = ViewModelFactory(requireNotNull(this.activity).application)
        viewModel = ViewModelProvider(requireActivity(),factory)[MenuViewModel::class.java]
        viewModel.setGreetings(view.findViewById(R.id.greeting_tv))
        view.findViewById<CardView>(R.id.movies_card).setOnClickListener{
            viewModel.navigateToListFragment(view,0)
        }
        view.findViewById<CardView>(R.id.games_card).setOnClickListener{
            viewModel.navigateToListFragment(view, 1)
        }
        view.findViewById<CardView>(R.id.books_card).setOnClickListener{
            viewModel.navigateToListFragment(view, 2)
        }
    }

}
