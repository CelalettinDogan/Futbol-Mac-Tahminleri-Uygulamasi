package com.example.bankokuponpro.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.bankokuponpro.R
import com.example.bankokuponpro.databinding.FragmentAiGuessBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class AiGuessFragment : Fragment() {

    private lateinit var binding: FragmentAiGuessBinding

    override
    fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAiGuessBinding.inflate(inflater, container, false)

        // Activity içindeki BottomNavigationView'e erişim
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)

        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.navController)

        // Button tıklamasında menü öğesini seçme
        binding.outlinedButton.setOnClickListener {
            bottomNavigationView.selectedItemId = R.id.vipFragment
        }

        return binding.root
    }
}