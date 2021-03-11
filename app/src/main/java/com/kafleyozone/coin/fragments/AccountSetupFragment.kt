package com.kafleyozone.coin.fragments

import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.platform.MaterialFade
import com.kafleyozone.coin.R
import com.kafleyozone.coin.data.domain.BankInstitutionEntity
import com.kafleyozone.coin.databinding.FragmentAccountSetupBinding
import com.kafleyozone.coin.rvadapters.BankListAdapter
import com.kafleyozone.coin.utils.Status
import com.kafleyozone.coin.utils.setMargins
import com.kafleyozone.coin.viewmodels.AccountSetupFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountSetupFragment : Fragment() {

    private lateinit var mPagerListener: OnboardingFlowFragment.PagerListenerInterface
    private val viewModel: AccountSetupFragmentViewModel by viewModels()
    private var _binding: FragmentAccountSetupBinding? = null
    private val binding get() = _binding!!
    private var listAdapter: ListAdapter<BankInstitutionEntity,
            BankListAdapter.BankListItemViewHolder>? = null

    companion object {
        private const val TAG = "AccountSetupFragment"
    }

    fun setPagerListener(pagerListener: OnboardingFlowFragment.PagerListenerInterface): AccountSetupFragment {
        mPagerListener = pagerListener
        return this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountSetupBinding.inflate(inflater, container, false)
        val view = binding.root
        listAdapter = BankListAdapter()

        // Simple Swipe to Delete implementation
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT + ItemTouchHelper.LEFT) {
            // to add an icon/background to the swipe, override onChildDraw
            // https://medium.com/@kitek/recyclerview-swipe-to-delete-easier-than-you-thought-cff67ff5e5f6
            override fun onMove(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
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

        binding.addMoneyChip.setOnClickListener {
            toggleAddMoneyVisibilityWithFade(true)
            binding.addMoneyTextInputLayout.requestFocus()
        }

        binding.cancelAddButton.setOnClickListener {
            toggleAddMoneyVisibilityWithFade(false)
        }

        viewModel.setupBankList.observe(viewLifecycleOwner) {
            binding.setupFinishButton.isEnabled = !it.isNullOrEmpty()
            (listAdapter as BankListAdapter).submitList(it.toList())
        }

        binding.setupFinishButton.setOnClickListener {
            viewModel.doAccountSetup()
        }

        viewModel.setupRes.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    try {
                        Bundle().let { b ->
                            b.putString(HomeContainerFragment.ID_ARG_KEY, it.data?.id)
                            findNavController().navigate(
                                R.id.action_global_homeContainerFragment,
                                b
                            )
                        }
                    } catch (e: Exception) {
                        mPagerListener.onAccountSetupComplete(it.data?.id ?: "")
                    }
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

        viewModel.name.observe(viewLifecycleOwner) {
            binding.accountSetupWelcome.text =
                    if (it.isNullOrEmpty()) getString(R.string.welcome_to_coin_no_name)
                    else getString(R.string.welcome_to_coin_with_name, it)
        }

        setupUI()

        return view
    }

    private fun setupUI() {
        try {
            val args: AccountSetupFragmentArgs by navArgs()
            args.name?.let {
                viewModel.setName(it)
                binding.setupFinishButton
                        .setMargins(requireContext(), left = 100, right = 100, bottom = 32)
                onBackPressedSetup()
            }
        } catch (e: Exception) {
            Log.i(TAG, "no navArgs found for AccountSetupFragment, must be on onboarding flow")
        }
    }

    fun setNameArgument(name: String) {
        viewModel.setName(name)
    }

    private fun showLoadingUi(isLoading: Boolean) {
        binding.accountSetupRecyclerView.isEnabled = !isLoading
        binding.setupFinishButton.isEnabled = !isLoading
        binding.addMoneyChip.isEnabled = !isLoading
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    private fun toggleAddMoneyVisibilityWithFade(adding: Boolean) {
        TransitionManager.beginDelayedTransition(binding.accountSetupContainer, MaterialFade())
        binding.addMoneyChip.visibility = if (adding) View.GONE else View.VISIBLE
        binding.addMoneyTextInputLayout.visibility = if (adding) View.VISIBLE else View.GONE
        binding.confirmAddButton.visibility = if (adding) View.VISIBLE else View.GONE
        binding.cancelAddButton.visibility = if (adding) View.VISIBLE else View.GONE
    }

    private fun onBackPressedSetup() {
        requireActivity().onBackPressedDispatcher.addCallback(this, true) {
            requireActivity().finish()
        }
    }
}