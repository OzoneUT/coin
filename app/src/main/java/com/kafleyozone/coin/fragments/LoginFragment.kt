package com.kafleyozone.coin.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.kafleyozone.coin.viewmodels.LoginFragmentViewModel
import com.kafleyozone.coin.R
import com.kafleyozone.coin.databinding.FragmentLoginBinding
import com.kafleyozone.coin.utils.Status
import com.kafleyozone.coin.utils.setEnabledById
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    companion object {
        const val TAG = "LoginFragment"
    }

    private val args: LoginFragmentArgs by navArgs()
    private val viewModel: LoginFragmentViewModel by viewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        if (args.email != "\"\"") // TODO need a better fix to reinit email from dataStore correctly
            binding.loginEmailField.setText(args.email)

        binding.loginButton.setOnClickListener {
            if (viewModel.validateLoginFields(view, binding)) {
                viewModel.doLogin(binding.loginEmailField.text.toString(),
                    binding.loginPasswordField.text.toString())
            }
        }

        binding.registerOnLoginButton.setOnClickListener {
            // TODO check if user has logged in from this device before and do not return to
            //  onboarding if they have
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToOnboardingFlowFragment())
        }

        viewModel.loginRes.observe(viewLifecycleOwner) {
           when(it.status) {
               Status.SUCCESS -> {
                   setLoadingUI(false)
                   binding.loginPasswordField.setText("")
                   val action = LoginFragmentDirections
                       .actionLoginFragmentToHomeFragment(it.data?.user)
                   view.findNavController().navigate(action)
               }
               Status.LOADING -> {
                   setLoadingUI(true)
               }
               Status.ERROR -> {
                   setLoadingUI(false)
                   Snackbar.make(view,
                           it.message.toString(),
                           Snackbar.LENGTH_SHORT)
                       .show()
               }
           }
        }

        return view
    }

    private fun setLoadingUI(loading: Boolean) {
        viewModel.loginInputValidations.keys.toList()
            .plus(R.id.login_button).plus(R.id.login_checkbox)
            .setEnabledById(!loading, view)
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }
}