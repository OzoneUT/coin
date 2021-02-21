package com.kafleyozone.coin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.kafleyozone.coin.R
import com.kafleyozone.coin.databinding.FragmentHomeContainerBinding
import com.kafleyozone.coin.viewmodels.HomeContainerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeContainerFragment : Fragment(R.layout.fragment_home_container) {
    companion object {
        const val TAG = "HomeContainerFragment"
        const val ID_ARG_KEY = "user_id"
    }

    private val containerViewModel: HomeContainerViewModel by viewModels()
    private var _binding: FragmentHomeContainerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeContainerBinding.inflate(inflater, container, false)
        val view = binding.root

        onBackPressedSetup()

        containerViewModel.userData.observe(viewLifecycleOwner) {
            if (!it.accountSetupComplete) {
                findNavController()
                    .navigate(
                        HomeContainerFragmentDirections
                            .actionHomeContainerFragmentToAccountSetupFragment(it.name)
                    )
                return@observe
            }
        }

        containerViewModel.authorized.observe(viewLifecycleOwner) { authorized ->
            if (!authorized) {
                requireActivity().finish()
            }
        }

        binding.toolbar.title = containerViewModel.getCurrentDate()
        containerViewModel.getUserFromDB(arguments?.getString(ID_ARG_KEY))

//        binding.logoutButton.setOnClickListener {
//            containerViewModel.logoutUser()
//        }

        return view
    }

    override fun onStart() {
        super.onStart()
        val navController =
            Navigation.findNavController(requireActivity(), R.id.main_nav_graph_view)
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    private fun onBackPressedSetup() {
        requireActivity().onBackPressedDispatcher.addCallback(this, true) {
            requireActivity().finish()
        }
    }
}