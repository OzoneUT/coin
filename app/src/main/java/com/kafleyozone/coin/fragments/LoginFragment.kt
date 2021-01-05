package com.kafleyozone.coin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kafleyozone.coin.viewmodels.LoginFragmentViewModel
import com.kafleyozone.coin.R
import com.kafleyozone.coin.databinding.FragmentLoginBinding
import com.kafleyozone.coin.setEnabledById

class LoginFragment : Fragment(R.layout.fragment_login) {

    companion object {
        const val TAG = "LoginFragment"
    }

    val viewModel: LoginFragmentViewModel by viewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.loginButton.setOnClickListener {
            onLoginTapHandler(view)
        }

        binding.registerOnLoginButton.setOnClickListener {
            findNavController().popBackStack()
            // TODO check if user has logged in from this device before and do not return to
            //  onboarding if they have
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
        if (viewModel.validateLoginFields(view, binding)) {
            // disable form interaction and enable progress bar
            viewModel.loginInputValidations.keys.toList()
                    .plus(R.id.login_button).plus(R.id.login_checkbox)
                    .setEnabledById(false, view)
            binding.progressBar.visibility = View.VISIBLE
            // call viewModel logic to handle registration with a listener
            viewModel.mockNetworkCallForLogin()
        }
    }
}