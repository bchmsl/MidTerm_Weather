package com.bchmsl.midterm_weather.ui.main

import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bchmsl.midterm_weather.R
import com.bchmsl.midterm_weather.adapter.DailyForecastAdapter
import com.bchmsl.midterm_weather.databinding.FragmentMainBinding
import com.bchmsl.midterm_weather.extensions.setImage
import com.bchmsl.midterm_weather.model.ForecastResponse
import com.bchmsl.midterm_weather.network.utils.ResponseHandler
import com.bchmsl.midterm_weather.ui.base.BaseFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {
    private val viewModel: MainViewModel by viewModels()
    private val forecastAdapter by lazy { DailyForecastAdapter() }

    private lateinit var firebaseAuth : FirebaseAuth
    private  lateinit var databaseReference: DatabaseReference
    private var uid: String? = null

    override fun start() {
        setWelcomeMessage()

        viewModel.getForecast()
        observers()
    }

    private fun setWelcomeMessage() {
        //init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        //get current user main info
        val firebaseUser  = firebaseAuth.currentUser
        //user id
        uid = firebaseUser?.uid
        if(uid!=null){
            databaseReference = FirebaseDatabase.getInstance().getReference("Users")
            databaseReference.child(uid!!).get().addOnSuccessListener {
                if (it.exists()){
                    val firstname = (it.value as HashMap<*, *>)["firstName"]
                    binding.tvGreeting.text = getString(R.string.welcome_message, firstname)
                } else {
                    Snackbar.make(binding.root, "No logged in User's name was found", Snackbar.LENGTH_SHORT)
                        .setTextMaxLines(2)
                        .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.regular_red))
                        .show()
                }
            }
        }

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