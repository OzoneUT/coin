package com.kafleyozone.coin.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.kafleyozone.coin.R
import com.kafleyozone.coin.data.network.models.RegistrationRequest
import com.kafleyozone.coin.databinding.FragmentRegistrationBinding
import com.kafleyozone.coin.utils.Status
import com.kafleyozone.coin.utils.setEnabledById
import com.kafleyozone.coin.viewmodels.LoginFragmentViewModel
import com.kafleyozone.coin.viewmodels.RegistrationFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment(
        private val pagerListener: OnboardingFlowFragment.PagerListenerInterface
) : Fragment() {

    companion object {
        private const val TAG = "RegistrationFragment"
    }

    private val viewModel by viewModels<RegistrationFragmentViewModel>()
    private val loginViewModel: LoginFragmentViewModel by activityViewModels()
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        val view = binding.root

        setupValidationListeners()

        binding.registerButton.setOnClickListener {
            binding.registrationScrollview.smoothScrollTo(0, 0)
            if (viewModel.validateRegistrationFields(view, binding)) {
                val model = RegistrationRequest(
                        binding.registerNameField.text.toString(),
                        binding.registerEmailField.text.toString(),
                        binding.registerPasswordField.text.toString()
                )
                viewModel.doRegistration(model)
            }
        }

        binding.loginOnRegisterButton.setOnClickListener {
            val action = OnboardingFlowFragmentDirections
                    .actionOnboardingFlowFragmentToLoginFragment()
            findNavController().navigate(action)
        }

        viewModel.stateEmailTaken.observe(viewLifecycleOwner) { taken ->
            if (taken) {
                binding.registerEmailFieldLayout.let {
                    it.error = "This email is taken."
                    it.isErrorEnabled = true
                }
            }
        }

        // auto-login
        loginViewModel.loginRes.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    try {
                        pagerListener.onRegisterComplete(it.data?.user?.name ?: "")
                    } catch (e: Exception) {
                        Log.e(TAG, "couldn't advance page; we may not be on onboarding flow")
                    }
                }
                Status.LOADING -> {
                    setLoadingUI(loading = true)
                }
                Status.ERROR -> {
                    try {
                        OnboardingFlowFragmentDirections.actionOnboardingFlowFragmentToLoginFragment(
                            viewModel.registrationRequestObject.email
                        )
                    } catch (e: Exception) {
                        Log.e(TAG, "there was an error; must not be on onboarding flow")
                    }
                }
            }
        }

        viewModel.registrationRes.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    // auto-login
                    loginViewModel.doLogin(viewModel.registrationRequestObject.email,
                            viewModel.registrationRequestObject.password)
                }
                Status.LOADING -> {
                    setLoadingUI(loading = true)
                }
                Status.ERROR -> {
                    setLoadingUI(loading = false)
                    Snackbar.make(view, resource.message.toString(), Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        return view
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

    // enable or disable screen elements and toggle the loading bar visibility accordingly
    private fun setLoadingUI(loading: Boolean) {
        viewModel.registrationInputValidations.keys.toList().plus(R.id.register_button)
                .setEnabledById(!loading, view)
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.INVISIBLE
    }
}