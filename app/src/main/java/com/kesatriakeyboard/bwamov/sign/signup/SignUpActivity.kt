package com.kesatriakeyboard.bwamov.sign.signup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.kesatriakeyboard.bwamov.R
import com.kesatriakeyboard.bwamov.sign.signin.User
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    lateinit var sUsername: String
    lateinit var sPassword: String
    lateinit var sNama: String
    lateinit var sEmail: String

    lateinit var mFirebaseInstance: FirebaseDatabase
    lateinit var mRootDatabase: DatabaseReference
    lateinit var mUserDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mRootDatabase = FirebaseDatabase.getInstance().getReference()
        mUserDatabase = mFirebaseInstance.getReference("User")


        button_sign_up.setOnClickListener {
            sUsername = edit_username.text.toString()
            sPassword = edit_password.text.toString()
            sNama = edit_nama.text.toString()
            sEmail = edit_email.text.toString()

            if (sUsername.isEmpty()) {
                edit_username.error = "Silahkan isi username anda"
                edit_username.requestFocus()
            } else if (sPassword.isEmpty()) {
                edit_password.error = "Silahkan isi password anda"
                edit_password.requestFocus()
            } else if (sNama.isEmpty()) {
                edit_nama.error = "Silahkan isi nama anda"
                edit_nama.requestFocus()
            } else if (sEmail.isEmpty()) {
                edit_email.error = "Silahkan isi email anda"
                edit_email.requestFocus()
            } else {
                saveUsername(sUsername, sPassword, sNama, sEmail)
            }
        }
    }

    private fun saveUsername(sUsername: String, sPassword: String, sNama: String, sEmail: String) {
        val user = User()
        user.username = sUsername
        user.password = sPassword
        user.email = sEmail
        user.nama = sNama

        checkingUsername(sUsername, user)
    }

    private fun checkingUsername(sUsername: String, data: User) {
        mUserDatabase.child(sUsername).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                toast(error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user == null) {
                    mUserDatabase.child(sUsername).setValue(data)
                    goToPhotoscreen(data.nama!!)
                } else {
                    toast("User sudah digunakan")
                }
            }
        })
    }

    private fun goToPhotoscreen(nama: String) {
        val intent = Intent(this@SignUpActivity, SignUpPhotoscreenActivity::class.java)
        intent.putExtra("nama", nama)
        startActivity(intent)
    }

    private fun toast(text: String) {
        Toast.makeText(
            this@SignUpActivity,
            text,
            Toast.LENGTH_LONG
        ).show()
    }
}