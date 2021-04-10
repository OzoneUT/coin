package com.kafleyozone.coin.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.SlideDistanceProvider
import com.kafleyozone.coin.R
import com.kafleyozone.coin.databinding.FragmentLoginBinding
import com.kafleyozone.coin.utils.Status
import com.kafleyozone.coin.utils.setEnabledById
import com.kafleyozone.coin.viewmodels.LoginFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    companion object {
        const val TAG = "LoginFragment"
    }

    private val args: LoginFragmentArgs by navArgs()
    private val viewModel: LoginFragmentViewModel by activityViewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        enterTransition = MaterialFadeThrough().apply {
            secondaryAnimatorProvider = SlideDistanceProvider(Gravity.TOP)
        }
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        // The args may contain an email from a previous logged in user. Pre-fill this in the
        // correct field if it's not empty and doesn't not contain empty quotes (dataStore quirk)
        if (args.email != "\"\"") // TODO need a better fix to reinit email from dataStore correctly
            binding.loginEmailField.setText(args.email)

        // Setup the login button to do a final validation on the username/password field values
        // and then call the viewModel's doLogin function
        binding.loginButton.setOnClickListener {
            if (viewModel.validateLoginFields(view, binding)) {
                viewModel.doLogin(binding.loginEmailField.text.toString(),
                    binding.loginPasswordField.text.toString())
            }
        }

        // There is an action set up from the Login screen to the beginning of the onboarding
        // flow.
        binding.registerOnLoginButton.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections
                    .actionLoginFragmentToOnboardingFlowFragment()
            )
        }

        // _loginRes in viewModel will update based on the response from the server. If there is a
        // success, erase the password field, re-enable all UI elements, and pass in the User object
        // from the response to the HomeContainerFragment.
        viewModel.loginRes.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    setLoadingUI(false)
                    binding.loginPasswordField.setText("")
                    exitTransition = TransitionInflater.from(requireContext())
                        .inflateTransition(R.transition.fade)
                    Bundle().let { b ->
                        b.putString(HomeContainerFragment.ID_ARG_KEY, it.data?.user?.id)
                        b.putInt(HomeContainerFragment.NAVIGATED_FROM_KEY, R.layout.fragment_login)
                        findNavController().navigate(R.id.action_global_homeContainerFragment, b)
                    }
                }
                Status.LOADING -> {
                    setLoadingUI(true)
                }
                Status.ERROR -> {
                    setLoadingUI(false)
                    Snackbar.make(
                        view,
                        it.message.toString(),
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
            }
        })
        return view
    }

    // enable or disable screen elements and toggle the loading bar visibility accordingly
    private fun setLoadingUI(loading: Boolean) {
        viewModel.loginInputValidations.keys.toList().plus(R.id.setup_finish_button)
            .setEnabledById(!loading, view)
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.INVISIBLE
    }
}