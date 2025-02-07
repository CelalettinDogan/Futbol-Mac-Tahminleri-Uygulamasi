package com.example.bankokuponpro.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bankokuponpro.R
import com.example.bankokuponpro.databinding.FragmentVipBinding


class VipFragment : Fragment() {


    private lateinit var binding:FragmentVipBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=FragmentVipBinding.inflate(inflater)
        
        return binding.root
    }





}