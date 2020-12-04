package com.kafleyozone.coin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.kafleyozone.coin.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login) {

    companion object {
        const val TAG = "LoginFragment"
    }

    val viewModel: LoginFragmentViewModel by viewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        setupValidationListeners()

        binding.loginButton.setOnClickListener {
            onLoginTapHandler(view)
        }

        binding.registerOnLoginButton.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragment_container_view, OnboardingFlowFragment(), OnboardingFlowFragment.TAG)
            }
        }

        viewModel.loginSuccessState.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.INVISIBLE
            Snackbar.make(view, "Login not implemented!", Snackbar.LENGTH_SHORT).show()
        }

        return view
    }

    /*
    * Helper function to handle logic when the user taps the 'Login' button.
    * */
    private fun onLoginTapHandler(view: View) {
        if (viewModel.validateLoginFields(view)) {
            // disable form interaction and enable progress bar
            viewModel.loginInputValidations.keys.toList()
                    .plus(R.id.login_button).plus(R.id.login_checkbox)
                    .setEnabledById(false, view)
            binding.progressBar.visibility = View.VISIBLE
            // call viewModel logic to handle registration with a listener
            viewModel.mockNetworkCallForLogin()
        }
    }

    /*
    * Helper function to setup listeners on registration input fields to fire when focus is lost
    * to validate the registration form on the fly
    * */
    private fun setupValidationListeners() {
        for (viewId in viewModel.loginInputValidations.keys) {
            val input = binding.root.findViewById<TextInputEditText>(viewId)
            input.setOnFocusChangeListener { _: View, hasFocus: Boolean ->
                viewModel.validateLoginFieldByInput(input, hasFocus)
            }
        }
    }
}