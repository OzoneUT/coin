package com.kafleyozone.coin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kafleyozone.coin.R
import com.kafleyozone.coin.databinding.FragmentHomeBinding
import com.kafleyozone.coin.viewmodels.HomeViewModel

class HomeFragment: Fragment(R.layout.fragment_home) {
    companion object {
        const val TAG = "HomeFragment"
    }

    // private val loginViewModel: LoginFragmentViewModel by activityViewModels()
    private val viewModel: HomeViewModel by viewModels()
    private val args: HomeFragmentArgs by navArgs()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel.userData.observe(viewLifecycleOwner) {
            binding.nameTextview.text = it.name
            binding.idTextview.text = it.id
        }

        viewModel.authorized.observe(viewLifecycleOwner) { authorized ->
            if (!authorized) {
                requireActivity().finish()
                Toast.makeText(requireContext(),
                    "Unauthorized: You must be logged in to view your dashboard.",
                    Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.setUserData(args.userData)

        return view
    }
}