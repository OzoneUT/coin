package com.kafleyozone.coin

import android.os.Bundle
import android.os.UserManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit

class PreAuthenticationActivity : AppCompatActivity(R.layout.activity_pre_authentication) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<OnboardingFlowFragment>(R.id.fragment_container_view, OnboardingFlowFragment.TAG)
            }
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentByTag(OnboardingFlowFragment.TAG)?.isVisible == true) {
            ExitOnboardingDialogFragment().show(supportFragmentManager, ExitOnboardingDialogFragment.TAG)
        } else {
            super.onBackPressed()
        }
    }
}