package com.kafleyozone.coin.rvadapters

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.kafleyozone.coin.R
import com.kafleyozone.coin.data.domain.Category
import com.kafleyozone.coin.databinding.ListItemCategoryBinding
import com.kafleyozone.coin.utils.getAssetUri

class CategoryListAdapter(
        private val context: Context,
    ) : ListAdapter<Category, CategoryListAdapter.CategoryItemViewHolder>(ItemDiffCallback()) {

    var selectionTracker: SelectionTracker<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        val binding = ListItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        return CategoryItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        val item = getItem(position)
        selectionTracker?.let {
            holder.bind(item, it.isSelected(item.id))
        }
    }

    override fun getItemId(position: Int): Long = position.toLong()

    inner class CategoryItemViewHolder(private val itemBinding: ListItemCategoryBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(category: Category, selected: Boolean) {
            val bluePrimary = ContextCompat.getColor(context, R.color.blue_primary)
            val bluePrimaryPale = ContextCompat.getColor(context, R.color.blue_primary_pale)
            with(itemBinding) {

                if (selected){
                    selectionIndicator.setBackgroundColor(bluePrimary)
                    categoryImageView.load(R.drawable.ic_check_circle)
                    categoryImageView.setColorFilter(bluePrimaryPale)
                    categoryNameTextview.setTextColor(bluePrimary)
                    cardInnerLayout.background =
                        ContextCompat.getDrawable(context, R.drawable.rounded_card_border_blue)
                } else  {
                    selectionIndicator
                        .setBackgroundColor(ContextCompat.getColor(context, R.color.off_white_text))
                    categoryImageView.load(getAssetUri(category.fileName))
                    categoryImageView.clearColorFilter()
                    categoryNameTextview
                        .setTextColor(ContextCompat.getColor(context, R.color.darker_gray_text))
                    cardInnerLayout.background =
                        ContextCompat.getDrawable(context, R.drawable.rounded_card_border)
                }

                categoryNameTextview.text = category.categoryName
            }

        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<String> =
            object : ItemDetailsLookup.ItemDetails<String>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): String = getItem(adapterPosition).id
                override fun inSelectionHotspot(e: MotionEvent): Boolean = true
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