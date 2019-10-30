package com.example.firebaseauth.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.example.firebaseauth.R
import com.example.firebaseauth.utils.login
import com.example.firebaseauth.utils.toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()

        button_register.setOnClickListener {
            val email = edit_text_email.text.toString().trim()
            val password = edit_text_password.toString().trim()

            if (email.isEmpty()){
                edit_text_email.error = "Email Required"
                edit_text_email.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edit_text_email.error = "Valid Email Required"
                edit_text_email.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty() || password.length < 6){
                edit_text_email.error = "Six Char Password Required"
                edit_text_email.requestFocus()
                return@setOnClickListener
            }
            registerUser(email,password)

        }



        text_view_login.setOnClickListener {
            startActivity(Intent(this@RegisterActivity,LoginActivity::class.java))
        }
    }

    private fun registerUser(email: String, password: String) {
        progressbar.visibility  = View.VISIBLE
       mAuth.createUserWithEmailAndPassword(email,password)
           .addOnCompleteListener(this){task ->
               progressbar.visibility = View.GONE
               if (task.isSuccessful){
                   login()
               }else{
                   task.exception?.message?.let{
                    toast(it)
                   }
               }
           }
    }

   override fun onStart() {
        super.onStart()

        mAuth.currentUser?.let {
            login()
        }
    }
}
