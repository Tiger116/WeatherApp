package com.zhuravlev.vitaly.weatherapp.ui.main_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhuravlev.vitaly.weatherapp.R
import com.zhuravlev.vitaly.weatherapp.base.atom.Atom
import com.zhuravlev.vitaly.weatherapp.base.kodein.KodeinFragment
import com.zhuravlev.vitaly.weatherapp.base.snackbar.Snackbar
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

        binding.swipeRefreshLayout.setColorSchemeResources(
            R.color.colorPrimary,
            R.color.colorPrimaryDark
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getCurrentWeather()
        }
        viewModel.currentWeatherLiveData.observe {
            when (it) {
                is Atom.Success -> {
                    binding.weather = it.content
                }
                is Atom.Error -> {
                    val exception = it.throwable
                    exception.message?.let { message ->
                        Snackbar().showMessage(
                            requireContext(),
                            message
                        )
                    }
                }
                null -> {
                    Snackbar().showMessage(
                        requireContext(),
                        getString(R.string.undefined_error_message)
                    )
                }
            }
        }
        viewModel.getCurrentWeather()
    }
}
