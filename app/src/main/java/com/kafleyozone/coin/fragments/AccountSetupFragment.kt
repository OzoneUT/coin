package com.kafleyozone.coin.fragments

import android.graphics.Color
import android.os.Bundle
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.google.android.material.transition.SlideDistanceProvider
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.kafleyozone.coin.R
import com.kafleyozone.coin.data.domain.SetupAmountEntity
import com.kafleyozone.coin.databinding.FragmentAccountSetupBinding
import com.kafleyozone.coin.rvadapters.SetupHistoryAdapter
import com.kafleyozone.coin.utils.Status
import com.kafleyozone.coin.utils.setMargins
import com.kafleyozone.coin.viewmodels.AccountSetupFragmentViewModel
import com.robinhood.ticker.TickerUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountSetupFragment : Fragment() {

    private val args: AccountSetupFragmentArgs by navArgs()
    private val viewModel: AccountSetupFragmentViewModel by viewModels()
    private var _binding: FragmentAccountSetupBinding? = null
    private val binding get() = _binding!!
    private var mListAdapter: ListAdapter<SetupAmountEntity,
            SetupHistoryAdapter.HistoryItemViewHolder>? = null

    companion object {
        private const val TAG = "AccountSetupFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        if (args.inOnboardingFlow) {
            enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
            exitTransition = TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.fade)
        } else {
            // interstitial AccountSetupFragment slide up
            enterTransition = MaterialFadeThrough().apply {
                secondaryAnimatorProvider = SlideDistanceProvider(Gravity.BOTTOM)
            }
            exitTransition = MaterialFadeThrough().apply {
                secondaryAnimatorProvider = SlideDistanceProvider(Gravity.TOP)
            }
        }

        _binding = FragmentAccountSetupBinding.inflate(inflater, container, false)
        val view = binding.root
        mListAdapter = SetupHistoryAdapter {
            // triggered on delete of this setupAmountEntity
            viewModel.removeSetupAmountFromHistory(it)
        }
        binding.totalTicker.setCharacterLists(TickerUtils.provideNumberList())
        binding.accountSetupRecyclerView.adapter = mListAdapter
        binding.accountSetupRecyclerView.layoutManager = LinearLayoutManager(context)

        binding.addMoneyChip.setOnClickListener {
            toggleAddMoneyVisibilityWithFade(true)
            binding.addMoneyTextInputLayout.requestFocus()
        }

        binding.cancelAddButton.setOnClickListener {
            toggleAddMoneyVisibilityWithFade(false)
        }

        binding.confirmAddButton.setOnClickListener {
            viewModel.addSetupAmountToHistory(binding.addMoneyEditText.text.toString())
            binding.addMoneyEditText.setText("")
            toggleAddMoneyVisibilityWithFade(false)
        }

        viewModel.sumAmountFormatted.observe(viewLifecycleOwner) {
            binding.totalTicker.text = it
        }

        viewModel.setupHistoryList.observe(viewLifecycleOwner) {
            viewModel.calculateSetupHistorySum()
            binding.numItems.text =
                resources.getQuantityString(R.plurals.number_of_items_in_history, it.size, it.size)
            binding.setupFinishButton.isEnabled = !it.isNullOrEmpty()
            (mListAdapter as SetupHistoryAdapter).submitList(it.toList())
        }

        binding.setupFinishButton.setOnClickListener {
            viewModel.doAccountSetup()
        }

        viewModel.setupRes.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    Bundle().let { bundle ->
                        bundle.putString(HomeContainerFragment.ID_ARG_KEY, it.data?.id)
                        bundle.putBoolean(
                            HomeContainerFragment.ENABLE_SLIDE_TRANSITION,
                            args.inOnboardingFlow
                        )
                        findNavController().navigate(
                            R.id.action_global_homeContainerFragment, bundle
                        )
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
                if (it.isEmpty()) getString(R.string.welcome_to_coin_no_name)
                else getString(R.string.welcome_to_coin_with_name, it)
        }

        setupUI()

        return view
    }

    private fun setupUI() {
        args.name?.let {
            viewModel.setName(it)
            binding.setupFinishButton
                .setMargins(requireContext(), left = 100, right = 100, bottom = 32)
            onBackPressedSetup()
        }
    }

    private fun showLoadingUi(isLoading: Boolean) {
        binding.accountSetupRecyclerView.isEnabled = !isLoading
        binding.setupFinishButton.isEnabled = !isLoading
        binding.addMoneyChip.isEnabled = !isLoading
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    private fun toggleAddMoneyVisibilityWithFade(adding: Boolean) {
        val transform = MaterialContainerTransform().apply {
            // Manually tell the container transform which Views to transform between.
            startView = if (adding) binding.addMoneyChip else binding.addMoneyTextInputLayout
            endView = if (adding) binding.addMoneyTextInputLayout else binding.addMoneyChip

            // Ensure the container transform only runs on a single target
            addTarget(endView)

            // Since View to View transforms often are not transforming into full screens,
            // remove the transition's scrim.
            scrimColor = Color.TRANSPARENT
        }
        TransitionManager.beginDelayedTransition(binding.accountSetupContainer, transform)
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