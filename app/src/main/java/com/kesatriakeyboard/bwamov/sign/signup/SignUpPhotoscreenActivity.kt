package com.kesatriakeyboard.bwamov.sign.signup

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.kesatriakeyboard.bwamov.HomeActivity
import com.kesatriakeyboard.bwamov.R
import com.kesatriakeyboard.bwamov.utils.Preferences
import kotlinx.android.synthetic.main.activity_sign_up_photoscreen.*
import java.util.*

class SignUpPhotoscreenActivity : AppCompatActivity(), PermissionListener {

    val REQUEST_IMAGE_CAPTURE = 1
    var statusAdd: Boolean = false
    lateinit var filePath: Uri

    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_photoscreen)

        preferences = Preferences(this)
        storage = FirebaseStorage.getInstance()
        storageReference = storage.getReference()

        tv_hello.text = "Selamat Datang\n" + intent.getStringExtra("nama")

        iv_add.setOnClickListener {
            if (statusAdd) {
                statusAdd = false
                btn_save.visibility = View.VISIBLE
                iv_add.setImageResource(R.drawable.ic_btn_add)
                iv_profile.setImageResource(R.drawable.user_pic)
            } else {
                Dexter
                    .withActivity(this)
                    .withPermission(Manifest.permission.CAMERA)
                    .withListener(this)
                    .check()
            }
        }

        btn_skip.setOnClickListener {
            goToHome()
        }

        btn_save.setOnClickListener {
            if (filePath != null) {
                var progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Uploading...")
                progressDialog.show()

                val ref = storageReference.child("images/" + UUID.randomUUID().toString())
                ref.putFile(filePath)
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        toast("Upload berhasil")

                        ref.downloadUrl.addOnSuccessListener {
                            preferences.setValues("url", it.toString())
                        }

                        goToHome()
                    }
                    .addOnFailureListener {
                        progressDialog.dismiss()
                        toast("Upload gagal")
                    }
                    .addOnProgressListener { taskSnapshot ->
                        val progress =
                            100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                        progressDialog.setMessage("Upload " + progress.toInt())
                    }
            } else {
                toast("Anda tidak memilih foto")
            }
        }
    }

    private fun goToHome() {
        finishAffinity()
        val intent = Intent(this@SignUpPhotoscreenActivity, HomeActivity::class.java)
        startActivity(intent)
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        toast("Anda tidak bisa menambahkan photo profile")
    }

    override fun onPermissionRationaleShouldBeShown(
        permission: PermissionRequest?,
        token: PermissionToken?
    ) {

    }

    private fun toast(text: String) {
        Toast.makeText(
            this@SignUpPhotoscreenActivity,
            text,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onBackPressed() {
        toast("Tergesah? klik tombol upload nanti aja")
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val bitmap = data?.extras?.get("data") as Bitmap
            statusAdd = true

            filePath = data.getData()!!
            Glide.with(this)
                .load(bitmap)
                .apply(RequestOptions.circleCropTransform())
                .into(iv_profile)

            btn_save.visibility = View.VISIBLE
            iv_add.setImageResource(R.drawable.ic_btn_delete)
        }
    }
}