package com.kafleyozone.coin.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        binding.categoriesRecylerview.adapter = CategoryListAdapter()

        binding.closeCategoryChooserButton.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.transactionType.observe(viewLifecycleOwner) {
            val list = when(it) {
                AddTransactionViewModel.TransactionType.EXPENSE -> {
                    viewModel.appCategories.expenseCategories
                }
                else -> viewModel.appCategories.incomeCategories
            }
            Log.i(javaClass.name, list.toString())
            (binding.categoriesRecylerview.adapter as CategoryListAdapter).submitList(list)
        }

        return binding.root
    }
}