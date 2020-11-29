package com.kafleyozone.coin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputEditText
import com.kafleyozone.coin.databinding.FragmentRegistrationBinding

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
            parentFragmentManager.commit {
                replace(R.id.fragment_container_view, LoginFragment(), LoginFragment.TAG)
            }
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
            setEnabledById(isEnabled = false, *viewModel.registrationInputValidations.keys.toIntArray(),
                    R.id.register_button)
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
            val input = view?.findViewById<TextInputEditText>(viewId)
            input?.setOnFocusChangeListener() { _: View, hasFocus: Boolean ->
                viewModel.validateRegistrationFieldByInput(input, hasFocus, binding)
            }
        }
    }

    /*
    * Utility function to set isEnabled to multiple views at once
    * */
    private fun setEnabledById(isEnabled: Boolean, vararg viewIds: Int) {
        for (id in viewIds) view?.findViewById<View>(id)?.isEnabled = isEnabled
    }
}