package com.bchmsl.midterm_weather.ui.main

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bchmsl.midterm_weather.adapter.DailyForecastAdapter
import com.bchmsl.midterm_weather.databinding.FragmentMainBinding
import com.bchmsl.midterm_weather.extensions.setImage
import com.bchmsl.midterm_weather.model.ForecastResponse
import com.bchmsl.midterm_weather.network.utils.ResponseHandler
import com.bchmsl.midterm_weather.ui.base.BaseFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {
    private val viewModel: MainViewModel by viewModels()
    private val forecastAdapter by lazy { DailyForecastAdapter() }
    override fun start() {
        viewModel.getForecast()
        observers()
    }

    private fun observers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.forecastResponse.collect { responseHandler ->
                binding.lpiLoading.isVisible = responseHandler.isLoading
                when (responseHandler) {
                    is ResponseHandler.Success -> handleForecastSuccess(responseHandler.data)
                    is ResponseHandler.Error -> handleError(responseHandler.error)
                    else -> {}
                }
            }
        }
    }

    private fun handleError(error: Throwable) {
        Snackbar.make(binding.root, "${error.message}", Snackbar.LENGTH_SHORT).show()
    }

    private fun handleForecastSuccess(data: ForecastResponse) {
        binding.apply {
            tvCityName.text = data.location?.name
            tvCondition.text = data.current?.condition?.text
            tvCurrentTemperature.text = data.current?.tempC.toString()
            ivIcon.setImage(data.current?.condition?.icon)
        }
        binding.rvForecast.adapter = forecastAdapter
        forecastAdapter.submitList(data.forecast?.forecastday)
        listeners()

    }

    private fun listeners() {
        forecastAdapter.onItemClick = { data ->
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToForecastOpenedFragment(
                    data,
                    binding.tvCityName.text.toString()
                )
            )
        }
    }
}