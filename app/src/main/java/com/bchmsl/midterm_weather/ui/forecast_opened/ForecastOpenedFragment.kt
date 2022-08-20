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
        binding.ibtnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setData(data: ForecastResponse.Forecast.ForecastDay, cityName: String) {
        binding.apply {
            tvDayName.text = data.date?.toWeekday()
            tvCityName.text = cityName
            tvCondition.text = data.day?.condition?.text
            ivIcon.setImage(data.day?.condition?.icon)
            tvAverageTemperature.text = data.day?.avgtempC.toString()
            tvDate.text = data.date.toString()
            tvMaxTemp.text = data.day?.maxtempC.toString()
            tvMinTemp.text = data.day?.mintempC.toString()
            tvHumidity.text = data.day?.avgHumidity.toString()
            tvSunrise.text = data.astro?.sunrise.toString()
            tvSunset.text = data.astro?.sunset.toString()
            tvMoonrise.text = data.astro?.moonrise.toString()
            tvMoonset.text = data.astro?.moonset.toString()
        }
    }

}