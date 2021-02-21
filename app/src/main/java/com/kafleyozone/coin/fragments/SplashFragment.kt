package com.kafleyozone.coin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kafleyozone.coin.R
import com.kafleyozone.coin.utils.Status
import com.kafleyozone.coin.viewmodels.SplashFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment: Fragment(R.layout.fragment_splash) {

    companion object {
        const val TAG = "SplashFragment"
    }

    private val viewModel: SplashFragmentViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewModel.cachedEmail.observe(viewLifecycleOwner) { email ->
            if (email.isNullOrEmpty() || email == "\"\"") {
                findNavController()
                    .navigate(SplashFragmentDirections.actionSplashFragmentToOnboardingFlowFragment())
            } else {
                findNavController()
                    .navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment(email))
            }
        }

        viewModel.userRes.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    // if we successfully got the user, the cached accessToken was valid. Move the
                    // user to their dashboard
                    Bundle().let { b ->
                        b.putString(HomeContainerFragment.ID_ARG_KEY, it.data?.id)
                        findNavController().navigate(R.id.action_global_homeContainerFragment, b)
                    }
                }
                Status.LOADING -> {
                    // do nothing TODO: after x sec have passed, show a loading spinner
                }
                Status.ERROR -> {
                    // if there's a previous email saved, go to login screen and forward that email
                    // else, Onboarding flow
                    viewModel.getUserEmail()
                }
            }
        }

        viewModel.initialize()
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}