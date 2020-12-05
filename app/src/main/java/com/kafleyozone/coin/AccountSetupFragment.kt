package com.kafleyozone.coin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kafleyozone.coin.databinding.FragmentAccountSetupBinding
import com.kafleyozone.coin.databinding.ListItemAccountSetupBinding

class AccountSetupFragment(pagerListener: OnboardingFlowFragment.PagerListenerInterface) : Fragment() {

    private var _binding: FragmentAccountSetupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAccountSetupBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.accountSetupRecyclerView.adapter = BankListAdapter()
        binding.accountSetupRecyclerView.layoutManager = LinearLayoutManager(context)

        binding.setupAddAccountButton.setOnClickListener {
            AddNewBankDialogFragment()
                    .show(parentFragmentManager, AddNewBankDialogFragment.TAG)
        }

        return view
    }

    // RecyclerView Classes
    class BankListAdapter : RecyclerView.Adapter<BankListAdapter.BankListItemViewHolder>() {

        class BankListItemViewHolder(private val itemBinding: ListItemAccountSetupBinding)
            : RecyclerView.ViewHolder(itemBinding.root) {

            fun bind() {
                itemBinding.institutionNameTextView.text = "Ally"
                itemBinding.institutionTypeTextView.text = "Savings"
                itemBinding.institutionAmountTextView.text = "$12,342.44"
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : BankListItemViewHolder {
            val binding = ListItemAccountSetupBinding.inflate(LayoutInflater.from(parent.context),
                    parent, false)
            return BankListItemViewHolder(binding)
        }

        override fun onBindViewHolder(holder: BankListItemViewHolder, position: Int) {
            holder.bind()
        }

        override fun getItemCount(): Int {
            return 5
        }
    }
}