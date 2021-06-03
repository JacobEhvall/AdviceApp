package com.example.adviceapp.View

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.adviceapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class LogInScreen : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()


        val loginButton = findViewById<Button>(R.id.login_button)
        val signUpButton = findViewById<Button>(R.id.button_singup)

        val emailInput = findViewById<EditText>(R.id.signup_email)
        val passwordInput = findViewById<EditText>(R.id.signup_password)


        loginButton.setOnClickListener {
            val userEmailInput = emailInput.text.toString()
            val userPasswordInput = passwordInput.text.toString()

            userLoggedInCheck(userEmailInput,userPasswordInput)

        }

        signUpButton.setOnClickListener {
            userSignUp()
        }

        val currentUser: FirebaseUser? = auth.currentUser

        if (currentUser != null) {

            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
            Toast.makeText(this, "YOU ARE LOGGED IN :)", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "YOU ARE NOT LOGGED IN :( ", Toast.LENGTH_SHORT).show()
        }

    }

    private fun toAdvicePage() {
        val intent = Intent(this, HomePage::class.java)
        startActivity(intent)
    }

    private fun userSignUp() {
        val intent = Intent(this, SignUp::class.java)
        startActivity(intent)
    }

     fun userLoggedInCheck(userEmailInput: String, userPasswordInput: String): Boolean {

        if (userEmailInput == "" || userPasswordInput == "") {

            //Toast.makeText(this, "EMPTY FIELDS ARE NOT ALLOWED", Toast.LENGTH_SHORT).show()
            return false
        } else {
            auth.signInWithEmailAndPassword(userEmailInput, userPasswordInput)
            toAdvicePage()
        }
        return true

    }
}