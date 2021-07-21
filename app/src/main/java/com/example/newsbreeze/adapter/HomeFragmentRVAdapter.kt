package com.example.newsbreeze.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.newsbreeze.R
import com.example.newsbreeze.data.db.BreakingNewsEntity
import com.example.newsbreeze.databinding.RvHomeItemBinding
import com.example.newsbreeze.utils.Utils

class HomeFragmentRVAdapter(val listener: HomeRVClickListener) :
    ListAdapter<BreakingNewsEntity, HomeFragmentRVAdapter.MyViewHolder>(MyDiffUtilCallback()) {

    class MyDiffUtilCallback : DiffUtil.ItemCallback<BreakingNewsEntity>() {
        override fun areItemsTheSame(
            oldItem: BreakingNewsEntity,
            newItem: BreakingNewsEntity
        ): Boolean {
            return oldItem.id!! == newItem.id!!
        }

        override fun areContentsTheSame(
            oldItem: BreakingNewsEntity,
            newItem: BreakingNewsEntity
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    class MyViewHolder(val binding: RvHomeItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(breakingNewsEntity: BreakingNewsEntity) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(breakingNewsEntity.urlToImage)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.no)
                    .into(coverIv)
                titleTv.text = breakingNewsEntity.title
                subtitleTv.text = breakingNewsEntity.description

                var dateToShow = breakingNewsEntity.publishedAt
                if (dateToShow != "Date N/A") {
                    dateToShow =
                        Utils.convertISOTimeToDate(dateToShow).toString()
                }
                dateTv.text = dateToShow
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RvHomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentNews = getItem(position)
        holder.bind(currentNews)

        holder.binding.readBt.setOnClickListener {
            listener.onReadClick(currentNews)
        }

        holder.binding.saveBt.setOnClickListener {
            listener.onSaveClick(currentNews)
        }
    }
}

interface HomeRVClickListener {
    fun onReadClick(entity: BreakingNewsEntity)

    fun onSaveClick(entity: BreakingNewsEntity)
}