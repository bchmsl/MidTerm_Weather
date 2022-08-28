package com.bchmsl.midterm_weather.ui.weather.main

import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bchmsl.midterm_weather.R
import com.bchmsl.midterm_weather.adapter.DailyForecastAdapter
import com.bchmsl.midterm_weather.databinding.FragmentMainBinding
import com.bchmsl.midterm_weather.extensions.asTemp
import com.bchmsl.midterm_weather.extensions.makeSnackbar
import com.bchmsl.midterm_weather.extensions.setImage
import com.bchmsl.midterm_weather.model.ForecastResponse
import com.bchmsl.midterm_weather.ui.base.BaseFragment
import com.bchmsl.midterm_weather.ui.weather.WeatherViewModel
import com.bchmsl.midterm_weather.utils.ResponseHandler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {
    private val viewModel: WeatherViewModel by activityViewModels()
    private val forecastAdapter by lazy { DailyForecastAdapter() }

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private var uid: String? = null

    override fun start() {
        setWelcomeMessage()
        getForecast()
        listeners()
    }


    private fun setWelcomeMessage() {
        //init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        //get current user main info
        val firebaseUser = firebaseAuth.currentUser
        //user id
        uid = firebaseUser?.uid
        if (uid != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("Users")
            databaseReference.child(uid!!).get().addOnSuccessListener {
                if (it.exists()) {
                    val firstname = (it.value as HashMap<*, *>)["firstName"]
                    binding.tvGreeting.text = getString(R.string.welcome_message, firstname)
                } else {
                    handleError("No logged in User's name was found")
                }
            }
        }
    }

    private fun getForecast() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getDatastoreValue()
            viewModel.readCityName?.firstOrNull()?.let { viewModel.getForecast(it) }
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
        binding.apply {
            tvCityName.text = data?.location?.name
            tvCondition.text = data?.current?.condition?.text
            tvCurrentTemperature.text = data?.current?.tempC?.asTemp()
            ivIcon.setImage(data?.current?.condition?.icon)
            rvForecast.visibility = View.VISIBLE
        }
        binding.rvForecast.adapter = forecastAdapter
        forecastAdapter.submitList(data?.forecast?.forecastday)
    }

    private fun listeners() {
        forecastAdapter.onItemClick = { index ->
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToForecastOpenedFragment(
                    index
                )
            )
        }
        binding.ibtnPreferences.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToPreferencesFragment())
        }
    }
}