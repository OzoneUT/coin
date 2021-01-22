package com.kafleyozone.coin.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
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

    private val viewModel: LoginFragmentViewModel by activityViewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.loginButton.setOnClickListener {
            if (viewModel.validateLoginFields(view, binding)) {
                viewModel.doLogin(binding.loginEmailField.text.toString(),
                    binding.loginPasswordField.text.toString())
            }
        }

        binding.registerOnLoginButton.setOnClickListener {
            findNavController().popBackStack()
            // TODO check if user has logged in from this device before and do not return to
            //  onboarding if they have
        }

        viewModel.loginRes.observe(viewLifecycleOwner) {
           when(it.status) {
               Status.SUCCESS -> {
                   viewModel.loginInputValidations.keys.toList()
                           .plus(R.id.login_button).plus(R.id.login_checkbox)
                           .setEnabledById(true, view)
                   binding.progressBar.visibility = View.GONE
                   val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                       .setUserData(it.data?.user)
                   view.findNavController().navigate(action)
               }
               Status.LOADING -> {
                   viewModel.loginInputValidations.keys.toList()
                       .plus(R.id.login_button).plus(R.id.login_checkbox)
                       .setEnabledById(false, view)
                   binding.progressBar.visibility = View.VISIBLE
               }
               Status.ERROR -> {
                   viewModel.loginInputValidations.keys.toList()
                       .plus(R.id.login_button).plus(R.id.login_checkbox)
                       .setEnabledById(true, view)
                   binding.progressBar.visibility = View.GONE
                   Snackbar.make(view,
                           "Internal error. Please try again later.",
                           Snackbar.LENGTH_SHORT)
                       .show()
               }
           }
        }

        return view
    }
}