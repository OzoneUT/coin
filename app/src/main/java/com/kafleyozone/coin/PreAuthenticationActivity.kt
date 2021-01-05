package com.kafleyozone.coin

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.kafleyozone.coin.fragments.ExitOnboardingDialogFragment

class PreAuthenticationActivity : AppCompatActivity(R.layout.activity_pre_authentication) {

    override fun onBackPressed() {
        when (findNavController(R.id.nav_host_fragment_container).currentDestination?.id) {
            R.id.onboardingFlowFragment -> {
                ExitOnboardingDialogFragment()
                        .show(supportFragmentManager, ExitOnboardingDialogFragment.TAG)
            } else -> super.onBackPressed()
        }

    }
}