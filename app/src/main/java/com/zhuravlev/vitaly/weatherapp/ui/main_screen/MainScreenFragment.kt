package com.zhuravlev.vitaly.weatherapp.ui.main_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.zhuravlev.vitaly.weatherapp.base.kodein.KodeinFragment
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentWeatherLiveData.observe {
            val text = Gson().toJson(it)
            binding.currentWeatherTextView.text = text
        }
        viewModel.getCurrentWeather()
    }
}
