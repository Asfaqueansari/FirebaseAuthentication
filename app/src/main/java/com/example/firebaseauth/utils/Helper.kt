package com.example.firebaseauth.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.firebaseauth.ui.HomeActivity

fun Context.toast(message:String) =
    Toast.makeText(this,"Something went Wrong",Toast.LENGTH_SHORT).show()

fun Context.login(){
    val intent = Intent(this, HomeActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    startActivity(intent)
}