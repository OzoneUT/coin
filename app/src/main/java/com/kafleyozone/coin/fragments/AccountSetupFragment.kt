package com.kafleyozone.coin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.kafleyozone.coin.R
import com.kafleyozone.coin.data.models.BankInstitutionEntity
import com.kafleyozone.coin.databinding.FragmentAccountSetupBinding
import com.kafleyozone.coin.rvadapters.BankListAdapter
import com.kafleyozone.coin.utils.Status
import com.kafleyozone.coin.utils.printListDebug
import com.kafleyozone.coin.utils.setMargins
import com.kafleyozone.coin.viewmodels.AccountSetupFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountSetupFragment : Fragment() {

    private val args: AccountSetupFragmentArgs by navArgs()
    private val viewModel: AccountSetupFragmentViewModel by viewModels(ownerProducer = { this })
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

        // *** if we have a non-null name argument, previous fragment was HomeFragment
        args.name?.let {
            binding.accountSetupWelcome.text =
                    getString(R.string.welcome_to_coin_with_name, it.trim())
            binding.setupFinishButton
                    .setMargins(requireContext(), left = 100, right = 100, bottom = 32)
            onBackPressedSetup()
        }

        // Simple Swipe to Delete implementation
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT + ItemTouchHelper.LEFT) {
            // to add an icon/background to the swipe, override onChildDraw
            // https://medium.com/@kitek/recyclerview-swipe-to-delete-easier-than-you-thought-cff67ff5e5f6
            override fun onMove(
                    recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder,
            ): Boolean {
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
            binding.setupFinishButton.isEnabled = !it.isNullOrEmpty()
            (listAdapter as BankListAdapter).submitList(it.toList())
        }

        binding.setupFinishButton.setOnClickListener {
            viewModel.doAccountSetup()
        }

        viewModel.setupRes.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    // TODO
                }
                Status.LOADING -> {
                    showLoadingUi(isLoading = true)
                }
                Status.ERROR -> {
                    showLoadingUi(isLoading = false)
                    Snackbar.make(view, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }

    private fun showLoadingUi(isLoading: Boolean) {
        binding.accountSetupRecyclerView.isEnabled = !isLoading
        binding.setupFinishButton.isEnabled = !isLoading
        binding.setupAddAccountButton.isEnabled = !isLoading
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    private fun onBackPressedSetup() {
        requireActivity().onBackPressedDispatcher.addCallback(this, true) {
            requireActivity().finish()
        }
    }
}