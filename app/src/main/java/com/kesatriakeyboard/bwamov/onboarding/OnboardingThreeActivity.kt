package com.kesatriakeyboard.bwamov.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kesatriakeyboard.bwamov.R
import com.kesatriakeyboard.bwamov.sign.signin.SignInActivity
import kotlinx.android.synthetic.main.activity_onboarding_three.*

class OnboardingThreeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_three)

        button_next.setOnClickListener {
            finishAffinity()
            val intent = Intent(this@OnboardingThreeActivity, SignInActivity::class.java)
            startActivity(intent)
        }
    }
}