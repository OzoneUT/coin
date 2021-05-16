package com.kafleyozone.coin.utils

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.widget.RecyclerView
import com.kafleyozone.coin.rvadapters.CategoryListAdapter

const val MOCK_DEBUG = false

class CategoryKeyProvider(
    private val adapter: CategoryListAdapter
) : ItemKeyProvider<String>(SCOPE_CACHED) {

    override fun getKey(position: Int): String =
        adapter.currentList[position].id


    override fun getPosition(key: String): Int =
        adapter.currentList.indexOfFirst { it.id == key}

}

class CategoryDetailsLookup(
    private val recyclerView: RecyclerView
) : ItemDetailsLookup<String>() {

    override fun getItemDetails(e: MotionEvent): ItemDetails<String>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y)
        view?.let {
            return (recyclerView.getChildViewHolder(view)
                    as CategoryListAdapter.CategoryItemViewHolder).getItemDetails()
        }
        return null
    }

}