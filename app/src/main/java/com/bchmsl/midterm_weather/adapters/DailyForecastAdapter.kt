package com.bchmsl.midterm_weather.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bchmsl.midterm_weather.R
import com.bchmsl.midterm_weather.app.App
import com.bchmsl.midterm_weather.databinding.LayoutForecastItemBinding
import com.bchmsl.midterm_weather.extensions.setImage
import com.bchmsl.midterm_weather.extensions.toSimplifiedDate
import com.bchmsl.midterm_weather.extensions.toTemperature
import com.bchmsl.midterm_weather.models.ForecastResponse

class DailyForecastAdapter :
    ListAdapter<ForecastResponse.Forecast.ForecastDay, DailyForecastAdapter.ForecastViewHolder>(
        ForecastItemCallback()
    ) {
    var onItemClick: ((index: Int) -> Unit)? = null
    private var isFahrenheit = false

    fun setFahrenheit(isFahrenheit: Boolean) {
        this.isFahrenheit = isFahrenheit
    }


    inner class ForecastViewHolder(private val binding: LayoutForecastItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val currentItem = getItem(adapterPosition)
            binding.apply {
                when (adapterPosition) {
                    0 -> tvDay.text = App.context.getText(R.string.today)
                    1 -> {
                        tvDay.text = App.context.getString(R.string.tomorrow)
                        tvDay.textSize = 13f
                    }
                    else -> tvDay.text = currentItem.date?.toSimplifiedDate()
                }
                ivIcon.setImage(currentItem.day?.condition?.icon)
                tvTemperature.text = currentItem.day?.avgtempC?.toTemperature(isFahrenheit)
                root.setOnClickListener { onItemClick?.invoke(adapterPosition) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ForecastViewHolder(
            LayoutForecastItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind()
    }

    class ForecastItemCallback : DiffUtil.ItemCallback<ForecastResponse.Forecast.ForecastDay>() {
        override fun areItemsTheSame(
            oldItem: ForecastResponse.Forecast.ForecastDay,
            newItem: ForecastResponse.Forecast.ForecastDay
        ) = oldItem.dateEpoch == newItem.dateEpoch


        override fun areContentsTheSame(
            oldItem: ForecastResponse.Forecast.ForecastDay,
            newItem: ForecastResponse.Forecast.ForecastDay
        ) = oldItem == newItem

    }
}