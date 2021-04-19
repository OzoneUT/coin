package com.kafleyozone.coin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.kafleyozone.coin.R
import com.kafleyozone.coin.databinding.FragmentProfileOverviewBinding
import com.kafleyozone.coin.utils.fadeThroughTransition
import com.kafleyozone.coin.viewmodels.HomeContainerViewModel

class ProfileOverviewFragment : Fragment(R.layout.fragment_profile_overview) {

    private val viewModel: HomeContainerViewModel by activityViewModels()
    private var _binding: FragmentProfileOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        enterTransition = fadeThroughTransition()
        returnTransition = fadeThroughTransition()
        exitTransition = fadeThroughTransition()

        _binding = FragmentProfileOverviewBinding.inflate(inflater, container, false)

        binding.closeProfileOverviewButton.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.userData.observe(viewLifecycleOwner) {
            binding.profileNameTextview.text = it.name
            binding.profileEmailTextview.text = it.email
        }

        return binding.root
    }
}