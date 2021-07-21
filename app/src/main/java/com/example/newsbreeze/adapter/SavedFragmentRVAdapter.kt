package com.example.newsbreeze.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.newsbreeze.R
import com.example.newsbreeze.data.db.SavedNewsEntity
import com.example.newsbreeze.databinding.RvSavedItemBinding
import com.example.newsbreeze.ui.viewmodel.SavedNewsViewModel
import com.example.newsbreeze.utils.Utils

class SavedFragmentRVAdapter(val savedNewsViewModel: SavedNewsViewModel, val listener: OnSavedNewsClickListener) :
    ListAdapter<SavedNewsEntity, SavedFragmentRVAdapter.MyViewHolder>(MyDiffUtilCallback()) {

    class MyDiffUtilCallback : DiffUtil.ItemCallback<SavedNewsEntity>() {
        override fun areItemsTheSame(oldItem: SavedNewsEntity, newItem: SavedNewsEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: SavedNewsEntity,
            newItem: SavedNewsEntity
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    class MyViewHolder(val binding: RvSavedItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(savedNewsEntity: SavedNewsEntity) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(savedNewsEntity.urlToImage)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.no)
                    .into(coverIv)
                titleTv.text = savedNewsEntity.title
                var dateToShow = savedNewsEntity.publishedAt
                if (dateToShow != "Date N/A") {
                    dateToShow =
                        Utils.convertISOTimeToDate(dateToShow).toString()
                }
                dateTv.text = dateToShow

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RvSavedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)

        holder.binding.delIv.setOnClickListener {
            savedNewsViewModel.deleteSavedNews(currentItem)
        }

        holder.itemView.setOnClickListener {
            listener.onSavedNewsClick(currentItem)
        }
    }
}

interface OnSavedNewsClickListener{

    fun onSavedNewsClick(savedNewsEntity: SavedNewsEntity)
}