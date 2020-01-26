package com.zhuravlev.vitaly.weatherapp.ui.main_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhuravlev.vitaly.weatherapp.base.KodeinFragment
import com.zhuravlev.vitaly.weatherapp.databinding.MainScreenFragmentBinding

class MainScreenFragment : KodeinFragment() {
    private val viewModel: MainScreenViewModel by lazy {
        provide(MainScreenViewModel::class.java)
    }
    private lateinit var binding: MainScreenFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainScreenFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }
}
