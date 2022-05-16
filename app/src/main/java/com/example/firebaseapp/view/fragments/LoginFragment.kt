package com.example.firebaseapp.view.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.firebaseapp.R
import com.example.firebaseapp.model.controllers.DatabaseController
import com.example.firebaseapp.viewmodel.LoginViewModel
import com.example.firebaseapp.viewmodel.ViewModelFactory

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = ViewModelFactory(requireNotNull(this.activity).application)
        viewModel = ViewModelProvider(requireActivity(),factory)[LoginViewModel::class.java]
        view.findViewById<Button>(R.id.login).setOnClickListener{
            viewModel.tryLogin(
                view.findViewById<EditText>(R.id.username).text.toString(),
                view.findViewById<EditText>(R.id.password).text.toString(),
            )
        }
        DatabaseController.authenticated.observe(viewLifecycleOwner) {
            if (it) viewModel.navigateToGamesFragment(view)
            else view.findViewById<TextView>(R.id.invalid_credentials).isVisible = true
        }
    }

}