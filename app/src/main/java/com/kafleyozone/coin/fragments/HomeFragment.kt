package com.kafleyozone.coin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kafleyozone.coin.MainActivity
import com.kafleyozone.coin.R
import com.kafleyozone.coin.databinding.FragmentHomeBinding
import com.kafleyozone.coin.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment: Fragment(R.layout.fragment_home) {
    companion object {
        const val TAG = "HomeFragment"
    }

    private val viewModel: HomeViewModel by viewModels()
    private val args: HomeFragmentArgs by navArgs()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        onBackPressedSetup()

        viewModel.userData.observe(viewLifecycleOwner) {
            if (!it.accountSetupComplete) {
                findNavController()
                    .navigate(
                        HomeFragmentDirections
                            .actionHomeFragmentToAccountSetupFragment(it.name)
                    )
                return@observe
            }
            binding.idTextview.text = it.toString()
        }

        viewModel.authorized.observe(viewLifecycleOwner) { authorized ->
            if (!authorized) {
                MainActivity.triggerRebirth(requireContext())
            }
        }

        viewModel.getUserFromDB(args.userId)

        binding.logoutButton.setOnClickListener {
            viewModel.logoutUser()
        }

        return view
    }

    private fun onBackPressedSetup() {
        requireActivity().onBackPressedDispatcher.addCallback(this, true) {
            requireActivity().finish()
        }
    }
}