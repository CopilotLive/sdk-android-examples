package com.example.copilotapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.copilotapp.databinding.HomeScreenFragmentBinding


class HomeScreenFragment : Fragment() {


    private var _binding: HomeScreenFragmentBinding? = null
    private val binding get() = _binding!!
    var mainActivity: MainActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using View Binding
        _binding = HomeScreenFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.chatButton.setOnClickListener {
            mainActivity?.openChat()
        }

        binding.callButton.setOnClickListener {
            mainActivity?.makeCall()
        }

        binding.loginButton.setOnClickListener {
            mainActivity?.login()
        }

        binding.logoutButton.setOnClickListener {
            mainActivity?.logout()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding to avoid memory leaks
        _binding = null
    }

}