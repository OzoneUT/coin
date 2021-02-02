package com.kafleyozone.coin.rvadapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kafleyozone.coin.data.models.BankInstitutionEntity
import com.kafleyozone.coin.databinding.ListItemAccountSetupBinding
import com.kafleyozone.coin.utils.convertStringToFormattedCurrency

class BankListAdapter : ListAdapter<BankInstitutionEntity,
        BankListAdapter.BankListItemViewHolder>(ItemDiffCallback()) {

    companion object {
        const val TAG = "BankListAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : BankListItemViewHolder {
        val binding = ListItemAccountSetupBinding.inflate(LayoutInflater.from(parent.context),
                parent, false)
        return BankListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BankListItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    // ########## ViewHolder Implementation ###########
    class BankListItemViewHolder(private val itemBinding: ListItemAccountSetupBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(item: BankInstitutionEntity) {
            itemBinding.institutionNameTextView.text = item.institutionName
            itemBinding.institutionTypeTextView.text = item.institutionType
            itemBinding.institutionAmountTextView.text =
                    convertStringToFormattedCurrency(item.initialAmount, symbol = true)
        }
    }

    // ########## DiffUtil's ItemCallback Implementation ###########
    class ItemDiffCallback : DiffUtil.ItemCallback<BankInstitutionEntity>() {
        override fun areItemsTheSame(oldItem: BankInstitutionEntity,
                                     newItem: BankInstitutionEntity
        ): Boolean {
            Log.i(TAG, "in areItemsTheSame()")
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: BankInstitutionEntity,
                                        newItem: BankInstitutionEntity
        ): Boolean {
            Log.i(TAG, "in areContentsTheSame()")
            return oldItem == newItem
        }
    }

}
