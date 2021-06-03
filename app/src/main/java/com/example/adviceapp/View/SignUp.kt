package com.example.adviceapp.View

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.adviceapp.Model.FirebaseData
import com.example.adviceapp.Controller.UserData
import com.example.adviceapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUp : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore


   override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          setContentView(R.layout.activity_signup_page)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val userEmailInput = findViewById<EditText>(R.id.signup_email)
        val userPasswordInput = findViewById<EditText>(R.id.signup_password)
        val buttonSignUp = findViewById<Button>(R.id.sign_up)


        buttonSignUp.setOnClickListener {

            val email = userEmailInput.text.toString()
            val password = userPasswordInput.text.toString()
            createAccount(email, password)
        }

    }



    private fun createAccount(email:String, password: String ) {
        if (email == "" || password == "" ) {
            Toast.makeText(this, "no empty fields", Toast.LENGTH_SHORT).show()
        }
        else {

            FirebaseData()
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            println("!!! User Created")
                            val user = auth.currentUser
                            saveUserData(email, password)
                        }
                    }
        }
    }

    private fun saveUserData(email: String, password: String) {
        val user = auth.currentUser
        val userUid = user!!.uid
        val uData = UserData(email, password)

        db.collection("users").document(userUid).set(uData)
                .addOnSuccessListener {
                    signedUpUser()
                }
    }

    private fun signedUpUser() {
        val intent = Intent(this, HomePage::class.java)
        startActivity(intent)
    }

}