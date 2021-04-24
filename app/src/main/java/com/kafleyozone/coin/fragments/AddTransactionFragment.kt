package com.kafleyozone.coin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.transition.MaterialContainerTransform
import com.kafleyozone.coin.databinding.FragmentAddTransactionBinding
import com.kafleyozone.coin.viewmodels.AddTransactionViewModel

class AddTransactionFragment : Fragment() {

    private val viewModel: AddTransactionViewModel by viewModels()
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

        viewModel.dateFormatted.observe(viewLifecycleOwner) {
            binding.dateField.setText(it)
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

        viewModel.initialize()
        return binding.root
    }
}