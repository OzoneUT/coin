package com.kafleyozone.coin.rvadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.kafleyozone.coin.data.domain.Category
import com.kafleyozone.coin.databinding.ListItemCategoryBinding
import com.kafleyozone.coin.utils.getAssetUri

class CategoryListAdapter :
    ListAdapter<Category, CategoryListAdapter.CategoryItemViewHolder>(ItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        val binding = ListItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        return CategoryItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class CategoryItemViewHolder(private val itemBinding: ListItemCategoryBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(category: Category) {
            itemBinding.categoryNameTextview.text = category.categoryName
            itemBinding.categoryImageView.load(getAssetUri(category.fileName))
        }
    }

    class ItemDiffCallback : DiffUtil.ItemCallback<Category>() {

        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }

    }
}