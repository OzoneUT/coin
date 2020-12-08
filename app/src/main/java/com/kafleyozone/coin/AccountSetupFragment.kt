package com.kafleyozone.coin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.kafleyozone.coin.databinding.FragmentAccountSetupBinding

class AccountSetupFragment(pagerListener: OnboardingFlowFragment.PagerListenerInterface) : Fragment() {

    private val viewModel: AccountSetupFragmentViewModel by viewModels(ownerProducer = {this})
    private var _binding: FragmentAccountSetupBinding? = null
    private val binding get() = _binding!!
    private var listAdapter: ListAdapter<BankInstitutionEntity,
            BankListAdapter.BankListItemViewHolder>? = null

    companion object {
        private const val TAG = "AccountSetupFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAccountSetupBinding.inflate(inflater, container, false)
        val view = binding.root
        listAdapter = BankListAdapter()
        binding.accountSetupRecyclerView.adapter = listAdapter
        binding.accountSetupRecyclerView.layoutManager = LinearLayoutManager(context)

        binding.setupAddAccountButton.setOnClickListener {
            AddNewBankDialogFragment()
                    .show(childFragmentManager, AddNewBankDialogFragment.TAG)
        }

        viewModel.setupBankList.observe(viewLifecycleOwner) {
            printListDebug(TAG, it)
            (listAdapter as BankListAdapter).submitList(it.toList())
        }

        return view
    }
}