package com.example.copilotapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.copilotapp.databinding.TokenConfigurationFragmentBinding


class TokenConfigurationFragment : Fragment() {


    private var _binding: TokenConfigurationFragmentBinding? = null
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
        _binding = TokenConfigurationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set up dropdown menu with arrow indicator
        binding.dropdownMenu.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0)

        binding.dropdownMenu.setOnClickListener {
            binding.dropdownMenu.showDropDown()
        }

        binding.continueButton.setOnClickListener {
            val token = binding.editText.text.toString().trim()
            val selectedOption = binding.dropdownMenu.text.toString().trim()
            if (token.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a token", Toast.LENGTH_SHORT).show()
            } else if (selectedOption.isEmpty()) {
                Toast.makeText(requireContext(), "Please select an environment", Toast.LENGTH_SHORT).show()
            } else {
                mainActivity?.initCopilot(token, selectedOption)
                mainActivity?.navController?.navigate(R.id.homeScreenFragment)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.dropdownMenu.inputType = 0
        binding.dropdownMenu.keyListener = null

        val options = arrayOf("sit", "uat", "prod")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, options)
        binding.dropdownMenu.setAdapter(adapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding to avoid memory leaks
        _binding = null
    }

}