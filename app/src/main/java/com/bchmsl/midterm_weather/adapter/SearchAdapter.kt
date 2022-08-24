package com.bchmsl.midterm_weather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bchmsl.midterm_weather.databinding.LayoutSearchResultsBinding
import com.bchmsl.midterm_weather.model.SearchResponse

class SearchAdapter : ListAdapter<SearchResponse, SearchAdapter.SearchViewHolder>(SearchItemCallBack()) {
    var itemClick: ((SearchResponse) -> Unit)? = null
    inner class SearchViewHolder(val binding: LayoutSearchResultsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val currentItem = getItem(adapterPosition)
            binding.root.text =
                currentItem.name.plus(", ")
                .plus(currentItem.region).plus(", ")
                .plus(currentItem.country)
            binding.root.setOnClickListener { itemClick?.invoke(currentItem) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            LayoutSearchResultsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind()
    }

    class SearchItemCallBack: DiffUtil.ItemCallback<SearchResponse>(){
        override fun areItemsTheSame(oldItem: SearchResponse, newItem: SearchResponse): Boolean {
            return oldItem.region == newItem.region
        }

        override fun areContentsTheSame(oldItem: SearchResponse, newItem: SearchResponse): Boolean {
            return oldItem == newItem
        }

    }
}