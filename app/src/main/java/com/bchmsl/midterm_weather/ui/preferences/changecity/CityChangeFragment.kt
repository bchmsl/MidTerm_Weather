package com.bchmsl.midterm_weather.ui.preferences.changecity

import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bchmsl.midterm_weather.R
import com.bchmsl.midterm_weather.adapter.SearchAdapter
import com.bchmsl.midterm_weather.databinding.FragmentCityChangeBinding
import com.bchmsl.midterm_weather.extensions.hideKeyboard
import com.bchmsl.midterm_weather.extensions.makeSnackbar
import com.bchmsl.midterm_weather.model.SearchResponse
import com.bchmsl.midterm_weather.network.utils.ResponseHandler
import com.bchmsl.midterm_weather.ui.base.BaseFragment
import kotlinx.coroutines.launch

class CityChangeFragment :
    BaseFragment<FragmentCityChangeBinding>(FragmentCityChangeBinding::inflate) {
    private val viewModel: CityChangeViewModel by viewModels()
    private val searchAdapter by lazy { SearchAdapter() }

    override fun start() {
        listeners()
        setupRecycler()
    }

    private fun setupRecycler() {
        binding.rvSearchResults.adapter = searchAdapter
    }

    private fun listeners() {
        binding.tilSearch.editText?.doAfterTextChanged {
            startSearching(it.toString())
        }
        searchAdapter.itemClick = {
            binding.rvSearchResults.hideKeyboard()
            lifecycleScope.launch {
                viewModel.saveCity(it)
            }.invokeOnCompletion {
                findNavController().popBackStack(R.id.mainFragment, false)
            }
        }
    }

    private fun startSearching(query: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchCity(query)
                viewModel.searchResponse.collect { responseHandler ->
                    binding.lpiSearch.isVisible = responseHandler.isLoading
                    when (responseHandler) {
                        is ResponseHandler.Success -> handleSearchSuccess(responseHandler.data!!)
                        is ResponseHandler.Error -> handleError(responseHandler.error.message!!)
                        else -> {}
                    }
                }
            }
        }
    }

    private fun handleSearchSuccess(data: List<SearchResponse>) {
        searchAdapter.submitList(data)
    }

    private fun handleError(message: String) {
        binding.root.makeSnackbar(message)
    }

}