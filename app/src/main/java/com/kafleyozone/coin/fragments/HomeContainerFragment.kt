package com.kafleyozone.coin.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.transition.Hold
import com.kafleyozone.coin.R
import com.kafleyozone.coin.databinding.FragmentHomeContainerBinding
import com.kafleyozone.coin.utils.fadeThroughTransition
import com.kafleyozone.coin.viewmodels.HomeContainerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeContainerFragment : Fragment(R.layout.fragment_home_container) {
    companion object {
        const val TAG = "HomeContainerFragment"
        const val ID_ARG_KEY = "user_id"
        const val ENABLE_SLIDE_TRANSITION = "enable_slide_transition"
    }

    private val containerViewModel: HomeContainerViewModel by activityViewModels()
    private var _binding: FragmentHomeContainerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeContainerBinding.inflate(inflater, container, false)
        val view = binding.root

        setupEnterTransition()
        onBackPressedSetup()

        binding.fab.setOnClickListener {
            exitTransition = Hold()
            val extras = FragmentNavigatorExtras(it to "fab_to_add_transaction_screen")
            findNavController().navigate(
                HomeContainerFragmentDirections
                    .actionHomeContainerFragmentToAddTransactionFragment(), extras
            )
        }

        containerViewModel.userData.observe(viewLifecycleOwner, {
            if (!it.accountSetupComplete) {
                findNavController()
                    .navigate(
                        HomeContainerFragmentDirections
                            .actionHomeContainerFragmentToAccountSetupFragment(it.name, false)
                    )
            }
        })

        containerViewModel.authorized.observe(viewLifecycleOwner, { authorized ->
            if (!authorized) {
                requireActivity().finish()
            }
        })

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)

        binding.toolbar.title = containerViewModel.getCurrentDate()
        containerViewModel.getUserFromDB(arguments?.getString(ID_ARG_KEY))

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) =
        inflater.inflate(R.menu.toolbar_menu, menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.profile_menu_item -> {
                exitTransition = fadeThroughTransition()
                reenterTransition = fadeThroughTransition()
                val action = HomeContainerFragmentDirections
                    .actionHomeContainerFragmentToProfileOverviewFragment()
                findNavController().navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        val navController =
            Navigation.findNavController(requireActivity(), R.id.main_nav_graph_view)
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    private fun setupEnterTransition() {
        if (arguments?.getBoolean(ENABLE_SLIDE_TRANSITION) == true)
            enterTransition = TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.slide_medium_rtl)
    }

    private fun onBackPressedSetup() {
        requireActivity().onBackPressedDispatcher.addCallback(this, true) {
            requireActivity().finish()
        }
    }
}