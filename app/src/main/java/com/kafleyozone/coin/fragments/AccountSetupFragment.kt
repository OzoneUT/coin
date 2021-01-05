package com.kafleyozone.coin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kafleyozone.coin.viewmodels.AccountSetupFragmentViewModel
import com.kafleyozone.coin.models.BankInstitutionEntity
import com.kafleyozone.coin.rvadapters.BankListAdapter
import com.kafleyozone.coin.databinding.FragmentAccountSetupBinding
import com.kafleyozone.coin.printListDebug

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

        // Simple Swipe to Delete implementation
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT + ItemTouchHelper.LEFT) {
            // to add an icon/background to the swipe, override onChildDraw
            // https://medium.com/@kitek/recyclerview-swipe-to-delete-easier-than-you-thought-cff67ff5e5f6
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.removeBankEntityItemAt(viewHolder.adapterPosition)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.accountSetupRecyclerView)

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