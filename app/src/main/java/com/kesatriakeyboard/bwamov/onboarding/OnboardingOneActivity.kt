package com.kesatriakeyboard.bwamov.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kesatriakeyboard.bwamov.R
import com.kesatriakeyboard.bwamov.sign.signin.SignInActivity
import com.kesatriakeyboard.bwamov.utils.Preferences
import kotlinx.android.synthetic.main.activity_onboarding_one.*

class OnboardingOneActivity : AppCompatActivity() {

    lateinit var preference: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_one)

        preference = Preferences(this)
        if (preference.getValues("onboarding").equals("1")) {
            goToSignIn()
        }

        button_next.setOnClickListener {
            val intent = Intent(this@OnboardingOneActivity, OnboardingTwoActivity::class.java)
            startActivity(intent)
        }

        button_skip.setOnClickListener {
            preference.setValues("onboarding", "1")
            goToSignIn()
        }
    }

    private fun goToSignIn() {
        finishAffinity()
        val intent = Intent(this@OnboardingOneActivity, SignInActivity::class.java)
        startActivity(intent)
    }
}