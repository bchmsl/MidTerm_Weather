package com.bchmsl.midterm_weather.ui.weather.forecast_opened

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bchmsl.midterm_weather.adapter.DetailsAdapter
import com.bchmsl.midterm_weather.databinding.FragmentForecastOpenedBinding
import com.bchmsl.midterm_weather.extensions.asTemp
import com.bchmsl.midterm_weather.extensions.setImage
import com.bchmsl.midterm_weather.extensions.toWeekday
import com.bchmsl.midterm_weather.model.DetailsKeyValue
import com.bchmsl.midterm_weather.model.ForecastResponse
import com.bchmsl.midterm_weather.network.utils.ResponseHandler
import com.bchmsl.midterm_weather.ui.base.BaseFragment
import com.bchmsl.midterm_weather.ui.weather.WeatherViewModel
import kotlinx.coroutines.launch


class ForecastOpenedFragment :
    BaseFragment<FragmentForecastOpenedBinding>(FragmentForecastOpenedBinding::inflate) {
    private val args: ForecastOpenedFragmentArgs by navArgs()
    private val viewModel: WeatherViewModel by activityViewModels()
    private val detailsAdapter by lazy{ DetailsAdapter() }
    override fun start() {
        setData(args.index)
        listeners()
    }

    private fun listeners() {
        binding.tvCityName.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setData(index: Int) {
        lifecycleScope.launch {
            binding.apply {
                viewModel.forecastResponse.collect { responseHandler ->
                    when (responseHandler) {
                        is ResponseHandler.Success<*> -> handleForecastSuccess(
                            responseHandler.data as ForecastResponse,
                            index
                        )
                        else -> {}
                    }
                }
            }
        }
    }

    private fun handleForecastSuccess(data: ForecastResponse, index: Int) {
        val chosenDay = data.forecast?.forecastday?.get(index)
        binding.apply {
            tvCityName.text = data.location?.name
            tvAverageTemperature.text = chosenDay?.day?.avgtempC?.asTemp()
            tvCondition.text = chosenDay?.day?.condition?.text
            tvDay.text = chosenDay?.date?.toWeekday()
            ivIcon.setImage(chosenDay?.day?.condition?.icon)
        }
        setupRecycler(chosenDay)

    }

    private fun setupRecycler(chosenDay: ForecastResponse.Forecast.ForecastDay?) {
        DetailsKeyValue.Data.addDetailsData(chosenDay)
        binding.rvDetailsTiles.adapter = detailsAdapter
        detailsAdapter.submitList(DetailsKeyValue.Data.data)

    }
}