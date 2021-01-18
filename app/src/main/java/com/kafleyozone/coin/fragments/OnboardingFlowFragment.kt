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
import com.kafleyozone.coin.databinding.FragmentOnboardingFlowBinding
import dagger.hilt.android.AndroidEntryPoint

class OnboardingFlowFragment : Fragment(){

    interface PagerListenerInterface {
        fun onFlowAdvance()
        fun registrationHandler()
    }

    companion object {
        const val TAG = "OnboardingFlowFragment"
    }

    private var _binding: FragmentOnboardingFlowBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentOnboardingFlowBinding.inflate(inflater, container, false)
        val view = binding.root

        val pagerListener: PagerListenerInterface = object : PagerListenerInterface {
            override fun onFlowAdvance() {
                binding.onboardingViewpager.currentItem++
            }
            override fun registrationHandler() {
                Log.i(TAG, "beginning registration process")
            }
        }

        onBackPressedSetup()

        binding.onboardingViewpager.adapter = OnboardingPagerAdapter(requireActivity(), pagerListener)
        //binding.onboardingViewpager.isUserInputEnabled = false TODO(enable eventually)
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
        pagerListener: PagerListenerInterface
    ) : FragmentStateAdapter(fa){

        val fragmentList = listOf(
            WelcomeFragment(pagerListener),
            RegistrationFragment(pagerListener),
            AccountSetupFragment(pagerListener))

        override fun getItemCount(): Int = fragmentList.size
        override fun createFragment(position: Int): Fragment = fragmentList[position]
    }
}