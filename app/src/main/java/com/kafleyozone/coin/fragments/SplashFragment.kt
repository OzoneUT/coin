package com.kafleyozone.coin.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kafleyozone.coin.R
import com.kafleyozone.coin.viewmodels.SplashFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment: Fragment(R.layout.fragment_splash) {

    companion object {
        const val TAG = "SplashFragment"
    }

    private val viewModel: SplashFragmentViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewModel.email.observe(viewLifecycleOwner) { email ->
            Log.i(TAG, "saved email was: $email")
            if (email.isNullOrEmpty()) {
                findNavController()
                        .navigate(SplashFragmentDirections.actionSplashFragmentToOnboardingFlowFragment())
            } else {
                findNavController()
                        .navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment(email))
            }
        }

        viewModel.getUser()

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}