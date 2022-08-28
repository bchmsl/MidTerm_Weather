package com.bchmsl.midterm_weather.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bchmsl.midterm_weather.databinding.LayoutDetailsTileDoubleBinding
import com.bchmsl.midterm_weather.databinding.LayoutDetailsTileNormalBinding
import com.bchmsl.midterm_weather.extensions.toTemperature
import com.bchmsl.midterm_weather.models.DetailsKeyValue

class DetailsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val ITEM_NORMAL = 1
        private const val ITEM_DOUBLE = 2
    }

    private var adapterList = listOf<DetailsKeyValue>()
    private var isFahrenheit = false

    fun setFahrenheit(isFahrenheit: Boolean) {
        this.isFahrenheit = isFahrenheit
    }

    inner class NormalViewHolder(private val binding: LayoutDetailsTileNormalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val currentItem = adapterList[adapterPosition]
            binding.tvKey.text = currentItem.key
            binding.tvValue.text = currentItem.value.toString()
            currentItem.image?.let { binding.ivIcon.setImageResource(it) }
        }
    }

    inner class DoubleViewHolder(private val binding: LayoutDetailsTileDoubleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val currentItem = adapterList[adapterPosition]
            binding.apply {
                tvMainKey.text = currentItem.key
                tvKey1.text = currentItem.listValue?.get(0)?.key ?: ""
                tvKey2.text = currentItem.listValue?.get(1)?.key ?: ""

                if (currentItem.isTemperature) {
                    tvValue1.text =
                        (currentItem.listValue?.get(0)?.value as Double).toTemperature(isFahrenheit)
                    tvValue2.text =
                        (currentItem.listValue[1].value as Double).toTemperature(isFahrenheit)
                } else {
                    tvValue1.text = (currentItem.listValue?.get(0)?.value ?: "") as CharSequence?
                    tvValue2.text = (currentItem.listValue?.get(1)?.value ?: "") as CharSequence?
                }
                currentItem.image?.let { ivIcon.setImageResource(it) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_DOUBLE -> DoubleViewHolder(
                LayoutDetailsTileDoubleBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            else -> {
                NormalViewHolder(
                    LayoutDetailsTileNormalBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DoubleViewHolder -> holder.bind()
            is NormalViewHolder -> holder.bind()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (adapterList[position].isDouble) {
            true -> ITEM_DOUBLE
            else -> ITEM_NORMAL
        }
    }

    override fun getItemCount() = adapterList.size

    fun submitList(newList: List<DetailsKeyValue>) {
        adapterList = newList
        notifyItemRangeChanged(0, adapterList.size)
    }
}