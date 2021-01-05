package com.kafleyozone.coin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kafleyozone.coin.databinding.FragmentWelcomeBinding

class WelcomeFragment(private val pagerListener: OnboardingFlowFragment.PagerListenerInterface) : Fragment() {
    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.startButton.setOnClickListener {
            pagerListener.onFlowAdvance()
        }

        return view
    }
}