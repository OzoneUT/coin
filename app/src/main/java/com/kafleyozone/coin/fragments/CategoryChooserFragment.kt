package com.kafleyozone.coin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import com.kafleyozone.coin.databinding.FragmentCategoryChooserBinding
import com.kafleyozone.coin.rvadapters.CategoryListAdapter
import com.kafleyozone.coin.viewmodels.AddTransactionViewModel

class CategoryChooserFragment: Fragment() {

    private val viewModel: AddTransactionViewModel by activityViewModels()
    private var _binding: FragmentCategoryChooserBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        _binding = FragmentCategoryChooserBinding.inflate(inflater, container, false)
        val categoryAdapter = CategoryListAdapter()
        binding.categoriesRecylerview.adapter = categoryAdapter

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
}