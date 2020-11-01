package com.kesatriakeyboard.bwamov.sign.signin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.kesatriakeyboard.bwamov.HomeActivity
import com.kesatriakeyboard.bwamov.R
import com.kesatriakeyboard.bwamov.sign.SignUpActivity
import com.kesatriakeyboard.bwamov.utils.Preferences
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity : AppCompatActivity() {

    lateinit var iUsername: String
    lateinit var iPassword: String

    lateinit var mDatabase: DatabaseReference
    lateinit var preference: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        mDatabase = FirebaseDatabase.getInstance().getReference("User")
        preference = Preferences(this)

        preference.setValues("onboarding", "1")
        if (preference.getValues("status").equals("1")) {
            goToHome()
        }

        button_sign_in.setOnClickListener {
            iUsername = edit_username.text.toString()
            iPassword = edit_password.text.toString()

            if (iUsername.isEmpty()) {
                edit_username.error = "Silahkan tulis username anda"
                edit_username.requestFocus()
            } else if (iPassword.isEmpty()) {
                edit_password.error = "Silahkan tulis password anda"
                edit_password.requestFocus()
            } else {
                pushLogin(iUsername, iPassword)
            }
        }

        button_sign_up.setOnClickListener {
            val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun pushLogin(iUsername: String, iPassword: String) {
        mDatabase.child(iUsername).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                toast(error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user == null) {
                    toast("User tidak ditemukan")
                } else {
                    if (user.password.equals(iPassword)) {
                        preference.setValues("nama", user.nama.toString())
                        preference.setValues("username", user.username.toString())
                        preference.setValues("url", user.url.toString())
                        preference.setValues("email", user.email.toString())
                        preference.setValues("saldo", user.saldo.toString())
                        preference.setValues("status", "1")

                        goToHome()
                    } else {
                        toast("Password anda salah")
                    }
                }
            }

        })
    }

    private fun goToHome() {
        finishAffinity()
        val intent = Intent(this@SignInActivity, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun toast(text: String) {
        Toast.makeText(
            this@SignInActivity,
            text,
            Toast.LENGTH_LONG
        ).show()
    }
}