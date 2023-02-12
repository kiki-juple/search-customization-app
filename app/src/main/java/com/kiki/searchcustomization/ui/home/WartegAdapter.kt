package com.kiki.searchcustomization.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kiki.searchcustomization.R
import com.kiki.searchcustomization.data.entity.WartegWithMenu
import com.kiki.searchcustomization.databinding.CardWartegBinding

class WartegAdapter(val onClick: (WartegWithMenu) -> Unit) :
    ListAdapter<WartegWithMenu, WartegAdapter.WartegViewHolder>(COMPARATOR) {

    inner class WartegViewHolder(private val binding: CardWartegBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(warteg: WartegWithMenu) {
            binding.tvLocation.text = itemView.resources.getString(
                R.string.distance,
                warteg.warteg.distance
            )
            binding.tvRating.text = warteg.warteg.rating.toString()
            binding.tvUlasan.text =
                itemView.resources.getString(
                    R.string.review,
                    warteg.warteg.review
                )
            binding.tvWarteg.text = warteg.warteg.name
            itemView.setOnClickListener { onClick(warteg) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WartegViewHolder(
        CardWartegBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: WartegViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<WartegWithMenu>() {
            override fun areItemsTheSame(
                oldItem: WartegWithMenu,
                newItem: WartegWithMenu,
            ): Boolean {
                return oldItem.warteg.wartegId == newItem.warteg.wartegId
            }

            override fun areContentsTheSame(
                oldItem: WartegWithMenu,
                newItem: WartegWithMenu,
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}