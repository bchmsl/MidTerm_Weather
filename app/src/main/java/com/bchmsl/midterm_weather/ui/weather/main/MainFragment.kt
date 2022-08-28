package com.bchmsl.midterm_weather.ui.weather.main

import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bchmsl.midterm_weather.R
import com.bchmsl.midterm_weather.adapters.DailyForecastAdapter
import com.bchmsl.midterm_weather.databinding.FragmentMainBinding
import com.bchmsl.midterm_weather.extensions.makeErrorSnackbar
import com.bchmsl.midterm_weather.extensions.makeSnackbar
import com.bchmsl.midterm_weather.extensions.setImage
import com.bchmsl.midterm_weather.extensions.toTemperature
import com.bchmsl.midterm_weather.models.ForecastResponse
import com.bchmsl.midterm_weather.ui.base.BaseFragment
import com.bchmsl.midterm_weather.ui.weather.WeatherViewModel
import com.bchmsl.midterm_weather.utils.ResponseHandler
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {
    private val viewModel: WeatherViewModel by activityViewModels()
    private val forecastAdapter by lazy { DailyForecastAdapter() }

    override fun start() {
        setWelcomeMessage()
        getForecast()
    }


    private fun setWelcomeMessage() {
        viewModel.getUserFirstName()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userFirstNameResponse.collect {
                    when (it) {
                        is ResponseHandler.Success -> {
                            val firstname = (it.data?.value as HashMap<*, *>?)?.get("firstName")
                            if (it.data?.value == null) {
                                binding.root.makeErrorSnackbar("Please provide your name in Profile Preferences")
                            } else {
                                binding.tvGreeting.text =
                                    getString(R.string.welcome_message, firstname)
                            }
                        }
                        is ResponseHandler.Error -> {
                            handleError("No logged in User's name was found")
                        }
                        else -> {}
                    }
                }

            }
        }

    }

    private fun getForecast() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getDatastoreValue().firstOrNull()?.let { viewModel.getForecast(it) }
            viewModel.forecastResponse.collect { responseHandler ->
                binding.lpiLoading.isVisible = responseHandler.isLoading
                Log.wtf("TAG", "Observed")
                when (responseHandler) {
                    is ResponseHandler.Success -> handleForecastSuccess(responseHandler.data)
                    is ResponseHandler.Error -> handleError(responseHandler.error.message!!)
                    else -> {}
                }
            }
        }
    }


    private fun handleError(error: String) {
        binding.root.makeSnackbar(error)
    }

    private fun handleForecastSuccess(data: ForecastResponse?) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getUnit().collectLatest {
                    setData(it, data)
                }
            }
        }

    }

    private fun setData(isFahrenheit: Boolean, data: ForecastResponse?) {
        binding.apply {
            tvCityName.text = data?.location?.name
            tvCondition.text = data?.current?.condition?.text
            tvCurrentTemperature.text = data?.current?.tempC?.toTemperature(isFahrenheit)
            ivIcon.setImage(data?.current?.condition?.icon)
            rvForecast.visibility = View.VISIBLE
            rvForecast.adapter = forecastAdapter
        }
        forecastAdapter.apply {
            submitList(data?.forecast?.forecastday)
            setFahrenheit(isFahrenheit)
        }
        listeners(isFahrenheit)
    }

    private fun listeners(isFahrenheit: Boolean) {
        forecastAdapter.onItemClick = { index ->
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToForecastOpenedFragment(
                    index,
                    isFahrenheit
                )
            )
        }
        binding.ibtnPreferences.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToPreferencesFragment())
        }
        binding.tvGreeting.setOnClickListener {
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToChangeUserInfoFragment(
                    false
                )
            )
        }
    }
}