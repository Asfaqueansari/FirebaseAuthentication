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
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        button_sign_in.setOnClickListener {
            val email = text_email.text.toString().trim()
            val password = text_password.text.toString().trim()

            if(email.isEmpty()){
                text_email.error = " Email Required"
                text_email.requestFocus()
                return@setOnClickListener
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                text_email.error = "Valid Email Required"
                text_email.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty() || password.length < 6){
                text_email.error = "Six Char Password Required"
                text_email.requestFocus()
                return@setOnClickListener
            }
            loginUser(email,password)
        }

        text_view_register.setOnClickListener {
            startActivity(Intent(this@LoginActivity,RegisterActivity::class.java))
        }
    }

    private fun loginUser(email: String, password: String) {
        progressbar.visibility = View.VISIBLE

        mAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) {task ->
                progressbar.visibility = View.GONE
                if (task.isSuccessful){
                   login()
                }else{
                    task.exception?.message?.let {
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
