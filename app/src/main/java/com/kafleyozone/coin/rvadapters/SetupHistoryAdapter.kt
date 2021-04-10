package com.kafleyozone.coin.rvadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kafleyozone.coin.data.domain.SetupAmountEntity
import com.kafleyozone.coin.databinding.ListItemAccountSetupBinding
import com.kafleyozone.coin.utils.convertDoubleToFormattedCurrency

class SetupHistoryAdapter(
    private val onDeleteListener: (SetupAmountEntity) -> Unit
) : ListAdapter<SetupAmountEntity,
        SetupHistoryAdapter.HistoryItemViewHolder>(ItemDiffCallback()) {

    companion object {
        const val TAG = "SetupHistoryAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        val binding = ListItemAccountSetupBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return HistoryItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onDeleteListener)
    }

    // ########## ViewHolder Implementation ###########
    class HistoryItemViewHolder(private val itemBinding: ListItemAccountSetupBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(item: SetupAmountEntity, onDeleteListener: (SetupAmountEntity) -> Unit) {
            itemBinding.institutionAmountTextView.text =
                convertDoubleToFormattedCurrency(item.amount, symbol = true)
            itemBinding.deleteItemButton.setOnClickListener { onDeleteListener(item) }
        }
    }

    // ########## DiffUtil's ItemCallback Implementation ###########
    class ItemDiffCallback : DiffUtil.ItemCallback<SetupAmountEntity>() {
        override fun areItemsTheSame(
            oldItem: SetupAmountEntity,
            newItem: SetupAmountEntity
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: SetupAmountEntity,
            newItem: SetupAmountEntity
        ): Boolean {
            return oldItem == newItem
        }
    }
}
