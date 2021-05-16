package com.kafleyozone.coin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import com.google.android.material.transition.MaterialSharedAxis
import com.kafleyozone.coin.databinding.FragmentCategoryChooserBinding
import com.kafleyozone.coin.rvadapters.CategoryListAdapter
import com.kafleyozone.coin.utils.CategoryDetailsLookup
import com.kafleyozone.coin.utils.CategoryKeyProvider
import com.kafleyozone.coin.viewmodels.AddTransactionViewModel

class CategoryChooserFragment: Fragment() {

    private val viewModel: AddTransactionViewModel by activityViewModels()
    private var _binding: FragmentCategoryChooserBinding? = null
    val binding get() = _binding!!

    companion object {
        private const val TAG = "CategoryChooserFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        _binding = FragmentCategoryChooserBinding.inflate(inflater, container, false)
        setupSingleSelectRecyclerView()

        binding.closeCategoryChooserButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.searchField.doOnTextChanged { text, _, _, _ ->
            text?.let {
                if (it.isEmpty()) {
                    binding.searchField.hint = "Search all categories"
                }
                viewModel.doSearch(text)
            }
        }

        viewModel.transactionType.observe(viewLifecycleOwner) {
            viewModel.initializeLists(it)
        }

        viewModel.filteredCategories.observe(viewLifecycleOwner) {
            (binding.categoriesRecylerview.adapter as CategoryListAdapter).submitList(it)
        }

        return binding.root
    }

    private fun setupSingleSelectRecyclerView() {
        val categoryAdapter = CategoryListAdapter(requireContext())
        binding.categoriesRecylerview.adapter = categoryAdapter

        val tracker = SelectionTracker.Builder(
            "category-selection",
            binding.categoriesRecylerview,
            CategoryKeyProvider(categoryAdapter),
            CategoryDetailsLookup(binding.categoriesRecylerview),
            StorageStrategy.createStringStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectSingleAnything()
        ).build()

        tracker.addObserver(
            object : SelectionTracker.SelectionObserver<String>() {
            }
        )

        categoryAdapter.selectionTracker = tracker
    }
}