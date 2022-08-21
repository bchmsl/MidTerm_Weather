package com.bchmsl.midterm_weather.ui.forecast_opened

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bchmsl.midterm_weather.databinding.FragmentForecastOpenedBinding
import com.bchmsl.midterm_weather.extensions.setImage
import com.bchmsl.midterm_weather.extensions.toWeekday
import com.bchmsl.midterm_weather.model.ForecastResponse
import com.bchmsl.midterm_weather.ui.base.BaseFragment


class ForecastOpenedFragment :
    BaseFragment<FragmentForecastOpenedBinding>(FragmentForecastOpenedBinding::inflate) {
    private val args: ForecastOpenedFragmentArgs by navArgs()
    override fun start() {
        setData(args.data, args.cityName)
        listeners()
    }

    private fun listeners() {
        binding.tvCityName.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setData(data: ForecastResponse.Forecast.ForecastDay, cityName: String) {
        binding.apply {

        }
    }

}