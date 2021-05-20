package com.kafleyozone.coin.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.Slide
import com.google.android.material.chip.ChipGroup
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialSharedAxis
import com.kafleyozone.coin.databinding.FragmentAddTransactionBinding
import com.kafleyozone.coin.utils.convertDoubleToFormattedCurrency
import com.kafleyozone.coin.viewmodels.AddTransactionViewModel


class AddTransactionFragment : Fragment() {

    private val viewModel: AddTransactionViewModel by activityViewModels()
    private var _binding: FragmentAddTransactionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedElementEnterTransition = MaterialContainerTransform()
        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)

        binding.closeAddTransactionButton.setOnClickListener {
            viewModel.clearCategorySelection()
            findNavController().popBackStack()
        }

        binding.confirmAddTransactionButton.setOnClickListener {
            if (isFormValidated()) {
                sharedElementReturnTransition = null
                returnTransition = Slide(Gravity.TOP)
                findNavController().popBackStack()
            }
        }

        binding.chooseDateButton.setOnClickListener {
            val dateValidator =
                CalendarConstraints
                    .Builder()
                    .setValidator(DateValidatorPointBackward.now())
                    .build()
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select transaction date")
                    .setSelection(viewModel.getDateMillis())
                    .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                    .setCalendarConstraints(dateValidator)
                    .build()
            datePicker.addOnPositiveButtonClickListener {
                viewModel.updateDate(it)
            }
            datePicker.show(parentFragmentManager, "transaction_date_picker")
        }

        binding.chooseCategoryButton.setOnClickListener {
            reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
            exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
            findNavController().navigate(
                AddTransactionFragmentDirections
                    .actionAddTransactionFragmentToCategoryChooserFragment()
            )
        }

        binding.chipGroup.setOnCheckedChangeListener { chipGroup: ChipGroup, _: Int ->
            viewModel.onUpdateTransactionType(chipGroup.checkedChipId)
        }

        binding.expenseChip.setOnClickListener {
            viewModel.clearCategorySelection()
        }

        binding.incomeChip.setOnClickListener {
            viewModel.clearCategorySelection()
        }

        binding.categoryField.doOnTextChanged() { text, _, _, _ ->
            if (!text.isNullOrBlank()) {
                binding.categoryFieldLayout.error = ""
                binding.categoryFieldLayout.isErrorEnabled = false
            }
        }

        binding.amountField.addTextChangedListener( object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.amountErrorTextview.visibility = View.GONE
            }

            override fun afterTextChanged(e: Editable?) {
                e.toString().let {
                    val cleanString: String = it.replace("[$,.]", "")
                    val parsed = convertDoubleToFormattedCurrency(cleanString.toDouble())
                    viewModel.parseAndUpdateAmount(parsed.toDouble())
                }
            }
        })

        viewModel.selectedCategory.observe(viewLifecycleOwner) {
            binding.categoryField.setText(it?.categoryName ?: "")
        }

        viewModel.dateFormatted.observe(viewLifecycleOwner) {
            binding.dateField.setText(it)
        }

        viewModel.initialize(checkedChipId = binding.chipGroup.checkedChipId)
        return binding.root
    }

    private fun isFormValidated() : Boolean {
        var validated = true
        if (viewModel.selectedCategory.value == null) {
            binding.categoryFieldLayout.let {
                it.isErrorEnabled = true
                it.error = "Please select a transaction category."
            }
            validated = false
        }
        if (viewModel.transactionAmount.value == null || viewModel.transactionAmount.value == 0.0) {
            binding.amountErrorTextview.visibility = View.VISIBLE
            validated = false
        }
        return validated
    }
}