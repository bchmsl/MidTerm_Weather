package com.bchmsl.midterm_weather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bchmsl.midterm_weather.databinding.LayoutForecastItemBinding
import com.bchmsl.midterm_weather.extensions.setImage
import com.bchmsl.midterm_weather.extensions.asTemp
import com.bchmsl.midterm_weather.extensions.toWeekday
import com.bchmsl.midterm_weather.model.ForecastResponse

class DailyForecastAdapter :
    ListAdapter<ForecastResponse.Forecast.ForecastDay, DailyForecastAdapter.ForecastViewHolder>(
        ForecastItemCallback()
    ) {
    var onItemClick: ((index: Int)-> Unit)? = null
    inner class ForecastViewHolder(private val binding: LayoutForecastItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val currentItem = getItem(adapterPosition)
            binding.apply {
                tvDay.text = currentItem.date?.toWeekday()
                ivIcon.setImage(currentItem.day?.condition?.icon)
                tvTemperature.text = currentItem.day?.avgtempC?.asTemp()
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