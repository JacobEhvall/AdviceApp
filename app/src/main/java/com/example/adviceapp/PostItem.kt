package com.example.adviceapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_post_item.*


class PostItem : AppCompatActivity() {

    lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_item)

        db = FirebaseFirestore.getInstance()

        /*
        val addImage = findViewById<ImageView>(R.id.add_image)
        addImage.setOnClickListener {

            //addPicture() (Not used yet)
        }
         */


        val postAdvice = findViewById<Button>(R.id.post_data)
        postAdvice.setOnClickListener{

            addPost()
        }
    }


    private fun addPost() {

        val decribeInput = describe_title.text.toString()
        val descriptionInput = description_title.text.toString()


        // TODO We can specify an error message in here instead of regular Toast!
        if ( decribeInput.isEmpty() || descriptionInput.isEmpty() ) {
            Toast.makeText(applicationContext, "No empty fields allowed", Toast.LENGTH_LONG).show()

        }
        else {
            // TODO maybe rename PostData to AdviceData?
            val advice = PostData(decribeInput, descriptionInput)
            db.collection("advice").add(advice)
            toFirstPage()

        }

    }
    private fun toFirstPage() {
        val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
    }

}


