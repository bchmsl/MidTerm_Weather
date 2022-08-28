package com.bchmsl.midterm_weather.ui.weather.forecast_opened

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bchmsl.midterm_weather.adapters.DetailsAdapter
import com.bchmsl.midterm_weather.databinding.FragmentForecastOpenedBinding
import com.bchmsl.midterm_weather.extensions.setImage
import com.bchmsl.midterm_weather.extensions.toSimplifiedDate
import com.bchmsl.midterm_weather.extensions.toTemperature
import com.bchmsl.midterm_weather.models.DetailsKeyValue
import com.bchmsl.midterm_weather.models.ForecastResponse
import com.bchmsl.midterm_weather.ui.base.BaseFragment
import com.bchmsl.midterm_weather.ui.weather.WeatherViewModel
import com.bchmsl.midterm_weather.utils.ResponseHandler
import kotlinx.coroutines.launch


class ForecastOpenedFragment :
    BaseFragment<FragmentForecastOpenedBinding>(FragmentForecastOpenedBinding::inflate) {
    private val args: ForecastOpenedFragmentArgs by navArgs()
    private val viewModel: WeatherViewModel by activityViewModels()
    private val detailsAdapter by lazy { DetailsAdapter() }
    override fun start() {
        setData(args.index, args.isFahrenheit)
        listeners()
    }

    private fun listeners() {
        binding.tvCityName.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setData(index: Int, isFahrenheit: Boolean) {
        lifecycleScope.launch {
            binding.apply {
                viewModel.forecastResponse.collect { responseHandler ->
                    when (responseHandler) {
                        is ResponseHandler.Success<*> -> handleForecastSuccess(
                            responseHandler.data as ForecastResponse,
                            index,
                            isFahrenheit
                        )
                        else -> {}
                    }
                }
            }
        }
    }

    private fun handleForecastSuccess(data: ForecastResponse, index: Int, isFahrenheit: Boolean) {
        val chosenDay = data.forecast?.forecastday?.get(index)
        binding.apply {
            tvCityName.text = data.location?.name
            tvAverageTemperature.text = chosenDay?.day?.avgtempC?.toTemperature(isFahrenheit)
            tvCondition.text = chosenDay?.day?.condition?.text
            tvDay.text = chosenDay?.date?.toSimplifiedDate()
            ivIcon.setImage(chosenDay?.day?.condition?.icon)
        }
        setupRecycler(chosenDay, isFahrenheit)
    }

    private fun setupRecycler(
        chosenDay: ForecastResponse.Forecast.ForecastDay?,
        isFahrenheit: Boolean
    ) {
        DetailsKeyValue.Data.addDetailsData(chosenDay)
        binding.rvDetailsTiles.adapter = detailsAdapter
        detailsAdapter.apply {
            submitList(DetailsKeyValue.Data.data)
            setFahrenheit(isFahrenheit)
        }

    }
}