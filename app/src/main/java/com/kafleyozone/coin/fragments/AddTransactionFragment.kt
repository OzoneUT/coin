package com.kafleyozone.coin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialContainerTransform
import com.kafleyozone.coin.databinding.FragmentAddTransactionBinding

class AddTransactionFragment : Fragment() {
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
        return binding.root
    }
}