package com.kafleyozone.coin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.ChipGroup
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialSharedAxis
import com.kafleyozone.coin.databinding.FragmentAddTransactionBinding
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
            findNavController().popBackStack()
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

        viewModel.dateFormatted.observe(viewLifecycleOwner) {
            binding.dateField.setText(it)
        }

        viewModel.initialize(checkedChipId = binding.chipGroup.checkedChipId)
        return binding.root
    }
}