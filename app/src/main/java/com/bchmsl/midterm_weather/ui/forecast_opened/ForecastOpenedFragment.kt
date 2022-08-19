package com.bchmsl.midterm_weather.ui.forecast_opened

import androidx.navigation.fragment.navArgs
import com.bchmsl.midterm_weather.databinding.FragmentForecastOpenedBinding
import com.bchmsl.midterm_weather.model.ForecastResponse
import com.bchmsl.midterm_weather.ui.base.BaseFragment


class ForecastOpenedFragment : BaseFragment<FragmentForecastOpenedBinding>(FragmentForecastOpenedBinding::inflate) {
    private val args: ForecastOpenedFragmentArgs by navArgs()
    override fun start() {
        setData(args.data!!)

    }
    private fun setData(data: ForecastResponse.Forecast.ForecastDay) {
        binding.apply {
        }
    }

}