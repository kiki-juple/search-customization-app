package com.kiki.searchcustomization.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kiki.searchcustomization.R
import com.kiki.searchcustomization.data.entity.SearchModel
import com.kiki.searchcustomization.databinding.ItemSearchBinding
import com.kiki.searchcustomization.util.toRupiah

class SearchAdapter(val onClick: (SearchModel) -> Unit) :
    ListAdapter<SearchModel, SearchAdapter.SearchViewHolder>(COMPARATOR) {
    inner class SearchViewHolder(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SearchModel) {
            with(binding) {
                tvMenu.text = data.menu.name
                tvWarteg.text = data.warteg.name
                tvPrice.text = data.menu.price.toRupiah()
                tvJarak.text = itemView.context.getString(R.string.distance, data.warteg.distance)
                tvUlasan.text = itemView.context.getString(R.string.review, data.warteg.review)
                tvRating.text = data.warteg.rating.toString()
            }
            itemView.setOnClickListener { onClick(data) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SearchViewHolder(
        ItemSearchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) =
        holder.bind(getItem(position))

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<SearchModel>() {
            override fun areItemsTheSame(
                oldItem: SearchModel,
                newItem: SearchModel,
            ): Boolean {
                return oldItem.menu.id == newItem.menu.id
            }

            override fun areContentsTheSame(
                oldItem: SearchModel,
                newItem: SearchModel,
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}