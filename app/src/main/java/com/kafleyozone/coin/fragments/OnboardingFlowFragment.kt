package com.kafleyozone.coin.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.kafleyozone.coin.R
import com.kafleyozone.coin.databinding.FragmentOnboardingFlowBinding

class OnboardingFlowFragment : Fragment() {

    interface PagerListenerInterface {
        fun onOnboardingStart()
        fun onRegisterComplete(name: String)
        fun onAccountSetupComplete(id: String)
    }

    companion object {
        const val TAG = "OnboardingFlowFragment"
        const val INDEX_REGISTRATION_PAGE = 1
        const val INDEX_SETUP_PAGE = 2
    }

    private var _binding: FragmentOnboardingFlowBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentOnboardingFlowBinding.inflate(inflater, container, false)
        val view = binding.root

        val pagerListener: PagerListenerInterface = object : PagerListenerInterface {
            override fun onOnboardingStart() {
                binding.onboardingViewpager.currentItem = INDEX_REGISTRATION_PAGE
            }

            override fun onRegisterComplete(name: String) {
                Log.i(TAG, "onRegisterComplete")
                ((binding.onboardingViewpager.adapter as OnboardingPagerAdapter)
                    .fragmentList[INDEX_SETUP_PAGE] as AccountSetupFragment)
                    .setNameArgument(name)
                binding.onboardingViewpager.currentItem++
            }

            override fun onAccountSetupComplete(id: String) {
                Bundle().let { b ->
                    b.putString(HomeContainerFragment.ID_ARG_KEY, id)
                    findNavController().navigate(R.id.action_global_homeContainerFragment, b)
                }
            }

        }

        onBackPressedSetup()

        binding.onboardingViewpager.adapter = OnboardingPagerAdapter(requireActivity(), pagerListener)
//        binding.onboardingViewpager.isUserInputEnabled = false
        TabLayoutMediator(binding.pageIndicator, binding.onboardingViewpager) { _, _->
            // Empty implementation
        }.attach()

        return view
    }

    private fun onBackPressedSetup() {
        val navController = findNavController()
        requireActivity().onBackPressedDispatcher.addCallback(this, true) {
            navController.navigate(OnboardingFlowFragmentDirections
                .actionOnboardingFlowFragmentToExitOnboardingDialogFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class OnboardingPagerAdapter(
            fa: FragmentActivity,
            pagerListener: PagerListenerInterface,
    ) : FragmentStateAdapter(fa) {

        val fragmentList = mutableListOf(
            WelcomeFragment(pagerListener),
            RegistrationFragment(pagerListener),
            AccountSetupFragment().setPagerListener(pagerListener)
        )

        override fun getItemCount(): Int = fragmentList.size
        override fun createFragment(position: Int): Fragment = fragmentList[position]
    }
}