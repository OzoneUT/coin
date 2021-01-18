package com.kafleyozone.coin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.kafleyozone.coin.R
import com.kafleyozone.coin.viewmodels.RegistrationFragmentViewModel
import com.kafleyozone.coin.databinding.FragmentRegistrationBinding
import com.kafleyozone.coin.utils.setEnabledById

class RegistrationFragment(private val pagerListener: OnboardingFlowFragment.PagerListenerInterface)
    : Fragment() {

    private val viewModel by viewModels<RegistrationFragmentViewModel>()
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        val view = binding.root

        setupValidationListeners()

        binding.registerButton.setOnClickListener {
            binding.registrationScrollview.smoothScrollTo(0, 0)
            onRegisterTapHandler(view)
        }

        binding.loginOnRegisterButton.setOnClickListener {
            val action = OnboardingFlowFragmentDirections
                    .actionOnboardingFlowFragmentToLoginFragment()
            findNavController().navigate(action)
        }

        viewModel.registrationSuccessState.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.INVISIBLE
            pagerListener.onFlowAdvance()
        }

        return view
    }

    /*
    * Helper function to handle logic when the user taps the 'Register' button.
    * */
    private fun onRegisterTapHandler(view: View) {
        if (viewModel.validateRegistrationFields(view, binding)) {
            // disable form interaction and enable progress bar
            viewModel.registrationInputValidations.keys.toList().plus(R.id.register_button)
                    .setEnabledById(false, view)
            binding.progressBar.visibility = View.VISIBLE
            // call viewModel logic to handle registration with a listener
            viewModel.mockNetworkCallForRegistration()
        }
    }

    /*
    * Helper function to setup listeners on registration input fields to fire when focus is lost
    * to validate the registration form on the fly
    * */
    private fun setupValidationListeners() {
        for (viewId in viewModel.registrationInputValidations.keys) {
            val input = binding.root.findViewById<TextInputEditText>(viewId)
            input.setOnFocusChangeListener { _: View, hasFocus: Boolean ->
                viewModel.validateRegistrationFieldByInput(input, hasFocus, binding)
            }
        }
    }
}